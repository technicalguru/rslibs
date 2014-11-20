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
 * Extends {@link PasswordHasher} by a method to detect responsibility.
 * @author ralph
 *
 */
public interface ExtendedPasswordHasher extends PasswordHasher {

	/**
	 * Returns whether the given hash string was produced using the implemented hashing algorithm.
	 * @param passwordHash the hash to be checked
	 * @return <code>true</code> when this hash was produced by this password hasher (or using the same algorithm).
	 */
	public boolean isHash(String passwordHash);
}
