/**
 * 
 */
package rs.otp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import rs.otp.secret.ISecret;

/**
 * @author ralph
 *
 */
public class GoogleApiQrGenerator {

	/** default height/width of QR image, default is 200. */
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
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(String keyId, ISecret secret) throws UnsupportedEncodingException {
		return getQrImageUrl(keyId, secret, OtpGen.DEFAULT_OTP_LENGTH, DEFAULT_QR_DIMENSION);
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
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(String keyId, ISecret secret, int numDigits) throws UnsupportedEncodingException {
		return getQrImageUrl(keyId, secret, numDigits, DEFAULT_QR_DIMENSION);
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
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(String keyId, ISecret secret, int numDigits, int imageDimension) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(128);
		sb.append("https://chart.googleapis.com/chart?chs=" + imageDimension + "x" + imageDimension + "&cht=qr&chl="
				+ imageDimension + "x" + imageDimension + "&chld=M|0&cht=qr&chl=");
		sb.append(URLEncoder.encode(secret.getOtpAuthUri(keyId, numDigits), StandardCharsets.UTF_8));
		System.out.println(sb.toString());
		return sb.toString();
	}

}
