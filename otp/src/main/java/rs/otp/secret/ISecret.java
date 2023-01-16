/**
 * 
 */
package rs.otp.secret;

/**
 * Interface for secret generators.
 * 
 * @author ralph
 *
 */
public interface ISecret {

	/**
	 * Returns the bytes of this secret.
	 * @return the bytes.
	 */
	public byte[] getBytes();
	
	/**
	 * Returns the encoded form of this secret.
	 * @return encoded secret
	 */
	public String encode();

}
