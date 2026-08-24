/**
 * 
 */
package rs.restclient.util;

import java.net.URI;

import rs.restclient.RsRestClient;
import rs.restclient.ClientConfig;

/**
 * Helper class to ease migration.
 */
public class WebTarget {

	private final ClientConfig config;
	private final RestUriBuilder     targetUri;
	
	protected WebTarget(String uri, RsRestClient parent) {
		this(RestUriBuilder.fromUri(uri), parent.getConfig());
	}
	
	protected WebTarget(URI uri, RsRestClient parent) {
        this(RestUriBuilder.fromUri(uri), parent.getConfig());
    }
	
	protected WebTarget(RestUriBuilder uriBuilder, RsRestClient parent) {
		this(uriBuilder.clone(), parent.getConfig());
	}
	
	protected WebTarget(RestUriBuilder uriBuilder, WebTarget that) {
        this(uriBuilder, that.config);
    }
	
	protected WebTarget(String uri, ClientConfig config) {
		this(RestUriBuilder.fromUri(uri), config);
	}
	
	protected WebTarget(RestUriBuilder uriBuilder, ClientConfig config) {
        this.targetUri = uriBuilder.clone();
        this.config    = config;
    }

    public URI getUri() {
        //checkNotClosed();
        try {
            return targetUri.build();
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }
	
    public RestUriBuilder getUriBuilder() {
        //checkNotClosed();
        return targetUri.clone();
    }

    public WebTarget path(String path) throws NullPointerException {
        //checkNotClosed();
        if (path == null) throw new NullPointerException("path is 'null'.");
        return new WebTarget(getUriBuilder().path(path), this);
    }
    
    public WebTarget queryParam(String name, Object... values) throws NullPointerException {
        //checkNotClosed();
        return new WebTarget(WebTarget.setQueryParam(getUriBuilder(), name, values), this);
    }

    private static RestUriBuilder setQueryParam(RestUriBuilder uriBuilder, String name, Object[] values) {
        if (values == null || values.length == 0 || (values.length == 1 && values[0] == null)) {
            return uriBuilder.replaceQueryParam(name, (Object[]) null);
        }

        for (Object value : values) {
        	if (value == null) throw new NullPointerException("Query parameter values must not be 'null'.");
        }
        return uriBuilder.queryParam(name, values);
    }
    
    /** Always JSON */
	public RequestBuilder request() {
		return new RequestBuilder(this, config);
	}
	
	
}
