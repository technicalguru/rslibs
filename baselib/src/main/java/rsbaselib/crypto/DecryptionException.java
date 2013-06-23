/**
 * 
 */
package rsbaselib.crypto;

/**
 * Exception thrown from encryption process.
 * @author ralph
 *
 */
public class DecryptionException extends Exception {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1450694907049334620L;

	/**
	 * Constructor.
	 */
	public DecryptionException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public DecryptionException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause root cause exception
	 */
	public DecryptionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause exception
	 */
	public DecryptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
