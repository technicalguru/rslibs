/**
 * 
 */
package rs.restclient.core.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.auth.AuthorizationStrategy;

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
 *       .with(configuration);
 *    client = RestClient.builder(MyMainClient.class)
 *       .with(targetBuilder)
 *       .withAuthorization(r -> new MyAuthorizationStrategy(r))
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
		this.target     = target;
		this.subClients = new HashMap<>();
		this.log        = LoggerFactory.getLogger(getClass());
		if (target.getAuthorizationStrategy() == null) {
			target.authorizationStrategy(createAuthorizationStrategy());
		}
	}
	
	/**
	 * Creates the authorization strategy for this client.
	 * <p>Notice that a strategy is automatically propagated to its sub-targets. That's why
	 * you only need to create your strategy in your main client. If your strategy is
	 * different for any sub-client, then call {@link Target#setAuthorizationStrategy(AuthorizationStrategy)}
	 * in that client's constructor.
	 * <p>Default implementation returns NULL.
	 * @return
	 */
	protected AuthorizationStrategy createAuthorizationStrategy() {
		return null;
	}
	
	/**
	 * Returns the target of this client.
	 * @return the target
	 */
	public final Target getTarget() {
		return target;
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
	 *       .withAuthorization(r -> new MyAuthorizationStrategy(r))
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
	 *       .withAuthorization(r -> new MyAuthorizationStrategy(r))
	 *       .build();
	 * </pre>
	 * @param <T> type of client
	 */
	public static class Builder<T extends RestClient> {
		
		private Target.Builder targetBuilder;
		private Class<T>       clientClass;
		
		private Function<RestClient, AuthorizationStrategy> authorizationFunction;
		
		/**
		 * Create the builder with this target builder.
		 * @param targetBuilder
		 */
		protected Builder(Class<T> clientClass) {
			this.clientClass   = clientClass;
		}

		public Builder<T> with(Target.Builder targetBuilder) {
			this.targetBuilder = targetBuilder;
			return this;
		}
		
		public Builder<T> withAuthorization(Function<RestClient, AuthorizationStrategy> authorizationFunction) {
			this.authorizationFunction = authorizationFunction;
			return this;
		}
		
		/**
		 * Builds the client using {@link #buildClient()} and configures
		 * {@link AuthorizationStrategy} for it if required.
		 * @return the client built and configured
		 */
		public T build() {
			T rc = buildClient();
			
			// Check if we have an authorization strategy set.
			Target target = rc.getTarget();
			if (target.getAuthorizationStrategy() == null) {
				if (authorizationFunction != null) {
					target.authorizationStrategy(authorizationFunction.apply(rc));
				}
			}
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
