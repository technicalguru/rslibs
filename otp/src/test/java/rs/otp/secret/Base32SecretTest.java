package rs.otp.secret;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Random;

import org.apache.commons.codec.binary.Base32;
import org.junit.Test;

/**
 * Test the functionality of the {@link Base32Secret}.
 * 
 * @author ralph
 * 
 */
public class Base32SecretTest {

	@Test
	public void testGenerateSecret() {
		assertEquals(16, Base32Secret.generate().length());
		assertEquals(16, Base32Secret.generate(16).length());
		assertEquals(1,  Base32Secret.generate(1).length());
	}

	@Test
	public void testDecodeBase32() {
		Random random = new Random();
		random.nextBytes(new byte[100]);
		Base32 base32 = new Base32();
		for (int i = 0; i < 10000; i++) {
			byte[] bytes = new byte[random.nextInt(10) + 1];
			random.nextBytes(bytes);
			String encoded = base32.encodeAsString(bytes);
			byte[] expected = base32.decode(encoded);
			byte[] actual = Base32Secret.decode(encoded);
			assertArrayEquals(expected, actual);
		}
	}

	@Test
	public void testBadBase32() {
		String[] strings = new String[] { "A", "AB", "ABC", "ABCD", "ABCDE", "ABCDEF", "ABCDEFG", "ABCDEFGH", "ABCDEFGHI" };
		Base32 base32 = new Base32();
		for (String str : strings) {
			byte[] decoded = Base32Secret.decode(str);
			String encoded = base32.encodeAsString(decoded);
			byte[] result = Base32Secret.decode(encoded);
			// System.out.println(str + " becomes " + encoded);
			assertArrayEquals(decoded, result);
		}
	}

	@Test
	public void testCoverage() {
		try {
			Base32Secret.decode(".");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}
		try {
			Base32Secret.decode("^");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}

		try {
			Base32Secret.decode("0");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}

		try {
			Base32Secret.decode("/");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}

		try {
			Base32Secret.decode("^");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}

		try {
			Base32Secret.decode("~");
			fail("Should have thrown");
		} catch (RuntimeException iae) {
			// expected
		}
	}

}
