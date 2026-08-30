/**
 * 
 */
package rs.restclient.core.util;

import org.springframework.http.HttpHeaders;

import rs.baselib.util.CommonUtils;

/**
 * Sets an user agent.
 * 
 * @author ralph
 *
 */
public class UserAgentInterceptor extends AddHeaderInterceptor {

	private String userAgent;
	
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
		super(HttpHeaders.USER_AGENT, CommonUtils.isEmpty(userAgent) ? "RestClient/6.1" : userAgent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "UserAgentInterceptor [userAgent=" + userAgent + "]";
	}

	
}
