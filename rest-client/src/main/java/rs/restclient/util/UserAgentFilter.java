/**
 * 
 */
package rs.restclient.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

/**
 * Handles cookies in requests and responses.
 * 
 * @author ralph
 *
 */
public class UserAgentFilter extends AbstractFilter {

	private String                 userAgent;
	
	/**
	 * Default Constructor.
	 */
	public UserAgentFilter() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param userAgent - the user agent string to be used.
	 */
	public UserAgentFilter(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Sets user agent if it was set.
	 */
	@Override
	public void intercept(HttpRequest request, byte[] body) throws IOException {
		if (userAgent != null) request.getHeaders().add(HttpHeaders.USER_AGENT, userAgent);
	}
}
