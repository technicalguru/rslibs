package rs.restclient.springboot;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

import rs.restclient.core.api.Target;
import rs.restclient.core.api.request.AbstractRequestBuilder;
import rs.restclient.core.api.request.Entity;
import rs.restclient.core.api.response.RestResponse;

/**
 * Request Builder for Spring Boot
 * @author ralph
 *
 */
public class SpringBootRequestBuilder extends AbstractRequestBuilder {

	private ResponseSpec response;
	
	/**
	 * Creates the builder.
	 * @param target the target of the request
	 */
	public SpringBootRequestBuilder(Target target) {
		super(target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RestResponse method(String name, Entity<?> entity) {
		RestClient.Builder builder = builder()
				.baseUrl(getTarget().getUri());
		RestClient client = builder.build();
		response = client.method(HttpMethod.valueOf(name)).retrieve();
		return new SpringBootResponse(getTarget(), response);
	}

	private RestClient.Builder builder() {
		return RestClient.builder();
	}
}
