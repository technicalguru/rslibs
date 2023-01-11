/**
 * 
 */
package rs.otp.secret;

/**
 * Abstract implementation of a secret generator.
 * 
 * @author ralph
 *
 */
public abstract class AbstractSecret implements ISecret {

	private int defaultLength;
	
	/**
	 * Constructor with a default length definition
	 * @param defaultLength - the default length of secrets to be generated.
	 */
	protected AbstractSecret(int defaultLength) {
		this.defaultLength = defaultLength;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate() {
		return generate(defaultLength);
	}

	
}
