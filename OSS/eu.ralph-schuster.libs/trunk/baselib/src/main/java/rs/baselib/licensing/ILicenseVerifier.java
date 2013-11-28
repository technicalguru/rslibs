/**
 * 
 */
package rs.baselib.licensing;


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
	public ILicense verify(String licenseKey, ILicenseContext context);

}
