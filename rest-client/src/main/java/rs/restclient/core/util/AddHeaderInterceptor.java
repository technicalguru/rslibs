package rs.restclient.core.util;

import java.io.IOException;

import rs.restclient.core.api.request.RestRequest;

/**
 * Implements simple header addition..
 * @author ralph
 *
 */
public class AddHeaderInterceptor extends AbstractRequestInterceptor {

	private String headerName;
	private String value;

	/**
	 * Creates interceptor with header name and value.
	 * @param headerName name of header
	 * @param value value of header
	 */
	public AddHeaderInterceptor(String headerName, String value) {
		this.headerName = headerName;
		this.value      = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void intercept(RestRequest request) throws IOException {
		request.addHeader(headerName, value);
	}
	
	
}
