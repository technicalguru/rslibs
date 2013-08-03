/**
 * 
 */
package rs.baselib.security;

/**
 * A callback that will deliver passwords.
 * @author ralph
 *
 */
public interface IPasswordCallback {

	/**
	 * Delivers the password.
	 * @return the password
	 */
	public char[] getPassword();

	/**
	 * Delivers the salt for encryption.
	 * @return the salt
	 */
	public byte[] getSalt();
	
}
