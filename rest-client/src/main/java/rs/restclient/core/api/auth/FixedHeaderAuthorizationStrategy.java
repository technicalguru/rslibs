package rs.restclient.core.api.auth;

import rs.restclient.core.api.auth.AuthorizationInterceptor.AuthorizationType;
import rs.restclient.core.api.request.RestRequest;
import rs.restclient.core.util.AddHeaderInterceptor;

/**
 * A strategy that is based on a fixed, well-known HTTP request header, e.g. an API key.
 * 
 * @author ralph
 *
 */
public class FixedHeaderAuthorizationStrategy extends AbstractAuthorizationStrategy {

	private AddHeaderInterceptor interceptor;
	private String               header;
	private String               value;
	
	/**
	 * Adds the given authorization header to all requests. 
	 * @param client the client
	 * @param header the header to set
	 * @param value the value of the header
	 */
	public FixedHeaderAuthorizationStrategy(AuthorizationType headerType, String value) {
		this(headerType.getHeaderName(), value);
	}
	
	/**
	 * Adds the given authorization header to all requests. 
	 * @param client the client
	 * @param header the header to set
	 * @param value the value of the header
	 */
	public FixedHeaderAuthorizationStrategy(String header, String value) {
		this.header = header;
		this.value  = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkAuthorization(RestRequest request) throws AuthorizationFailedException {
		if (value == null) throw new AuthorizationFailedException("No authorization value available");
		if (interceptor == null) {
			interceptor = new AddHeaderInterceptor(header, value);
			request.register(interceptor);
		}
	}

	/**
	 * Sets the header.
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		this.header = header;
	}

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
}
