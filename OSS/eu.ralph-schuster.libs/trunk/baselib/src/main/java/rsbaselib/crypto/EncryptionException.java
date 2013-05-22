/**
 * 
 */
package rsbaselib.crypto;

/**
 * Exception thrown from encryption process.
 * @author ralph
 *
 */
public class EncryptionException extends Exception {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -7127130750843438740L;

	/**
	 * Constructor.
	 */
	public EncryptionException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public EncryptionException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause root cause exception
	 */
	public EncryptionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause exception
	 */
	public EncryptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
