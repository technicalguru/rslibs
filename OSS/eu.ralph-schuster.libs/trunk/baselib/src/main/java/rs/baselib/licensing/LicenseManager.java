/**
 * 
 */
package rs.baselib.licensing;

import java.security.PublicKey;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.io.ConverterUtils;

/**
 * The Key Manager, responsible to verify licenses.
 * @author ralph
 *
 */
public class LicenseManager {

	private Base32 base32 = new Base32(true);
	private DataSigner signer;

	/**
	 * Constructor.
	 */
	public LicenseManager(PublicKey publicKey) throws DecryptionException {
		signer = new DataSigner(publicKey);
	}
	
	/**
	 * Verifies the given license string.
	 * @param licenseKey the license string
	 * @param productId the product ID that must match
	 * @param licenseHolder the license holder that must match
	 * @throws LicenseException when the license is invalid
	 */
	public void verify(String licenseKey, int productId, String licenseHolder) {
		try {
			byte bytes[] = base32.decode(ungroup(licenseKey));
			
			// expiration
			byte b[] = new byte[4];
			System.arraycopy(bytes, 0, b, 0, 4);
			long expirationTime = ConverterUtils.toInt(b)*1000L;
			
			// signature
			byte signature[] = new byte[bytes.length-4];
			System.arraycopy(bytes, 4, signature, 0, signature.length);

			// Verify
			License license = new License(productId, expirationTime, licenseHolder);
			if (!signer.verify(signature, License.serialize(license))) {
				throw new LicenseException("Invalid license: "+licenseKey);
			}
			
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Invalid license: "+licenseKey, t);
		}
	}

	/**
	 * Returns the un-grouped version of the key.
	 * @param s the key
	 * @return the un-grouped version (dashes removed)
	 */
	protected static String ungroup(String s) {
		return s.replaceAll("\\-", "");
	}

}
