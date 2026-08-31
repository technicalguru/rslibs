/**
 * 
 */
package rs.restclient.core.api.request;

import rs.restclient.core.api.RequestInterceptor;
import rs.restclient.core.api.RestClientException;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.auth.AuthorizationStrategy;
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.util.EndofChainRequestExecution;
import rs.restclient.core.util.RestRequestExecution;

/**
 * Abstract implementation of request builder.
 * The final class is dependent on the implementation (Jersey, SpringBoot's RestClient) etc.
 */
public class RequestBuilder extends AbstractRequestSpec<RequestBuilder> {

	/**
	 * HTTP Method with encapsulated default body response type expected.
	 */
	public static enum RequestMethod {
		GET(MediaType.APPLICATION_JSON),
		POST(MediaType.APPLICATION_JSON),
		PUT(MediaType.APPLICATION_JSON),
		DELETE(MediaType.APPLICATION_JSON),
		HEAD,
		OPTIONS(MediaType.APPLICATION_JSON);
		
		private String defaultResponseMediaType;
		
		private RequestMethod() {
			this(null);
		}
		
		private RequestMethod(String defaultResponseMediaType) {
			this.defaultResponseMediaType = defaultResponseMediaType;
		}

		/**
		 * Returns the defaultResponseMediaType.
		 * @return the defaultResponseMediaType
		 */
		public String getDefaultResponseMediaType() {
			return defaultResponseMediaType;
		}
		
	}

	protected Target                 target;
	
	public RequestBuilder(Target target) {
		super(target);
		this.target = target;
	}
	
	/**
	 * Returns the target.
	 * @return the target
	 */
	public Target getTarget() {
		return target;
	}

	/**
	 * Executes a GET request.
	 * @return the response
	 */
	public RestResponse get() {
		return method(RequestMethod.GET);
	}
	
	/**
	 * Executes a POST request.
	 * @param entity the entity to send in request body
	 * @return the response
	 */
	public RestResponse post(Entity<?> entity) {
		return method(RequestMethod.POST, entity);
	}
	
	/**
	 * Executes a PUT request.
	 * @param entity the entity to send in request body
	 * @return the response
	 */
	public RestResponse put(Entity<?> entity) {
		return method(RequestMethod.PUT, entity);
	}
	
	/**
	 * Executes a DELETE request.
	 * @return the response
	 */
	public RestResponse delete() {
		return method(RequestMethod.DELETE);
	}
	
	/**
	 * Executes a HEAD request.
	 * @return the response
	 */
	public RestResponse head() {
		return method(RequestMethod.HEAD);
	}
	
	/**
	 * Executes an OPTIONS request.
	 * @return the response
	 */
	public RestResponse options() {
		return method(RequestMethod.OPTIONS);
	}
	
	/**
	 * Executes a request with arbitrary method.
	 * @param method method to execute
	 * @return the response
	 */
	public RestResponse method(RequestMethod method) {
		return method(method, (Entity<?>)null);
	}
	
	/**
	 * Executes a request with arbitrary method.
	 * @param method method to execute
	 * @return the response
	 */
	public RestResponse method(String method) {
		return method(method, null, (Entity<?>)null);
	}
	
	/**
	 * Executes a request with arbitrary method.
	 * @param method method to execute
	 * @param entity the entity to send in request body
	 * @return the response
	 */
	public RestResponse method(RequestMethod method, Entity<?> entity) {
		return method(method.name(), method.getDefaultResponseMediaType(), entity);
	}
	
	/**
	 * Executes a request with arbitrary method.
	 * @param method method to execute
	 * @param responseMediaType the expected response type
	 * @param entity the entity to send in request body
	 * @return the response
	 */
	public RestResponse method(String method, String responseMediaType, Entity<?> entity) {
		// 1st Build RestRequest  - standard object
		Target      target  = getTarget();
		RestRequest request = new RestRequest(target.getUri(), method, responseMediaType, headers(), queryParams(), entity, 
				                              interceptors(), target.configuration(), target.implementation());
		// Ask the authorization strategy whether we can proceed, but synchronize
		AuthorizationStrategy authorizationStrategy = authorizationStrategy();
		if (authorizationStrategy != null) {
			synchronized(authorizationStrategy) {
				authorizationStrategy.checkAuthorization(request);
			}
		}
		// Process all interceptors...
		RestRequestExecution execution = createExecution(request);
		try {
			return execution.execute(request);
		} catch (Throwable t) {
			throw new RestClientException("Cannot execute request", t);
		}
	}

	/**
	 * Creates the interceptor execution chain.
	 * @return the execution chain
	 */
	private RestRequestExecution createExecution(RestRequest request) {
		RestRequestExecution execution = new EndofChainRequestExecution(getTarget().implementation());
		return request.getInterceptors().stream()
				.reduce(RequestInterceptor::andThen)
				.map(interceptor -> interceptor.apply(execution))
				.orElse(execution);
	}

}
