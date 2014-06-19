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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link PhpUnserializer}.
 * @author ralph
 *
 */
public class PhpUnserializerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testString() {
		String s = (String)PhpUnserializer.unserialize("s:13:\"a test object\"");
		assertEquals("Not correct string serialization", "a test object", s);
	}

	@Test
	public void testCharacter() {
		String s = (String)PhpUnserializer.unserialize("s:1:\"c\"");
		assertEquals("Not correct char serialization", "c", s);
	}

	@Test
	public void testNull() {
		Object o = PhpUnserializer.unserialize("N");
		assertNull("Not correct NULL serialization", o);
	}

	@Test
	public void testInteger() {
		int i = (Integer)PhpUnserializer.unserialize("i:15");
		assertEquals("Not correct int serialization", 15, i);
	}

	@Test
	public void testLong() {
		Number n = (Number)PhpUnserializer.unserialize("i:15");
		assertEquals("Not correct long serialization", 15, n);
		n = (Number)PhpUnserializer.unserialize("d:9223372036854775807");
		assertEquals("Not correct long serialization", Long.MAX_VALUE, n);
	}

	@Test
	public void testDouble() {
		double d = (Double)PhpUnserializer.unserialize("d:15.0");
		assertEquals("Not correct double serialization", 15d, d, 0.00001);
	}

	@Test
	public void testBoolean() {
		boolean b = (Boolean)PhpUnserializer.unserialize("b:1");
		assertEquals("Not correct boolean serialization", true, b);
		b = (Boolean)PhpUnserializer.unserialize("b:0");
		assertEquals("Not correct boolean serialization", false, b);
	}
	
	@Test
	public void testArray() {
		Object a[] = (Object[])PhpUnserializer.unserialize("a:5:{i:0;i:15;i:1;i:15;i:2;d:15.0;i:3;b:1;i:4;s:11:\"test string\"}");
		assertArrayEquals("Not correct array serialization", new Object[] { 15, 15, 15d, true, "test string" }, a);
	}
	
	@Test
	public void testMap() {
		Map<?, ?> map = (Map<?, ?>)PhpUnserializer.unserialize("a:3:{d:15.0;b:1;s:3:\"key\";s:11:\"test string\";i:15;i:15}");
		assertEquals("Not correct map serialization", 3, map.size());
		assertNotNull("Not correct map serialization", map.get(15));
		assertEquals("Not correct map serialization", 15, map.get(15));
		assertNotNull("Not correct map serialization", map.get(15d));
		assertEquals("Not correct map serialization", Boolean.TRUE, map.get(15d));
		assertNotNull("Not correct map serialization", map.get("key"));
		assertEquals("Not correct map serialization", "test string", map.get("key"));
	}
	


}
