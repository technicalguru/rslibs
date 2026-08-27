/**
 * 
 */
package rs.restclient.core.util;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.api.response.RestResponse;

/**
 * Handles cookies and "Accept" header in requests and responses.
 * 
 * @author ralph
 *
 */
public class CookieInterceptor extends AbstractRequestInterceptor {

	private Map<String, HttpCookie> cookies;
	
	/**
	 * Default Constructor.
	 */
	public CookieInterceptor() {
		cookies = new HashMap<>();
	}

	/**
	 * Filters the reponse and evaluate the cookies to be set.
	 */
	@Override
	public void intercept(RestRequest request, RestResponse response) throws IOException {
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
	public void intercept(RestRequest request) throws IOException {
		for (Map.Entry<String, HttpCookie> entry : cookies.entrySet()) {
			String name = entry.getKey();
			HttpCookie cookie = entry.getValue();
			if (cookie.hasExpired()) {
				cookies.remove(name);
			} else {
				request.addCookie(cookie);
			}
		}
	}

}
