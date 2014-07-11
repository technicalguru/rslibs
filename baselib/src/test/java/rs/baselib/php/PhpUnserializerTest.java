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
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 * Tests the {@link PhpUnserializer}.
 * @author ralph
 *
 */
public class PhpUnserializerTest {

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
	
	@Test
	public void testArrayOfArrays() {
		//                                                                                                                        1                                                                                                              2                                                                             
		//                     1          2          3           4         5          6          7          8          9          0          1          2          3          4           5          6          7          8         9           0          1          2         3           4          5          6          7
		//          012345678 901 23456789012 345678901 2345678 901234567890123456789 012345 678901234 5678901 23456789012345678 901234 5678901234 5678901 23456789012345 6789012 345 67890123456 789012345 6789012 3456789012345678901 234567 890123456 7890123 45678901234567890 123456 7890123456 7890123 45678901234567 8901
		String s = "a:2:{s:2:\"de\";a:3:{s:8:\"category\";s:20:\"Schweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}s:2:\"en\";a:3:{s:8:\"category\";s:18:\"Sweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}}";
		Object o = PhpUnserializer.unserialize(s);
		assertNotNull("No unserialization", o);
		assertTrue("Not a map", o instanceof Map);
		Map<?,?> map = (Map<?,?>)o;
		assertEquals("No the correct map size", 2, map.size());
		assertTrue("Map does not contain DE key", map.containsKey("de"));
		assertTrue("Map does not contain EN key", map.containsKey("en"));
		assertTrue("DE value is not a map", map.get("de") instanceof Map);
		assertTrue("EN value is not a map", map.get("en") instanceof Map);
		Map<?,?> deMap = (Map<?,?>)map.get("de");
		Map<?,?> enMap = (Map<?,?>)map.get("en");
		assertEquals("No the correct DE map size", 3, deMap.size());
		assertEquals("No the correct EN map size", 3, enMap.size());
		assertEquals("DE(category) not correct", "Schweden: Superettan", deMap.get("category"));
		assertEquals("EN(category) not correct", "Sweden: Superettan",   enMap.get("category"));
		assertEquals("DE(homeTeam) not correct", "Varbergs BOIS FC", deMap.get("homeTeam"));
		assertEquals("EN(homeTeam) not correct", "Varbergs BOIS FC",   enMap.get("homeTeam"));
		assertEquals("DE(guestTeam) not correct", "Ljungskile SK", deMap.get("guestTeam"));
		assertEquals("EN(guestTeam) not correct", "Ljungskile SK",   enMap.get("guestTeam"));
	}

//	@Test
//	public void testArrayOfArray() {
//		// a:2:{s:2:"de";a:3:{s:8:"category";s:29:"Internationale Turniere: Uefa";s:8:"homeTeam";s:16:"NK Siroki Brijeg";s:9:"guestTeam";s:12:"FK Qï¿½bï¿½lï¿½";}s:2:"en";a:3:{s:8:"category";s:27:"International Matches: Uefa";s:8:"homeTeam";s:16:"NK Siroki Brijeg";s:9:"guestTeam";s:12:"FK Qï¿½bï¿½lï¿½";}}
//		String s = "a:2:{s:2:\"de\";a:3:{s:8:\"category\";s:29:\"Internationale Turniere: Uefa\";s:8:\"homeTeam\";s:16:\"NK Siroki Brijeg\";s:9:\"guestTeam\";s:12:\"FK Qï¿½bï¿½lï¿½\";}s:2:\"en\";a:3:{s:8:\"category\";s:27:\"International Matches: Uefa\";s:8:\"homeTeam\";s:16:\"NK Siroki Brijeg\";s:9:\"guestTeam\";s:12:\"FK Qï¿½bï¿½lï¿½\";}}";
//		assertNotNull("No unserialization", PhpUnserializer.unserialize(s));
//	}
}
