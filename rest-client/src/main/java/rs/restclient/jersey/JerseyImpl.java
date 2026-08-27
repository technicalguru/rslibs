package rs.restclient.jersey;

import java.util.logging.Level;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.logging.LoggingFeature;

import com.fasterxml.jackson.core.util.JacksonFeature;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import rs.restclient.core.api.ProxyConfig;
import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * The implementation of JerseyClient backend.
 * @author ralph
 *
 */
public class JerseyImpl implements TargetImplementation {

	/** The instance for usage */
	public static final JerseyImpl JERSEY = new JerseyImpl();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse execute(RestRequest request) {
		Target target = request.getTarget();
		
		// Create WebTarget
		WebTarget jerseyTarget = createWebTarget(target);
		
		// Build request with JSON response media type and set headers
		String mediaType = request.getResponseMediaType();
		Invocation.Builder requestBuilder = mediaType != null ? jerseyTarget.request(MediaType.APPLICATION_JSON) : jerseyTarget.request();
		// TODO requestBuilder = requestBuilder.header(headerName, value);
		
		// Build invocation with/without request body
		Entity<?> entity = request.getEntity();
		Invocation invocation = null;
		if (entity == null) {
			invocation = requestBuilder.build(request.getMethod());
		} else {
			jakarta.ws.rs.client.Entity<?> jerseyEntity = jakarta.ws.rs.client.Entity.entity(entity.getEntity(), entity.getMediaType());
			invocation = requestBuilder.build(request.getMethod(), jerseyEntity);
		}
		
		// Invoke
		Response response = invocation.invoke();
		return new JerseyResponse(request.getTarget(), response);
	}
	
	protected WebTarget createWebTarget(Target target) {
		Client client = createClient(target);
		return client.target(target.getUri());
	}
	
	/**
	 * Creates the actual JAX-WS Jersey client instance.
	 * @return the client
	 * @see #createClientConfig()
	 */
	protected Client createClient(Target target) {
		return ClientBuilder.newClient(createClientConfig(target.getConfiguration()));
	}
	
	/**
	 * Creates the Jersey client configuration based on the configuration.
	 * @return the config to be used for the JAX-WS Jersey client.
	 */
	protected ClientConfig createClientConfig(RestClientConfiguration configuration) {
		ClientConfig clientConfig = new ClientConfig();
		if (configuration.isVerbose()) {
			clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_TEXT);
			clientConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, Level.INFO.getName());
		}
		if (configuration.getMapper2() != null) {
			JsonMapperProvider.setMapper(configuration.getMapper2());
			clientConfig.register(JsonMapperProvider.class);
			clientConfig.register(JacksonFeature.class);
		}
		ProxyConfig proxyConfig = configuration.getProxyConfig();
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

	

}
