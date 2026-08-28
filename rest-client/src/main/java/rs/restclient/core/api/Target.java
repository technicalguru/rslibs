/**
 * 
 */
package rs.restclient.core.api;

import java.net.URI;
import java.util.List;

import rs.restclient.core.api.auth.AuthorizationStrategy;
import rs.restclient.core.api.request.AbstractRequestSpec;
import rs.restclient.core.api.request.HeadersSpec;
import rs.restclient.core.api.request.RequestBuilder;
import rs.restclient.core.util.UriBuilder;

/**
 * Describes a target and creates your specific API requests.
 */
public class Target extends AbstractRequestSpec {

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
				parentTarget.getHeaders(), parentTarget.getAuthorizationStrategy());
	}
	
	/**
	 * Main Constructor.
	 * @param uri the URI for the target
	 * @param configuration the configuration
	 * @param implementation the underlying implementation
	 * @param interceptors interceptor list
	 * @param headers headers to be present
	 * @param authorizationStrategy the authorization strategy
	 */
	private Target(URI uri, RestClientConfiguration configuration, TargetImplementation implementation,
			List<RequestInterceptor> interceptors, HeadersSpec headers, AuthorizationStrategy authorizationStrategy) {
		super(headers, interceptors, authorizationStrategy);
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
	
	/**
	 * Returns the request builder.
	 * @return the request builder
	 */
	public RequestBuilder request() {
		return new RequestBuilder(this);
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
	public static class Builder extends AbstractRequestSpec {

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
			return new Target(URI.create(configuration.getUri()), configuration, implementation, getInterceptors(), getHeaders(), getAuthorizationStrategy());
		}
	}

}
