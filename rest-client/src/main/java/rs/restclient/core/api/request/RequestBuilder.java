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
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.util.EndofChainRequestExecution;
import rs.restclient.core.util.RestRequestExecution;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;

/**
 * Abstract implementation of request builder.
 * The final class is dependent on the implementation (Jersey, SpringBoot's RestClient) etc.
 */
public class RequestBuilder  {

	public static final String GET     = "GET";
	public static final String POST    = "POST";
	public static final String PUT     = "PUT";
	public static final String DELETE  = "DELETE";
	public static final String HEAD    = "HEAD";
	public static final String OPTIONS = "OPTIONS";

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
		return method(GET);
	}
	
	public <T> T get(Class<T> responseType) {
		return method(GET, responseType);
	}

	public <T> T get(JavaType responseType) {
		return method(GET, responseType);
	}

	public <T> T get(TypeReference<T> responseType) {
		return method(GET, responseType);
	}

	public RestResponse post(Entity<?> entity) {
		return method(POST, entity);
	}
	
	public <T> T post(Entity<?> entity, Class<T> responseType) {
		return method(POST, entity, responseType);
	}
	
	public <T> T post(Entity<?> entity, JavaType responseType) {
		return method(POST, entity, responseType);
	}
	
	public <T> T post(Entity<?> entity, TypeReference<T> responseType) {
		return method(POST, entity, responseType);
	}
	
	public RestResponse put(Entity<?> entity) {
		return method(PUT, entity);
	}
	
	public <T> T put(Entity<?> entity, Class<T> responseType) {
		return method(PUT, entity, responseType);
	}
	
	public <T> T put(Entity<?> entity, JavaType responseType) {
		return method(PUT, entity, responseType);
	}
	
	public <T> T put(Entity<?> entity, TypeReference<T> responseType) {
		return method(PUT, entity, responseType);
	}
	
	public RestResponse delete() {
		return method(DELETE);
	}
	
	public <T> T delete(Class<T> responseType) {
		return method(DELETE, responseType);
	}
	
	public <T> T delete(JavaType responseType) {
		return method(DELETE, responseType);
	}
	
	public <T> T delete(TypeReference<T> responseType) {
		return method(DELETE, responseType);
	}
	
	public RestResponse head() {
		return method(HEAD);
	}
	
	public RestResponse options() {
		return method(OPTIONS);
	}
	
	public <T> T options(Class<T> responseType) {
		return method(OPTIONS, responseType);
    }

	public <T> T options(JavaType responseType) {
		return method(OPTIONS, responseType);
    }

	public <T> T options(TypeReference<T> responseType) {
		return method(OPTIONS, responseType);
    }

	public RestResponse method(String name) {
		return method(name, (Entity<?>)null);
	}
	
	public <T> T method(String name, Class<T> responseType) {
		return method(name, null, responseType);
	}

	public <T> T method(String name, JavaType responseType) {
		return method(name, null, responseType);
	}

	public <T> T method(String name, TypeReference<T> responseType) {
		return method(name, null, responseType);
	}

	public RestResponse method(String methodName, Entity<?> entity) {
		// 1st Build RestRequest  - standard object
		RestRequest request = new RestRequest(getTarget(), methodName, headers, entity);
		// Process all interceptors...
		RestRequestExecution execution = createExecution();
		try {
			return execution.execute(request, null);
		} catch (Throwable t) {
			throw new RestClientException("Cannot execute request", t);
		}
	}

    public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
    	return method(name, entity).as(responseType);
 	}

    public <T> T method(String name, Entity<?> entity, JavaType responseType) {
       	return method(name, entity).as(responseType);
	}

    public <T> T method(String name, Entity<?> entity, TypeReference<T> responseType) {
       	return method(name, entity).as(responseType);
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
