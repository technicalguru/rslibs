package rs.restclient.core.api.response;

/**
 * Response was redirection (3xx).
 * 
 * @author ralph
 *
 */
public class RedirectionException extends RestResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param response response object
	 */
	public RedirectionException(RestResponse response) {
		super(response);
	}

}
