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

import org.junit.Test;

import rs.baselib.test.PersonBuilder.Person;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
			assertNotNull("No person generated", actual);
			assertNotNull("First name was empty", actual.firstName);
			assertNotNull("Last name was empty", actual.lastName);
			assertNotNull("Gender was empty", actual.gender);
			assertNotNull("Birthday was empty", actual.birthday);
			assertTrue("Min age was violated", actual.age >= 18);
			assertTrue("Max age was violated", actual.age < 70);
		}
	}

	@Test
	public void testEmptyGenerations() {
		PersonBuilder b = $Person().withoutFirstNames().withoutLastNames().withoutGenders();
		for (int i=0; i<10000; i++) {
			Person actual = b.build();
			assertNotNull("No person generated", actual);
			assertNull("First name was not empty", actual.firstName);
			assertNull("Last name was not empty", actual.lastName);
			assertNull("Gender was not empty", actual.gender);
		}
	}
}
