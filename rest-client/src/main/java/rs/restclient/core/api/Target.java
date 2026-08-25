/**
 * 
 */
package rs.restclient.core.api;

import java.net.URI;

import rs.restclient.core.util.UriBuilder;

/**
 * Describes a target and creates your specific API requests.
 */
public class Target {

	private RestClientConfiguration  configuration;
	private TargetImplementation     implementation;
	private URI                      uri;

	protected Target(RestClientConfiguration configuration, TargetImplementation implementation, URI uri) {
		this.configuration  = configuration;
		this.implementation = implementation;
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
		return new Target(configuration, implementation, uriBuilder.appendSegments(path).build());
	}
	
	/**
	 * Returns the request builder.
	 * @return the request builder
	 */
	public RequestBuilder request() {
		return implementation.requestBuilder(this);
	}
	
	/**
	 * Returns the builder for a target.
	 * @return the builder
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * The builder class for target.
	 */
	public static class Builder {

		private TargetImplementation     implementation;
		private RestClientConfiguration  configuration;
		private URI                      baseUri;
		protected Builder() {
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
			return new Target(configuration, implementation, baseUri);
		}
	}

}
