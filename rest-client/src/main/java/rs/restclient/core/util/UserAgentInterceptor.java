/**
 * 
 */
package rs.restclient.core.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;

import rs.restclient.core.api.request.RestRequest;

/**
 * Handles cookies in requests and responses.
 * 
 * @author ralph
 *
 */
public class UserAgentInterceptor extends AbstractRequestInterceptor {

	private String                 userAgent;
	
	/**
	 * Default Constructor.
	 */
	public UserAgentInterceptor() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param userAgent - the user agent string to be used.
	 */
	public UserAgentInterceptor(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request) throws IOException {
		request.addHeader(HttpHeaders.USER_AGENT, userAgent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "UserAgentInterceptor [userAgent=" + userAgent + "]";
	}

	
}
