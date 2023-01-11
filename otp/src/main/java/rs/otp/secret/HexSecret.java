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

//	private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
	
	/**
	 * Default Constructor.
	 */
	public HexSecret() {
		super(32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(int length) {
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
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decode(String s) {
		try {
			return Hex.decodeHex(s);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot decode hex string");
		}
//		// each hex character encodes 4 bits
//		int numBytes = ((s.length() * 4) + 7) / 8;
//		byte[] result = new byte[numBytes];
//		int resultIndex = 0;
//		int which = 0;
//		int working = 0;
//		for (int i = 0; i < s.length(); i++) {
//			char ch = s.charAt(i);
//			int val;
//			if (ch >= '0' && ch <= '9') {
//				val = (ch - '0');
//			} else if (ch >= 'a' && ch <= 'f') {
//				val = 10 + (ch - 'a');
//			} else if (ch >= 'A' && ch <= 'F') {
//				val = 10 + (ch - 'A');
//			} else {
//				throw new IllegalArgumentException("Invalid hex character: " + ch);
//			}
//			/*
//			 * There are probably better ways to do this but this seemed the most straightforward.
//			 */
//			if (which == 0) {
//				// top 4 bits
//				working = (val & 0xF) << 4;
//				which = 1;
//			} else {
//				// lower 4 bits
//				working |= (val & 0xF);
//				result[resultIndex++] = (byte) working;
//				which = 0;
//			}
//		}
//		if (which != 0) {
//			result[resultIndex++] = (byte) (working >> 4);
//		}
//		if (resultIndex != result.length) {
//			// may not happen but let's be careful out there
//			result = Arrays.copyOf(result, resultIndex);
//		}
//		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(byte bytes[]) {
		return Hex.encodeHexString(bytes);
	}

}
