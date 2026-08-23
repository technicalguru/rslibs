/**
 * 
 */
package rs.jerseyclient;

import java.util.logging.Level;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import com.fasterxml.jackson.core.util.JacksonFeature;

import rs.jerseyclient.util.AbstractClient;
import rs.jerseyclient.util.CookieAuthorizationFilter;
import rs.jerseyclient.util.ObjectMapperProvider;
import rs.jerseyclient.util.ProxyConfig;
import rs.jerseyclient.util.UserAgentFilter;


/**
 * The main entrance class.
 * 
 * @author ralph
 *
 */
public class JerseyClient extends AbstractClient {

	/** Default name for User-Agent */
	public static String NAME    = "jersey-client";
	/** Default version for User-Agent */
	public static String VERSION = "1.0.0";
	/** Default URL for User-Agent */
	public static String URL     = "https://github.com/technicalguru/jersey-client";
	
	private Client             client;
	private JerseyClientConfig config;

	/**
	 * Constructor.
	 * <p>Be aware that the constructor immediately calls {@link #configure(JerseyClientConfig)} and
	 *    {@link #authorize()}.</p>
	 * @param config - the config to be used
	 */
	public JerseyClient(JerseyClientConfig config) {
		super();
		configure(config);
		authorize();
	}

	/**
	 * Constructor.
	 * <p>Be aware that the constructor immediately calls {@link #authorize()}.</p>
	 * @param config - the config to be used
	 */
	public JerseyClient(Client config) {
		super();
		authorize();
	}

	/**
	 * Configures JAX-RS client and main web target based on this config.
	 * <p>Calls {@link #createClient()} to actually create the client.</p>
	 * @param config - the config to be used
	 * @see #createClient()
	 */
	protected void configure(JerseyClientConfig config) {
		this.config = config;
		this.client = createClient();
		setTarget(client.target(config.getUri()));
	}
	
	/**
	 * Creates the Jersey client configuration based on the config for this client.
	 * <p>Descendants can override this method in order to manipulate the config.</p>
	 * @return the config to be used for the JAX-WS Jersey client.
	 */
	protected ClientConfig createClientConfig() {
		ClientConfig clientConfig = new ClientConfig();
		if (config.isVerbose()) {
			clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_TEXT);
			clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, Level.INFO.getName());
		}
		if (config.getObjectMapper() != null) {
			ObjectMapperProvider.setMapper(config.getObjectMapper());
			clientConfig.register(ObjectMapperProvider.class);
			clientConfig.register(JacksonFeature.class);
		}
		ProxyConfig proxyConfig = config.getProxyConfig();
		if ((proxyConfig != null) && (proxyConfig.getProxyHost() != null)) {
			clientConfig.connectorProvider(new ApacheConnectorProvider());
			clientConfig.property(ClientProperties.PROXY_URI, "http://"+proxyConfig.getProxyHost()+":"+proxyConfig.getProxyPort());
			if (proxyConfig.getUsername() != null) {
				clientConfig.property(ClientProperties.PROXY_USERNAME, proxyConfig.getUsername());
				clientConfig.property(ClientProperties.PROXY_PASSWORD, proxyConfig.getPassword());
			}
		}

		return clientConfig;
	}

	/**
	 * Creates the actual JAX-WS Jersey client instance.
	 * <p>Desecendants can override this method when they want full control over the creation of the client.</p>
	 * <p>Also calls {@link #configure(Client)}.</p>
	 * @return the client
	 * @see #createClientConfig()
	 * @see #configure(Client)
	 */
	protected Client createClient() {
		Client rc = ClientBuilder.newClient(createClientConfig());
		configure(rc);
		return rc;
	}
	
	/**
	 * Configures the client by setting the default auth filter and - if applicable - the
	 * proxy filter.
	 * @param client the client to configure
	 * @see #getAuthorizationFilter()
	 */
	protected void configure(Client client) {
		client.register(new UserAgentFilter(getUserAgent()));
		ClientRequestFilter authFilter = getAuthorizationFilter();
		if (authFilter != null) client.register(authFilter);
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
	protected ClientRequestFilter getAuthorizationFilter() {
		return new CookieAuthorizationFilter();
	}
	
	/**
	 * Returns the configured Jersey client.
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Returns the config of this client.
	 * @return the config
	 */
	public JerseyClientConfig getConfig() {
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
		client.close();
	}

}
