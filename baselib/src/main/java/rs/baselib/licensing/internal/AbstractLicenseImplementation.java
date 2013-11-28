/**
 * 
 */
package rs.baselib.licensing.internal;

import java.security.Key;

/**
 * General implementation for internal licensing objects.
 * @author ralph
 *
 */
public class AbstractLicenseImplementation {

	private Key key;
	
	/**
	 * Constructor.
	 */
	public AbstractLicenseImplementation() {
	}

	/**
	 * Returns the {@link #key}.
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Sets the license key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

}
