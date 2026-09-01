package rs.restclient.springboot;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import rs.baselib.util.UriBuilder;
import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * The implementation of SpringBoot backend.
 * <p>You can sub-class this implementation in case you need additional SpringBoot RestClient
 *    configuration settings to be done.
 * @author ralph
 *
 */
public class SpringBootImpl implements TargetImplementation {

	/** The instance for usage */
	public static final SpringBootImpl SPRING_BOOT = new SpringBootImpl();

	private List<HttpMessageConverter<?>> converters = null;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse execute(RestRequest request) {
		URI uri = createUri(request);
		
		RestClient.Builder builder = clientBuilder().baseUrl(uri);
		builder = applyHeaders(builder, request);
		builder = applyInterceptors(builder, request);
		
		// Verbosity and response catching
		SpringBootRequestInterceptor interceptor = new SpringBootRequestInterceptor(request.getConfiguration().isVerbose());
		builder = builder.requestInterceptor(interceptor);

		RestClient client = builder.build();
		RequestBodyUriSpec uriSpec = client.method(HttpMethod.valueOf(request.getMethod()));
		Entity<?> entity = request.getEntity();
		ResponseSpec responseSpec = null;
		if (entity != null) {
			RequestBodySpec bodySpec = uriSpec
				.body(entity.getEntity())
				.contentType(MediaType.valueOf(entity.getMediaType()));
			responseSpec = bodySpec.retrieve();
		} else {
			responseSpec = uriSpec.retrieve();
		}
		return execute(request, responseSpec, interceptor);
	}

	/**
	 * Executes the SpringBoot specification and creates the RestResponse from the result.
	 * @param request request object
	 * @param response SpringBoot specification
	 * @param interceptor interceptor that catches all required information.
	 * @return the response created
	 */
	protected RestResponse execute(RestRequest request, ResponseSpec response, SpringBootRequestInterceptor interceptor) {
		String body = response.body(String.class);

		return RestResponse.builder()
				.with(request)
				.withStatus(interceptor.getStatusCode().value(), interceptor.getStatusMessage())
				.with(interceptor.getHeaders())
				.with(body == null ? Optional.empty() : Optional.of(body))
				.build();
	}
	
	/**
	 * Builds th complete URI using the information from the request
	 * @param request the request
	 * @return the complete URI to query
	 */
	protected URI createUri(RestRequest request) {
		UriBuilder uriBuilder = UriBuilder.from(request.getUri());
		MultiValuedMap<String,Object> queryParams = request.getQueryParams().getParams();
		for (String name : queryParams.keys()) {
			for (Object value : queryParams.get(name).toArray()) {
				if (value != null) uriBuilder.queryParams().put(name, value.toString());
			}
		}
		return uriBuilder.build();
	}
	
	/**
	 * Applies a required and configured headers from the request
	 * @param builder the builder to apply on
	 * @param request the request
	 * @return the builder
	 */
	protected RestClient.Builder applyHeaders(RestClient.Builder builder, RestRequest request) {
		return builder.defaultHeaders((headers) -> {
			MultiValuedMap<String, Object> requestHeaders = request.getHeaders().getHeaders();
			if (request.getResponseMediaType() != null) headers.add(HttpHeaders.ACCEPT, request.getResponseMediaType());
			for (String name : requestHeaders.keySet()) {
				for (Object value : requestHeaders.get(name)) {
					if (value != null) headers.add(name, value.toString());
				}
			}
		});
	}
	
	/**
	 * Apply any additional interceptors.
	 * @param builder the builder to apply on
	 * @param request the request
	 * @return the builder
	 */
	protected RestClient.Builder applyInterceptors(RestClient.Builder builder, RestRequest request) {
		return builder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder rc = new StringBuilder();
		for (HttpMessageConverter<?> converter : getConverters()) {
			if (rc.length() > 0) rc.append(",");
			rc.append(converter.getClass().getName());
		}
		return "SpringBootImpl [converters={"+rc.toString()+"}]";
	}

	/**
	 * The SpringBoot request builder.
	 * @return the builder
	 */
	protected RestClient.Builder clientBuilder() {
		return registerConverters(RestClient.builder());
	}

	/**
	 * Register a converter for SpringBoot in case the request body encoding is
	 * not supported (error: "no HttpMessageConverter for &lt;type&gt;").
	 * <p>The converter is inserted at the beginning of the default list
	 *    of SpringBoot converters so it overwrites any default for the same
	 *    MediaType and entity class.
	 * @param converter converter to register
	 */
	protected void registerMessageConverter(HttpMessageConverter<?> converter) {
		List<HttpMessageConverter<?>> converters = getConverters();
		converters.add(0, converter);
	}
	
	/**
	 * Register converters for message content.
	 * @param builder
	 * @return
	 */
	protected RestClient.Builder registerConverters(RestClient.Builder builder) {
		return builder
			.configureMessageConverters(clientBuilder -> {
				// Unfortunately we have to do this manually each time
				//HttpMessageConverters.forClient().registerDefaults().build().forEach(c -> clientBuilder.addCustomConverter(c));
				for (HttpMessageConverter<?> converter : getConverters()) {
					clientBuilder.addCustomConverter(converter);
				}
			});
	}
	
	protected List<HttpMessageConverter<?>> getConverters() {
		if (converters == null) {
			converters = new ArrayList<>();
			converters.add(new FormMessageConverter());
			HttpMessageConverters.forClient().registerDefaults().build().forEach(c -> converters.add(c));
		}
		return converters;
	}
}
