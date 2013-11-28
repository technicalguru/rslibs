/**
 * 
 */
package rs.baselib.licensing;

import java.security.Key;

/**
 * Internal interface for implementations of license generators.
 * @author ralph
 *
 */
public interface ILicenseCreator {

	/**
	 * Generates a key from the given license.
	 * @param license the license to be created.
	 * @return the license key
	 */
	public String createLicenseKey(License license);

	/**
	 * Sets the key for cryptographic operations.
	 * @param key a key to perform cryptographic operations.
	 */
	public void setKey(Key key);

}
