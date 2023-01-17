/**
 * 
 */
package rs.otp;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Produces URLs to target Google API for QR images.
 * <p><b>Warning!</b> It is not recommended to transfer secrets to third parties. Use a library instead to
 * generate the QR yourself.</p>
 * 
 * @author ralph
 *
 */
public class GoogleApiQrGenerator {

	/** default height/width of QR image, default is 200. */
	public static int DEFAULT_QR_DIMENSION = 200;

	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret. The image has the {@link #DEFAULT_QR_DIMENSION} size.
	 * <p>Issuer and account will be taken from the TOTP.</p>
	 * 
	 * @param generator - The OTP generator to create a QR image for.
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(TotpGen generator) throws UnsupportedEncodingException {
		return getQrImageUrl(generator.getUri(), DEFAULT_QR_DIMENSION);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * <p>Issuer and account will be taken from the TOTP.</p>
	 * 
	 * @param generator - The OTP generator to create a QR image for.
	 * @param imageDimension - The dimension of the image, width and height. Can be set to {@link #DEFAULT_QR_DIMENSION}.
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(TotpGen generator, int imageDimension) throws UnsupportedEncodingException {
		return getQrImageUrl(generator.getUri(), imageDimension);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret. The image has the {@link #DEFAULT_QR_DIMENSION} size.
	 * 
	 * @param generator - The OTP generator to create a QR image for.
	 * @param issuer - issuer of the key, may be {@code null} but must not contain colon
	 * @param account - name of account
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(TotpGen generator, String issuer, String account) throws UnsupportedEncodingException {
		return getQrImageUrl(generator.getUri(issuer, account), DEFAULT_QR_DIMENSION);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * 
	 * @param generator - The OTP generator to create a QR image for.
	 * @param issuer - issuer of the key, may be {@code null} but must not contain colon
	 * @param account - name of account
	 * @param imageDimension - The dimension of the image, width and height. Can be set to {@link #DEFAULT_QR_DIMENSION}.
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(TotpGen generator, String issuer, String account, int imageDimension) throws UnsupportedEncodingException {
		return getQrImageUrl(generator.getUri(issuer, account), imageDimension);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret. The image has the {@link #DEFAULT_QR_DIMENSION} size.
	 * 
	 * @param otpUri - the URI to be shown on the QR code
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(URI otpUri) throws UnsupportedEncodingException {
		return getQrImageUrl(otpUri.toString(), DEFAULT_QR_DIMENSION);
	}
		
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * 
	 * @param otpUri - the URI to be shown on the QR code
	 * @param imageDimension - The dimension of the image, width and height. Can be set to {@link #DEFAULT_QR_DIMENSION}.
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(URI otpUri, int imageDimension) throws UnsupportedEncodingException {
		return getQrImageUrl(otpUri.toString(), imageDimension);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret. The image has the {@link #DEFAULT_QR_DIMENSION} size.
	 * 
	 * @param otpUri - the URI to be shown on the QR code
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(String otpUri) throws UnsupportedEncodingException {
		return getQrImageUrl(otpUri.toString(), DEFAULT_QR_DIMENSION);
	}
	
	/**
	 * Return the QR image url thanks to Google. This can be shown to the user and scanned by the authenticator program
	 * as an easy way to enter the secret.
	 * 
	 * @param otpUri - the URI to be shown on the QR code
	 * @param imageDimension - The dimension of the image, width and height. Can be set to {@link #DEFAULT_QR_DIMENSION}.
	 * @return the image URL
	 * @throws UnsupportedEncodingException when the URL encoding fails
	 */
	public static String getQrImageUrl(String otpUri, int imageDimension) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder(128);
		sb.append("https://chart.googleapis.com/chart?chs=" + imageDimension + "x" + imageDimension + "&cht=qr&chl="
				+ imageDimension + "x" + imageDimension + "&chld=M|0&cht=qr&chl=");
		sb.append(URLEncoder.encode(otpUri, StandardCharsets.UTF_8));
		return sb.toString();
	}
		
}
