package rs.jerseyclient.util;

import org.springframework.web.client.RestClientException;

/**
 * Exception thrown when HTTP COnnection fails.
 *
 */
public class HttpConnectionException extends RestClientException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a status code only.
	 */
	public HttpConnectionException(String message, Throwable t) {
		super(message, t);
	}

}
