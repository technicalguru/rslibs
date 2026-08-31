package rs.restclient.core.api.auth;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.request.RestRequest;

/**
 * An authorization strategy (if defined for your client) will be called each time
 * a request is about to be made. It's purpose is to report whether the request
 * is authorized and the call can be made safely.
 * <p>Calls to the strategy will be synchronized so that login, authorization and renewal
 *    requests will be made in order and not overlap.
 * <p>Implementation shall set interceptors or trigger login/renewal requests
 * if required.
 * <p>Creation of an authorization strategy can be cumbersome, especially when you
 *    require customized information or references to your {@link RestClient}.
 *    You need to create the strategy within {@link RestClient} but not yet configure
 *    it. Introduce additional methods to configure your strategy, e.g. setApiKey(), setClient()
 *    or alike.
 * 
 * @author ralph
 * @see RestClient
 */
public interface AuthorizationStrategy {

	/**
	 * A NONE strategy. Authorization is not required.
	 */
	static final AuthorizationStrategy NONE = new AuthorizationStrategy() {
		@Override
		public void checkAuthorization(RestRequest request) { }
	};
	
	/**
	 * Checks the authorization status of this request and
	 * throws Exception when authorization attempts fail.
	 * @param request the request that shall be made
	 * @throws AuthorizationFailedException when when it shall be aborted.
	 */
	void checkAuthorization(RestRequest request) throws AuthorizationFailedException;
}
