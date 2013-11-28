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
public interface ILicenseVerifier {

	/**
	 * Verifies the given license string.
	 * @param licenseKey the license string
	 * @param productId the product ID that must match
	 * @param licenseHolder the license holder that must match
	 * @throws LicenseException when the license is invalid
	 */
	public License verify(String licenseKey, int productId, String licenseHolder);

	/**
	 * Sets the key for cryptographic operations.
	 * @param key a key to perform cryptographic operations.
	 */
	public void setKey(Key key);


}
