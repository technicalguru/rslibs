/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.security;

/**
 * Implements the basic methods for an authorization callback.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractAuthorizationCallback implements AuthorizationCallback {

	private String name;
	private String password;
	
	/**
	 * Default Constructor.
	 */
	public AbstractAuthorizationCallback() {
	}

	/**
	 * Returns the login name.
	 * The method will eventually invoke further procedures such as asking
	 * the user on command line of GUI for the name.
	 * @return login name.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the password.
	 * The method will eventually invoke further procedures such as asking
	 * the user on command line of GUI for the password.
	 * @return a password.
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the login name.
	 * @param name - the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the password.
	 * @param password - the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


}
