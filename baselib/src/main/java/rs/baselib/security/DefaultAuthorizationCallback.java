/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.security;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import rs.baselib.configuration.IConfigurable;

/**
 * Implements authorization information retrieval from configuration object.
 * This implementation can be used to directly store authorization information
 * in a global config without loading extra information from other files.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 * &lt;AuthorizationCallback class="rs.baselib.security.DefaultAuthorizationCallback"&gt;
 *    &lt;login&gt;my-user-name&lt;/login&gt;
 *    &lt;password&gt;my-password&lt;/password&gt;
 * &lt;/AuthorizationCallback&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class DefaultAuthorizationCallback extends SimpleAuthorizationCallback implements IConfigurable {

	/**
	 * Default Constructor.
	 */
	public DefaultAuthorizationCallback() {
	}

	/**
	 * Constructor.
	 * @param name name or login name
	 * @param password password for login
	 */
	public DefaultAuthorizationCallback(String name, String password) {
		super(name, password);
	}

	/**
	 * Configures the callback.
	 * Configuration object must must contain two elements &lt;login&gt;
	 * and &lt;password&gt;.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		setName(config.getString("login"));
		setPassword(config.getString("password"));
	}

}
