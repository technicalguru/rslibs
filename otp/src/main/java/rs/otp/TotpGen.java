/**
 * 
 */
package rs.otp;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.URIReferenceException;

import rs.otp.secret.Base32Secret;
import rs.otp.secret.ISecret;

/**
 * Implementation of the Time-based One-Time Password (TOTP) two factor authentication algorithm. You need to:
 * 
 * <ol>
 * <li>Use generateBase32Secret() to generate a secret key for a user.</li>
 * <li>Store the secret key in the database associated with the user account.</li>
 * <li>Display the QR image URL returned by qrImageUrl(...) to the user.</li>
 * <li>User uses the image to load the secret key into his authenticator application.</li>
 * </ol>
 * 
 * <p>
 * Whenever the user logs in:
 * </p>
 * 
 * <ol>
 * <li>The user enters the number from the authenticator application into the login form.</li>
 * <li>Read the secret associated with the user account from the database.</li>
 * <li>The server compares the user input with the output from generateCurrentNumber(...).</li>
 * <li>If they are equal then the user is allowed to log in.</li>
 * </ol>
 * 
 * <p>
 * The original class was taken from <a href="https://github.com/j256/two-factor-auth">two-factor-auth</a>.
 * </p>
 * 
 * <p>
 * For more details about this magic algorithm, see: http://en.wikipedia.org/wiki/Time-based_One-time_Password_Algorithm
 * </p>
 * 
 * @author graywatson
 * @author ralph
 */
public class TotpGen {

	/** default number of digits in a OTP string, 6 is default */
	public static final int DEFAULT_OTP_LENGTH = 6;
	/** default time-step which is part of the spec, 30 seconds is default */
	public static final int DEFAULT_TIME_STEP_SECONDS = 30;
	/** set to the number of digits to control 0 prefix, set to 0 for no prefix */
	private static final int MAX_NUM_DIGITS_OUTPUT = 100;

	private static final String blockOfZeros;

	static {
		char[] chars = new char[MAX_NUM_DIGITS_OUTPUT];
		Arrays.fill(chars, '0');
		blockOfZeros = new String(chars);
	}

	private ISecret secret;
	private int     numDigits;
	private int     timeStepSeconds;
	private String  issuer;
	private String  account;
	
	/**
	 * Creates the generator based on secret with 6 digits for the OTP and 30sec time step.
	 * @param secret - the secret
	 */
	public TotpGen(ISecret secret) {
		this(secret, DEFAULT_OTP_LENGTH, DEFAULT_TIME_STEP_SECONDS);
	}

	/**
	 * Creates the generator based on secret with the digits for the OTP.
	 * @param secret - the secret
	 * @param numDigits - the number of digits to produce
	 */
	public TotpGen(ISecret secret, int numDigits) {
		this(secret, numDigits, DEFAULT_TIME_STEP_SECONDS);
	}

	/**
	 * Creates the generator based on secret with the digits for the OTP.
	 * @param secret - the secret
	 * @param numDigits - the number of digits to produce
	 * @param timeStepSeconds - the time step in seconds
	 */
	public TotpGen(ISecret secret, int numDigits, int timeStepSeconds) {
		this.secret          = secret;
		this.numDigits       = numDigits;
		this.timeStepSeconds = timeStepSeconds;
	}

	/**
	 * Returns the secret.
	 * @return the secret
	 */
	public ISecret getSecret() {
		return secret;
	}

	/**
	 * Returns the number of digits to produce.
	 * @return the number of digits to produce
	 */
	public int getNumDigits() {
		return numDigits;
	}

	/**
	 * Returns the time step in seconds.
	 * @return the time step in seconds
	 */
	public int getTimeStepSeconds() {
		return timeStepSeconds;
	}

	/**
	 * Returns the issuer of this TOTP (info only). Can be {@code null} but must not contain colons.
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * Sets the issuer of this TOTP (info only).
	 * @param issuer the issuer of this TOTP (info only). Can be {@code null} but must not contain colons.
	 */
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * Sets the account of this TOTP (info only). Can be {@code null}.
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * Sets the account of this TOTP (info only).
	 * @param account the account of this TOTP.  Can be {@code null}.
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * Returns the otpauth URI scheme to be used e.g. for QR codes.
	 * Uses the issuer and account strings of this generator, if set.
	 * <p>Please refer to <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">otpauth URI scheme</a>.</p>
	 * @return the URI to be used when adding to external auth generators.
	 */
	public URI getUri() {
		return getUri(issuer, account);
	}

	/**
	 * Returns the otpauth URI scheme to be used e.g. for QR codes.
	 * Uses the issuer string of this generator, if set.
	 * <p>Please refer to <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">otpauth URI scheme</a>.</p>
	 * @param account - name of account
	 * @return the URI to be used when adding to external auth generators.
	 */
	public URI getUri(String account) {
		return getUri(issuer, account);
	}

	/**
	 * Returns the otpauth URI scheme to be used e.g. for QR codes.
	 * <p>Please refer to <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">otpauth URI scheme</a>.</p>
	 * @param issuer - issuer of the key, may be {@code null} but must not contain colon
	 * @param account - name of account
	 * @return the URI to be used when adding to external auth generators.
	 */
	public URI getUri(String issuer, String account) {
		if (account == null) account = this.account;
		if (account == null) throw new RuntimeException("Cannot use empty account string");
		StringBuilder rc = new StringBuilder();
		rc.append("otpauth://totp/");
		if (issuer != null) {
			rc.append(URLEncoder.encode(issuer, StandardCharsets.UTF_8));
			rc.append(":");
		}
		rc.append(account);
		rc.append("?secret=");
		rc.append(secret.encode());
		rc.append("&digits=");
		rc.append(numDigits);
		if (timeStepSeconds != 30) {
			rc.append("&period=");
			rc.append(timeStepSeconds);
		}
		if (issuer != null) {
			rc.append("&issuer=");
			rc.append(URLEncoder.encode(issuer, StandardCharsets.UTF_8));
		}
		try {
			return new URI(rc.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot create URI: ", e);
		}
	}

	/**
	 * Validates an OTP. 
	 * 
	 * <p>WARNING: This requires a system clock that is in sync with the world.</p>
	 * 
	 * @param otp
	 *            One time password provided by the user from their authenticator application.
	 * @return True if the OTP matches the calculated OTP at exactly this moment.
	 * @throws GeneralSecurityException when the verification cannot be performed
	 */
	public boolean verify(String otp) throws GeneralSecurityException {
		return verify(otp, 0L, System.currentTimeMillis(), timeStepSeconds, numDigits);
	}

	/**
	 * Validates an OTP. 
	 * This allows you to set a window in milliseconds to account for people being close to the end of the time-step. 
	 * For example, if windowMillis is 10000 then this method will check the OTP against the generated number from 
	 * 10 seconds before now through 10 seconds after now.
	 * 
	 * <p>WARNING: This requires a system clock that is in sync with the world.</p>
	 * 
	 * @param otp
	 *            One time password provided by the user from their authenticator application.
	 * @param windowMillis
	 *            Number of milliseconds that they are allowed to be off and still match. This checks before and after
	 *            the current time to account for clock variance. Set to 0 for no window.
	 * @return True if the OTP matched the calculated OTP within the specified window.
	 * @throws GeneralSecurityException when the verification cannot be performed
	 */
	public boolean verify(String otp, long windowMillis) throws GeneralSecurityException {
		return verify(otp, windowMillis, System.currentTimeMillis(), timeStepSeconds, numDigits);
	}

	/**
	 * Validates an OTP. 
	 * This allows you to set a window in milliseconds to account for people being close to the end of the time-step. 
	 * For example, if windowMillis is 10000 then this method will check the OTP against the generated number from 
	 * 10 seconds before now through 10 seconds after now.
	 * 
	 * @param otp
	 *            One time password provided by the user from their authenticator application.
	 * @param windowMillis
	 *            Number of milliseconds that they are allowed to be off and still match. This checks before and after
	 *            the current time to account for clock variance. Set to 0 for no window.
	 * @param timeInMillis
	 *            Time in milliseconds.
	 * @param timeStepSeconds
	 *            Time step in seconds. The default value is 30 seconds here. See {@link #DEFAULT_TIME_STEP_SECONDS}.
	 * @param numDigits
	 *            The number of digits of the OTP.
	 * @return True if the OTP matched the calculated OTP within the specified window.
	 * @throws GeneralSecurityException when the verification cannot be performed
	 */
	protected boolean verify(String otp, long windowMillis, long timeInMillis, int timeStepSeconds, int numDigits) throws GeneralSecurityException {
		return verifyOtp(otp, windowMillis, timeInMillis, timeStepSeconds, numDigits);
	}

	/**
	 * Returns the current OTP. 
	 * 
	 * <p>WARNING: This requires a system clock that is in sync with the world.</p>
	 * 
	 * @return The OTP which should match the user's authenticator application output.
	 * @throws GeneralSecurityException when the generation cannot be performed
	 */
	public String current() throws GeneralSecurityException {
		return otpAt(System.currentTimeMillis(), timeStepSeconds, numDigits);
	}
	
	/**
	 * Returns an OTP at a given time. 
	 * 
	 * @param timeInMillis
	 *            Time in milliseconds.
	 * @param timeStepSeconds
	 *            Time step in seconds. The default value is 30 seconds here. See {@link #DEFAULT_TIME_STEP_SECONDS}.
	 * @param numDigits
	 *            The number of digits of the OTP.
	 * @return The OTP which should match the user's authenticator application output.
	 * @throws GeneralSecurityException when the generation cannot be performed
	 */
	protected String otpAt(long timeInMillis, int timeStepSeconds, int numDigits) throws GeneralSecurityException {
		long timeIndex = getTimeIndex(timeInMillis, timeStepSeconds);
		return stringify(currentOtp(timeIndex, numDigits), numDigits);
	}

	/**
	 * Returns the OTP's time index based on the given time and time step duration.
	 * @param timeInMillis - time in ms
	 * @param timeStepSeconds - time step duration
	 * @return the time index
	 */
	private long getTimeIndex(long timeInMillis, int timeStepSeconds) {
		return timeInMillis / 1000 / timeStepSeconds;
	}

	/**
	 * Internal helper method actually calculating the current OTP.
	 * @param timeIndex - the time index to calculate
	 * @param numDigits - the number of digits to be used
	 * @return the OTP as a number (needs to be 0-padded on the left for string representation)
	 * @throws GeneralSecurityException when the generation fails
	 */
	private int currentOtp(long timeIndex, int numDigits) throws GeneralSecurityException {
		byte[] data = new byte[8];
		for (int i = 7; timeIndex > 0; i--) {
			data[i] = (byte) (timeIndex & 0xFF);
			timeIndex >>= 8;
		}

		// encrypt the data with the key and return the SHA1 of it in hex
		SecretKeySpec signKey = new SecretKeySpec(secret.getBytes(), "HmacSHA1");
		// if this is expensive, could put in a thread-local
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		// take the 4 least significant bits from the encrypted string as an offset
		int offset = hash[hash.length - 1] & 0xF;

		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = offset; i < offset + 4; ++i) {
			truncatedHash <<= 8;
			// get the 4 bytes at the offset
			truncatedHash |= (hash[i] & 0xFF);
		}
		// cut off the top bit
		truncatedHash &= 0x7FFFFFFF;

		// the token is then the last <length> digits in the number
		long mask = 1;
		for (int i = 0; i < numDigits; i++) {
			mask *= 10;
		}
		truncatedHash %= mask;
		return (int) truncatedHash;
	}

	/**
	 * Internal helper method to verify an OTP given.
	 * @param otp - the OTP as a string
	 * @param windowMillis
	 *            Number of milliseconds that they are allowed to be off and still match. This checks before and after
	 *            the current time to account for clock variance. Set to 0 for no window.
	 * @param timeInMillis
	 *            Time in milliseconds.
	 * @param timeStepSeconds
	 *            Time step in seconds. The default value is 30 seconds here. See {@link #DEFAULT_TIME_STEP_SECONDS}.
	 * @param numDigits
	 *            The number of digits of the OTP.
	 * @return whether the OTP verifies successfully
	 * @throws GeneralSecurityException when the verification cannot be performed
	 */
	protected boolean verifyOtp(String otp, long windowMillis, long timeInMillis, int timeStepSeconds, int numDigits) throws GeneralSecurityException {
		try {
			return verifyOtp(Integer.parseInt(otp), windowMillis, timeInMillis, timeStepSeconds, numDigits);
		} catch (NumberFormatException e) {
			throw new GeneralSecurityException("OTP is not a valid number: "+otp);
		}
	}

	/**
	 * Internal helper method to verify an OTP given.
	 * @param otp - the OTP as number
	 * @param windowMillis
	 *            Number of milliseconds that they are allowed to be off and still match. This checks before and after
	 *            the current time to account for clock variance. Set to 0 for no window.
	 * @param timeInMillis
	 *            Time in milliseconds.
	 * @param timeStepSeconds
	 *            Time step in seconds. The default value is 30 seconds here. See {@link #DEFAULT_TIME_STEP_SECONDS}.
	 * @param numDigits
	 *            The number of digits of the OTP.
	 * @return whether the OTP verifies successfully
	 * @throws GeneralSecurityException when the verification cannot be performed
	 */
	private boolean verifyOtp(int otp, long windowMillis, long timeInMillis, int timeStepSeconds, int numDigits) throws GeneralSecurityException {
		if (windowMillis <= 0) {
			// just test the current time
			long timeIndex = getTimeIndex(timeInMillis, timeStepSeconds);
			long current   = currentOtp(timeIndex, numDigits);
			return (current == otp);
		}
		// maybe check multiple values
		long startIndex = getTimeIndex(timeInMillis - windowMillis, timeStepSeconds);
		long endIndex = getTimeIndex(timeInMillis + windowMillis, timeStepSeconds);
		for (long timeIndex = startIndex; timeIndex <= endIndex; timeIndex++) {
			long current = currentOtp(timeIndex, numDigits);
			if (current == otp) {
				return true;
			}
		}
		return false;
	}


	/**
	 * Return the string prepended with 0s.
	 * @param otp - the OTP to pad
	 * @param digits - the digits to be achieved
	 * @return the 0-padded OTP
	 */
	protected static String stringify(int otp, int digits) {
		String numStr = Integer.toString(otp);
		if (numStr.length() >= digits) {
			return numStr;
		} else {
			StringBuilder sb = new StringBuilder(digits);
			int zeroCount = digits - numStr.length();
			sb.append(blockOfZeros, 0, zeroCount);
			sb.append(numStr);
			return sb.toString();
		}
	}

	/**
	 * Creates a generator from the given URI.
	 * <p>Please refer to <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">otpauth URI scheme</a>.</p>
	 * @param uri - the TOTP URI
	 * @return the generator from this URI
	 * @throws URISyntaxException when the URI is syntactically invalid
	 * @throws URIReferenceException when the URI is semantically invalid
	 */
	public static TotpGen from(String uri) throws URISyntaxException, URIReferenceException {
		return from(new URI(uri));
	}
	
	/**
	 * Creates a generator from the given URI.
	 * <p>Please refer to <a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">otpauth URI scheme</a>.</p>
	 * @param uri - the TOTP URI
	 * @return the generator from this URI
	 * @throws URIReferenceException when the URI is semantically invalid
	 */
	public static TotpGen from(URI uri) throws URIReferenceException {
		if (!"otpauth".equalsIgnoreCase(uri.getScheme())) {
			throw new URIReferenceException("Not a otpauth:// URI.");
		}
		if (!"totp".equalsIgnoreCase(uri.getHost())) {
			throw new URIReferenceException("Not a TOTP URI.");
		}
		String path = uri.getPath().substring(1);
		String parts[] = path.split(":");
		String issuer  = parts.length > 1 ? parts[0] : null;
		String account = parts.length > 1 ? parts[1] : parts[0];
		String query   = uri.getQuery();
		Map<String,String> params = new HashMap<>();
		try {
			for (String part : query.split("&")) {
				String kv[] = part.split("=");
				params.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
			}
		} catch (Throwable t) {
			throw new URIReferenceException("No valid TOTP parameters");
		}
		if (!params.containsKey("secret")) {
			throw new URIReferenceException("No valid TOTP parameters: secret missing");
		}
		ISecret secret = new Base32Secret(params.get("secret"));
		int     digits = 6;  
		if (params.containsKey("digits")) {
			digits = Integer.parseInt(params.get("digits"));
		}
		if (params.containsKey("issuer")) {
			issuer = params.get("issuer");
		}
		int period = 30;
		if (params.containsKey("period")) {
			period = Integer.parseInt(params.get("period"));
		}
		TotpGen rc = new TotpGen(secret, digits, period);
		rc.setAccount(account);
		rc.setIssuer(issuer);
		return rc;
	}
	
	public static void main(String args[]) {
		if (args.length > 0) {
			try {
				String testSecret = args[0];
				TotpGen utils = new TotpGen(new Base32Secret(testSecret));
				String currentOtp = null;
				while (true) {
					String otp = utils.current();
					if (!otp.equalsIgnoreCase(currentOtp)) {
						System.out.println(otp);
						currentOtp = otp;
					}
					Thread.sleep(1000L);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} else {
			System.out.println("You need to give the Base32 secret as argument.");
		}
	}
}
