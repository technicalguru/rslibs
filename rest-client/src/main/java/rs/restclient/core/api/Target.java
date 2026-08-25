/**
 * 
 */
package rs.restclient.core.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.list.UnmodifiableList;

import rs.restclient.core.api.request.RequestBuilder;
import rs.restclient.core.util.UriBuilder;

/**
 * Describes a target and creates your specific API requests.
 */
public class Target {

	private RestClientConfiguration  configuration;
	private TargetImplementation     implementation;
	private List<RequestInterceptor> interceptors;
	private URI                      uri;

	protected Target(RestClientConfiguration configuration, TargetImplementation implementation, List<RequestInterceptor> interceptors, URI uri) {
		this.configuration  = configuration;
		this.implementation = implementation;
		this.interceptors   = new ArrayList<>(interceptors);
		this.uri            = uri != null ? uri : URI.create(configuration.getUri());
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
	 * Returns the list of interceptors.
	 * @return the interceptors
	 */
	public List<RequestInterceptor> getInterceptors() {
		return UnmodifiableList.unmodifiableList(interceptors);
	}

	/**
	 * Register the interceptor for execution in this target.
	 * @param interceptor interceptor
	 */
	public void register(RequestInterceptor interceptor) {
		this.interceptors.add(interceptor);
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
		return new Target(configuration, implementation, interceptors, uriBuilder.appendSegments(path).build());
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
		return "Target [configuration=" + configuration + ", implementation=" + implementation + ", interceptors="
				+ interceptors + ", uri=" + uri + "]";
	}


	/**
	 * The builder class for target.
	 */
	public static class Builder {

		private TargetImplementation     implementation;
		private RestClientConfiguration  configuration;
		private List<RequestInterceptor> interceptors;
		private URI                      baseUri;
		
		protected Builder() {
			this.interceptors = new ArrayList<>();
		}
		
		public Builder with(TargetImplementation implementation) {
			this.implementation = implementation;
			return this;
		}
		
		public Builder with(RestClientConfiguration configuration) {
			this.configuration = configuration;
			return this;
		}
		
		/**
		 * Register the interceptor for execution.
		 * @param interceptor interceptor
		 * @return this builder for chaining
		 */
		public Builder register(RequestInterceptor interceptor) {
			this.interceptors.add(interceptor);
			return this;
		}
		
		/**
		 * Overwrites URI from configuration.
		 * @param baseUri new base URI
		 * @return this builder for chaining
		 */
		public Builder with(URI baseUri) {
			this.baseUri = baseUri;
			return this;
		}
		
		/**
		 * Builds a target with specific implementation and configuration.
		 * @return the target built
		 */
		public Target build() {
			if (configuration  == null) throw new RestClientException("configuration must not be null");
			if (implementation == null) throw new RestClientException("implementation must not be null");
			return new Target(configuration, implementation, interceptors, baseUri);
		}
	}

}
