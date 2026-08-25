/**
 * 
 */
package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import rs.restclient.core.api.RequestBuilder;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.response.RestResponse;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;

/**
 * Abstract implementation of request builder.
 * The final class is dependent on the implementation (Jersey, SpringBoot's RestClient) etc.
 */
public abstract class AbstractRequestBuilder implements RequestBuilder {

	protected Target                         target;
    protected MultiValuedMap<String, Object> headers;
	protected Set<HttpCookie>                cookies;
	
	protected AbstractRequestBuilder(Target target) {
		this.target   = target;
		this.headers  = new ArrayListValuedHashMap<>();
		this.cookies  = new HashSet<>();
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

    public abstract RestResponse method(String name, Entity<?> entity);

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
		for (Object value : values) {
			if (value != null) headers.put(name, value);
		}
		return this;
	}
	
	public RequestBuilder headers(MultiValuedMap<String, Object> headers) {
		this.headers.putAll(headers);
		return this;
	}
	
	public RequestBuilder cookie(String name, String cookie) {
		return cookie(new HttpCookie(name, cookie));
	}
	
	public RequestBuilder cookie(String cookie) {
		return cookie(HttpCookie.parse(cookie));
	}
	
	public RequestBuilder cookie(HttpCookie cookie) {
		cookies.add(cookie);
		return this;
	}
	
	public RequestBuilder cookie(Collection<HttpCookie> cookies) {
		this.cookies.addAll(cookies);
		return this;
	}


}
