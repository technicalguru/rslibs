/**
 * 
 */
package rs.restclient.core.util;

import java.net.URI;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;

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
			uri.getQuery(),
			uri.getFragment()
		);
	}
	
	protected UriBuilder(String scheme, String userInfo, String host, int port, String path, String query, String fragment) {
		
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
		return null;
	}
	
	public static UriBuilder from(String uri) {
		return from(URI.create(uri));
	}
	
	public static UriBuilder from(URI uri) {
		return new UriBuilder(uri);
	}
}
