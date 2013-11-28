/**
 * 
 */
package rs.baselib.licensing.internal;

import java.security.Key;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.io.ConverterUtils;
import rs.baselib.licensing.License;
import rs.baselib.licensing.LicenseException;
import rs.baselib.util.CommonUtils;

/**
 * Verifies an Octet License.
 * @author ralph
 *
 */
public class OctetLicenseVerifier extends AbstractLicenseVerifier {

	private Base32 decoder;
	private DataSigner signer;
	
	/**
	 * Constructor.
	 */
	public OctetLicenseVerifier() {
		decoder = new Base32(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public License verify(String licenseKey, int productId, String licenseHolder) {
		try {
			byte bytes[] = decoder.decode(ungroup(licenseKey));
			
			// XOR the first 4 byte with the second four bytes to remove variance for unlimited
			for (int i=0; i<4; i++) {
				bytes[i] = (byte) (bytes[i] ^ bytes[4+i]);
			}

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
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(Key key) {
		try {
			signer = new DataSigner((PublicKey)key);
		} catch (Throwable t) {
			throw new LicenseException("Cannot create data signer", t);
		}
		super.setKey(key);
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
