package rs.jerseyclient.util;

import java.nio.charset.Charset;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Exception thrown when an HTTP 3xx is received.
 *
 */
public class HttpRedirectErrorException extends HttpStatusCodeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a status code only.
	 */
	public HttpRedirectErrorException(HttpStatusCode statusCode) {
		super(statusCode);
	}

	/**
	 * Constructor with a status code and status text.
	 */
	public HttpRedirectErrorException(HttpStatusCode statusCode, String statusText) {
		super(statusCode, statusText);
	}

	/**
	 * Constructor with a status code and status text, and content.
	 */
	public HttpRedirectErrorException(
			HttpStatusCode statusCode, String statusText, byte @Nullable [] body, @Nullable Charset responseCharset) {

		super(statusCode, statusText, body, responseCharset);
	}

	/**
	 * Constructor with a status code and status text, headers, and content.
	 */
	public HttpRedirectErrorException(HttpStatusCode statusCode, String statusText,
			@Nullable HttpHeaders headers, byte @Nullable [] body, @Nullable Charset responseCharset) {

		super(statusCode, statusText, headers, body, responseCharset);
	}

	/**
	 * Constructor with a status code and status text, headers, and content,
	 * and a prepared message.
	 */
	public HttpRedirectErrorException(String message, HttpStatusCode statusCode, String statusText,
			@Nullable HttpHeaders headers, byte @Nullable [] body, @Nullable Charset responseCharset) {

		super(message, statusCode, statusText, headers, body, responseCharset);
	}

}
