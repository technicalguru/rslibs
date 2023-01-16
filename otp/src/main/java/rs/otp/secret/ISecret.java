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

	/**
	 * Returns the otpauth URI scheme to be used e.g. for QR codes.
	 * @param keyId - the key ID
	 * @param numDigits - numbe rof digits.
	 * @return the URI to be used when adding to external auth generators.
	 */
	public String getOtpAuthUri(String keyId, int numDigits);

}
