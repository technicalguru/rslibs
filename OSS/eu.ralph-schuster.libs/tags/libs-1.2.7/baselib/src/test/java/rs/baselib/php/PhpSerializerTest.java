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
package rs.baselib.php;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link PhpSerializer}.
 * @author ralph
 *
 */
public class PhpSerializerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testString() {
		String s = PhpSerializer.serialize("a test object");
		assertEquals("Not correct string serialization", "s:13:\"a test object\"", s);
	}

	@Test
	public void testCharacter() {
		String s = PhpSerializer.serialize('c');
		assertEquals("Not correct char serialization", "s:1:\"c\"", s);
	}

	@Test
	public void testNull() {
		String s = PhpSerializer.serialize(null);
		assertEquals("Not correct NULL serialization", "N", s);
	}

	@Test
	public void testInteger() {
		String s = PhpSerializer.serialize(15);
		assertEquals("Not correct int serialization", "i:15", s);
	}

	@Test
	public void testLong() {
		String s = PhpSerializer.serialize(15L);
		assertEquals("Not correct long serialization", "i:15", s);
		s = PhpSerializer.serialize(Long.MAX_VALUE);
		assertEquals("Not correct long serialization", "d:9223372036854775807", s);
	}

	@Test
	public void testDouble() {
		String s = PhpSerializer.serialize(15d);
		assertEquals("Not correct double serialization", "d:15.0", s);
	}

	@Test
	public void testBoolean() {
		String s = PhpSerializer.serialize(true);
		assertEquals("Not correct boolean serialization", "b:1", s);
		s = PhpSerializer.serialize(false);
		assertEquals("Not correct boolean serialization", "b:0", s);
	}
	
	@Test
	public void testArray() {
		String s = PhpSerializer.serialize(new Object[] { 15, 15L, 15d, true, "test string" });
		assertEquals("Not correct array serialization", "a:5:{i:0;i:15;i:1;i:15;i:2;d:15.0;i:3;b:1;i:4;s:11:\"test string\"}", s);
	}
	
	@Test
	public void testMap() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put(15,  15L);
		map.put(15d, true);
		map.put("key", "test string");
		String s = PhpSerializer.serialize(map);
		assertEquals("Not correct map serialization", "a:3:{d:15.0;b:1;s:3:\"key\";s:11:\"test string\";i:15;i:15}", s);
	}
	


}
