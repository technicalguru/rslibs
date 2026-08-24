/**
 * 
 */
package rs.restclient;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import rs.restclient.util.AbstractClient;
import rs.restclient.util.CookieAuthorizationFilter;
import rs.restclient.util.LoggingFilter;
import rs.restclient.util.ProxyConfig;
import rs.restclient.util.UserAgentFilter;


/**
 * The main entrance class.
 * 
 * @author ralph
 *
 */
public class RsRestClient extends AbstractClient {

	/** Default name for User-Agent */
	public static String NAME    = "jersey-client";
	/** Default version for User-Agent */
	public static String VERSION = "1.0.0";
	/** Default URL for User-Agent */
	public static String URL     = "https://github.com/technicalguru/jersey-client";
	
	private RestClient         client;
	private ClientConfig config;

	/**
	 * Constructor.
	 * <p>Be aware that the constructor immediately calls {@link #configure(ClientConfig)} and
	 *    {@link #authorize()}.</p>
	 * @param config - the config to be used
	 */
	public RsRestClient(ClientConfig config) {
		super();
		configure(config);
		authorize();
	}

	/**
	 * Constructor.
	 * <p>Be aware that the constructor immediately calls {@link #authorize()}.</p>
	 * @param config - the config to be used
	 */
	public RsRestClient(RestClient config) {
		super();
		authorize();
	}

	/**
	 * Configures JAX-RS client and main web target based on this config.
	 * <p>Calls {@link #createClient()} to actually create the client.</p>
	 * @param config - the config to be used
	 * @see #createClient()
	 */
	protected void configure(ClientConfig config) {
		this.config = config;
		this.client = createClient();
	}
	
	/**
	 * Creates the actual JAX-WS Jersey client instance.
	 * <p>Desecendants can override this method when they want full control over the creation of the client.</p>
	 * <p>Also calls {@link #configure(Client)}.</p>
	 * @return the client
	 * @see #createClientConfig()
	 * @see #configure(Client)
	 */
	protected RestClient createClient() {
		RestClient.Builder builder = createClientBuilder();
		//.newClient(createClientConfig());
		configure(builder);
		return builder.build();
	}
	
	protected RestClient.Builder createClientBuilder() {
		return RestClient.builder();
	}
	
	/**
	 * Configures the client by setting the default auth filter and - if applicable - the
	 * proxy filter.
	 * @param client the client to configure
	 * @see #getAuthorizationFilter()
	 */
	protected RestClient.Builder configure(RestClient.Builder builder) {
		builder = builder
			.baseUrl(getConfig().getUri())
			.requestInterceptor(new UserAgentFilter(getUserAgent()));
		ClientHttpRequestInterceptor authFilter = getAuthorizationFilter();
		if (authFilter != null) builder = builder.requestInterceptor(authFilter);
		
		if (config.isVerbose()) {
			builder = builder.requestInterceptor(new LoggingFilter());
		}
		ProxyConfig proxyConfig = config.getProxyConfig();
		if ((proxyConfig != null) && (proxyConfig.getProxyHost() != null)) {
			throw new RuntimeException("HTTP Proxy is currently not supported");
//			clientConfig.connectorProvider(new ApacheConnectorProvider());
//			clientConfig.property(ClientProperties.PROXY_URI, "http://"+proxyConfig.getProxyHost()+":"+proxyConfig.getProxyPort());
//			if (proxyConfig.getUsername() != null) {
//				clientConfig.property(ClientProperties.PROXY_USERNAME, proxyConfig.getUsername());
//				clientConfig.property(ClientProperties.PROXY_PASSWORD, proxyConfig.getPassword());
//			}
		}

		return builder;
	}
	
	/**
	 * Returns the User-Agent string to be injected in calls.
	 * @return the user agent string
	 * @see #NAME
	 * @see #VERSION
	 * @see #URL
	 * @see UserAgentFilter
	 */
	protected String getUserAgent() {
		return NAME+"/"+VERSION+" ("+URL+")";
	}
	
	/**
	 * Returns the default filter(s) for authorization.
	 * @return the authorization filter.
	 * @see CookieAuthorizationFilter
	 */
	protected ClientHttpRequestInterceptor getAuthorizationFilter() {
		return new CookieAuthorizationFilter();
	}
	
	/**
	 * Returns the configured Jersey client.
	 * @return the client
	 */
	public RestClient getClient() {
		return client;
	}

	/**
	 * Returns the config of this client.
	 * @return the config
	 */
	public ClientConfig getConfig() {
		return config;
	}

	/**
	 * Base method for authentication.
	 * <p>The method is called after configuration from Constructor. You can override it 
	 * to implement an automatic authentication.</P>
	 * <p>The default implementation does nothing.</p>
	 */
	protected void authorize() {
	}
	
	/**
	 * Close the client.
	 */
	public void close() {
	}

}
