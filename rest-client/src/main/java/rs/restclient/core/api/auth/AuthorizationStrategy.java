package rs.restclient.core.api.auth;

import rs.restclient.core.api.request.RestRequest;

/**
 * An authorization strategy (if defined for your client) will be called each time
 * a request is about to be made. It's purpose is to report whether the request
 * is authorized and the call can be made safely.
 * <p>Calls to the strategy will be synchronized so that login, authorization and renewal
 *    requests will be made in order and not overlap.
 * <p>Implementation shall set interceptors or trigger login/renewal requests
 * if required.
 * 
 * @author ralph
 *
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
