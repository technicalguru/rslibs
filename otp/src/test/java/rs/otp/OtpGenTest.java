/**
 * 
 */
package rs.otp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.security.GeneralSecurityException;
import java.util.Random;

import org.junit.Test;

import rs.otp.secret.Base32Secret;
import rs.otp.secret.HexSecret;

/**
 * Test the OTP generation and verification.
 * 
 * @author ralph
 *
 */
public class OtpGenTest {

	@Test
	public void testZeroPrepend() {
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {
			int num = random.nextInt(1000000);
			/**
			 * NOTE: Did a speed test of these and the zeroPrepend is ~13x faster.
			 */
			assertEquals(String.format("%06d", num), OtpGen.stringify(num, 6));
		}
	}

	@Test
	public void testVariusKnownSecretTimeCodes() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(new Base32Secret("NY4A5CPJZ46LXZCP"));

		testStringAndNumber(utils, 1000L, "748810");
		testStringAndNumber(utils, 7451000L, "325893");
		testStringAndNumber(utils, 15451000L, "064088");
		testStringAndNumber(utils, 348402049542546145L, "009637");
		testStringAndNumber(utils, 2049455124374752571L, "000743");
		testStringAndNumber(utils, 1359002349304873750L, "000092");
		testStringAndNumber(utils, 6344447817348357059L, "000007");
		testStringAndNumber(utils, 2125701285964551130L, "000000");

		testStringAndNumber(utils, 7451000L, "3", 1);
		testStringAndNumber(utils, 7451000L, "93", 2);
		testStringAndNumber(utils, 7451000L, "893", 3);
		testStringAndNumber(utils, 7451000L, "5893", 4);
		testStringAndNumber(utils, 7451000L, "25893", 5);
		testStringAndNumber(utils, 7451000L, "325893", 6);
		testStringAndNumber(utils, 7451000L, "9325893", 7);
		testStringAndNumber(utils, 7451000L, "89325893", 8);

		testStringAndNumber(utils, 1000L, "34748810", 8);
		testStringAndNumber(utils, 7451000L, "89325893", 8);
		testStringAndNumber(utils, 15451000L, "67064088", 8);
		testStringAndNumber(utils, 5964551130L, "05993908", 8);
		testStringAndNumber(utils, 348402049542546145L, "26009637", 8);
		testStringAndNumber(utils, 2049455124374752571L, "94000743", 8);
		testStringAndNumber(utils, 1359002349304873750L, "86000092", 8);
		testStringAndNumber(utils, 6344447817348357059L, "80000007", 8);
		testStringAndNumber(utils, 2125701285964551130L, "24000000", 8);
	}

	private void testStringAndNumber(OtpGen utils, long timeInMillis, String expectedString) throws GeneralSecurityException {
		testStringAndNumber(utils, timeInMillis, expectedString, OtpGen.DEFAULT_OTP_LENGTH);
	}

	private void testStringAndNumber(OtpGen utils, long timeInMillis, String expectedString, int length) throws GeneralSecurityException {
		String otp = utils.otpAt(timeInMillis, OtpGen.DEFAULT_TIME_STEP_SECONDS, length);
		assertEquals(length, otp.length());
		assertEquals(expectedString, otp);
	}

	@Test
	public void testVerify() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(new Base32Secret("NY4A5CPJZ46LXZCP"));
		assertEquals("162123", utils.otpAt(7439999, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify("325893", 0, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertFalse(utils.verify("948323", 0, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		// this should of course match
		assertTrue(utils.verify("325893", 15000, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));

		/*
		 * Test upper window which starts +15000 milliseconds.
		 */

		// but this is the next value and the window doesn't quite take us to the next time-step
		assertFalse(utils.verify("948323", 14999, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		// but this is the next value which is 15000 milliseconds ahead
		assertTrue(utils.verify("948323", 15000, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));

		/*
		 * The lower window is less than -15000 milliseconds so we have to test a window of 15001.
		 */

		// but this is the previous value and the window doesn't quite take us to the previous time-step
		assertFalse(utils.verify("287511", 15000, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		// but this is the previous value which is 15001 milliseconds earlier
		assertTrue(utils.verify("162123", 15001, 7455000, OtpGen.DEFAULT_TIME_STEP_SECONDS));
	}

	@Test
	public void testWindow() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(Base32Secret.generateSecret());
		long window = 10000;
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			long now = random.nextLong();
			if (now < 0) {
				now = -now;
			}
			String otp = utils.otpAt(now, OtpGen.DEFAULT_TIME_STEP_SECONDS);
			assertTrue(utils.verify(otp, window, now - window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
			assertTrue(utils.verify(otp, window, now, OtpGen.DEFAULT_TIME_STEP_SECONDS));
			assertTrue(utils.verify(otp, window, now + window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		}
	}

	@Test
	public void testWindowStuff() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(Base32Secret.generateSecret());
		long window = 10000;
		long now = 5462669356666716002L;
		String otp = utils.otpAt(now, OtpGen.DEFAULT_TIME_STEP_SECONDS);
		assertTrue(utils.verify(otp, window, now - window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now + window, OtpGen.DEFAULT_TIME_STEP_SECONDS));

		now = 8835485943423840000L;
		otp = utils.otpAt(now, OtpGen.DEFAULT_TIME_STEP_SECONDS);
		assertFalse(utils.verify(otp, window, now - window - 1, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now - window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now + window, OtpGen.DEFAULT_TIME_STEP_SECONDS));

		now = 8363681401523009999L;
		otp = utils.otpAt(now, OtpGen.DEFAULT_TIME_STEP_SECONDS);
		assertTrue(utils.verify(otp, window, now - window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertTrue(utils.verify(otp, window, now + window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		assertFalse(utils.verify(otp, window, now + window + 1, OtpGen.DEFAULT_TIME_STEP_SECONDS));
	}

	@Test
	public void testHexWindow() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(HexSecret.generateSecret());
		long window = 10000;
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			long now = random.nextLong();
			if (now < 0) {
				now = -now;
			}
			String number = utils.otpAt(now, OtpGen.DEFAULT_TIME_STEP_SECONDS);
			assertTrue(utils.verify(number, window, now - window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
			assertTrue(utils.verify(number, window, now, OtpGen.DEFAULT_TIME_STEP_SECONDS));
			assertTrue(utils.verify(number, window, now + window, OtpGen.DEFAULT_TIME_STEP_SECONDS));
		}
	}

	@Test
	public void testCoverage() throws GeneralSecurityException {
		OtpGen utils = new OtpGen(new Base32Secret("ny4A5CPJZ46LXZCP"));
		utils.verify("948323", 15000);
		assertEquals(OtpGen.DEFAULT_OTP_LENGTH, utils.current().length());

		String otp = utils.current();
		assertTrue(utils.verify(otp, 0, System.currentTimeMillis(), OtpGen.DEFAULT_TIME_STEP_SECONDS));

		int len = 3;
		assertEquals(len, utils.current(len).length());
		int num = Integer.parseInt(utils.current());
		assertTrue(num >= 0 && num < 1000000);
		num = Integer.parseInt(utils.current(3));
		assertTrue(num >= 0 && num < 1000);

		utils = new OtpGen(new HexSecret("0123456789abcdefABCDEF"));
		num = Integer.parseInt(utils.current());
		assertTrue(num >= 0 && num < 1000000);
		num = Integer.parseInt(utils.current(3));
		assertTrue(num >= 0 && num < 1000);
		utils.verify(""+num, 0);
		assertNotNull(utils.current());
		assertNotNull(utils.current(3));
	}
}
