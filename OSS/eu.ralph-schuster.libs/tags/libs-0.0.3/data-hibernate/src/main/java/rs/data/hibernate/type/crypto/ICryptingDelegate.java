/**
 * 
 */
package rs.data.hibernate.type.crypto;

/**
 * Delegate for encrypting and decrypting.
 * @author ralph
 *
 */
public interface ICryptingDelegate {

	/**
	 * Initialize the delegate.
	 * @param factory factory for the delegate.
	 */
	public void init(ICryptingDelegateFactory factory);
	
	/**
	 * Encrypts the given bytes.
	 * @param bytes bytes to be encrypted
	 * @return encrypted bytes
	 * @throws Exception when an error occurs
	 */
	public byte[] encrypt(byte[] bytes) throws Exception;
	
	/**
	 * Decrypts the given bytes.
	 * @param bytes bytes to be decrypted
	 * @return decrypted bytes
	 * @throws Exception when an error occurs
	 */
	public byte[] decrypt(byte[] bytes) throws Exception;
}
