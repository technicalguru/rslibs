package rs.restclient.core.api.auth;

import rs.restclient.core.api.RestClient;
import rs.restclient.core.api.request.RestRequest;

/**
 * Abstract implementation of {@link AuthorizationStrategy}. Derive from
 * here and implement various methods:
 * <ul>
 * <li>{@link #isAuthorized(RestRequest)}</li>
 * <li>{@link #authorize(RestRequest)}</li>
 * <li>{@link #needsRenewal(RestRequest)}</li>
 * <li>{@link #canRenew(RestRequest)}</li>
 * <li>{@link #renewAuthorization(RestRequest)}</li>
 * </ul>
 * {@link #checkAuthorization(RestRequest)} has a default implementation.
 * @author ralph
 *
 */
public abstract class AbstractAuthorizationStrategy implements AuthorizationStrategy {

	private RestClient client;
	
	protected AbstractAuthorizationStrategy(RestClient client) {
		this.client = client;
	}
	
	/**
	 * Returns the client.
	 * <p>You can use this client to start your authorization calls.
	 * @return the client
	 */
	public RestClient getClient() {
		return client;
	}

	/**
	 * This implementation checks {@link #isAuthorized(RestRequest)} to see whether
	 * an authorization is already given or not. If not, the it will
	 * ask {@link #canRenew(RestRequest)} to check whether a renewal would resolve
	 * it and finally {@link #renewAuthorization(RestRequest)} to fix it.
	 * If renewal is not possible, {@link #authorize(RestRequest)} will be called.
	 * <p>In case the authorization is already present, {@link #needsRenewal(RestRequest)}
	 * checks whether renewal shall be executed. {@link #canRenew(RestRequest)} will test
	 * whether renewal is still possible and finally {@link #renewAuthorization(RestRequest)}
	 * will be executed.
	 * @see #isAuthorized(RestRequest)
	 * @see #authorize(RestRequest)
	 * @see #needsRenewal(RestRequest)
	 * @see #canRenew(RestRequest)
	 * @see #renewAuthorization(RestRequest)
	 */
	@Override
	public void checkAuthorization(RestRequest request) throws AuthorizationFailedException {
		if (!isAuthorized(request)) {
			if (canRenew(request)) {
				renewAuthorization(request);
				return;
			}
			authorize(request);
		} else {
			if (needsRenewal(request)) {
				if (canRenew(request)) {
					renewAuthorization(request);
				}
			}
		}
	}

	/**
	 * Checks whether authorization is already present.
	 * <p>Default implementation returns true.
	 * @param request the request to be authorized.
	 * @return true when authorization is present
	 */
	protected boolean isAuthorized(RestRequest request) {
		return true;
	}
	
	/**
	 * Checks whether a renewal shall be made (e.g. 30min before expiry).
	 * <p>Default implementation returns false.
	 * @param request the request to be authorized.
	 * @return true when renewal shall be attempted
	 */
	protected boolean needsRenewal(RestRequest request) {
		return false;
	}
	
	/**
	 * Checks whether a renewal can be made (e.g. the renewal token is still valid).
	 * <p>Default implementation returns false.
	 * @param request the request to be authorized.
	 * @return true when renewal can be made
	 */
	protected boolean canRenew(RestRequest request) {
		return false;
	}
	
	/**
	 * Authorize the request (e.g. make a login call).
	 * <p>This method does nothing.
	 * @param request request to be made
	 * @throws AuthorizationFailedException when authorization fails
	 */
	protected void authorize(RestRequest request) throws AuthorizationFailedException {
	}
	
	/**
	 * Renew the authorization (e.g. make a renewal call).
	 * <p>This method does nothing.
	 * @param request request to be made
	 * @throws AuthorizationFailedException when renewal fails
	 */
	protected void renewAuthorization(RestRequest request) throws AuthorizationFailedException {
		authorize(request);
	}
}
