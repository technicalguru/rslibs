/**
 * 
 */
package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;

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
public class RequestBuilder  {

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

	protected Target      target;
	protected HeadersSpec headers;
	
	public RequestBuilder(Target target) {
		this.target   = target;
		this.headers  = new HeadersSpec();
	}
	
	/**
	 * Returns the target.
	 * @return the target
	 */
	public Target getTarget() {
		return target;
	}

	public RestResponse get() {
		return method(RequestMethod.GET);
	}
	
	public RestResponse post(Entity<?> entity) {
		return method(RequestMethod.POST, entity);
	}
	
	public RestResponse put(Entity<?> entity) {
		return method(RequestMethod.PUT, entity);
	}
	
	public RestResponse delete() {
		return method(RequestMethod.DELETE);
	}
	
	public RestResponse head() {
		return method(RequestMethod.HEAD);
	}
	
	public RestResponse options() {
		return method(RequestMethod.OPTIONS);
	}
	
	public RestResponse method(RequestMethod method) {
		return method(method, (Entity<?>)null);
	}
	
	public RestResponse method(String method) {
		return method(method, null, (Entity<?>)null);
	}
	
	public RestResponse method(RequestMethod method, Entity<?> entity) {
		return method(method.name(), method.getDefaultResponseMediaType(), entity);
	}
	
	public RestResponse method(String method, String responseMediaType, Entity<?> entity) {
		// 1st Build RestRequest  - standard object
		RestRequest request = new RestRequest(getTarget(), method, responseMediaType, headers, entity);
		// Ask the authorization strategy whether we can proceed, but synchronize
		AuthorizationStrategy authorizationStrategy = getTarget().getAuthorizationStrategy();
		if (authorizationStrategy != null) {
			synchronized(authorizationStrategy) {
				authorizationStrategy.checkAuthorization(request);
			}
		}
		// Process all interceptors...
		RestRequestExecution execution = createExecution();
		try {
			return execution.execute(request);
		} catch (Throwable t) {
			throw new RestClientException("Cannot execute request", t);
		}
	}

	public RequestBuilder header(String name, Object ...values) {
		headers.add(name, values);
		return this;
	}
	
	public RequestBuilder headers(MultiValuedMap<String, Object> headers) {
		this.headers.add(headers);
		return this;
	}
	
	public RequestBuilder cookie(String name, String cookie) {
		this.headers.addCookie(name, cookie);
		return this;
	}
	
	public RequestBuilder cookie(String cookie) {
		this.headers.addCookie(cookie);
		return this;
	}
	
	public RequestBuilder cookie(HttpCookie cookie) {
		this.headers.addCookie(cookie);
		return this;
	}
	
	public RequestBuilder cookie(Collection<HttpCookie> cookies) {
		this.headers.addCookies(cookies);
		return this;
	}

	private RestRequestExecution createExecution() {
		RestRequestExecution execution = new EndofChainRequestExecution(getTarget().getImplementation());
		return getTarget().getInterceptors().stream()
				.reduce(RequestInterceptor::andThen)
				.map(interceptor -> interceptor.apply(execution))
				.orElse(execution);
	}

}
