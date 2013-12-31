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
 * Interface that defines a callback for getting user authorizations.
 * @author Ralph Schuster
 *
 */
public interface AuthorizationCallback {
	
	/**
	 * Returns the login name.
	 * The method will eventually invoke further procedures such as asking
	 * the user on command line of GUI for the name.
	 * @return login name.
	 */
	public String getName();
	
	/**
	 * Returns the password.
	 * The method will eventually invoke further procedures such as asking
	 * the user on command line of GUI for the password.
	 * @return a password.
	 */
	public String getPassword();

}
