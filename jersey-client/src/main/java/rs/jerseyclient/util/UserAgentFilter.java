/**
 * 
 */
package rs.jerseyclient.util;

import java.io.IOException;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;

/**
 * Handles cookies in requests and responses.
 * 
 * @author ralph
 *
 */
public class UserAgentFilter implements ClientRequestFilter {

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
	public void filter(ClientRequestContext requestContext) throws IOException {
		if (userAgent != null) requestContext.getHeaders().add("User-Agent", userAgent);
	}

}
