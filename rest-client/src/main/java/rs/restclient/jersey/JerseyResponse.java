package rs.restclient.jersey;

import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;

import jakarta.ws.rs.core.Response;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.response.RestResponse;

/**
 * Response class for Jersey clients.
 * @author ralph
 *
 */
public class JerseyResponse extends RestResponse {

	/**
	 * Constructor.
	 */
	protected JerseyResponse(Target target, Response jerseyResponse) {
		super(target);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Optional<String> retrieveBody() {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MultiValuedMap<String, String> retrieveHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int retrieveStatusCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String retrieveStatusMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
