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
package rs.baselib.crypto;

/**
 * An interface that is able to hash passwords and check existing hashes against a password.
 * @author ralph
 * @since 1.2.9
 *
 */
public interface PasswordHasher {

	/**
	 * Returns a hash of the given plain password.
	 * @param plainPassword the password to be hashed
	 * @return the hash of the password
	 */
	public String getPasswordHash(String plainPassword);
	
	/**
	 * Tests a plain Password whether it is the same as a previously hashed password.
	 * @param plainPassword the plain password to be checked
	 * @param passwordHash the hash of another password
	 * @return <code>true</code> when both passwords match each other
	 */
	public boolean testPassword(String plainPassword, String passwordHash);
}
