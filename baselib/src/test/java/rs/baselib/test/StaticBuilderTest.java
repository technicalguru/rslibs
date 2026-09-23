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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.baselib.test.BuilderUtils.$Person;
import static rs.baselib.test.BuilderUtils.fixed;

import org.junit.jupiter.api.Test;

import rs.baselib.test.PersonBuilder.Person;

/**
 * Test the static builder.
 * @author ralph
 *
 */
public class StaticBuilderTest {

	@Test
	public void testStaticBuilder_Long() {
		Builder<Long> b = fixed(3L);
		for (int i=0; i<10; i++) assertEquals(3L, b.build());
	}

	@Test
	public void testStaticBuilder_Int() {
		Builder<Integer> b = fixed(3);
		for (int i=0; i<10; i++) assertEquals(3, b.build());
	}

	@Test
	public void testStaticBuilder_Boolean() {
		Builder<Boolean> b = fixed(true);
		for (int i=0; i<10; i++) assertTrue(b.build());
	}

	@Test
	public void testStaticBuilder_String() {
		Builder<String> b = fixed("Hello");
		for (int i=0; i<10; i++) assertEquals("Hello", b.build());
	}

	@Test
	public void testStaticBuilder_Object() {
		Person person = $Person().build();
		Builder<Person> b = fixed(person);
		for (int i=0; i<10; i++) assertEquals(person, b.build());
	}
	
}
