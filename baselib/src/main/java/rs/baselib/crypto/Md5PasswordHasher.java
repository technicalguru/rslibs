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
import org.apache.commons.codec.digest.Md5Crypt;

/**
 * Uses salted MD5 hashes from {@link Md5Crypt}.
 * @author ralph
 * @see Md5Crypt
 *
 */
public class Md5PasswordHasher implements ExtendedPasswordHasher {

	/** A static instance of the MD5 hasher */
	public static final ExtendedPasswordHasher INSTANCE = new Md5PasswordHasher();

	/**
	 * Constructor.
	 */
	public Md5PasswordHasher() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPasswordHash(String plainPassword) {
		return Md5Crypt.md5Crypt(plainPassword.getBytes());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean testPassword(String plainPassword, String passwordHash) {
		if ((plainPassword != null) && isHash(passwordHash)) {
			String saltString = passwordHash.substring(0,  12);
			String otherHash = Md5Crypt.md5Crypt(StringUtils.getBytesUtf8(plainPassword), saltString);
			return otherHash.equals(passwordHash);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHash(String passwordHash) {
		return (passwordHash != null) && passwordHash.startsWith("$1$");
	}

	
}
