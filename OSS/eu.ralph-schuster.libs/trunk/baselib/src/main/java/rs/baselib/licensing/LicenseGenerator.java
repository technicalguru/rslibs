/**
 * 
 */
package rs.baselib.licensing;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Date;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.crypto.SigningException;
import rs.baselib.io.ConverterUtils;

/**
 * The generator for license.
 * @author ralph
 *
 */
public class LicenseGenerator {

	private Base32 base32 = new Base32(true);
	private DataSigner signer;
	
	/**
	 * Constructor.
	 */
	public LicenseGenerator(PrivateKey privateKey) throws DecryptionException {
		signer = new DataSigner(privateKey);
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
			byte licenseBytes[] = License.serialize(license);
			byte signature[] = sign(licenseBytes);
			byte bytes[] = combine((int)(license.getExpiryTime()/1000), signature);
			return group(base32.encodeToString(bytes));
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

	/**
	 * Signes the given byte array.
	 * @param bytes the bytes to be signed
	 * @return the signature
	 * @throws SigningException
	 * @throws NoSuchAlgorithmException
	 */
	protected byte[] sign(byte bytes[]) throws SigningException, NoSuchAlgorithmException {
		return signer.getByteSignature(bytes);
	}
	
	/**
	 * Builds the combined license key (raw bytes).
	 * @param prefix the integer prefix basically containing the expiry time in seconds since epoch time
	 * @param signature teh signature
	 * @return the combined byte array
	 * @throws IOException
	 */
	protected byte[] combine(int prefix, byte signature[]) {
		int len = signature.length+4;
		byte rc[] = new byte[len];
		System.arraycopy(ConverterUtils.toBytes(prefix), 0, rc, 0, 4);
		System.arraycopy(signature, 0, rc, 4, signature.length);
		// XOR the first 4 byte with the second four bytes to allow more variance for unlimited
		for (int i=0; i<4; i++) {
			rc[i] = (byte) (rc[i] ^ rc[4+i]);
		}
		return rc;
	}
	
	/**
	 * Groups a string into 8-character blocks.
	 * @param s the string to be grouped
	 * @return the grouped string
	 */
	public static String group(String s) {
		StringBuilder b = new StringBuilder(s.length()+10);
		int count = 0;
		for (char c : s.toCharArray()) {
			if (count >= 8) {
				b.append('-');
				count = 0;
			}
			b.append(c);
			count++;
		}
		// Remove trailing =
		while (b.charAt(b.length()-1) == '=') b.deleteCharAt(b.length()-1);
		return b.toString();
	}

}
