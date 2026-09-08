/**
 * 
 */
package rs.baselib.util;

import java.net.URI;
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
	
	/**
	 * Constructor.
	 * @param uri URI to build with
	 */
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
	
	/**
	 * Constructor.
	 * @param scheme scheme of URI
	 * @param userInfo userInfo of URI
	 * @param host host of URI
	 * @param port port of URI
	 * @param path path of URI
	 * @param query query of URI
	 * @param fragment fragment of URI
	 */
	protected UriBuilder(String scheme, String userInfo, String host, int port, String path, String query, String fragment) {
		this.segments    = new ArrayList<>();
		this.queryParams = new ArrayListValuedHashMap<>();
		scheme(scheme);
		if (!CommonUtils.isEmpty(userInfo)) {
			String info[] = userInfo.split(":");
			user(info[0]);
			if (info.length > 1) password(info[1]);
		}
		host(host);
		port(port);
		path(path);
		query(query);
		fragment(fragment);
	}

	/**
	 * Returns the scheme.
	 * @return the scheme
	 */
	public String scheme() {
		return scheme;
	}

	/**
	 * Sets the scheme.
	 * @param scheme the scheme to set
	 * @return this builder for chaining
	 */
	public UriBuilder scheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * Returns the user.
	 * @return the user
	 */
	public String user() {
		return user;
	}

	/**
	 * Sets the user.
	 * @param user the user to set
	 * @return this builder for chaining
	 */
	public UriBuilder user(String user) {
		this.user = user;
		return this;
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String password() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password the password to set
	 * @return this builder for chaining
	 */
	public UriBuilder password(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Returns the host.
	 * @return the host
	 */
	public String host() {
		return host;
	}

	/**
	 * Sets the host.
	 * @param host the host to set
	 * @return this builder for chaining
	 */
	public UriBuilder host(String host) {
		this.host = host;
		return this;
	}

	/**
	 * Returns the port.
	 * @return the port
	 */
	public String port() {
		return port;
	}

	/**
	 * Sets the port.
	 * @param port the port to set
	 * @return this builder for chaining
	 */
	public UriBuilder port(String port) {
		this.port = port;
		return this;
	}

	/**
	 * Sets the port.
	 * @param port the port to set
	 * @return this builder for chaining
	 */
	public UriBuilder port(int port) {
		port(port > 0 ? ""+port : null);
		return this;
	}
	
	/**
	 * Returns the segments.
	 * @return the segments
	 */
	public String path() {
		StringBuilder path = new StringBuilder();
		for (String segment : segments) {
			path.append('/');
			path.append(segment);
		}
		String rc = path.toString();
		while (rc.indexOf("//") >= 0) rc = rc.replace("//", "/");
		return rc;
	}

	/**
	 * Sets the segments.
	 * @param segments the segments to set
	 * @return this builder for chaining
	 */
	public UriBuilder path(String path) {
		this.segments.clear();
		if (path != null) {
			String segments[] = path.split("\\/");
			for (String segment : segments) {
				if (!CommonUtils.isEmpty(segment)) this.segments.add(segment);
			}
			if (path.endsWith("/")) this.segments.add("");
		}
		return this;
	}

	/**
	 * Returns the queryParams.
	 * @return the queryParams
	 */
	public MultiValuedMap<String, String> queryParams() {
		return UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(queryParams);
	}

	/**
	 * Sets the queryParams.
	 * @param queryParams the queryParams to set
	 * @return this builder for chaining
	 */
	public UriBuilder queryParams(MultiValuedMap<String, String> queryParams) {
		this.queryParams.clear();
		this.queryParams.putAll(queryParams);
		return this;
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
	 * @return this builder for chaining
	 */
	public UriBuilder query(String query) {
		this.queryParams.clear();
		if (query != null) {
			String params[] = query.split("&");
			for (String param : params) {
				String p[] = param.split("=");
				if (p.length > 1) queryParam(UriUtils.decodeQueryParamPart(p[0]), UriUtils.decodeQueryParamPart(p[1]));
				else queryParam(UriUtils.decodeQueryParamPart(p[0]), null);
			}
		}
		return this;
	}
	
	/**
	 * Returns the fragment.
	 * @return the fragment
	 */
	public String fragment() {
		return fragment;
	}

	/**
	 * Sets the fragment.
	 * @param fragment the fragment to set
	 * @return this builder for chaining
	 */
	public UriBuilder fragment(String fragment) {
		this.fragment = fragment;
		return this;
	}

	/**
	 * Appends the given segment(s)
	 * @param segments the segments to add (if a "/" is in a segment then it will be considered as a separator)
	 * @return this builder for chaining
	 */
	public UriBuilder appendSegments(String ...segments) {
		for (String segment : segments) {
			boolean isMultiple = segment.indexOf('/') > 0;
			if (isMultiple) appendSegments(segment.split("\\/"));
			else this.segments.add(segment);
		}
		return this;
	}
	
	/**
	 * Returns the fragments.
	 * @return the fragments
	 */
	protected List<String> segments() {
		return this.segments;
	}
	
	/**
	 * Build the URI
	 * @return URI built.
	 */
	public URI build() {
    	StringBuilder uri = new StringBuilder();
    	if (scheme != null) {
    		uri.append(scheme);
    		uri.append(":");
    	}
    	if (!CommonUtils.isEmpty(user) || !CommonUtils.isEmpty(host) || !CommonUtils.isEmpty(port)) {
    		uri.append("//");
    		if (!CommonUtils.isEmpty(user)) {
    			uri.append(UriUtils.encodeAuthInfo(user));
    			if (!CommonUtils.isEmpty(password)) {
    				uri.append(':');
    				uri.append(UriUtils.encodeAuthInfo(password));
    			}
    			uri.append("@");
    		}
    		if (!CommonUtils.isEmpty(host)) {
    			uri.append(host);
    		}
    		if (!CommonUtils.isEmpty(port)) {
    			uri.append(":");
    			uri.append(port);
    		}
    	}
    	if (!segments.isEmpty() || ((queryParams != null) && !queryParams.isEmpty()) || !CommonUtils.isEmpty(fragment)) {
    		if (!segments.isEmpty()) {
   				// trailing slash appears when last segement is empty string
    			uri.append(UriUtils.encodePath(path()));
    		}
    		if ((queryParams != null) && !queryParams.isEmpty()) {
    			uri.append('?');
    			uri.append(UriUtils.encodeQuery(queryParams));
    		}
    		if (!CommonUtils.isEmpty(fragment)) {
    			uri.append('#');
    			uri.append(UriUtils.encodeFragment(fragment));
    		}
    	}
        return URI.create(uri.toString());
	}
	
	/**
	 * Create Builder from string
	 * @param uri URI as string
	 * @return the builder
	 */
	public static UriBuilder empty() {
		return from(URI.create(""));
	}
	

	/**
	 * Create Builder from string
	 * @param uri URI as string
	 * @return the builder
	 */
	public static UriBuilder from(String uri) {
		return from(URI.create(uri));
	}
	
	/**
	 * Create Builder from URI
	 * @param uri URI as string
	 * @return the builder
	 */
	public static UriBuilder from(URI uri) {
		return new UriBuilder(uri);
	}
	

}
