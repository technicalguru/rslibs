/**
 * 
 */
package rs.otp;

/**
 * @author ralph
 *
 */
public class QrUrlGenerator {

	/** default height/width of QR image */
	public static int DEFAULT_QR_DIMENSION = 200;

	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * 
	 * @param keyId
	 *            Name of the key that you want to show up in the users authentication application. Should already be
	 *            URL encoded.
	 * @param secret
	 *            Secret string that will be used when generating the current number.
	 *            
	 * @return the image URL
	 */
	public static String qrImageUrl(String keyId, String secret) {
		return qrImageUrl(keyId, secret, OtpGen.DEFAULT_OTP_LENGTH, DEFAULT_QR_DIMENSION);
	}

	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 *
	 * @param keyId
	 *            Name of the key that you want to show up in the users authentication application. Should already be
	 *            URL encoded.
	 * @param secret
	 *            Secret string that will be used when generating the current number.
	 * @param numDigits
	 *            The number of digits of the OTP.
	 * @return the image URL
	 */
	public static String qrImageUrl(String keyId, String secret, int numDigits) {
		return qrImageUrl(keyId, secret, numDigits, DEFAULT_QR_DIMENSION);
	}

	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * 
	 * @param keyId
	 *            Name of the key that you want to show up in the users authentication application. Should already be
	 *            URL encoded.
	 * @param secret
	 *            Secret string that will be used when generating the current number.
	 * @param numDigits
	 *            The number of digits of the OTP. Can be set to {@link OtpGen#DEFAULT_OTP_LENGTH}.
	 * @param imageDimension
	 *            The dimension of the image, width and height. Can be set to {@link #DEFAULT_QR_DIMENSION}.
	 * @return the image URL
	 */
	public static String qrImageUrl(String keyId, String secret, int numDigits, int imageDimension) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("https://chart.googleapis.com/chart?chs=" + imageDimension + "x" + imageDimension + "&cht=qr&chl="
				+ imageDimension + "x" + imageDimension + "&chld=M|0&cht=qr&chl=");
		addOtpAuthPart(keyId, secret, sb, numDigits);
		return sb.toString();
	}

	/**
	 * Return the otp-auth part of the QR image which is suitable to be injected into other QR generators (e.g. JS
	 * generator).
	 *
	 * @param keyId
	 *            Name of the key that you want to show up in the users authentication application. Should already be
	 *            URL encoded.
	 * @param secret
	 *            Secret string that will be used when generating the current number.
	 * @return the otp-auth part for an image URL
	 */
	public static String generateOtpAuthUrl(String keyId, String secret) {
		return generateOtpAuthUrl(keyId, secret, OtpGen.DEFAULT_OTP_LENGTH);
	}

	/**
	 * Return the otp-auth part of the QR image which is suitable to be injected into other QR generators (e.g. JS
	 * generator).
	 *
	 * @param keyId
	 *            Name of the key that you want to show up in the users authentication application. Should already be
	 *            URL encoded.
	 * @param secret
	 *            Secret string that will be used when generating the current number.
	 * @param numDigits
	 *            The number of digits" of the OTP.
	 * @return the otp-auth part for an image URL
	 */
	public static String generateOtpAuthUrl(String keyId, String secret, int numDigits) {
		StringBuilder sb = new StringBuilder(128);
		addOtpAuthPart(keyId, secret, sb, numDigits);
		return sb.toString();
	}

	private static void addOtpAuthPart(String keyId, String secret, StringBuilder sb, int numDigits) {
		sb.append("otpauth://totp/")
				.append(keyId)
				.append("%3Fsecret%3D")
				.append(secret)
				.append("%26digits%3D")
				.append(numDigits);
	}

}
