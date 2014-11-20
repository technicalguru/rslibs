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
 * A dummy implementation that actually does not hash any password but uses the plain password.
 * <p>This implementation shall be used for fallback testing when no other hash algorithm fits. You should never
 * use this class to actually hash passwords.</p>
 * @author ralph
 * @since 1.2.9
 *
 */
public class DummyPasswordHasher implements ExtendedPasswordHasher {

	/** A static instance of the dummy  hasher */
	public static final ExtendedPasswordHasher INSTANCE = new DummyPasswordHasher();
	
	/**
	 * Constructor.
	 */
	public DummyPasswordHasher() {
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
		if ((plainPassword != null) && isHash(passwordHash)) {
			return passwordHash.equals(plainPassword);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHash(String passwordHash) {
		return passwordHash != null;
	}

}
