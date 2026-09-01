package rs.restclient.core.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.restclient.core.api.RestClientException;
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

	private static Logger log = LoggerFactory.getLogger(FixedHeaderAuthorizationStrategy.class);
	
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
			if ((header != null) && (value != null)) {
				interceptor = new AddHeaderInterceptor(header, value);
				request.register(interceptor);
			} else {
				log.info("checkAuthorization() was called without prior configuration.");
			}
		}
	}

	/**
	 * Configures the strategy.
	 * @param headerType type of header
	 * @param value value of header
	 */
	public void setAuthorization(AuthorizationType headerType, String value) {
		
	}

	/**
	 * Configures the strategy.
	 * @param header the header to set
	 * @param value the value of the header
	 */
	public void setAuthorization(String header, String value) {
		
	}
	
	/**
	 * Sets the header.
	 * @param header the header to set
	 */
	public void setHeader(String header) {
		if (interceptor != null) throw new RestClientException("AuthorizationStrategy already configured");
		this.header = header;
	}

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(String value) {
		if (interceptor != null) throw new RestClientException("AuthorizationStrategy already configured");
		this.value = value;
	}

	
}
