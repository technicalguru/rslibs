package rs.restclient.springboot;

import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
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

	private ResponseSpec                   response;
	private SpringBootRequestInterceptor   interceptor;
	private MultiValuedMap<String, String> headers;
	private Integer                        statusCode;
	private String                         statusMessage;
	private Optional<String>               body;
 	private boolean                        executed;
 	
	protected SpringBootResponse(Target target, ResponseSpec response, SpringBootRequestInterceptor interceptor) {
		super(target);
		this.response    = response;
		this.interceptor = interceptor;
		this.executed    = false;
	}

	private void checkExecution() {
		if (!executed) {
			String body        = response.body(String.class);
			this.body          = body == null ? Optional.empty() : Optional.of(body);
			this.headers       = interceptor.getHeaders();
			this.statusCode    = interceptor.getStatusCode().value();
			this.statusMessage = interceptor.getStatusMessage();
			executed = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> retrieveBody() {
		checkExecution();
		return body;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MultiValuedMap<String, String> retrieveHeaders() {
		checkExecution();
		return headers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int retrieveStatusCode() {
		checkExecution();
		return statusCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String retrieveStatusMessage() {
		checkExecution();
		return statusMessage;
	}
	
	
}
