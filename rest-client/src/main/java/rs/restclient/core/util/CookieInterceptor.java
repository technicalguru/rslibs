/**
 * 
 */
package rs.restclient.core.util;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * Handles cookies and "Accept" header in requests and responses.
 * 
 * @author ralph
 *
 */
public class CookieInterceptor extends AbstractRequestInterceptor {

	/** Default Accept header value */
	public static final String DEFAULT_ACCEPT_HEADER = "application/json";
	
	private Map<String, HttpCookie> cookies;
	private String                  acceptableMediaTypes = DEFAULT_ACCEPT_HEADER;
	
	/**
	 * Default Constructor.
	 */
	public CookieInterceptor() {
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
	public void intercept(RestResponse response) throws IOException {
//		for (String value : response.getHeaders().get(HttpHeaders.SET_COOKIE)) {
//			for (HttpCookie cookie : HttpCookie.parse(value)) {
//				String n = cookie.getName();
//				cookies.put(n, cookie);
//			}
//		}
	}

	/**
	 * Sets cookies if required in the request.
	 */
	@Override
	public void intercept(RestRequest request, byte[] body) throws IOException {
		for (Map.Entry<String, HttpCookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			HttpCookie cookie = entry.getValue();
			if (cookie.hasExpired()) {
				cookies.remove(name);
			} else {
				request.addCookie(cookie);
			}
		}
		String v = getAcceptableMediaTypes();
		if (v != null) request.addHeader(HttpHeaders.ACCEPT, v);
	}

}
