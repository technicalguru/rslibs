/**
 * 
 */
package rs.baselib.licensing.internal;

import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import rs.baselib.crypto.Decrypter;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.licensing.License;
import rs.baselib.licensing.LicenseException;
import rs.baselib.util.CommonUtils;

/**
 * Verifies a full license.
 * @author ralph
 *
 */
public class FullLicenseVerifier extends AbstractLicenseVerifier {

	private Base64 decoder;
	private Decrypter decrypter;
	
	/**
	 * Constructor.
	 */
	public FullLicenseVerifier() {
		decoder = new Base64(64);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public License verify(String licenseKey, int productId, String licenseHolder) {
		try {
			byte bytes[] = decrypt(decoder.decode(licenseKey));
			License license = License.unserialize(bytes);
			// Invalid license
			if (license == null) {
				throw new LicenseException("License unknown: "+licenseKey);
			}
			
			// Product ID
			if (license.getProductId() != productId) {
				throw new LicenseException("License not valid for this product: "+licenseKey);
			}
			
			// License holder
			if (!license.getLicenseHolder().equals(licenseHolder)) {
				throw new LicenseException("License not valid for this installation: "+licenseKey);
			}
			
			// Expiration
			if (license.isExpired()) {
				throw new LicenseException("License expired (valid until "+CommonUtils.DATE_TIME_FORMATTER.format(license.getExpiryDate())+": "+licenseKey);
			}
			return license;
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Invalid license: "+licenseKey, t);
		}
	}

	/**
	 * Decrypts the bytes.
	 * @param bytes bytes to be decrypted
	 * @return the plain message
	 * @throws DecryptionException
	 */
	protected byte[] decrypt(byte bytes[]) throws DecryptionException {
		return decrypter.decrypt(bytes);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			decrypter = new Decrypter(cipher);
		} catch (Throwable t) {
			throw new LicenseException("Cannot create encrypter", t);
		}
		super.setKey(key);
	}


}
