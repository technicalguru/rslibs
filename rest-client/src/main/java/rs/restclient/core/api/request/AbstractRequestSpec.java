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
		this.headers               = new HeadersSpec(other.getHeaders());
		this.queryParams           = new QueryParamsSpec(other.getQueryParams());
		this.interceptors          = new ArrayList<>(other.getInterceptors());
		this.authorizationStrategy = other.getAuthorizationStrategy();
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
	public List<RequestInterceptor> getInterceptors() {
		return UnmodifiableList.unmodifiableList(interceptors);
	}

	/**
	 * Register the interceptor for execution in this target.
	 * @param interceptor interceptor
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
	public HeadersSpec getHeaders() {
		return UnmodifiableHeadersSpec.unmodifiableHeadersSpec(headers);
	}

	public T header(String name, Object ...values) {
		headers.add(name, values);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	public T headers(MultiValuedMap<String, Object> headers) {
		this.headers.add(headers);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	public T cookie(String name, String cookie) {
		this.headers.addCookie(name, cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	public T cookie(String cookie) {
		this.headers.addCookie(cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	public T cookie(HttpCookie cookie) {
		this.headers.addCookie(cookie);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}
	
	public T cookie(Collection<HttpCookie> cookies) {
		this.headers.addCookies(cookies);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

	/**
	 * Returns the queryParams.
	 * @return the queryParams
	 */
	public QueryParamsSpec getQueryParams() {
		return UnmodifiableQueryParamsSpec.unmodifiableQueryParamsSpec(queryParams);
	}

	
	/**
	 * @param name
	 * @param values
	 * @see QueryParamsSpec#add(java.lang.String, java.lang.Object[])
	 */
	public T queryParam(String name, Object... values) {
		queryParams.add(name, values);
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

	/**
	 * @param params
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
	public AuthorizationStrategy getAuthorizationStrategy() {
		return authorizationStrategy;
	}

	/**
	 * Sets the authorizationStrategy.
	 * @param authorizationStrategy the authorizationStrategy to set
	 */
	public T authorizationStrategy(AuthorizationStrategy authorizationStrategy) {
		this.authorizationStrategy = authorizationStrategy;
		@SuppressWarnings("unchecked")
		T t = (T)this;
		return t;
	}

}
