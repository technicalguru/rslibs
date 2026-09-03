package rs.restclient.core.api.response;

/**
 * Response was a client error (4xx).
 * 
 * @author ralph
 *
 */
public class ClientErrorException extends RestResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param response response object
	 */
	public ClientErrorException(RestResponse response) {
		super(response);
	}

}
