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

import java.io.Console;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import rs.baselib.configuration.IConfigurable;

/**
 * Implements authorization information retrieval from command line.
 * This implementation prompts the user for name (if no default name
 * was given) and for the password.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 * &lt;AuthorizationCallback class="rs.baselib.security.CommandLineAuthorizationCallback"&gt;
 * 
 *    &lt;!-- The default user name (optional). --&gt;
 *    &lt;login&gt;default-user-name&lt;/login&gt;
 *    
 *    &lt;!-- The text to be used for user name prompt (optional). --&gt;
 *    &lt;login-prompt&gt;login-prompt-text&lt;/login-prompt&gt;
 *    
 *    &lt;!-- The text to be used for password prompt (optional). --&gt;
 *    &lt;password-prompt&gt;password-prompt-text&lt;/password-prompt&gt;
 *    
 * &lt;/AuthorizationCallback&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class CommandLineAuthorizationCallback extends AbstractAuthorizationCallback implements IConfigurable {

	/** The default prompt for the login name ("Login: "). */
	public static final String DEFAULT_LOGIN_PROMPT = "Login: "; 
	/** The default prompt for the password ("Password: "). */
	public static final String DEFAULT_PASSWORD_PROMPT = "Password: "; 

	private String loginPrompt;
	private String passwordPrompt;
	
	/**
	 * Default Constructor.
	 */
	public CommandLineAuthorizationCallback() {
	}

	/**
	 * Configures the name callback.
	 * Configuration can contain three elements:
	 * <ul>
	 * <li>&lt;login&gt; - the default user name</li>
	 * <li>&lt;login-prompt&gt; - the prompt text for login name</li>
	 * <li>&lt;password-prompt&gt; - the prompt text for password</li>
	 * </ul>
	 * Please note, that a login-prompt definition is useless when
	 * you configured a default login name.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		setName(config.getString("login"));
		setLoginPrompt(config.getString("login-prompt"));
		setPasswordPrompt(config.getString("password-prompt"));
	}

	/**
	 * Returns the login prompt text.
	 * @return the login prompt text.
	 */
	public String getLoginPrompt() {
		return loginPrompt;
	}

	/**
	 * Sets a text for the login prompt. 
	 * @param loginPrompt - the login prompt text to set
	 */
	public void setLoginPrompt(String loginPrompt) {
		if (loginPrompt == null) loginPrompt = DEFAULT_LOGIN_PROMPT;
		this.loginPrompt = loginPrompt;
	}

	/**
	 * Returns the password prompt text.
	 * @return the password prompt text
	 */
	public String getPasswordPrompt() {
		return passwordPrompt;
	}

	/**
	 * Sets a text for the password prompt.
	 * @param passwordPrompt - the password prompt text to set
	 */
	public void setPasswordPrompt(String passwordPrompt) {
		if (passwordPrompt == null) passwordPrompt = DEFAULT_PASSWORD_PROMPT;
		this.passwordPrompt = passwordPrompt;
	}

	/**
	 * Asks the user on command line for name if no default name was set.
	 * @return default name or input from user.
	 */
	@Override
	public String getName() {
		String s = super.getName();
		if (s != null) return s;
		
		// Ask for name
		System.out.print(getLoginPrompt());
		System.out.flush();
		Console con = System.console();
		if (con != null) {
			s  = con.readLine();
			super.setName(s);
			return s;
		}
		throw new IllegalStateException("No console available");
	}

	/**
	 * Asks the user on command line for a password.
	 * @return password from user
	 */
	@Override
	public String getPassword() {
		String s = super.getPassword();
		if (s != null) return s;

		// Ask for password
		System.out.print(getPasswordPrompt());
		System.out.flush();
		Console con = System.console();
		if (con != null) {
			s = new String(con.readPassword());
			super.setPassword(s);
			return s;
		}
		throw new IllegalStateException("No console available");
	}

	
}
