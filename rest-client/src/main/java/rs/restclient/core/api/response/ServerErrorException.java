package rs.restclient.core.api.response;

/**
 * Response was a server error (5xx).
 * 
 * @author ralph
 *
 */
public class ServerErrorException extends RestResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param response response object
	 */
	public ServerErrorException(RestResponse response) {
		super(response);
	}

}
