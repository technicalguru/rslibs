/**
 * 
 */
package rsbaselib.crypto;

/**
 * Exception thrown from signing process.
 * @author U434983
 *
 */
public class SigningException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9041677196642112473L;

	/**
	 * Constructor.
	 */
	public SigningException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public SigningException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause root cause exception
	 */
	public SigningException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause exception
	 */
	public SigningException(String message, Throwable cause) {
		super(message, cause);
	}

}
