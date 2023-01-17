/**
 * 
 */
package rs.otp.secret;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * Test the functionality of the {@link HexSecret}.
 * 
 * @author ralph
 *
 */
public class HexSecretTest {

	@Test
	public void testGenerateSecret() {
		assertEquals(32, HexSecret.generate().length());
		assertEquals(16, HexSecret.generate(16).length());
		assertEquals(1, HexSecret.generate(1).length());
	}

	@Test
	public void testDecodeHexadecimal() throws DecoderException {
		Random random = new Random();
		random.nextBytes(new byte[100]);
		for (int i = 0; i < 10000; i++) {
			byte[] bytes = new byte[random.nextInt(10) + 1];
			random.nextBytes(bytes);
			String encoded = HexSecret.encode(bytes);
			byte[] expected = Hex.decodeHex(encoded.toCharArray());
			byte[] actual = HexSecret.decode(encoded);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void testCoverage() {
		try {
			HexSecret.decode("z");
			fail("Should have thrown");
		} catch (Throwable iae) {
			// expected
		}
	
		try {
			HexSecret.decode("/");
			fail("Should have thrown");
		} catch (Throwable iae) {
			// expected
		}
	
		try {
			HexSecret.decode("^");
			fail("Should have thrown");
		} catch (Throwable iae) {
			// expected
		}
	
		try {
			HexSecret.decode("~");
			fail("Should have thrown");
		} catch (Throwable iae) {
			// expected
		}
	}
}
