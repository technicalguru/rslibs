/**
 * 
 */
package rs.otp.secret;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

/**
 * Generates 32-character secret keys in hexadecimal format (0-9A-F) using {@link SecureRandom}. Could be
 * used to generate the QR image to be shared with the user. 
 * 
 * <p>The original code was taken from <a href="https://github.com/j256/two-factor-auth">two-factor-auth</a>.</p>
 * 
 * @author graywatson
 * @author ralph
 */
public class HexSecret extends AbstractSecret {

	/**
	 * Constructor with byte array of secret
	 * @param bytes - the bytes of this secret.
	 */
	public HexSecret(byte[] bytes) {
		super(bytes);
	}

	/**
	 * Constructor.
	 * @param s - the string representation of this secret.
	 */
	public HexSecret(String s) {
		super(decode(s));
	}

	/**
	 * Generates a secret of given length.
	 * @param length - the length
	 * @return the random secret of given length
	 */
	public static String generate(int length) {
		StringBuilder sb = new StringBuilder(length);
		Random random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			int val = random.nextInt(16);
			if (val < 10) {
				sb.append((char) ('0' + val));
			} else {
				sb.append((char) ('A' + (val - 10)));
			}
		}
		return sb.toString();
	}
	
	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public static String generate() {
		return generate(32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode() {
		return Hex.encodeHexString(getBytes());
	}

	/**
	 * Encodes a secret of this type to a string representation.
	 * @param bytes the bytes
	 * @return the encoded secret
	 */
	public static String encode(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}
	
	/**
	 * Decodes a encoded secret of this type to bytes.
	 * @param s the encoded secret
	 * @return the decoded secret
	 */
	public static byte[] decode(String s) {
		try {
			return Hex.decodeHex(s);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot decode hex string");
		}
	}

	/**
	 * Returns a generated secret object of default length.
	 * @return the generated secret
	 */
	public static HexSecret generateSecret() {
		return generateSecret(16);
	}

	/**
	 * Returns a generated secret object of specified length.
	 * @param length - the length
	 * @return the generated secret
	 */
	public static HexSecret generateSecret(int length) {
		return new HexSecret(generate(length));
	}

}
