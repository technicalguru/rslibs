package rs.restclient.jersey;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.core.Response;
import rs.restclient.core.api.Target;
import rs.restclient.core.api.response.RestResponse;

/**
 * Response class for Jersey clients.
 * @author ralph
 *
 */
public class JerseyResponse extends RestResponse {

	private static Logger log = LoggerFactory.getLogger(JerseyResponse.class);
	
	private Response response;
	
	/**
	 * Constructor.
	 */
	protected JerseyResponse(Target target, Response response) {
		super(target);
		this.response = response;
		response.bufferEntity();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Optional<String> retrieveBody() {
		if (response.hasEntity()) {
			Object entity = response.getEntity();
			if (entity instanceof InputStream) try {
				byte body[] = ((InputStream)entity).readAllBytes();
				return Optional.of(new String(body, StandardCharsets.UTF_8));
			} catch (Exception e) {
				log.error("Error when reading body", e);
				return Optional.empty();
			}
			return Optional.of(entity.toString());
		}
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected MultiValuedMap<String, String> retrieveHeaders() {
		// TODO Auto-generated method stub
		return new ArrayListValuedHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int retrieveStatusCode() {
		return response.getStatusInfo().getStatusCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String retrieveStatusMessage() {
		return response.getStatusInfo().getReasonPhrase();
	}

	
}
