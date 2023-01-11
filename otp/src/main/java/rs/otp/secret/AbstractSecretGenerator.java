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
public abstract class AbstractSecretGenerator implements ISecretGenerator {

	private int defaultLength;
	
	/**
	 * Constructor with a default length definition
	 * @param defaultLength - the default length of secrets to be generated.
	 */
	protected AbstractSecretGenerator(int defaultLength) {
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
