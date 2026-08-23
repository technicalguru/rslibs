/**
 * 
 */
package rs.mail.templates;

/**
 * Thrown when message building fails.
 * 
 * @author ralph
 *
 */
public class BuilderException extends Exception {

	private static final long serialVersionUID = 1L;
	/** The builder that threw the exception */
	private MessageBuilder<?> builder;
	
	/**
	 * Constructor.
	 * @param builder - the message builder causing the exception
	 */
	public BuilderException(MessageBuilder<?> builder) {
		this.builder = builder;
	}

	/**
	 * Constructor.
	 * @param builder - the message builder causing the exception
	 * @param message - error message
	 */
	public BuilderException(MessageBuilder<?> builder, String message) {
		super(message);
		this.builder = builder;
	}

	/**
	 * Constructor.
	 * @param builder - the message builder causing the exception
	 * @param cause - root cause
	 */
	public BuilderException(MessageBuilder<?> builder, Throwable cause) {
		super(cause);
		this.builder = builder;
	}

	/**
	 * Constructor.
	 * @param builder - the message builder causing the exception
	 * @param message - error message
	 * @param cause - root cause
	 */
	public BuilderException(MessageBuilder<?> builder, String message, Throwable cause) {
		super(message, cause);
		this.builder = builder;
	}

	/**
	 * Returns the builder that caused the issue
	 * @return the builder
	 */
	public MessageBuilder<?> getBuilder() {
		return builder;
	}

	
}
