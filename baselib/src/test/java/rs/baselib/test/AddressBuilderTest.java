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
package rs.baselib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static rs.baselib.test.BuilderUtils.$Address;

import org.junit.jupiter.api.Test;

import rs.baselib.type.Address;

/**
 * Tests the {@link AddressBuilder}.
 * 
 * @author ralph
 *
 */
public class AddressBuilderTest {

	@Test
	public void testDefault() {
		AddressBuilder b = $Address();
		for (int i=0; i<1000; i++) {
			Address address = b.build();
			assertNotNull(address);
			assertNotNull(address.getStreet1());
			assertNull(address.getStreet2());
			assertNotNull(address.getCity());
			assertNotNull(address.getCountry());
			assertEquals(b.getLastCity().getName(), address.getCity());
			assertEquals(b.getLastCity().getCountry(), address.getCountry());
		}
	}
}
