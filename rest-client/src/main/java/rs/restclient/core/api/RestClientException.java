/**
 * 
 */
package rs.restclient.core.api;

/**
 * Base class for all exceptions.
 */
public class RestClientException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public RestClientException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause error cause
	 */
	public RestClientException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public RestClientException(String message) {
		this(message, null);
	}

	/**
	 * Constructor.
	 * @param cause error cause
	 */
	public RestClientException(Throwable cause) {
		this(null, cause);
	}

	
}
