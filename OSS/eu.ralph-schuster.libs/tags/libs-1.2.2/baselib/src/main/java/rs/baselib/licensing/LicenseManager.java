/**
 * 
 */
package rs.baselib.licensing;


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
	public LicenseManager() {
		this((ILicensingScheme)null);
	}

	/**
	 * Constructor.
	 */
	public LicenseManager(ILicensingScheme scheme) {
		this(scheme != null ? scheme.getLicenseVerifier() : LicensingScheme.RSA_LICENSE.getLicenseVerifier());
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
	 * @param context the context containing verification parameters
	 * @throws LicenseException when the license is invalid
	 */
	public ILicense verify(String licenseKey, ILicenseContext context) {
		try {
			return getLicenseVerifier().verify(licenseKey, context);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

}
