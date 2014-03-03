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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.Test;

/**
 * Tests {@link EncryptionUtils}.
 * @author ralph
 *
 */
public class EncryptionUtilsTest {

	/**
	 * Test method for {@link EncryptionUtils#generateSalt(long)}.
	 */
	@Test
	public void testGenerateSaltLong() {
		byte b[] = EncryptionUtils.generateSalt(System.currentTimeMillis());
		assertNotNull("No salt generated", b);
		assertEquals("Salt length problem", 8, b.length);
	}

	/**
	 * Test method for {@link EncryptionUtils#generatePassword(java.lang.String, long, int)}.
	 */
	@Test
	public void testGeneratePasswordStringLongInt() {
		String s = EncryptionUtils.generatePassword(EncryptionUtils.PASSWORD_CHARS, System.currentTimeMillis(), 20);
		assertNotNull("No password generated", s);
		assertEquals("Password length problem", 20, s.length());
		for (char c : s.toCharArray()) {
			assertTrue("Invalid character in password: "+c, EncryptionUtils.PASSWORD_CHARS.indexOf(c) >= 0);
		}
	}

	/**
	 * Test method for {@link EncryptionUtils#generateKey(java.lang.String)}.
	 */
	@Test
	public void testGenerateKeyString() {
		try {
			String seed = EncryptionUtils.generatePassword(8);
			assertNotNull("No seed", seed);

			KeyPair keyPair = EncryptionUtils.generateKey(seed.getBytes());
			assertNotNull(keyPair);
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey   = keyPair.getPublic();
			assertNotNull("No private key", privateKey);
			assertNotNull("No public Key", publicKey);
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}

	/**
	 * Test method for {@link EncryptionUtils#encodeBase64(java.lang.String)}.
	 */
	@Test
	public void testEncodeBase64() {
		String seed = EncryptionUtils.generatePassword(8);
		assertNotNull("No seed", seed);
		byte b[] = seed.getBytes();
		assertArrayEquals(b, EncryptionUtils.decodeBase64(EncryptionUtils.encodeBase64(b)));
	}

	/**
	 * Test method for {@link EncryptionUtils#encodeBase64(java.security.PublicKey)}.
	 * Test method for {@link EncryptionUtils#encodeBase64(java.security.PrivateKey)}.
	 */
	@Test
	public void testEncodeBase64Key() {
		try {
			// Create the key first
			String seed = EncryptionUtils.generatePassword(8);
			assertNotNull("No seed", seed);
			KeyPair keyPair = EncryptionUtils.generateKey(seed.getBytes());
			assertNotNull(keyPair);
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey   = keyPair.getPublic();
			assertNotNull("No private key", privateKey);
			assertNotNull("No public Key", publicKey);
			
			assertEquals(privateKey, EncryptionUtils.decodeBase64PrivateKey(EncryptionUtils.encodeBase64(privateKey)));
			assertEquals(publicKey, EncryptionUtils.decodeBase64PublicKey(EncryptionUtils.encodeBase64(publicKey)));
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.getMessage());
		}
	}

}
