/**
 * 
 */
package rs.jerseyclient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import rs.jerseyclient.util.ProxyConfig;

/**
 * Holds configuration info for the client.
 * <p>The class can be subclassed to add more config information.</p>
 * 
 * @author ralph
 *
 */
public class JerseyClientConfig {

	private String uri;
	private boolean verbose         = false;
	private ObjectMapper mapper     = null;
	private ProxyConfig proxyConfig = null;
	
	/**
	 * Default constructor.
	 */
	public JerseyClientConfig() {
	}

	/**
	 * Constructor.
	 * @param uri - Base URI of the API that needs to be targeted.
	 * @param verbose - whether the Jersey client shall be configured to be verbose.
	 */
	public JerseyClientConfig(String uri, boolean verbose) {
		this.uri     = uri;
		this.verbose = verbose;
	}

	/**
	 * Returns the base URI of the API that needs to be targeted.
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the base URI of the API that needs to be targeted.
	 * @param uri - Base URI of the API that needs to be targeted.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Returns whether the Jersey client shall be configured to be verbose
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Sets whether the Jersey client shall be configured to be verbose.
	 * @param verbose - whether the Jersey client shall be configured to be verbose.
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Returns the mapper.
	 * @return the mapper
	 */
	@JsonIgnore
	public ObjectMapper getObjectMapper() {
		return mapper;
	}

	/**
	 * Sets the mapper.
	 * @param mapper the mapper to set
	 */
	@JsonIgnore
	public void setObjectMapper(ObjectMapper mapper) {
		this.mapper = mapper;
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

}
