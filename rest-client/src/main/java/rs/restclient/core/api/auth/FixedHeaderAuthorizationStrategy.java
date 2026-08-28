package rs.restclient.core.api.auth;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.auth.AuthorizationInterceptor.AuthorizationType;
import rs.restclient.core.util.AddHeaderInterceptor;

/**
 * A strategy that is based on a fixed, well-known HTTP request header, e.g. an API key.
 * 
 * @author ralph
 *
 */
public class FixedHeaderAuthorizationStrategy extends AbstractAuthorizationStrategy {

	/**
	 * Adds the given authorization header to all requests. 
	 * @param client the client
	 * @param header the header to set
	 * @param value the value of the header
	 */
	public FixedHeaderAuthorizationStrategy(RestClient client, AuthorizationType headerType, String value) {
		this(client, headerType.getHeaderName(), value);
	}
	
	/**
	 * Adds the given authorization header to all requests. 
	 * @param client the client
	 * @param header the header to set
	 * @param value the value of the header
	 */
	public FixedHeaderAuthorizationStrategy(RestClient client, String header, String value) {
		super(client);
		client.getTarget().register(new AddHeaderInterceptor(header, value));
	}

}
