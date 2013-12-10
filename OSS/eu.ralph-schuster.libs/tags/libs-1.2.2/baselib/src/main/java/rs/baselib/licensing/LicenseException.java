/**
 * 
 */
package rs.baselib.licensing;

/**
 * A License problem.
 * @author ralph
 *
 */
public class LicenseException extends RuntimeException {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public LicenseException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message
	 * @param cause
	 */
	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message
	 */
	public LicenseException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause
	 */
	public LicenseException(Throwable cause) {
		super(cause);
	}


}
