/**
 * 
 */
package rs.restclient.core.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

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
	protected void intercept(RestRequest request, byte[] body) throws IOException {
		request.addHeader(HttpHeaders.USER_AGENT, userAgent);

		System.out.println("Request: "+request);
		System.out.println("Body:    "+body);	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestResponse response) throws IOException {
		System.out.println("Response: "+response);
	}

	
}
