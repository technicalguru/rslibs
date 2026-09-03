/**
 * 
 */
package rs.restclient.core.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;
import rs.restclient.core.api.auth.AuthorizationStrategy;
import rs.restclient.core.api.response.ClientErrorException;
import rs.restclient.core.api.response.RedirectionException;
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.api.response.RestResponseException;
import rs.restclient.core.api.response.ServerErrorException;
import rs.restclient.core.util.UserAgentInterceptor;
import rs.restclient.data.HateOasPagedList;
import rs.restclient.data.HateOasPagedList.EmbeddedResultList;
import rs.restclient.data.ResultList;

/**
 * Abstract implementation of a client. The API that will be interacted with by your users.
 * <p>Derive all your clients from this class. Your main entry point shall use the
 * {@link #builder(Class)} method to create a builder that you configure:
 * <pre>
 *    RestClientConfiguration configuration = RestClientConfiguration.builder()
 *       .with(myUri)
 *       .verbose(true)
 *       .build();
 *    Target.Builder targetBuilder = Target.builder()
 *       .with(myImpl)
 *       .with(configuration)
 *       .register(new CookieInterceptor())
 *       .register(new UserAgentInterceptor("MyClient/1.0"));
 *    client = RestClient.builder(MyMainClient.class)
 *       .with(targetBuilder)
 *       .authorizationStrategy(new MyAuthorizationStrategy())
 *       .build();
 * </pre>
 * Sub-clients must use {@link #RestClient(Target)} as constructor.
 */
public abstract class RestClient {

	private   Target                                       target;
	private   Map<Class<? extends RestClient>, RestClient> subClients;
	protected Logger                                       log;
	
	/**
	 * The main constructor for your main client implementation. This client has no parent.
	 * @param targetBuilder the builder for the main target.
	 */
	protected RestClient(Target.Builder targetBuilder) {
		this(targetBuilder.build());
	}
	
	/**
	 * Constructor for all sub-clients.
	 * <p>When deriving, call this with a specific sub-path, e.g.
	 * <pre>
	 *
	 *    MySubClient(Target target) {
	 *       super(target.path("users"));
	 *    }
	 *
	 * </pre>
	 * 
	 * @param target the target to be used.
	 */
	protected RestClient(Target target) {
		if (target == null) throw new RestClientException("target must not be null");
		this.subClients = new HashMap<>();
		this.log        = LoggerFactory.getLogger(getClass());
		this.target     = configureTarget(target);
	}
	
	/**
	 * Called by the constructor to allow your client to further configure the target,
	 * e.g. to adjust the path, set your authorization strategy or register any interceptors
	 * for this client.
	 * <p>Please understand that your objects, such as {@link AuthorizationStrategy} should
	 *    use the target carefully as the target clones itself when settings change. This
	 *    method must return the target that is fully configured. 
	 * <p>Notice that all settings in a target are automatically propagated to sub-clients and
	 * its targets. That's why you only need to make global settings in your main client 
	 * unless your sub-client uses a different target.
	 * <p>Default implementation configures the user agent if required.
	 * @return the new target
	 * @see #getUserAgent()
	 */
	protected Target configureTarget(Target target) {
		String ua = getUserAgent();
		if (ua != null) target = target.register(new UserAgentInterceptor(ua));
		return target;
	}
	
	/**
	 * Returns the target of this client.
	 * @return the target
	 */
	public final Target getTarget() {
		return target;
	}
	
	/**
	 * Returns the configuration.
	 * <p>This is equivalent to {@code getTarget().configuration()}.
	 * @return the configuration
	 * @see Target#configuration()
	 */
	public RestClientConfiguration getConfiguration() {
		return getTarget().configuration();
	}
	
	/**
	 * Returns the underlying implementation.
	 * <p>This is equivalent to {@code getTarget().implementation()}.
	 * @return the implementation
	 * @see Target#implementation()
	 */
	public TargetImplementation getImplementation() {
		return getTarget().implementation();
	}
	
	/**
	 * Returns the user agent of the client.
	 * <p>The method will be called by default {@link #configureTarget(Target)} implementation
	 *    to add an interceptor for it. Return NULL if you don't need it.
	 * <p>Default method return NULL.
	 * @return the user agent
	 */
	protected String getUserAgent() {
		return null;
	}
	
	/**
	 * Returns a sub-client of given class.
	 * <p>if the client was not yet created, it will be created using {@link #createClient(Class)}
	 * . Otherwise the cached instance will be returned.
	 * @param <T> the client class type
	 * @param clientClass the client class
	 * @return instance of your sub-client
	 */
	protected <T extends RestClient> T getClient(Class<T> clientClass) {
		@SuppressWarnings("unchecked")
		T rc = (T)subClients.get(clientClass);
		if (rc == null) {
			rc = createClient(clientClass);
			subClients.put(clientClass, rc);
		}
		return rc;
	}
	
	/**
	 * Creates a sub-client based on this client.
	 * @param <T> the client class type
	 * @param clientClass the client class
	 * @return new instance of your sub-client (called with target of this class)
	 */
	protected <T extends RestClient> T createClient(Class<T> clientClass) {
		try {
			return clientClass.getConstructor(Target.class).newInstance(target);
		} catch (Throwable t) {
			throw new RestClientException("Cannot create client.", t);
		}
	}

	/**
	 * Returns the target with paging parameters applied.
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the target
	 */
	protected Target getTarget(Integer page, Integer pageSize) {
		return applyPaging(getTarget(), page, pageSize);
	}
	
	/**
	 * Returns the target with sort parameter applied.
	 * @param sort - the sort parameter
	 * @return the target
	 */
	protected Target getTarget(String sort) {
		return applySort(getTarget(), sort);
	}
	
	/**
	 * Returns the target with paging and sort parameters applied.
	 * @param sort     - the sort parameter
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the target
	 */
	protected Target getTarget(String sort, Integer page, Integer pageSize) {
		return applySort(applyPaging(getTarget(), page, pageSize), sort);
	}
	
	/**
	 * Applies paging parameters to the target.
	 * @param target   - the base target
	 * @param page     - page index (0-based) 
	 * @param pageSize - page size
	 * @return the new target
	 */
	protected Target applyPaging(Target target, Integer page, Integer pageSize) {
		if (page     != null) target = target.queryParam("page",     page);
		if (pageSize != null) target = target.queryParam("pageSize", pageSize);
		return target;
	}

	/**
	 * Applies sort parameter to the target.
	 * @param target - the base target
	 * @param sort   - the sort parameter
	 * @return the new target
	 */
	protected Target applySort(Target target, String sort) {
		if (sort != null) target = target.queryParam("sort", sort);
		return target;
	}
	
	/**
	 * Retrieves the results from the REST response object.
	 * @param <T> - class of result type
	 * @param pagedList - the response object
	 * @return the list of objects (or empty list)
	 */
	protected <T> ResultList<T> getResults(HateOasPagedList<T> pagedList) {
		if (pagedList != null) {
			EmbeddedResultList<T> embedded = pagedList.get_embedded();
			if (embedded != null) {
				return new ResultList<>(embedded.getResults(), pagedList.getPage());
			}
		}
		return new ResultList<>(CommonUtils.newList(), pagedList.getPage());
	}
	
	
	/**
	 * Helper method to raise exceptions in case of any other response than 2xx successful.
	 * @param response the {@link Response} object
	 */
	protected void checkResponse(RestResponse response) {
		checkResponse(response, null);
	}
	
	/**
	 * Helper method to raise exceptions in case of any other response than 2xx successful.
	 * @param <T> - the type of successful return
	 * @param response the {@link Response} object
	 * @param successValue the value to return when response was successfull
	 * @return usually the successValue - everything else will raise a runtime exception
	 */
	protected <T> T checkResponse(RestResponse response, T successValue) {
		int statusCode = response.getStatusCode();
		if ((statusCode / 100 == 1) || (statusCode / 100 == 2)) return successValue;
		if (statusCode / 100 == 3) throw new RedirectionException(response);
		if (statusCode / 100 == 3) throw new ClientErrorException(response);
		if (statusCode / 100 == 3) throw new ServerErrorException(response);
		throw new RestResponseException(response);
	}

	/**
	 * Helper method to raise exceptions in case of any other response than 2xx successful.
	 * @param <T> - the type of successful return
	 * @param responseClass the {@link Response} object type
	 * @param successValue the value to return when response was successfull
	 * @return usually the successValue - everything else will raise a runtime exception
	 */
	protected <T> T getResponse(RestResponse response, Class<T> successClass) {
		int statusCode = response.getStatusCode();
		if ((statusCode / 100 == 1) || (statusCode / 100 == 2)) return response.as(successClass);
		if (statusCode / 100 == 3) throw new RedirectionException(response);
		if (statusCode / 100 == 3) throw new ClientErrorException(response);
		if (statusCode / 100 == 3) throw new ServerErrorException(response);
		throw new RestResponseException(response);
	}

	/**
	 * Create a builder for a {@link RestClient} of given type.
	 * <p>You should not subclass this builder and interact only during bootstrap setup of
	 * your main client:
	 * <pre>
	 *    RestClientConfiguration configuration = RestClientConfiguration.builder()
	 *       .with(myUri)
	 *       .verbose(true)
	 *       .build();
	 *    Target.Builder targetBuilder = Target.builder()
	 *       .with(myImpl)
	 *       .with(configuration);
	 *    client = RestClient.builder(MyMainClient.class)
	 *       .with(targetBuilder)
	 *       .authorizationStrategy(new MyAuthorizationStrategy())
	 *       .build();
	 * </pre>
	 * @param <T> type of client
	 * @param clientClass the class of the client
	 * @return the client built.
	 */
	public static <T extends RestClient> Builder<T> builder(Class<T> clientClass) {
		return new Builder<>(clientClass);
	}
	
	/**
	 * Create a builder for a {@link RestClient} of given type.
	 * <p>You should not subclass this builder and interact only during bootstrap setup of
	 * your main client:
	 * <pre>
	 *    RestClientConfiguration configuration = RestClientConfiguration.builder()
	 *       .with(myUri)
	 *       .verbose(true)
	 *       .build();
	 *    Target.Builder targetBuilder = Target.builder()
	 *       .with(myImpl)
	 *       .with(configuration);
	 *    client = RestClient.builder(MyMainClient.class)
	 *       .with(targetBuilder)
	 *       .authorizationStrategy(new MyAuthorizationStrategy())
	 *       .build();
	 * </pre>
	 * @param <T> type of client
	 */
	public static class Builder<T extends RestClient> {
		
		private Target.Builder targetBuilder;
		private Class<T>       clientClass;
		
		/**
		 * Create the builder with this target builder.
		 * @param targetBuilder
		 */
		protected Builder(Class<T> clientClass) {
			this.clientClass   = clientClass;
		}

		/**
		 * Use the given target builder to create the client.
		 * @param targetBuilder builder to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(Target.Builder targetBuilder) {
			this.targetBuilder = targetBuilder;
			return this;
		}
		
		/**
		 * Builds the client using {@link #buildClient()} and configures
		 * {@link AuthorizationStrategy} for it if required.
		 * @return the client built and configured
		 */
		public T build() {
			T rc = buildClient();
			return rc;
		}
		
		/**
		 * Builds the client.
		 * @return the client built from target builder
		 */
		protected T buildClient() {
			try {
				Constructor<T> constructor = clientClass.getConstructor(Target.Builder.class);
				return constructor.newInstance(targetBuilder);
			} catch (Throwable t) {
				throw new RestClientException("Cannot create RestClient", t);
			}
		}
	}
}
