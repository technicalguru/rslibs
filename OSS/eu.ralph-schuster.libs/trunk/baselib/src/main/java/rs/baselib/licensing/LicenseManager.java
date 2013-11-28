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
	 * @param productId the product ID that must match
	 * @param licenseHolder the license holder that must match
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

	/**
	 * Instantiates a {@link LicenseManager} from given file with public key.
	 * @param f file where public key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @param scheme the licensing scheme
	 * @return the manager
	 * @throws LicenseException when the manager cannot be instantiated
	 */
//	public static LicenseManager generateFrom(File f, String algorithm) {
//		return generateFrom(f, algorithm, LicensingScheme.RSA_LICENSE);
//	}
	
	/**
	 * Instantiates a {@link LicenseManager} from given file with public key.
	 * @param f file where public key is stored in BASE64 X.509 encoding 
	 * @param algorithm algorithm that the key used
	 * @param scheme the licensing scheme
	 * @return the manager
	 * @throws LicenseException when the manager cannot be instantiated
	 */
//	public static LicenseManager generateFrom(File f, String algorithm, ILicensingScheme scheme) {
//		try {
//			byte[] encodedPublicKey = Base64.decodeBase64(CommonUtils.loadContent(f));
//			KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
//			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
//			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
//			return new LicenseManager(publicKey, scheme);
//		} catch (LicenseException e) {
//			throw e;
//		} catch (Throwable t) {
//			throw new LicenseException("Cannot create LicenseManager", t);
//		}
//	}
}
