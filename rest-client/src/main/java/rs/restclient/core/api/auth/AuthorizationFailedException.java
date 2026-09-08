package rs.restclient.core.api.auth;

import rs.restclient.core.api.RestClientException;

/**
 * Thrown by the {@link AuthorizationStrategy} when authorization fails.
 * @author ralph
 *
 */
public class AuthorizationFailedException extends RestClientException {

	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor.
	 */
	public AuthorizationFailedException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause error cause
	 */
	public AuthorizationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public AuthorizationFailedException(String message) {
		this(message, null);
	}

	/**
	 * Constructor.
	 * @param cause error cause
	 */
	public AuthorizationFailedException(Throwable cause) {
		this(null, cause);
	}

}
