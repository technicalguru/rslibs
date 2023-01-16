/**
 * 
 */
package rs.otp.secret;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rs.otp.GoogleApiQrGenerator;

/**
 * Tests the {@link GoogleApiQrGenerator}.
 * 
 * @author ralph
 *
 */
public class GoogleApiQrGeneratorTest {

	@Test
	public void test() throws Exception {
		ISecret secret = new Base32Secret("ny4A5CPJZ46LXZCP");
		assertEquals("https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fkey%3Fsecret%3DNY4A5CPJZ46LXZCP%26digits%3D6", GoogleApiQrGenerator.getQrImageUrl("key", secret));
		assertEquals("https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fkey%3Fsecret%3DNY4A5CPJZ46LXZCP%26digits%3D8", GoogleApiQrGenerator.getQrImageUrl("key", secret, 8));
		assertEquals("https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fkey%3Fsecret%3DNY4A5CPJZ46LXZCP%26digits%3D6", GoogleApiQrGenerator.getQrImageUrl("key", secret));
		assertEquals("https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fkey%3Fsecret%3DNY4A5CPJZ46LXZCP%26digits%3D3", GoogleApiQrGenerator.getQrImageUrl("key", secret, 3));
		assertEquals("https://chart.googleapis.com/chart?chs=500x500&cht=qr&chl=500x500&chld=M|0&cht=qr&chl=otpauth%3A%2F%2Ftotp%2Fkey%3Fsecret%3DNY4A5CPJZ46LXZCP%26digits%3D3", GoogleApiQrGenerator.getQrImageUrl("key", secret, 3, 500));
	}
}
