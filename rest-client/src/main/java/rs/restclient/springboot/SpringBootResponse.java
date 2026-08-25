package rs.restclient.springboot;

import java.util.Optional;

import org.springframework.web.client.RestClient.ResponseSpec;

import rs.restclient.core.api.Target;
import rs.restclient.core.api.response.RestResponse;

/**
 * The response.
 * 
 * @author ralph
 *
 */
public class SpringBootResponse extends RestResponse {

	private ResponseSpec response;
	
	protected SpringBootResponse(Target target, ResponseSpec response) {
		super(target);
		this.response = response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> retrieveBody() {
		String body = response.body(String.class);
		return body == null ? Optional.empty() : Optional.of(body);
	}
	
	
}
