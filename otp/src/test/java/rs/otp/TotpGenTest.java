/**
 * 
 */
package rs.otp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.GeneralSecurityException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import rs.otp.secret.Base32Secret;
import rs.otp.secret.HexSecret;

/**
 * Test the OTP generation and verification.
 * 
 * @author ralph
 *
 */
public class TotpGenTest {

	@Test
	public void testZeroPrepend() {
		Random random = new Random();
		for (int i = 0; i < 10000; i++) {
			int num = random.nextInt(1000000);
			/**
			 * NOTE: Did a speed test of these and the zeroPrepend is ~13x faster.
			 */
			assertEquals(String.format("%06d", num), TotpGen.stringify(num, 6));
		}
	}

	@Test
	public void testVariusKnownSecretTimeCodes() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(new Base32Secret("NY4A5CPJZ46LXZCP"));

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

	private void testStringAndNumber(TotpGen utils, long timeInMillis, String expectedString) throws GeneralSecurityException {
		testStringAndNumber(utils, timeInMillis, expectedString, TotpGen.DEFAULT_OTP_LENGTH);
	}

	private void testStringAndNumber(TotpGen utils, long timeInMillis, String expectedString, int length) throws GeneralSecurityException {
		String otp = utils.otpAt(timeInMillis, TotpGen.DEFAULT_TIME_STEP_SECONDS, length);
		assertEquals(length, otp.length());
		assertEquals(expectedString, otp);
	}

	@Test
	public void testVerify() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(new Base32Secret("NY4A5CPJZ46LXZCP"));
		assertEquals("162123", utils.otpAt(7439999, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify("325893", 0, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertFalse(utils.verify("948323", 0, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		// this should of course match
		assertTrue(utils.verify("325893", 15000, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));

		/*
		 * Test upper window which starts +15000 milliseconds.
		 */

		// but this is the next value and the window doesn't quite take us to the next time-step
		assertFalse(utils.verify("948323", 14999, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		// but this is the next value which is 15000 milliseconds ahead
		assertTrue(utils.verify("948323", 15000, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));

		/*
		 * The lower window is less than -15000 milliseconds so we have to test a window of 15001.
		 */

		// but this is the previous value and the window doesn't quite take us to the previous time-step
		assertFalse(utils.verify("287511", 15000, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		// but this is the previous value which is 15001 milliseconds earlier
		assertTrue(utils.verify("162123", 15001, 7455000, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
	}

	@Test
	public void testWindow() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(Base32Secret.generateSecret());
		long window = 10000;
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			long now = random.nextLong();
			if (now < 0) {
				now = -now;
			}
			String otp = utils.otpAt(now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH);
			assertTrue(utils.verify(otp, window, now - window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
			assertTrue(utils.verify(otp, window, now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
			assertTrue(utils.verify(otp, window, now + window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		}
	}

	@Test
	public void testWindowStuff() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(Base32Secret.generateSecret());
		long window = 10000;
		long now = 5462669356666716002L;
		String otp = utils.otpAt(now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH);
		assertTrue(utils.verify(otp, window, now - window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now + window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));

		now = 8835485943423840000L;
		otp = utils.otpAt(now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH);
		assertFalse(utils.verify(otp, window, now - window - 1, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now - window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now + window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));

		now = 8363681401523009999L;
		otp = utils.otpAt(now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH);
		assertTrue(utils.verify(otp, window, now - window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertTrue(utils.verify(otp, window, now + window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		assertFalse(utils.verify(otp, window, now + window + 1, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
	}

	@Test
	public void testHexWindow() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(HexSecret.generateSecret());
		long window = 10000;
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			long now = random.nextLong();
			if (now < 0) {
				now = -now;
			}
			String number = utils.otpAt(now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH);
			assertTrue(utils.verify(number, window, now - window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
			assertTrue(utils.verify(number, window, now, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
			assertTrue(utils.verify(number, window, now + window, TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));
		}
	}

	@Test
	public void testCoverage() throws GeneralSecurityException {
		TotpGen utils = new TotpGen(new Base32Secret("ny4A5CPJZ46LXZCP"));
		utils.verify("948323", 15000);
		assertEquals(TotpGen.DEFAULT_OTP_LENGTH, utils.current().length());

		String otp = utils.current();
		assertTrue(utils.verify(otp, 0, System.currentTimeMillis(), TotpGen.DEFAULT_TIME_STEP_SECONDS, TotpGen.DEFAULT_OTP_LENGTH));

		int len = 3;
		assertEquals(len, utils.otpAt(System.currentTimeMillis(), TotpGen.DEFAULT_TIME_STEP_SECONDS, len).length());
		int num = Integer.parseInt(utils.current());
		assertTrue(num >= 0 && num < 1000000);
		num = Integer.parseInt(utils.otpAt(System.currentTimeMillis(), TotpGen.DEFAULT_TIME_STEP_SECONDS, 3));
		assertTrue(num >= 0 && num < 1000);

		utils = new TotpGen(new HexSecret("0123456789abcdefABCDEF"));
		num = Integer.parseInt(utils.current());
		assertTrue(num >= 0 && num < 1000000);
		num = Integer.parseInt(utils.otpAt(System.currentTimeMillis(), TotpGen.DEFAULT_TIME_STEP_SECONDS, 3));
		assertTrue(num >= 0 && num < 1000);
		utils.verify(""+num, 0);
		assertNotNull(utils.current());
		assertNotNull(utils.otpAt(System.currentTimeMillis(), TotpGen.DEFAULT_TIME_STEP_SECONDS, 3));
	}
	
	@Test
	public void testFrom1() throws Exception {
		TotpGen gen = TotpGen.from("otpauth://totp/key?secret=NY4A5CPJZ46LXZCP&digits=6");
		assertEquals("NY4A5CPJZ46LXZCP", gen.getSecret().encode());
		assertNull(gen.getIssuer());
		assertEquals("key", gen.getAccount());
		assertEquals(6,     gen.getNumDigits());
		assertEquals(30,    gen.getTimeStepSeconds());
	}
	
	@Test
	public void testFrom2() throws Exception {
		TotpGen gen = TotpGen.from("otpauth://totp/issuerName:key?secret=NY4A5CPJZ46LXZCP&digits=8");
		assertEquals("NY4A5CPJZ46LXZCP", gen.getSecret().encode());
		assertEquals("issuerName", gen.getIssuer());
		assertEquals("key",        gen.getAccount());
		assertEquals(8,            gen.getNumDigits());
		assertEquals(30,           gen.getTimeStepSeconds());
	}
	
	@Test
	public void testFrom3() throws Exception {
		TotpGen gen = TotpGen.from("otpauth://totp/issuerName:key?secret=NY4A5CPJZ46LXZCP&digits=8&issuer=issuerName");
		assertEquals("NY4A5CPJZ46LXZCP", gen.getSecret().encode());
		assertEquals("issuerName", gen.getIssuer());
		assertEquals("key",        gen.getAccount());
		assertEquals(8,            gen.getNumDigits());
		assertEquals(30,           gen.getTimeStepSeconds());
	}
	
	@Test
	public void testFrom4() throws Exception {
		TotpGen gen = TotpGen.from("otpauth://totp/key?secret=NY4A5CPJZ46LXZCP&digits=6&period=60");
		assertEquals("NY4A5CPJZ46LXZCP", gen.getSecret().encode());
		assertNull(gen.getIssuer());
		assertEquals("key", gen.getAccount());
		assertEquals(6,     gen.getNumDigits());
		assertEquals(60,    gen.getTimeStepSeconds());
	}
}
