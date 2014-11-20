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

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.Sha2Crypt;

/**
 * Uses salted SHA256 hashes from {@link Sha2Crypt}.
 * @author ralph
 * @see Sha2Crypt
 * @since 1.2.9
 */
public class Sha256PasswordHasher implements ExtendedPasswordHasher {

	/** The prefix that is present for hashes of this algorithm */
	public static final String PREFIX = "$5$";

	/** A static instance of the SHA512 hasher */
	public static final ExtendedPasswordHasher INSTANCE = new Sha256PasswordHasher();

	/**
	 * Constructor.
	 */
	public Sha256PasswordHasher() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPasswordHash(String plainPassword) {
		return Sha2Crypt.sha256Crypt(plainPassword.getBytes());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean testPassword(String plainPassword, String passwordHash) {
		if ((plainPassword != null) && isHash(passwordHash)) {
			String otherHash = Sha2Crypt.sha256Crypt(StringUtils.getBytesUtf8(plainPassword), passwordHash);
			return otherHash.equals(passwordHash);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHash(String passwordHash) {
		return (passwordHash != null) && passwordHash.startsWith(PREFIX);
	}

}
