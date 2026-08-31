package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;

import rs.restclient.core.api.RequestInterceptor;
import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.TargetImplementation;

/**
 * A wrapper for the request.
 * 
 * @author ralph
 *
 */
public class RestRequest {

	private URI                      uri;
	private String                   method;
	private String                   responseMediaType;
    private HeadersSpec              headers;
    private QueryParamsSpec          queryParams;
	private Entity<?>                entity;
	private List<RequestInterceptor> interceptors;
	private RestClientConfiguration  configuration;
	private TargetImplementation     implementation;

	/**
	 * Constructor.
	 * @param target target of the request
	 */
	protected RestRequest(URI uri, String method, String responseMediaType, HeadersSpec headers, QueryParamsSpec queryParams, 
			Entity<?> entity, List<RequestInterceptor> interceptors, RestClientConfiguration configuration, 
			TargetImplementation implementation) {
		this.uri               = uri;
		this.method            = method;
		this.responseMediaType = responseMediaType;
		this.headers           = new HeadersSpec(headers);
		this.queryParams       = new QueryParamsSpec(queryParams);
		this.entity            = entity;
		this.interceptors      = new ArrayList<>(interceptors);
		this.configuration     = configuration;
		this.implementation    = implementation;
	}
	
	/**
	 * Returns the uri.
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Returns the method.
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Returns the responseMediaType.
	 * @return the responseMediaType
	 */
	public String getResponseMediaType() {
		return responseMediaType;
	}

	/**
	 * Returns the headers.
	 * @return the headers
	 */
	public HeadersSpec getHeaders() {
		return headers;
	}
	
	/**
	 * Returns the queryParams.
	 * @return the queryParams
	 */
	public QueryParamsSpec getQueryParams() {
		return queryParams;
	}

	/**
	 * Returns the entity.
	 * @return the entity
	 */
	public Entity<?> getEntity() {
		return entity;
	}

	/**
	 * Adds the given header with values.
	 * @param name name of header
	 * @param values values
	 */
	public void addHeader(String name, Object... values) {
		this.headers.add(name, values);
	}

	/**
	 * Adds the given headers.
	 * @param headers headers to add
	 */
	public void addHeaders(MultiValuedMap<String, Object> headers) {
		this.headers.add(headers);
	}

	/**
	 * Adds the cookie to the headers.
	 * @param name name of cookie
	 * @param cookie cookie to add
	 */
	public void addCookie(String name, String cookie) {
		this.headers.addCookie(name, cookie);
	}

	/**
	 * Adds the cookie to the headers.
	 * @param cookie cookie to add
	 */
	public void addCookie(String cookie) {
		this.headers.addCookie(cookie);
	}

	/**
	 * Adds the cookies to the headers.
	 * @param cookies cookies to add
	 */
	public void addCookies(Collection<HttpCookie> cookies) {
		this.headers.addCookies(cookies);
	}

	/**
	 * Adds the cookie to the headers.
	 * @param cookie cookie to add
	 */
	public void addCookie(HttpCookie cookie) {
		this.headers.addCookie(cookie);
	}

	/**
	 * Returns the interceptors.
	 * @return the interceptors
	 */
	public List<RequestInterceptor> getInterceptors() {
		return interceptors;
	}

	/**
	 * Register an interceptor.
	 * @param interceptor interceptor to add
	 */
	public void register(RequestInterceptor interceptor) {
		this.interceptors.add(interceptor);
	}
	
	/**
	 * Returns the configuration.
	 * @return the configuration
	 */
	public RestClientConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns the implementation.
	 * @return the implementation
	 */
	public TargetImplementation getImplementation() {
		return implementation;
	}

	/**
	 * Creates a copy of the request.
	 * @param request the request to copy
	 * @return the copy
	 */
	public static RestRequest from(RestRequest request) {
		return new RestRequest(request.getUri(), request.getMethod(), request.getResponseMediaType(), request.getHeaders(), request.getQueryParams(), 
				request.getEntity(), request.getInterceptors(), request.getConfiguration(), request.getImplementation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RestRequest [uri=" + uri + ", method=" + method + ", responseMediaType=" + responseMediaType
				+ ", headers=" + headers + ", queryParams=" + queryParams + ", entity=" + entity + ", interceptors="
				+ interceptors + ", configuration=" + configuration + ", implementation=" + implementation + "]";
	}

	
	
}
