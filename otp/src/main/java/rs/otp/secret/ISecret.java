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
	 * Generates a secret of given length.
	 * @param length - the length
	 * @return the random secret of given length
	 */
	public String generate(int length);
	
	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public String generate();
	
	/**
	 * Decodes the given secret into bytes.
	 * @param s the secret to be decoded
	 * @return the decodes secret as bytes
	 */
	public byte[] decode(String s);
	
	/**
	 * Encodes the given secret into string representation.
	 * @param bytes the secret to be encoded
	 * @return the encoded secret
	 */
	public String encode(byte bytes[]);
	
}
