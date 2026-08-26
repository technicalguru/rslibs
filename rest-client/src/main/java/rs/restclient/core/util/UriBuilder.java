/**
 * 
 */
package rs.restclient.core.util;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

/**
 * Helps to construct new URIs.
 */
public class UriBuilder {

	private String                         scheme;
	private String                         user;
	private String                         password;
	private String                         host;
	private String                         port;
	private List<String>                   segments;
	private MultiValuedMap<String, String> queryParams;
	private String                         fragment;
	
	protected UriBuilder(URI uri) {
		this(
			uri.getScheme(),
			uri.getUserInfo(),
			uri.getHost(),
			uri.getPort(),
			uri.getPath(),
			uri.getRawQuery(),
			uri.getFragment()
		);
	}
	
	protected UriBuilder(String scheme, String userInfo, String host, int port, String path, String query, String fragment) {
		this.segments    = new ArrayList<>();
		this.queryParams = new ArrayListValuedHashMap<>();
		setScheme(scheme);
		if (!RestClientUtils.isEmpty(userInfo)) {
			String info[] = userInfo.split(":");
			setUser(info[0]);
			if (info.length > 1) setPassword(info[1]);
		}
		setHost(host);
		setPort(port);
		setPath(path);
		setQuery(query);
		setFragment(fragment);
	}

	/**
	 * Returns the scheme.
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * Sets the scheme.
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * Returns the user.
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the host.
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns the port.
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets the port.
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Sets the port.
	 * @param port the port to set
	 */
	public void setPort(int port) {
		setPort(port > 0 ? ""+port : null);
	}
	
	/**
	 * Returns the segments.
	 * @return the segments
	 */
	public String getPath() {
		StringBuilder path = new StringBuilder();
		for (String segment : segments) {
			path.append('/');
			path.append(encodePathSegment(segment));
		}
		String rc = path.toString();
		while (rc.indexOf("//") >= 0) rc = rc.replace("//", "/");
		return rc;
	}

	/**
	 * Sets the segments.
	 * @param segments the segments to set
	 */
	public void setPath(String path) {
		this.segments.clear();
		if (path != null) {
			String segments[] = path.split("\\/");
			for (String segment : segments) {
				if (!RestClientUtils.isEmpty(segment)) this.segments.add(segment);
			}
			if (path.endsWith("/")) this.segments.add("");
		}
	}

	/**
	 * Returns the queryParams.
	 * @return the queryParams
	 */
	public MultiValuedMap<String, String> getQueryParams() {
		return UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(queryParams);
	}

	/**
	 * Sets the queryParams.
	 * @param queryParams the queryParams to set
	 */
	public void setQueryParams(MultiValuedMap<String, String> queryParams) {
		this.queryParams.clear();
		this.queryParams.putAll(queryParams);
	}

	/**
	 * Adds a single query param
	 * @param name name of param
	 * @param value value of param
	 */
	public void queryParam(String name, String value) {
		this.queryParams.put(name, value);
	}
	
	/**
	 * Replaces the complete query.
	 * @param query the query to be set
	 */
	public void setQuery(String query) {
		this.queryParams.clear();
			if (query != null) {
			String params[] = query.split("&");
			for (String param : params) {
				String p[] = param.split(":");
				if (p.length > 1) queryParam(decodeQueryParam(p[0]), decodeQueryParam(p[1]));
				else queryParam(decodeQueryParam(p[0]), null);
			}
		}
	}
	
	/**
	 * Returns the fragment.
	 * @return the fragment
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * Sets the fragment.
	 * @param fragment the fragment to set
	 */
	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public UriBuilder appendSegments(String ...segments) {
		for (String segment : segments) {
			boolean isMultiple = segment.indexOf('/') > 0;
			if (isMultiple) appendSegments(segment.split("\\/"));
			else this.segments.add(segment);
		}
		return this;
	}
	
	public URI build() {
    	StringBuilder uri = new StringBuilder();
    	if (scheme != null) {
    		uri.append(scheme);
    		uri.append(":");
    	}
    	if (!RestClientUtils.isEmpty(user) || !RestClientUtils.isEmpty(host) || !RestClientUtils.isEmpty(port)) {
    		uri.append("//");
    		if (!RestClientUtils.isEmpty(user)) {
    			uri.append(encodeAuthInfo(user));
    			if (!RestClientUtils.isEmpty(password)) {
    				uri.append(':');
    				uri.append(encodeAuthInfo(password));
    			}
    			uri.append("@");
    		}
    		if (!RestClientUtils.isEmpty(host)) {
    			uri.append(host);
    		}
    		if (!RestClientUtils.isEmpty(port)) {
    			uri.append(":");
    			uri.append(port);
    		}
    	}
    	if (!segments.isEmpty() || ((queryParams != null) && !queryParams.isEmpty()) || !RestClientUtils.isEmpty(fragment)) {
    		if (!segments.isEmpty()) {
   				// trailing slash appears when last segement is empty string
    			uri.append(getPath());
    		}
    		if ((queryParams != null) && !queryParams.isEmpty()) {
    			uri.append('?');
    			uri.append(encodeQuery());
    		}
    		if (!RestClientUtils.isEmpty(fragment)) {
    			uri.append('#');
    			uri.append(encodeFragment(fragment));
    		}
    	}
        return URI.create(uri.toString());
	}
	
	public static UriBuilder from(String uri) {
		return from(URI.create(uri));
	}
	
	public static UriBuilder from(URI uri) {
		return new UriBuilder(uri);
	}
	
	private String encodeAuthInfo(String s) {
		if (s == null) return null;
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}

	private String encodePathSegment(String s) {
		if (s == null) return null;
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}

	private String encodeQuery() {
		if (queryParams == null || queryParams.isEmpty()) {
			return null;
		}

		StringBuilder rc = new StringBuilder();
		for (String name : queryParams.keySet()) {
			for (String value : queryParams.get(name)) {
				if (rc.length() > 0) {
					rc.append('&');
				}
				rc.append(encodeQueryParam(name));
				if (!RestClientUtils.isEmpty(value)) rc.append('=').append(encodeQueryParam(value));
			}
		}
		return rc.toString();
	}

	private String decodeQueryParam(String s) {
		return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}
	
	private String encodeQueryParam(String s) {
		if (s == null)
			return null;
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}

    private String encodeFragment(String s) {
    	if (s == null) return null;
    	return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

}
