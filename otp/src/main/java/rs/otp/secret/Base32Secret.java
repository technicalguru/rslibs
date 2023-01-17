/**
 * 
 */
package rs.otp.secret;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Base32;

/**
 * Generates 16-character secret keys in base32 format (A-Z2-7) using {@link SecureRandom}. Could be used
 * to generate the QR image to be shared with the user.
 * Default length is 16.
 * 
 * <p>The original code was taken from <a href="https://github.com/j256/two-factor-auth">two-factor-auth</a>.</p>
 * 
 * @author graywatson
 * @author ralph
 *
 */
public class Base32Secret extends AbstractSecret {

	private static Base32 ENCODER = new Base32();

	/**
	 * Constructor.
	 * @param s - the string representation of this secret.
	 */
	public Base32Secret(String s) {
		super(decode(s));
	}

	/**
	 * Constructor with byte array of secret
	 * @param bytes - the bytes of this secret.
	 */
	public Base32Secret(byte bytes[]) {
		super(bytes);
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
			int val = random.nextInt(32);
			if (val < 26) {
				sb.append((char) ('A' + val));
			} else {
				sb.append((char) ('2' + (val - 26)));
			}
		}
		return sb.toString();
	}

	/**
	 * Generates a secret of default length.
	 * @return the random secret of default length
	 */
	public static String generate() {
		return generate(16);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode() {
		return encode(getBytes());
	}

	/**
	 * Encodes a secret of this type to a string representation.
	 * @param bytes the bytes
	 * @return the encoded secret
	 */
	public static String encode(byte[] bytes) {
		return ENCODER.encodeToString(bytes);
	}
	
	/**
	 * Decodes a encoded secret of this type to bytes.
	 * @param s the encoded secret
	 * @return the decoded secret
	 */
	public static byte[] decode(String s) {
		byte rc[] = ENCODER.decode(s);
		if ((rc == null) || (rc.length == 0)) throw new RuntimeException("Invalid secret");
		return rc;
	}

	/**
	 * Returns a generated secret object of default length.
	 * @return the generated secret
	 */
	public static Base32Secret generateSecret() {
		return generateSecret(16);
	}

	/**
	 * Returns a generated secret object of specified length.
	 * @param length - the length
	 * @return the generated secret
	 */
	public static Base32Secret generateSecret(int length) {
		return new Base32Secret(generate(length));
	}
}
