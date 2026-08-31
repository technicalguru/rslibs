package rs.restclient.jersey;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.LogManager;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.util.JacksonFeature;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import rs.baselib.util.CommonUtils;
import rs.restclient.core.api.ProxyConfig;
import rs.restclient.core.api.RestClientConfiguration;
import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.HeadersSpec;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;
import rs.restclient.core.util.LoggingUtils;

/**
 * The implementation of JerseyClient backend.
 * @author ralph
 *
 */
public class JerseyImpl implements TargetImplementation {

	private static Logger         log        = LoggerFactory.getLogger(JerseyImpl.class);
	private static LoggingFeature JUL_LOGGER = new LoggingFeature(new LoggingUtils.JulLogger(log));
	
	/** The instance for usage */
	public static final JerseyImpl JERSEY = new JerseyImpl();

	static {
	    // Install SLF4J bridge
		LogManager.getLogManager().reset();
	    SLF4JBridgeHandler.install();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse execute(RestRequest request) {
		// Create WebTarget
		WebTarget jerseyTarget = createWebTarget(request);
		
		// Query params
		jerseyTarget = applyQueryParams(jerseyTarget, request);
				
		// Build request with JSON response media type
		String mediaType = request.getResponseMediaType();
		Invocation.Builder requestBuilder = mediaType != null ? jerseyTarget.request(MediaType.APPLICATION_JSON) : jerseyTarget.request();
		
		// Add headers
		requestBuilder = requestBuilder.headers(convertHeaders(request.getHeaders()));
		
		// Accept header
		if (mediaType != null) requestBuilder = requestBuilder.header(HttpHeaders.ACCEPT, request.getResponseMediaType());
		
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
		return execute(request, invocation);
	}
	
	/**
	 * Creates Jersey's web target from the URI alone.
	 * @param request the request
	 * @return the Jersey {@link WebTarget}
	 */
	protected WebTarget createWebTarget(RestRequest request) {
		Client client = createClient(request);
		return client.target(request.getUri());
	}
	
	/**
	 * Applies the query parameters to the WebTarget.
	 * @param jerseyTarget the Jersey WebTarget
	 * @param request the request
	 * @return a new WebTarget with all query params applied
	 */
	protected WebTarget applyQueryParams(WebTarget jerseyTarget, RestRequest request) {
		MultiValuedMap<String,Object> queryParams = request.getQueryParams().getParams();
		for (String name : queryParams.keys()) {
			Object values[] = queryParams.get(name).toArray();
			jerseyTarget = jerseyTarget.queryParam(name, values);
		}
		return jerseyTarget;
	}
	
	/**
	 * Creates the actual JAX-WS Jersey client instance.
	 * @param request the request
	 * @return the client
	 * @see #createClientConfig(RestClientConfiguration)
	 */
	protected Client createClient(RestRequest request) {
		return ClientBuilder.newClient(createClientConfig(request.getConfiguration()));
	}
	
	/**
	 * Creates the Jersey client configuration based on the configuration.
	 * @return the config to be used for the JAX-WS Jersey client.
	 */
	protected ClientConfig createClientConfig(RestClientConfiguration configuration) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.connectorProvider(new ApacheConnectorProvider());
		
		// logging
		if (configuration.isVerbose()) {
			clientConfig.register(JUL_LOGGER);
			clientConfig.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_TEXT);
			clientConfig.property(LoggingFeature.DEFAULT_REDACT_HEADERS, CommonUtils.join( ";", LoggingUtils.SENSITIVE_HEADERS));
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
		
		// Fixes a bug in Jersey with content-length header
		clientConfig.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);
		
		return clientConfig;
	}

	/**
	 * Executes the Jersey invocation and creates the reponse. 
	 * @param request the request object
	 * @param invocation Jersey's invocation specification
	 * @return the response object
	 */
	protected RestResponse execute(RestRequest request, Invocation invocation) {
		Response response = invocation.invoke();
		// body
		Optional<String> body = Optional.empty();
		if (response.hasEntity()) {
			Object entity = response.getEntity();
			if (entity instanceof InputStream) try {
				byte b[] = ((InputStream)entity).readAllBytes();
				body = Optional.of(new String(b, StandardCharsets.UTF_8));
			} catch (Exception e) {
				log.error("Error when reading body", e);
			} else {
				body = Optional.of(entity.toString());
			}
		}

		return RestResponse.builder()
			.with(request)
			.withStatus(response.getStatusInfo().getStatusCode(), response.getStatusInfo().getReasonPhrase())
			.with(convertHeaders(response.getHeaders()))
			.with(body)
			.build();
	}

	/**
	 * Convert (request) headers from {@link RestRequest} to Jersey's version of the headers.
	 * @param headersSpec RestClient headers
	 * @return Jersey's headers
	 */
	private static MultivaluedMap<String, Object> convertHeaders(HeadersSpec headersSpec) {
		MultiValuedMap<String, Object> headers = headersSpec.getHeaders();
		MultivaluedMap<String, Object> rc = new MultivaluedHashMap<>();
		for (String name : headers.keySet()) {
			rc.put(name, new ArrayList<>(headers.get(name)));
		}
		return rc;
	}

	/**
	 * Converts Jersey's response headers to {@link RestResponse} headers.
	 * @param headers Jersey's response headers
	 * @return RestResponse headers
	 */
	private static MultiValuedMap<String, String> convertHeaders(MultivaluedMap<String, Object> headers) {
		MultiValuedMap<String, String> rc = new ArrayListValuedHashMap<>();
		for (String name : headers.keySet()) {
			for (Object value : headers.get(name)) {
				rc.put(name, value.toString());
			}
		}
		return rc;
	}
}
