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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link DummyPasswordHasher}.
 * @author ralph
 *
 */
public class DummyPasswordHasherTest {

	public static final String PASSWORD = "aPassword";
	
	/**
	 * Constructor.
	 */
	@Test
	public void testHash() {
		String hash = DummyPasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertNotNull(hash);
		assertEquals(PASSWORD, hash);
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testIsHash() {
		String hash = DummyPasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertTrue(DummyPasswordHasher.INSTANCE.isHash(hash));
	}

	/**
	 * Constructor.
	 */
	@Test
	public void testTestPassword() {
		String hash = DummyPasswordHasher.INSTANCE.getPasswordHash(PASSWORD);
		assertTrue(DummyPasswordHasher.INSTANCE.testPassword(PASSWORD, hash));
	}

}
