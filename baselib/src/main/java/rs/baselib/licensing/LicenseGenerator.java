/**
 * 
 */
package rs.baselib.licensing;

import java.security.PrivateKey;
import java.util.Date;

import rs.baselib.crypto.DecryptionException;

/**
 * The generator for license.
 * @author ralph
 *
 */
public class LicenseGenerator {

	/** The license creator */
	private ILicenseCreator licenseCreator;
	
	/**
	 * Constructor.
	 */
	public LicenseGenerator(PrivateKey privateKey) throws DecryptionException {
		this(privateKey, null);
	}
	
	/**
	 * Constructor.
	 */
	public LicenseGenerator(PrivateKey privateKey, ILicensingScheme scheme) throws DecryptionException {
		this(scheme != null ? scheme.getLicenseCreator() : LicensingScheme.FULL_LICENSE.getLicenseCreator());
		getLicenseCreator().setKey(privateKey);
	}
	
	/**
	 * Constructor.
	 */
	public LicenseGenerator(ILicenseCreator licenseCreator) throws DecryptionException {
		this.licenseCreator = licenseCreator;
	}
	
	/**
	 * Returns the {@link #licenseCreator}.
	 * @return the licenseCreator
	 */
	public ILicenseCreator getLicenseCreator() {
		return licenseCreator;
	}

	/**
	 * Generates a key from the given license (does not expire).
	 * @param productId the product id to be encoded
	 * @param licenseHolder the license holder string
	 * @return the license key generated
	 */
	public String createLicenseKey(int productId, String licenseHolder) {
		return createLicenseKey(productId, 0L, licenseHolder);
	}

	/**
	 * Generates a key from the given license.
	 * @param productId the product id to be encoded
	 * @param expiryTime the expiration time to be encoded (<code>null</code> is no expiration)
	 * @param licenseHolder the license holder string
	 * @return the license key generated
	 */
	public String createLicenseKey(int productId, Date expiryTime, String licenseHolder) {
		return createLicenseKey(productId, expiryTime != null ? expiryTime.getTime() : 0L, licenseHolder);
	}

	/**
	 * Generates a key from the given license.
	 * @param productId the product id to be encoded
	 * @param expiryTime the expiration time to be encoded (0 is no expiration)
	 * @param licenseHolder the license holder string
	 * @return the license key generated
	 */
	public String createLicenseKey(int productId, long expiryTime, String licenseHolder) {
		return createLicenseKey(new License(productId, expiryTime, licenseHolder));
	}

	/**
	 * Generates a key from the given license.
	 * @param license the license to be created.
	 * @return the license key
	 */
	public String createLicenseKey(License license) {
		try {
			return getLicenseCreator().createLicenseKey(license);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

}
