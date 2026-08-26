package rs.restclient.springboot;

import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestBodySpec;
import org.springframework.web.client.RestClient.RequestBodyUriSpec;
import org.springframework.web.client.RestClient.ResponseSpec;

import rs.restclient.core.api.TargetImplementation;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * The implementation of SpringBoot backend.
 * @author ralph
 *
 */
public class SpringBootImpl implements TargetImplementation {

	/** The instance for usage */
	public static final SpringBootImpl BUILDER = new SpringBootImpl();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse execute(RestRequest request) {
		RestClient.Builder builder = clientBuilder()
				.baseUrl(request.getTarget().getUri())
				.defaultHeaders((headers) -> {
					MultiValuedMap<String, Object> requestHeaders = request.getHeaders().getHeaders();
					for (String name : requestHeaders.keySet()) {
						for (Object value : requestHeaders.get(name)) {
							if (value != null) headers.add(name, value.toString());
						}
					}
				});
		if (request.getTarget().getConfiguration().isVerbose()) {
			builder = builder.requestInterceptor(new VerboseRequestInterceptor());
		}
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
		return new SpringBootResponse(request.getTarget(), responseSpec);
	}

	/**
	 * The SpringBoot request builder.
	 * @return the builder
	 */
	private static RestClient.Builder clientBuilder() {
		return RestClient.builder();
	}

}
