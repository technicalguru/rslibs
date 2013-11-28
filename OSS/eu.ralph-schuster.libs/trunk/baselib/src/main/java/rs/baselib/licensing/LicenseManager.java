/**
 * 
 */
package rs.baselib.licensing;

import java.security.PublicKey;

/**
 * The Key Manager, responsible to verify licenses.
 * @author ralph
 *
 */
public class LicenseManager {

	private ILicenseVerifier licenseVerifier;
	
	/**
	 * Constructor.
	 */
	public LicenseManager(PublicKey publicKey) {
		this(publicKey, null);
	}
	
	/**
	 * Constructor.
	 */
	public LicenseManager(PublicKey publicKey, ILicensingScheme scheme) {
		this(scheme != null ? scheme.getLicenseVerifier() : LicensingScheme.FULL_LICENSE.getLicenseVerifier());
		getLicenseVerifier().setKey(publicKey);
	}
	
	/**
	 * Constructor.
	 */
	public LicenseManager(ILicenseVerifier licenseVerifier) {
		this.licenseVerifier = licenseVerifier;
	}
	
	/**
	 * Returns the {@link #licenseVerifier}.
	 * @return the licenseVerifier
	 */
	public ILicenseVerifier getLicenseVerifier() {
		return licenseVerifier;
	}

	/**
	 * Verifies the given license string.
	 * @param licenseKey the license string
	 * @param productId the product ID that must match
	 * @param licenseHolder the license holder that must match
	 * @throws LicenseException when the license is invalid
	 */
	public License verify(String licenseKey, int productId, String licenseHolder) {
		try {
			return getLicenseVerifier().verify(licenseKey, productId, licenseHolder);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

}
