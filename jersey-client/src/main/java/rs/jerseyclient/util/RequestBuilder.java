package rs.jerseyclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.http.client.ClientHttpResponse;

import rs.jackson.Json;
import rs.jerseyclient.JerseyClientConfig;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

/**
 * Replaces the Request.Builder.
 * 
 * @author ralph
 *
 */
public class RequestBuilder {

	public static final String GET     = "GET";
	public static final String POST    = "POST";
	public static final String PUT     = "PUT";
	public static final String DELETE  = "DELETE";
	public static final String HEAD    = "HEAD";
	public static final String OPTIONS = "OPTIONS";
	
	private WebTarget                      target;
	private JerseyClientConfig             config;
	private Json                           json;
    private MultiValuedMap<String, Object> headers;
	private Set<HttpCookie>                cookies;
	
	public RequestBuilder(WebTarget target, JerseyClientConfig config) {
		this.target  = target;
		this.config  = config;
		this.json    = null;
		this.headers = new ArrayListValuedHashMap<>();
		this.cookies = new HashSet<>();
	}
	
	public ClientHttpResponse get() {
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

	public ClientHttpResponse post(Entity<?> entity) {
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
	
	public ClientHttpResponse put(Entity<?> entity) {
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
	
	public ClientHttpResponse delete() {
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
	
	public ClientHttpResponse head() {
		return method(HEAD);
	}
	
	public ClientHttpResponse options() {
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

    public ClientHttpResponse method(String name) {
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

    public ClientHttpResponse method(String name, Entity<?> entity) {
    	// TODO
		return null;
	}

    public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
    	return convertBody(method(name, entity), responseType);
 	}

    public <T> T method(String name, Entity<?> entity, JavaType responseType) {
       	return convertBody(method(name, entity), responseType);
	}

    public <T> T method(String name, Entity<?> entity, TypeReference<T> responseType) {
       	return convertBody(method(name, entity), responseType);
	}

	protected <T> T convertBody(ClientHttpResponse response, Class<T> responseType) {
		try (InputStream body = response.getBody()) {
			return getJson().fromJson(body, responseType);
		} catch (IOException e) {
			throw new HttpConnectionException("Cannot read response body", e);
		}
	}

	protected <T> T convertBody(ClientHttpResponse response, JavaType responseType) {
		try (InputStream body = response.getBody()) {
			return getJson().fromJson(body, responseType);
		} catch (IOException e) {
			throw new HttpConnectionException("Cannot read response body", e);
		}
	}

	protected <T> T convertBody(ClientHttpResponse response, TypeReference<T> responseType) {
		try (InputStream body = response.getBody()) {
			return getJson().fromJson(body, responseType);
		} catch (IOException e) {
			throw new HttpConnectionException("Cannot read response body", e);
		}
	}

	public Json getJson() {
		if (json == null) {
			JsonMapper mapper = config.getJsonMapper();
			Json.Builder builder = Json.builder();
			if (mapper != null) builder = builder.with(mapper);
			json = builder.build();
		}
		return json;
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
