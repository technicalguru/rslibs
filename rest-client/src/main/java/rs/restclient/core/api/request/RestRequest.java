package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;

import rs.restclient.core.api.Target;

/**
 * A wrapper for the request.
 * 
 * @author ralph
 *
 */
public class RestRequest {

	private Target          target;
	private String          method;
	private String          responseMediaType;
    private HeadersSpec     headers;
    private QueryParamsSpec queryParams;
	private Entity<?>       entity;
	
	/**
	 * Constructor.
	 * @param target target of the request
	 */
	protected RestRequest(Target target, String method, String responseMediaType, HeadersSpec headers, QueryParamsSpec queryParams, Entity<?> entity) {
		this.target            = target;
		this.method            = method;
		this.responseMediaType = responseMediaType;
		this.headers           = new HeadersSpec(headers);
		this.queryParams       = new QueryParamsSpec(queryParams);
		this.entity            = entity;
	}
	
	/**
	 * Returns the target.
	 * @return the target
	 */
	public Target getTarget() {
		return target;
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
	 * Creates a copy of the request.
	 * @param request the request to copy
	 * @return the copy
	 */
	public static RestRequest from(RestRequest request) {
		return new RestRequest(request.getTarget(), request.getMethod(), request.getResponseMediaType(), request.getHeaders(), request.getQueryParams(), request.getEntity());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RestRequest [target=" + target + ", method=" + method + ", headers=" + headers + ", entity=" + entity + "]";
	}
	
	
}
