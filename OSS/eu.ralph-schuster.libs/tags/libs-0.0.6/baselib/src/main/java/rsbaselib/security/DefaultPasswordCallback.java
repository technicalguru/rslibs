/**
 * 
 */
package rsbaselib.security;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.ArrayUtils;

import rsbaselib.configuration.Configurable;

/**
 * Returns the password given in configuration.
 * @author ralph
 *
 */
public class DefaultPasswordCallback implements PasswordCallback, Configurable {

	private char[] password;
	
	/**
	 * Constructor.
	 */
	public DefaultPasswordCallback() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		if (cfg != null) {
			String s = cfg.getString("password(0)");
			if (s != null) password = s.toCharArray();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeConfiguration() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterConfiguration() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char[] getPassword() {
		return password;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getSalt() {
		try {
			byte b[] = new String(getPassword()).getBytes("UTF-8");
			while (b.length < 8) b = ArrayUtils.addAll(b, b);
			if (b.length > 8) b = ArrayUtils.subarray(b, 0, 8);
			return b;
		} catch (Exception e) {
			throw new RuntimeException("Cannot create salt from passphrase", e);
		}
	}

	
}
