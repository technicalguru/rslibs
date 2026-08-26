/**
 * 
 */
package rs.restclient.core.api;

import tools.jackson.databind.json.JsonMapper;

/**
 * Describes the main aspect of a client
 * such as logging, authorization, logging
 * and object mapping.
 */
public class RestClientConfiguration {

	private String      uri         = null;
	private boolean     verbose     = false;
	private JsonMapper  mapper      = null;
	private ProxyConfig proxyConfig = null;
	
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

	
	// missing: how to describe client authorization: cookie or token (fixed) or by auth/renew.
	
	
}
