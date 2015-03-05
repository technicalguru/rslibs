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

import org.slf4j.LoggerFactory;

/**
 * Uses PHP-alike hashes.
 * @author ralph
 * @since 1.2.9
 *
 */
public class PhpPasswordHasher implements ExtendedPasswordHasher {

	/** A static instance of the PHP hasher */
	public static final ExtendedPasswordHasher INSTANCE = new PhpPasswordHasher();
	
	/**
	 * Constructor.
	 */
	public PhpPasswordHasher() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPasswordHash(String plainPassword) {
		return plainPassword;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean testPassword(String plainPassword, String passwordHash) {
		if (isHash(passwordHash)) {
			LoggerFactory.getLogger(getClass()).error("PHPass hashing currently not supported");
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHash(String passwordHash) {
		return (passwordHash != null) && passwordHash.startsWith("$P$");
	}

	
}
