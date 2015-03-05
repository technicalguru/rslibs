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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests the {@link CombinedPasswordHasher}.
 * @author ralph
 *
 */
public class CombinedPasswordHasherTest {

	public static final String PASSWORD = "aPassword";
	
	/**
	 * Constructor.
	 */
	@Test
	public void testMd5Hash() {
		String hash = CombinedPasswordHasher.UNIX_STRATEGY_MD5.getPasswordHash(PASSWORD);
		assertNotNull("No hash created", hash);
		assertNotEquals("Hash returned the plain password", PASSWORD, hash);
		assertTrue("Not a MD5 hash", hash.startsWith(Md5PasswordHasher.PREFIX));
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testSha512Hash() {
		String hash = CombinedPasswordHasher.UNIX_STRATEGY_SHA512.getPasswordHash(PASSWORD);
		assertNotNull("No hash created", hash);
		assertNotEquals("Hash returned the plain password", PASSWORD, hash);
		assertTrue("Not a MD5 hash", hash.startsWith(Sha512PasswordHasher.PREFIX));
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testSha256Hash() {
		String hash = CombinedPasswordHasher.UNIX_STRATEGY_SHA256.getPasswordHash(PASSWORD);
		assertNotNull("No hash created", hash);
		assertNotEquals("Hash returned the plain password", PASSWORD, hash);
		assertTrue("Not a MD5 hash", hash.startsWith(Sha256PasswordHasher.PREFIX));
	}


	/**
	 * Constructor.
	 */
	@Test
	public void testTestPassword() {
		String hash = CombinedPasswordHasher.UNIX_STRATEGY_MD5.getPasswordHash(PASSWORD);
		assertTrue("Combined hasher does not test his own hash", CombinedPasswordHasher.UNIX_STRATEGY_MD5.testPassword(PASSWORD, hash));
	}

}
