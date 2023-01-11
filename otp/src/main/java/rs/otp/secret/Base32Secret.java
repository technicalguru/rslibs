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

	/**
	 * Default Constructor.
	 */
	public Base32Secret() {
		super(16);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generate(int length) {
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
	 * {@inheritDoc}
	 */
	@Override
	public byte[] decode(String s) {
		Base32 encoder = new Base32();
		byte rc[] = encoder.decode(s);
		if ((rc == null) || (rc.length == 0)) throw new RuntimeException("Invalid secret");
		return rc;

//		// each base-32 character encodes 5 bits
//		int numBytes = ((s.length() * 5) + 7) / 8;
//		byte[] result = new byte[numBytes];
//		int resultIndex = 0;
//		int which = 0;
//		int working = 0;
//		for (int i = 0; i < s.length(); i++) {
//			char ch = s.charAt(i);
//			int val;
//			if (ch >= 'a' && ch <= 'z') {
//				val = ch - 'a';
//			} else if (ch >= 'A' && ch <= 'Z') {
//				val = ch - 'A';
//			} else if (ch >= '2' && ch <= '7') {
//				val = 26 + (ch - '2');
//			} else if (ch == '=') {
//				// special case
//				which = 0;
//				break;
//			} else {
//				throw new IllegalArgumentException("Invalid base-32 character: " + ch);
//			}
//			/*
//			 * There are probably better ways to do this but this seemed the most straightforward.
//			 */
//			switch (which) {
//			case 0:
//				// all 5 bits is top 5 bits
//				working = (val & 0x1F) << 3;
//				which = 1;
//				break;
//			case 1:
//				// top 3 bits is lower 3 bits
//				working |= (val & 0x1C) >> 2;
//				result[resultIndex++] = (byte) working;
//				// lower 2 bits is upper 2 bits
//				working = (val & 0x03) << 6;
//				which = 2;
//				break;
//			case 2:
//				// all 5 bits is mid 5 bits
//				working |= (val & 0x1F) << 1;
//				which = 3;
//				break;
//			case 3:
//				// top 1 bit is lowest 1 bit
//				working |= (val & 0x10) >> 4;
//				result[resultIndex++] = (byte) working;
//				// lower 4 bits is top 4 bits
//				working = (val & 0x0F) << 4;
//				which = 4;
//				break;
//			case 4:
//				// top 4 bits is lowest 4 bits
//				working |= (val & 0x1E) >> 1;
//				result[resultIndex++] = (byte) working;
//				// lower 1 bit is top 1 bit
//				working = (val & 0x01) << 7;
//				which = 5;
//				break;
//			case 5:
//				// all 5 bits is mid 5 bits
//				working |= (val & 0x1F) << 2;
//				which = 6;
//				break;
//			case 6:
//				// top 2 bits is lowest 2 bits
//				working |= (val & 0x18) >> 3;
//				result[resultIndex++] = (byte) working;
//				// lower 3 bits of byte 6 is top 3 bits
//				working = (val & 0x07) << 5;
//				which = 7;
//				break;
//			case 7:
//				// all 5 bits is lower 5 bits
//				working |= (val & 0x1F);
//				result[resultIndex++] = (byte) working;
//				which = 0;
//				break;
//			}
//		}
//		if (which != 0) {
//			result[resultIndex++] = (byte) working;
//		}
//		if (resultIndex != result.length) {
//			result = Arrays.copyOf(result, resultIndex);
//		}
//		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encode(byte bytes[]) {
		Base32 encoder = new Base32();
		return encoder.encodeToString(bytes);
/*
		String result = "";
		if (bytes.length % 5 != 0) {
			return result;
		}

		byte[] bits = new byte[bytes.length * 8];
		// convert to bits
		for (int i = 0; i < bytes.length; i++) {
			bits[i * 8] = (byte) ((bytes[i] & 0x80) >> 7);
			bits[i * 8 + 1] = (byte) ((bytes[i] & 0x40) >> 6);
			bits[i * 8 + 2] = (byte) ((bytes[i] & 0x20) >> 5);
			bits[i * 8 + 3] = (byte) ((bytes[i] & 0x10) >> 4);
			bits[i * 8 + 4] = (byte) ((bytes[i] & 0x08) >> 3);
			bits[i * 8 + 5] = (byte) ((bytes[i] & 0x04) >> 2);
			bits[i * 8 + 6] = (byte) ((bytes[i] & 0x02) >> 1);
			bits[i * 8 + 7] = (byte) ((bytes[i] & 0x01) >> 0);
		}
		// extract 5 bit values and convert to string
		for (int i = 0; i < bytes.length / 5 * 8; i++) {
			if (i > 0 && i % 4 == 0) {
				result += '-';
			}
			byte value = (byte) (bits[i * 5 + 0] << 4
					| bits[i * 5 + 1] << 3 | bits[i * 5 + 2] << 2
					| bits[i * 5 + 3] << 1 | bits[i * 5 + 4] << 0);

			if (value >= 0 && value < 26) {
				result = result + (char) (value + 'A');
			}

			if (value >= 26 && value < 30) {
				result = result + (char) (value - 26 + '2');
			}

			if (value == 30) {
				result = result + '7';
			}

			if (value == 31) {
				result = result + '9';
			}
		}
		return result;
*/
	}

}
