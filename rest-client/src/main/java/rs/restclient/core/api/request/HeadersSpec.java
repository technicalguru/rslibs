package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;
import org.springframework.http.HttpHeaders;

/**
 * To encapsulate in various classes for holding headers.
 * @author ralph
 *
 */
public class HeadersSpec {

    protected MultiValuedMap<String, Object> headers;

	/**
	 * Copy the spec
	 * @param spec the spec to copy
	 */
	public HeadersSpec(HeadersSpec spec) {
		this(spec.getHeaders());
	}

	/**
	 * Creates an empty spec.
	 */
	public HeadersSpec() {
		this(new ArrayListValuedHashMap<>());
	}

	/**
	 * Constructor with predefined headers and cookies.
	 * @param headers headers to be used
	 * @param cookies cookies to be used
	 */
	public HeadersSpec(MultiValuedMap<String, Object> headers) {
		this.headers = new ArrayListValuedHashMap<>(headers);
	}
	
	/**
	 * Returns the headers (cannot be modified).
	 * @return the headers
	 */
	public MultiValuedMap<String, Object> getHeaders() {
		return UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(headers);
	}

	/**
	 * Adds a header.
	 * @param name name of header
	 * @param values value(s) of header
	 */
	public void add(String name, Object ...values) {
		for (Object value : values) {
			if (value != null) headers.put(name, value);
		}
	}
	
	/**
	 * Adds multiple headers.
	 * @param headers headers to add
	 */
	public void add(MultiValuedMap<String, Object> headers) {
		this.headers.putAll(headers);
	}
	
	/**
	 * Adds a cookie.
	 * @param name name of cookie
	 * @param cookie value of cookie
	 */
	public void addCookie(String name, String cookie) {
		add(HttpHeaders.COOKIE, cookie);
	}
	
	/**
	 * Adds a cookie.
	 * @param cookie cookie to add
	 */
	public void addCookie(String cookie) {
		addCookies(HttpCookie.parse(cookie));
	}
	
	/**
	 * Adds all cookies.
	 * @param cookies cookies to add
	 */
	public void addCookies(Collection<HttpCookie> cookies) {
		for (HttpCookie cookie : cookies) {
			addCookie(cookie);
		}
	}

	/**
	 * Adds a cookie.
	 * @param cookie cookie to add
	 */
	public void addCookie(HttpCookie cookie) {
		add(HttpHeaders.COOKIE, cookie.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "HeadersSpec [headers=" + headers + "]";
	}
	


}
