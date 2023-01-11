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

	private byte[] bytes;
	
	/**
	 * Constructor with byte array of secret
	 * @param bytes - the bytes of this secret.
	 */
	public AbstractSecret(byte[] bytes) {
		this.bytes = bytes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getBytes() {
		return bytes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return encode();
	}
}
