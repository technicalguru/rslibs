/**
 * 
 */
package rs.restclient.core.api;

import java.net.HttpCookie;
import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;

import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.response.RestResponse;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;

/**
 * The actual builder for request against a target.
 */
public interface RequestBuilder {

	public static final String GET     = "GET";
	public static final String POST    = "POST";
	public static final String PUT     = "PUT";
	public static final String DELETE  = "DELETE";
	public static final String HEAD    = "HEAD";
	public static final String OPTIONS = "OPTIONS";

	RestResponse head();
	
	RestResponse get();
	<T> T get(Class<T> responseType);
	<T> T get(JavaType responseType);
	<T> T get(TypeReference<T> responseType);

	RestResponse post(Entity<?> entity);
	<T> T post(Entity<?> entity, Class<T> responseType);
	<T> T post(Entity<?> entity, JavaType responseType);
	<T> T post(Entity<?> entity, TypeReference<T> responseType);
	
	RestResponse put(Entity<?> entity);
	<T> T put(Entity<?> entity, Class<T> responseType);
	<T> T put(Entity<?> entity, JavaType responseType);
	<T> T put(Entity<?> entity, TypeReference<T> responseType);
	
	RestResponse delete();
	<T> T delete(Class<T> responseType);
	<T> T delete(JavaType responseType);
	<T> T delete(TypeReference<T> responseType);
	
	RestResponse options();
	<T> T options(Class<T> responseType);
	<T> T options(JavaType responseType);
	<T> T options(TypeReference<T> responseType);
	
	RestResponse method(String name);
	<T> T method(String name, Class<T> responseType);
	<T> T method(String name, JavaType responseType);
	<T> T method(String name, TypeReference<T> responseType);
	RestResponse method(String name, Entity<?> entity);
	<T> T method(String name, Entity<?> entity, Class<T> responseType);
	<T> T method(String name, Entity<?> entity, JavaType responseType);
	<T> T method(String name, Entity<?> entity, TypeReference<T> responseType);

	RequestBuilder header(String name, Object ...values);
	RequestBuilder headers(MultiValuedMap<String, Object> headers);
	
	RequestBuilder cookie(String name, String cookie);
	RequestBuilder cookie(String cookie);
	RequestBuilder cookie(HttpCookie cookie);
	RequestBuilder cookie(Collection<HttpCookie> cookies);

}
