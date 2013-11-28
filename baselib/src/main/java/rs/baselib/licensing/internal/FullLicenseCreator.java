/**
 * 
 */
package rs.baselib.licensing.internal;

import java.security.Key;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import rs.baselib.crypto.Encrypter;
import rs.baselib.crypto.EncryptionException;
import rs.baselib.licensing.License;
import rs.baselib.licensing.LicenseException;

/**
 * Creates a full license.
 * @author ralph
 *
 */
public class FullLicenseCreator extends AbstractLicenseCreator {

	private Base64 encoder;
	private Encrypter encrypter;

	/**
	 * Constructor.
	 */
	public FullLicenseCreator() {
		encoder = new Base64(64);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createLicenseKey(License license) {
		try {
			byte licenseBytes[] = License.serialize(license);
			byte bytes[] = encrypt(licenseBytes);
			return encoder.encodeToString(bytes);
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

	/**
	 * Encrypts the given bytes.
	 * @param bytes the plain bytes
	 * @return the bytes encrypted
	 * @throws EncryptionException
	 */
	protected byte[] encrypt(byte bytes[]) throws EncryptionException {
		return encrypter.encrypt(bytes);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(Key key) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypter = new Encrypter(cipher);
		} catch (Throwable t) {
			throw new LicenseException("Cannot create encrypter", t);
		}
		super.setKey(key);
	}

}
