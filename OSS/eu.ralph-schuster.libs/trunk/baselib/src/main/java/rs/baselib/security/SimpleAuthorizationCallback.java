/**
 * 
 */
package rs.baselib.security;

/**
 * A simple authorization callback to be used within programming.
 * @author ralph
 *
 */
public class SimpleAuthorizationCallback extends AbstractAuthorizationCallback {

	/**
	 * Default Constructor.
	 */
	public SimpleAuthorizationCallback() {
	}

	/**
	 * Constructor.
	 */
	public SimpleAuthorizationCallback(String name, String password) {
		setName(name);
		setPassword(password);
	}

}
