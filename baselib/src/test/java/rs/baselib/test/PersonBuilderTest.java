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

import org.junit.jupiter.api.Test;

import rs.baselib.test.PersonBuilder.Person;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static rs.baselib.test.BuilderUtils.$Person;

/**
 * Test the {@link PersonBuilder}.
 * 
 * @author ralph
 *
 */
public class PersonBuilderTest {

	@Test
	public void testDefault() {
		PersonBuilder b = $Person();
		for (int i=0; i<10000; i++) {
			Person actual = b.build();
			assertNotNull(actual);
			assertNotNull(actual.firstName);
			assertNotNull(actual.lastName);
			assertNotNull(actual.gender);
			assertNotNull(actual.birthday);
			assertTrue(actual.age >= 18);
			assertTrue(actual.age < 70);
		}
	}

	@Test
	public void testEmptyGenerations() {
		PersonBuilder b = $Person().withoutFirstNames().withoutLastNames().withoutGenders();
		for (int i=0; i<10000; i++) {
			Person actual = b.build();
			assertNotNull(actual);
			assertNull(actual.firstName);
			assertNull(actual.lastName);
			assertNull(actual.gender);
		}
	}
}
