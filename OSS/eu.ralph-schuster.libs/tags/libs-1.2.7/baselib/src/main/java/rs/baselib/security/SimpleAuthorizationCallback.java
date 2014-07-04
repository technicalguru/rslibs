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
