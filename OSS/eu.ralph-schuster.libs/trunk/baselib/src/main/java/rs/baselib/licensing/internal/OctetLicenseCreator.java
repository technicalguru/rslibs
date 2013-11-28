/**
 * 
 */
package rs.baselib.licensing.internal;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import org.apache.commons.codec.binary.Base32;

import rs.baselib.crypto.DataSigner;
import rs.baselib.crypto.SigningException;
import rs.baselib.io.ConverterUtils;
import rs.baselib.licensing.License;
import rs.baselib.licensing.LicenseException;

/**
 * Creates an Octet License.
 * @author ralph
 *
 */
public class OctetLicenseCreator extends AbstractLicenseCreator {

	private Base32 encoder;
	private DataSigner signer;

	/**
	 * Constructor.
	 */
	public OctetLicenseCreator() {
		encoder = new Base32(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String createLicenseKey(License license) {
		try {
			byte licenseBytes[] = License.serialize(license);
			byte signature[] = sign(licenseBytes);
			byte bytes[] = combine((int)(license.getExpiryTime()/1000), signature);
			return group(encoder.encodeToString(bytes));
		} catch (LicenseException e) {
			throw e;
		} catch (Throwable t) {
			throw new LicenseException("Cannot create license key", t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(Key key) {
		try {
			signer = new DataSigner((PrivateKey)key);
		} catch (Throwable t) {
			throw new LicenseException("Cannot create data signer", t);
		}
		super.setKey(key);
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
