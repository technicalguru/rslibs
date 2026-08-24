package rs.jerseyclient.util;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

/**
 * @author ralph
 *
 */
public class RestUriBuilder {

	// All fields should be in the percent-encoded form
    private String scheme;
    private String userInfo;
    private String host;
    private String port;
    private final StringBuilder path;
    private MultiValuedMap<String, String> queryParams;
    private String fragment;
    
    /**
     * Create new implementation of {@code UriBuilder}.
     */
    public RestUriBuilder() {
        path = new StringBuilder();
    }

    private RestUriBuilder(final RestUriBuilder that) {
        this.scheme = that.scheme;
        this.userInfo = that.userInfo;
        this.host = that.host;
        this.port = that.port;
        this.path = new StringBuilder(that.path);
        this.queryParams = that.queryParams == null ? null : new ArrayListValuedHashMap<>(that.queryParams);
        this.fragment = that.fragment;
    }

    public RestUriBuilder clone() {
        return new RestUriBuilder(this);
    }
    
    public RestUriBuilder uri(final URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri must not be 'null'");
        }

        if (uri.getRawFragment() != null) {
            fragment = uri.getRawFragment();
        }

        if (uri.isOpaque()) {
            scheme = uri.getScheme();
            return this;
        }

        if (uri.getScheme() != null) {
            scheme = uri.getScheme();
        }

        if (uri.getRawUserInfo() != null) {
            userInfo = uri.getRawUserInfo();
        }
        if (uri.getHost() != null) {
            host = uri.getHost();
        }
        if (uri.getPort() != -1) {
            port = String.valueOf(uri.getPort());
        }

        if (uri.getRawPath() != null && !uri.getRawPath().isEmpty()) {
            path.setLength(0);
            path.append(uri.getRawPath());
        }
        if (uri.getRawQuery() != null && !uri.getRawQuery().isEmpty()) {
            queryParams = parseQuery(uri.getRawQuery());
        }

        return this;
    }
    
    public RestUriBuilder scheme(final String scheme) {
        if (scheme != null) {
            this.scheme = scheme;
         } else {
            this.scheme = null;
        }
        return this;
    }
    
    public RestUriBuilder userInfo(final String ui) {
         this.userInfo = ui;
        return this;
    }
    
    public RestUriBuilder host(final String host) {
        if (host != null) {
            if (host.isEmpty()) {
                throw new IllegalArgumentException("host must not be empty");
            }
            this.host = host;
        } else {
            // null is used to reset host setting
            this.host = null;
        }
        return this;
    }
    
    public RestUriBuilder port(final int port) {
        if (port < -1) {
            // -1 is used to reset port setting and since URI allows
            // as port any positive integer, so do we.
            throw new IllegalArgumentException("port must not be negative");
        }
        this.port = port == -1 ? null : String.valueOf(port);
        return this;
    }
    
    public RestUriBuilder replacePath(final String path) {
    	this.path.setLength(0);
        if (path != null) {
            appendPath(path);
        }
        return this;
    }
    
    public RestUriBuilder path(final String path) {
        appendPath(path);
        return this;
    }
    
    public RestUriBuilder segment(final String... segments) throws IllegalArgumentException {
        if (segments == null) {
            throw new IllegalArgumentException("segments must not be 'null'");
        }

        for (final String segment : segments) {
            appendPath(segment, true);
        }
        return this;
    }
    
    public RestUriBuilder queryParam(String name, final Object... values) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be 'null'");
        }
        if (values == null) {
            throw new IllegalArgumentException("values must not be 'null'");
        }
        if (values.length == 0) {
            return this;
        }

        name = encodeQueryParam(name);

        for (final Object value : values) {
            if (value == null) {
                throw new IllegalArgumentException("value must not be 'null'");
            }

            queryParams.put(name, encodeQueryParam(value.toString()));
        }
        return this;
    }

    public RestUriBuilder replaceQueryParam(String name, final Object... values) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be 'null'");
        }
       	if (queryParams == null) queryParams = new ArrayListValuedHashMap<>();
        
        queryParams.remove(encodeQueryParam(name));
        if (values != null) return queryParam(name, values);
        return this;
    }
    
    public RestUriBuilder fragment(final String fragment) {
        this.fragment = (fragment != null)
                ? encodeFragment(fragment)
                : null;
        return this;
    }
    
    public URI build() {
    	StringBuilder uri = new StringBuilder();
    	if (scheme != null) {
    		uri.append(scheme);
    		uri.append(":");
    	}
    	if (!RestClientUtils.isEmpty(userInfo) || !RestClientUtils.isEmpty(host) || !RestClientUtils.isEmpty(port)) {
    		uri.append("//");
    		if (!RestClientUtils.isEmpty(userInfo)) {
    			uri.append(userInfo);
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
    	if (!path.isEmpty() || ((queryParams != null) && !queryParams.isEmpty()) || !RestClientUtils.isEmpty(fragment)) {
    		if (!path.isEmpty()) {
    			if (path.charAt(0) != '/') {
    				uri.append('/');
    			}
    			uri.append(path);
    		}
    		if ((queryParams != null) && !queryParams.isEmpty()) {
    			uri.append('?');
    			uri.append(encodeQuery());
    		}
    		if (!RestClientUtils.isEmpty(fragment)) {
    			uri.append('#');
    			uri.append(fragment);
    		}
    	}
        return URI.create(uri.toString());
    }
    
    private void appendPath(final String path) {
        appendPath(path, false);
    }
    
    private void appendPath(String segments, final boolean isSegment) {
        if (segments == null) {
            throw new IllegalArgumentException("segments cannot be 'null'");
        }
        if (segments.isEmpty()) {
            return;
        }

        segments = isSegment ? encodePathSegment(segments) : encodePath(segments);

        final boolean pathEndsInSlash = path.length() > 0 && path.charAt(path.length() - 1) == '/';
        final boolean segmentStartsWithSlash = segments.charAt(0) == '/';

        if (path.length() > 0 && !pathEndsInSlash && !segmentStartsWithSlash) {
            path.append('/');
        } else if (pathEndsInSlash && segmentStartsWithSlash) {
            segments = segments.substring(1);
            if (segments.isEmpty()) {
                return;
            }
        }

        path.append(segments);
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
                rc.append(name).append('=').append(value);
            }
        }
        return rc.toString();
    }
    
    private String encodeQueryParam(String s) {
    	if (s == null) return null;
    	return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    
    private String encodePathSegment(String s) {
    	if (s == null) return null;
    	return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    
    private String encodePath(String s) {
    	if (s == null) return null;
    	StringBuilder rc = new StringBuilder();
    	for (String part : s.split("/")) {
    		rc.append("/");
    		if (!part.equals("")) rc.append(encodePathSegment(part));
    	}
 
    	return rc.toString();
    }
    
    private String encodeFragment(String s) {
    	if (s == null) return null;
    	return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    
    private MultiValuedMap<String, String> parseQuery(String query) {
    	if (RestClientUtils.isEmpty(query)) return null;
    	MultiValuedMap<String, String> rc = new ArrayListValuedHashMap<>();
    	for (String p : query.split("&")) {
    		SimpleImmutableEntry<String, String> param = parseQueryParam(p);
    		rc.put(param.getKey(), param.getValue());
    	}
        return rc;
    }

    private SimpleImmutableEntry<String, String> parseQueryParam(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new SimpleImmutableEntry<>(
            URLDecoder.decode(key, StandardCharsets.UTF_8),
            URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }
    
    public static RestUriBuilder fromUri(String uri) {
    	RestUriBuilder rc = new RestUriBuilder();
    	return rc.uri(URI.create(uri));
    }
    
    public static RestUriBuilder fromUri(URI uri) {
    	RestUriBuilder rc = new RestUriBuilder();
    	return rc.uri(uri);
    }
}
