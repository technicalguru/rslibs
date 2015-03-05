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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests the {@link Md5PasswordHasher}.
 * @author ralph
 *
 */
public class Md5PasswordHasherTest {

	public static final String PASSWORD = "aPassword";
	
	/**
	 * Constructor.
	 */
	@Test
	public void testHash() {
		String hash = Md5PasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertNotNull("No hash created", hash);
		assertNotEquals("Hash returned the plain password", PASSWORD, hash);
		assertTrue("Not a MD5 hash", hash.startsWith(Md5PasswordHasher.PREFIX));
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testIsHash() {
		String hash = Md5PasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertTrue("MD5 hasher does not recognize its own hash", Md5PasswordHasher.INSTANCE.isHash(hash));
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testTestPassword() {
		String hash = Md5PasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertTrue("MD5 hasher does not test his own hash", Md5PasswordHasher.INSTANCE.testPassword(PASSWORD, hash));
	}

}
