package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.list.UnmodifiableList;

import rs.restclient.core.api.RequestInterceptor;
import rs.restclient.core.api.auth.AuthorizationStrategy;

/**
 * Shared implementation for specifiying the request parameters.
 * 
 * @author ralph
 *
 */
public abstract class AbstractRequestSpec<T extends AbstractRequestSpec<T>> {

	private HeadersSpec              headers;
	private List<RequestInterceptor> interceptors;
	private AuthorizationStrategy    authorizationStrategy;
	private QueryParamsSpec          queryParams;
	/**
	 * Default constructor.
	 */
	protected AbstractRequestSpec() {
		this(new HeadersSpec(), new QueryParamsSpec(), new ArrayList<>(), null);
	}

	/**
	 * Copy constructor.
	 */
	protected AbstractRequestSpec(AbstractRequestSpec<?> other) {
		this.headers               = new HeadersSpec(other.headers());
		this.queryParams           = new QueryParamsSpec(other.queryParams());
		this.interceptors          = new ArrayList<>(other.interceptors());
		this.authorizationStrategy = other.authorizationStrategy();
	}

	
	/**
	 * Full Constructor.
	 * @param headers headers to copy from
	 * @param interceptors interceptors to copy from
	 * @param authorizationStrategy authorization strategy
	 */
	public AbstractRequestSpec(HeadersSpec headers, QueryParamsSpec queryParams, List<RequestInterceptor> interceptors, AuthorizationStrategy authorizationStrategy) {
		this.headers               = new HeadersSpec(headers);
		this.queryParams           = new QueryParamsSpec(queryParams);
		this.interceptors          = new ArrayList<>(interceptors);
		this.authorizationStrategy = authorizationStrategy;
	}

	/**
	 * Returns the interceptors.
	 * @return the interceptors
	 */
	public List<RequestInterceptor> interceptors() {
		return UnmodifiableList.unmodifiableList(interceptors);
	}

	/**
	 * Register the interceptor for execution in this target.
	 * @param interceptor interceptor
	 * @return this object for chaining
	 */
	public T register(RequestInterceptor interceptor) {
		this.interceptors.add(interceptor);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Returns the headers.
	 * @return the headers
	 */
	public HeadersSpec headers() {
		return UnmodifiableHeadersSpec.unmodifiableHeadersSpec(headers);
	}

	/**
	 * Adds a header.
	 * @param name name of header
	 * @param values value(s)
	 * @return this object for chaining
	 */
	public T header(String name, Object ...values) {
		headers.add(name, values);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Adds multiple headers.
	 * @param headers headers to add
	 * @return this object for chaining
	 */
	public T headers(MultiValuedMap<String, Object> headers) {
		this.headers.add(headers);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Adds a cookie (Note: This is "Cookie" header, not "Set-Cookie").
	 * @param name name of cookie
	 * @param cookie value of cookie
	 * @return this object for chaining
	 */
	public T cookie(String name, String cookie) {
		this.headers.addCookie(name, cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Adds a cookie (Note: This is "Cookie" header, not "Set-Cookie").
	 * @param cookie to add
	 * @return this object for chaining
	 */
	public T cookie(String cookie) {
		this.headers.addCookie(cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Adds a cookie (Note: This is "Cookie" header, not "Set-Cookie").
	 * @param cookie cookie to add
	 * @return this object for chaining
	 */
	public T cookie(HttpCookie cookie) {
		this.headers.addCookie(cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	/**
	 * Adds cookies (Note: This is "Cookie" header, not "Set-Cookie").
	 * @param cookies cookies to add
	 * @return this object for chaining
	 */
	public T cookies(Collection<HttpCookie> cookies) {
		this.headers.addCookies(cookies);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

	/**
	 * Returns the queryParams.
	 * @return the queryParams
	 */
	public QueryParamsSpec queryParams() {
		return UnmodifiableQueryParamsSpec.unmodifiableQueryParamsSpec(queryParams);
	}

	
	/**
	 * Adds a query parameter.
	 * @param name name of parameter
	 * @param values value(s)
	 * @return this object for chaining
	 * @see QueryParamsSpec#add(java.lang.String, java.lang.Object[])
	 */
	public T queryParam(String name, Object... values) {
		queryParams.add(name, values);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

	/**
	 * Adds multiple query parameters.
	 * @param params  parameters to add
	 * @return this object for chaining
	 * @see QueryParamsSpec#add(org.apache.commons.collections4.MultiValuedMap)
	 */
	public T queryParams(MultiValuedMap<String, Object> params) {
		queryParams.add(params);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

	/**
	 * Returns the authorizationStrategy.
	 * @return the authorizationStrategy
	 */
	public AuthorizationStrategy authorizationStrategy() {
		return authorizationStrategy;
	}

	/**
	 * Sets the authorizationStrategy.
	 * @param authorizationStrategy the authorizationStrategy to set
	 * @return this object for chaining
	 */
	public T authorizationStrategy(AuthorizationStrategy authorizationStrategy) {
		this.authorizationStrategy = authorizationStrategy;
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

}
