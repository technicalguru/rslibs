/**
 * 
 */
package rs.otp.secret;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import rs.otp.QrUrlGenerator;

/**
 * Tests the {@link QrUrlGenerator}.
 * 
 * @author ralph
 *
 */
public class QrUrlGeneratorTest {

	@Test
	public void test() {
		String secret = "ny4A5CPJZ46LXZCP";
		assertNotNull(QrUrlGenerator.generateOtpAuthUrl("key", secret));
		assertNotNull(QrUrlGenerator.generateOtpAuthUrl("key", secret, 8));
		assertNotNull(QrUrlGenerator.qrImageUrl("key", secret));
		assertNotNull(QrUrlGenerator.qrImageUrl("key", secret, 3));
		assertNotNull(QrUrlGenerator.qrImageUrl("key", secret, 3, 500));
	}
}
