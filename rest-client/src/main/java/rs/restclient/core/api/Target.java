/**
 * 
 */
package rs.restclient.core.api;

import java.net.HttpCookie;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;

import rs.baselib.util.UriBuilder;
import rs.restclient.core.api.auth.AuthorizationStrategy;
import rs.restclient.core.api.request.AbstractRequestSpec;
import rs.restclient.core.api.request.HeadersSpec;
import rs.restclient.core.api.request.QueryParamsSpec;
import rs.restclient.core.api.request.RequestBuilder;

/**
 * Describes a target and creates your specific API requests.
 */
public class Target extends AbstractRequestSpec<Target> {

	private RestClientConfiguration  configuration;
	private TargetImplementation     implementation;
	private URI                      uri;

	/**
	 * Constructor used by {@link #path(String)} method.
	 * @param parentTarget parent target to copy values from
	 * @param uri the URI for this target
	 */
	private Target(Target parentTarget, URI uri) {
		this(uri, parentTarget.getConfiguration(), parentTarget.getImplementation(), parentTarget.getInterceptors(),
				parentTarget.getHeaders(), parentTarget.getQueryParams(), parentTarget.getAuthorizationStrategy());
	}
	
	/**
	 * Main Constructor.
	 * @param uri the URI for the target
	 * @param configuration the configuration
	 * @param implementation the underlying implementation
	 * @param interceptors interceptor list
	 * @param headers headers to be present
	 * @param queryParams query params to be present
	 * @param authorizationStrategy the authorization strategy
	 */
	private Target(URI uri, RestClientConfiguration configuration, TargetImplementation implementation,
			List<RequestInterceptor> interceptors, HeadersSpec headers, QueryParamsSpec queryParams, AuthorizationStrategy authorizationStrategy) {
		super(headers, queryParams, interceptors, authorizationStrategy);
		if (uri == null) throw new RestClientException("uri must not be null");
		this.uri                   = uri;
		this.configuration         = configuration;
		this.implementation        = implementation;
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
	 * Returns the uri.
	 * @return the uri
	 */
	public URI getUri() {
		return uri;
	}

	/**
	 * Creates a new target at given path.
	 * <p>If path is absolute then it will be built again from configuration uri appended by the path).
	 * @param path the sub path to append to the current target.
	 * @return the new target
	 */
	public Target path(String path) {
		UriBuilder uriBuilder;
		if (path.startsWith("/")) {
			uriBuilder = UriBuilder.from(configuration.getUri());
		} else {
			uriBuilder = UriBuilder.from(uri);
		}
		return new Target(this, uriBuilder.appendSegments(path).build());
	}
	
	// Manipulate all super methids to return a copy instead
	
	/**
	 * Returns the request builder.
	 * @return the request builder
	 */
	public RequestBuilder request() {
		return new RequestBuilder(this);
	}
	
	/**
	 * Registers the interceptor.
	 * @param interceptor the interceptor
	 * @return a new target
	 */
	@Override
	public Target register(RequestInterceptor interceptor) {
		return builder(this).register(interceptor).build();
	}

	/**
	 * Adds a header.
	 * @param name name of header
	 * @param values values to add
	 * @return a new target
	 */
	@Override
	public Target header(String name, Object... values) {
		return builder(this).header(name, values).build();
	}

	/**
	 * Adds headers.
	 * @param headers headers to add
	 * @return a new target
	 */
	@Override
	public Target headers(MultiValuedMap<String, Object> headers) {
		return builder(this).headers(headers).build();
	}

	/**
	 * Adds a cookie.
	 * @param name name of cookie
	 * @param cookie value of cookie
	 * @return a new target
	 */
	@Override
	public Target cookie(String name, String cookie) {
		return builder(this).cookie(name, cookie).build();
	}

	/**
	 * Adds a cookie.
	 * @param cookie to add
	 * @return a new target
	 */
	@Override
	public Target cookie(String cookie) {
		return builder(this).cookie(cookie).build();
	}

	/**
	 * Adds a cookie.
	 * @param cookie cookie to add
	 * @return a new target
	 */
	@Override
	public Target cookie(HttpCookie cookie) {
		return builder(this).cookie(cookie).build();
	}

	/**
	 * Adds cookies.
	 * @param cookies cookies to add
	 * @return a new target
	 */
	@Override
	public Target cookie(Collection<HttpCookie> cookies) {
		return builder(this).build().cookie(cookies);
	}

	/**
	 * Adds a query param.
	 * @param name name of query param
	 * @param values values to add
	 * @return a new target
	 */
	@Override
	public Target queryParam(String name, Object... values) {
		return builder(this).queryParam(name, values).build();
	}

	/**
	 * Adds all query params.
	 * @param params query params to add
	 * @return a new target
	 */
	@Override
	public Target queryParams(MultiValuedMap<String, Object> params) {
		return builder(this).queryParams(params).build();
	}

	/**
	 * Sets the authorization strategy.
	 * @param authorizationStrategy the new strategy
	 * @return a new target
	 */
	@Override
	public Target authorizationStrategy(AuthorizationStrategy authorizationStrategy) {
		return builder(this).authorizationStrategy(authorizationStrategy).build();
	}

	public static Builder builder(Target from) {
		return builder()
			.with(from.getImplementation())
			.with(from.getConfiguration())
			.authorizationStrategy(from.getAuthorizationStrategy())
			.headers(from.getHeaders().getHeaders())
			.queryParams(from.getQueryParams().getParams());		
	}
	
	/**
	 * Returns the builder for a target.
	 * @return the builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Target [configuration=" + configuration + ", implementation=" + implementation + ", headers=" 
				+ getHeaders() + ", interceptors=" + getInterceptors() + ", uri=" + uri + "]";
	}

	/**
	 * The builder class for target.
	 */
	public static class Builder extends AbstractRequestSpec<Builder> {

		private TargetImplementation     implementation;
		private RestClientConfiguration  configuration;
		
		protected Builder() {
		}
		
		/**
		 * Build with given target implementation.
		 * @param implementation target imlementation
		 * @return this builder for chaining
		 */
		public Builder with(TargetImplementation implementation) {
			this.implementation = implementation;
			return this;
		}
		
		/**
		 * Build with given configuration.
		 * @param configuration the configuration
		 * @return this builder for chaining
		 */
		public Builder with(RestClientConfiguration configuration) {
			this.configuration = configuration;
			return this;
		}
		
		/**
		 * Builds a target with specific implementation and configuration.
		 * @return the target built
		 */
		public Target build() {
			if (configuration  == null) throw new RestClientException("configuration must not be null");
			if (implementation == null) throw new RestClientException("implementation must not be null");
			// Usually done by the implementation
			//if (configuration.isVerbose()) register(new LoggingInterceptor());
			return new Target(URI.create(configuration.getUri()), configuration, implementation, getInterceptors(), getHeaders(), getQueryParams(), getAuthorizationStrategy());
		}
	}

}
