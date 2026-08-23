package rs.jerseyclient.util;

/**
 * Proxy configuration
 * 
 * @author ralph
 *
 */
public class ProxyConfig {

	private String proxyHost;
	private int    proxyPort;
	private String username;
	private String password;
	
	/**
	 * Default constructor.
	 */
	public ProxyConfig() {
	}

	/**
	 * Constructor.
	 * @param proxyHost - the proxy host
	 * @param proxyPort - the proxy port (usually 8080)
	 */
	public ProxyConfig(String proxyHost, int proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	/**
	 * Constructor for authenticated proxy.
	 * @param proxyHost - the proxy host
	 * @param proxyPort - the proxy port (usually 8080)
	 * @param username - proxy username
	 * @param password - proxy password
	 */
	public ProxyConfig(String proxyHost, int proxyPort, String username, String password) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.username = username;
		this.password = password;
	}

	/**
	 * Returns the proxyHost.
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets the proxyHost.
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * Returns the proxyPort.
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets the proxyPort.
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * Returns the username.
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
}
