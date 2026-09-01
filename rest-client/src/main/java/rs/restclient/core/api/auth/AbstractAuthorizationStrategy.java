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
 * @see RestClient
 */
public abstract class AbstractAuthorizationStrategy implements AuthorizationStrategy {

	private volatile boolean inProgress = false;
	
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
		if (!inProgress) {
			try {
				if (!isAuthorized(request)) {
					this.inProgress = true;
					if (canRenew(request)) {
						renewAuthorization(request);
						return;
					}
					authorize(request);
				} else {
					if (needsRenewal(request)) {
						if (canRenew(request)) {
							this.inProgress = true;
							renewAuthorization(request);
						}
					}
				}
				applyAuthorization(request);
			} finally {
				this.inProgress = false;
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

	/**
	 * Returns whether an authorization call is in progress.
	 * <p>This is intended for status information to other objects. The abstract implementation
	 *    will consider authorization checking when authorization is alrealy in progress.
	 * @return the inProgress
	 */
	public boolean isInProgress() {
		return inProgress;
	}
	
	/**
	 * Apply the authorization to the request.
	 * <p>The method is called when {@link #isAuthorized(RestRequest)}, {@link #authorize(RestRequest)}
	 *    or {@link #renewAuthorization(RestRequest)} was successfull. Override here to
	 *    manipulate the request and add your authorization information.
	 * <p>The default implementation does nothing.
	 * @param request the request to manipulate
	 */
	public void applyAuthorization(RestRequest request) {
		
	}
}
