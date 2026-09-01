/**
 * 
 */
package rs.restclient.core.api;

import java.net.URI;

import tools.jackson.databind.json.JsonMapper;

/**
 * Describes the main aspect of a client such as logging, authorization, logging and object mapping.
 * <p>The configuration can be sub-classed and you can configure it yourself, or you can use
 *    the generic builder:
 * <pre>
 *    MyConfiguration config = RestClientConfiguration.builder(MyConfiguration.class)
 *    	.with(myUri)
 *    	.verbose(false)          // default: false
 *    	.with(myJsonMapper)      // default: built by Json or Json2
 *      .with(myProxyConfig)     // default: null / current implementation cannot use proxyConfig
 *      .build();
 * </pre>
 * The configuration can use Jackson2 and/or Jackson3 json mappers.
 * Except URI, all properties can be left default.
 */
public class RestClientConfiguration {

	private String      uri         = null;
	private boolean     verbose     = false;
	private JsonMapper  mapper      = null;
	private com.fasterxml.jackson.databind.json.JsonMapper  mapper2     = null;
	private ProxyConfig proxyConfig = null;
	
	
	/**
	 * Constructor.
	 */
	protected RestClientConfiguration() {
	}
	
	/**
	 * Returns the uri.
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * Sets the uri.
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * Returns the verbose.
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}
	/**
	 * Sets the verbose.
	 * @param verbose the verbose to set
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	/**
	 * Returns the mapper.
	 * @return the mapper
	 */
	public JsonMapper getMapper() {
		return mapper;
	}
	/**
	 * Sets the mapper.
	 * @param mapper the mapper to set
	 */
	public void setMapper(JsonMapper mapper) {
		this.mapper = mapper;
	}
	/**
	 * Returns the mapper.
	 * @return the mapper
	 */
	public com.fasterxml.jackson.databind.json.JsonMapper getMapper2() {
		return mapper2;
	}
	/**
	 * Sets the mapper.
	 * @param mapper the mapper to set
	 */
	public void setMapper2(com.fasterxml.jackson.databind.json.JsonMapper mapper) {
		this.mapper2 = mapper;
	}
	/**
	 * Returns the proxyConfig.
	 * @return the proxyConfig
	 */
	public ProxyConfig getProxyConfig() {
		return proxyConfig;
	}
	/**
	 * Sets the proxyConfig.
	 * @param proxyConfig the proxyConfig to set
	 */
	public void setProxyConfig(ProxyConfig proxyConfig) {
		this.proxyConfig = proxyConfig;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "RestClientConfiguration [uri=" + uri + ", verbose=" + verbose + ", proxyConfig=" + proxyConfig + "]";
	}

	/**
	 * Creates a builder for the base {@link RestClientConfiguration}.
	 * @return the new builder
	 */
	public static Builder<RestClientConfiguration> builder() {
		return builder(RestClientConfiguration.class);
	}
	
	/**
	 * Creates a builder using your own configuration class (sub-class of {@link RestClientConfiguration}.
	 * @param <T> Class of configuration
	 * @return the new builder
	 */
	public static <T extends RestClientConfiguration> Builder<T> builder(Class<T> configClass) {
		return new Builder<>(configClass);
	}
	
	/**
	 * A builder for the configuration.
	 * @param <T> the configuration class to build
	 * @author ralph
	 *
	 */
	public static class Builder<T extends RestClientConfiguration> {
		
		private Class<T>    configClass = null;
		private String      uri         = null;
		private boolean     verbose     = false;
		private JsonMapper  mapper      = null;
		private com.fasterxml.jackson.databind.json.JsonMapper  mapper2     = null;
		private ProxyConfig proxyConfig = null;

		private Builder(Class<T> configClass) {
			this.configClass = configClass;
		}
		
		/**
		 * Use the given URI.
		 * @param uri uri to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(String uri) {
			this.uri = uri;
			return this;
		}
		
		/**
		 * Use the given URI.
		 * @param uri uri to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(URI uri) {
			return this.with(uri.toString());
		}
		
		public String uri() {
			return this.uri;
		}
		
		/**
		 * Verbose the request/responses.
		 * @param verbose true when it shall be verbosed output (default: false)
		 * @return this builder for chaining
		 */
		public Builder<T> verbose(boolean verbose) {
			this.verbose = verbose;
			return this;
		}
		
		/**
		 * Whether the client will be verbose
		 * @return true when verbose logging is on
		 */
		public boolean verbose() {
			return this.verbose;
		}
		
		/**
		 * Use the given Jackson 3 {@link JsonMapper}.
		 * @param jsonMapper mapper to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(JsonMapper jsonMapper) {
			this.mapper = jsonMapper;
			return this;
		}
		
		/**
		 * Returns the Jackson 3 {@link JasonMapper}.
		 * @return mapper used or NULL
		 */
		public JsonMapper jsonMapper() {
			return this.mapper;
		}
		
		/**
		 * Use the given Jackson 2 {@link com.fasterxml.jackson.databind.json.JsonMapper JsonMapper}.
		 * @param jsonMapper mapper to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(com.fasterxml.jackson.databind.json.JsonMapper jsonMapper) {
			this.mapper2 = jsonMapper;
			return this;
		}
		
		/**
		 * Returns the Jackson 2 {@link com.fasterxml.jackson.databind.json.JsonMapper JasonMapper}.
		 * @return mapper used or NULL
		 */
		public com.fasterxml.jackson.databind.json.JsonMapper jsonMapper2() {
			return this.mapper2;
		}
		
		/**
		 * Use the given Proxy configuration.
		 * @param proxyConfig proxy configuration to be used
		 * @return this builder for chaining
		 */
		public Builder<T> with(ProxyConfig proxyConfig) {
			this.proxyConfig = proxyConfig;
			return this;
		}
		
		/**
		 * Returns the proxy configuration to be used.
		 * @return config used or NULL
		 */
		public ProxyConfig proxyConfig() {
			return this.proxyConfig;
		}
		
		/**
		 * Builda configuration of the declared class.
		 * @return the class
		 */
		public T build() {
			try {
				T rc = configClass.getDeclaredConstructor().newInstance();
				rc.setUri(uri);
				rc.setVerbose(verbose);
				rc.setMapper(mapper);
				rc.setMapper2(mapper2);
				rc.setProxyConfig(proxyConfig);
				return rc;
			} catch (Throwable t) {
				throw new RestClientException("Cannot create configuration class", t);
			}
		}
	}
	
}
