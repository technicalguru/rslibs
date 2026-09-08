package rs.restclient.core.api.response;

import rs.restclient.core.api.RestClientException;

/**
 * Exception around a response.
 * @author ralph
 *
 */
public class RestResponseException extends RestClientException {

	private static final long serialVersionUID = 1L;
	
	private RestResponse response;
	
	/**
	 * Constructor with response only.
	 * @param response the response object
	 */
	public RestResponseException(RestResponse response) {
		this(response, null, null);
	}

	/**
	 * Constructor.
	 * @param response the response object
	 * @param message error message
	 * @param cause error cause
	 */
	public RestResponseException(RestResponse response, String message, Throwable cause) {
		super(message != null ? message : createMessage(response), cause);
		this.response = response;
	}

	/**
	 * Constructor.
	 * @param response the response object
	 * @param message error message
	 */
	public RestResponseException(RestResponse response, String message) {
		this(response, message, null);
	}

	/**
	 * Constructor.
	 * @param response the response object
	 * @param cause error cause
	 */
	public RestResponseException(RestResponse response, Throwable cause) {
		this(response, null, cause);
	}

	/**
	 * Returns the response.
	 * @return the response
	 */
	public RestResponse getResponse() {
		return response;
	}

	
	/**
	 * @return
	 * @see rs.restclient.core.api.response.RestResponse#getStatusCode()
	 */
	public final int getStatusCode() {
		return response.getStatusCode();
	}

	/**
	 * @return
	 * @see rs.restclient.core.api.response.RestResponse#getStatusMessage()
	 */
	public final String getStatusMessage() {
		return response.getStatusMessage();
	}

	/**
	 * Creates the exception message from the respose.
	 * @param response the response object
	 * @return the exception message
	 */
	protected static String createMessage(RestResponse response) {
		return "Request returned: "+response.getStatusCode()+" "+response.getStatusMessage();
	}
}
