/**
 * 
 */
package rs.jerseyclient.util;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.RuntimeDelegate;

/**
 * Handles cookies and "Accept" header in requests and responses.
 * 
 * @author ralph
 *
 */
public class CookieAuthorizationFilter implements ClientRequestFilter, ClientResponseFilter {

	/** Default Accept header value */
	public static final String DEFAULT_ACCEPT_HEADER = "application/json";
	
	private Map<String, NewCookie> cookies;
	private String                 acceptableMediaTypes = DEFAULT_ACCEPT_HEADER;
	
	/**
	 * Default Constructor.
	 */
	public CookieAuthorizationFilter() {
		cookies = new HashMap<>();
	}

	/**
	 * Returns the acceptable media types to signal to the sever. Default is {@link #DEFAULT_ACCEPT_HEADER}.
	 * @return the acceptable mediatypes
	 */
	public String getAcceptableMediaTypes() {
		return acceptableMediaTypes;
	}

	/**
	 * Sets the acceptable media types to signal to the sever. Default is {@link #DEFAULT_ACCEPT_HEADER}.
	 * @param acceptableMediaTypes the acceptable media types to set
	 */
	public void setAcceptableMediaTypes(String acceptableMediaTypes) {
		this.acceptableMediaTypes = acceptableMediaTypes;
	}

	/**
	 * Filters the reponse and evaluate the cookies to be set.
	 */
	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		for (Map.Entry<String, NewCookie> cookie : responseContext.getCookies().entrySet()) {
			String n = cookie.getKey();
			NewCookie c = cookie.getValue();
			cookies.put(n, c);
		}
	}

	/**
	 * Sets cookies if required in the request.
	 */
	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		Date now = new Date();
		for (Map.Entry<String, NewCookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			NewCookie cookie = entry.getValue();
			Date expiry = cookie.getExpiry();
			if ((expiry != null) && expiry.before(now)) {
				cookies.remove(name);
			} else {
				requestContext.getHeaders().add("Cookie", RuntimeDelegate.getInstance().createHeaderDelegate(NewCookie.class).toString(cookie)); //cookie.getName()+'='+cookie.getValue());
			}
		}
		String v = getAcceptableMediaTypes();
		if (v != null) requestContext.getHeaders().add("Accept", v);
	}

}
