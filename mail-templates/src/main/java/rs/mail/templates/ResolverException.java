/**
 * 
 */
package rs.mail.templates;

/**
 * Thrown when resolving fails.
 * 
 * @author ralph
 *
 */
public class ResolverException extends Exception {

	private static final long serialVersionUID = 1L;
	/** The resolver that threw the exception */
	private Resolver<?> resolver;
	
	/**
	 * Constructor.
	 * @param resolver - the message resolver causing the exception
	 */
	public ResolverException(Resolver<?> resolver) {
		this.resolver = resolver;
	}

	/**
	 * Constructor.
	 * @param resolver - the message resolver causing the exception
	 * @param message - error message
	 */
	public ResolverException(Resolver<?> resolver, String message) {
		super(message);
		this.resolver = resolver;
	}

	/**
	 * Constructor.
	 * @param resolver - the message resolver causing the exception
	 * @param cause - root cause
	 */
	public ResolverException(Resolver<?> resolver, Throwable cause) {
		super(cause);
		this.resolver = resolver;
	}

	/**
	 * Constructor.
	 * @param resolver - the message resolver causing the exception
	 * @param message - error message
	 * @param cause - root cause
	 */
	public ResolverException(Resolver<?> resolver, String message, Throwable cause) {
		super(message, cause);
		this.resolver = resolver;
	}

	/**
	 * Returns the resolver that caused the issue
	 * @return the resolver
	 */
	public Resolver<?> getResolver() {
		return resolver;
	}

	
}
