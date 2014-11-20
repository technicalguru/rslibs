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
 * Uses salted Blowfish hashes from ???.
 * @author ralph
 *
 */
public class BlowfishPasswordHasher implements ExtendedPasswordHasher {

	/** A static instance of the Blowfish hasher */
	public static final ExtendedPasswordHasher INSTANCE = new BlowfishPasswordHasher();
	
	/**
	 * Constructor.
	 */
	public BlowfishPasswordHasher() {
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
			LoggerFactory.getLogger(getClass()).error("Blowfish hashing currently not supported");
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHash(String passwordHash) {
		return (passwordHash != null) && passwordHash.startsWith("$2a$");
	}

	
}
