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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link PhpUnserializer}.
 * @author ralph
 *
 */
public class PhpUnserializerTest {

	@Test
	public void testString() {
		String s = (String)PhpUnserializer.unserialize("s:13:\"a test object\"");
		assertEquals("a test object", s);
	}

	@Test
	public void testCharacter() {
		String s = (String)PhpUnserializer.unserialize("s:1:\"c\"");
		assertEquals("c", s);
	}

	@Test
	public void testNull() {
		Object o = PhpUnserializer.unserialize("N");
		assertNull(o);
	}

	@Test
	public void testInteger() {
		int i = (Integer)PhpUnserializer.unserialize("i:15");
		assertEquals(15, i);
	}

	@Test
	public void testLong() {
		Number n = (Number)PhpUnserializer.unserialize("i:15");
		assertEquals(15, n);
		n = (Number)PhpUnserializer.unserialize("d:9223372036854775807");
		assertEquals(Long.MAX_VALUE, n);
	}

	@Test
	public void testDouble() {
		double d = (Double)PhpUnserializer.unserialize("d:15.0");
		assertEquals(15d, d, 0.00001);
	}

	@Test
	public void testBoolean() {
		boolean b = (Boolean)PhpUnserializer.unserialize("b:1");
		assertEquals(true, b);
		b = (Boolean)PhpUnserializer.unserialize("b:0");
		assertEquals(false, b);
	}
	
	@Test
	public void testArray() {
		Object a[] = (Object[])PhpUnserializer.unserialize("a:5:{i:0;i:15;i:1;i:15;i:2;d:15.0;i:3;b:1;i:4;s:11:\"test string\"}");
		assertArrayEquals(new Object[] { 15, 15, 15d, true, "test string" }, a);
	}
	
	@Test
	public void testMap() {
		Map<?, ?> map = (Map<?, ?>)PhpUnserializer.unserialize("a:3:{d:15.0;b:1;s:3:\"key\";s:11:\"test string\";i:15;i:15}");
		assertEquals(3, map.size());
		assertNotNull(map.get(15));
		assertEquals(15, map.get(15));
		assertNotNull(map.get(15d));
		assertEquals(Boolean.TRUE, map.get(15d));
		assertNotNull(map.get("key"));
		assertEquals("test string", map.get("key"));
	}
	
	@Test
	public void testArrayOfArrays() {
		//                                                                                                                        1                                                                                                              2                                                                             
		//                     1          2          3           4         5          6          7          8          9          0          1          2          3          4           5          6          7          8         9           0          1          2         3           4          5          6          7
		//          012345678 901 23456789012 345678901 2345678 901234567890123456789 012345 678901234 5678901 23456789012345678 901234 5678901234 5678901 23456789012345 6789012 345 67890123456 789012345 6789012 3456789012345678901 234567 890123456 7890123 45678901234567890 123456 7890123456 7890123 45678901234567 8901
		String s = "a:2:{s:2:\"de\";a:3:{s:8:\"category\";s:20:\"Schweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}s:2:\"en\";a:3:{s:8:\"category\";s:18:\"Sweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}}";
		Object o = PhpUnserializer.unserialize(s);
		assertNotNull(o);
		assertTrue(o instanceof Map);
		Map<?,?> map = (Map<?,?>)o;
		assertEquals(2, map.size());
		assertTrue(map.containsKey("de"));
		assertTrue(map.containsKey("en"));
		assertTrue(map.get("de") instanceof Map);
		assertTrue(map.get("en") instanceof Map);
		Map<?,?> deMap = (Map<?,?>)map.get("de");
		Map<?,?> enMap = (Map<?,?>)map.get("en");
		assertEquals(3, deMap.size());
		assertEquals(3, enMap.size());
		assertEquals("Schweden: Superettan", deMap.get("category"));
		assertEquals("Sweden: Superettan",   enMap.get("category"));
		assertEquals("Varbergs BOIS FC", deMap.get("homeTeam"));
		assertEquals("Varbergs BOIS FC",   enMap.get("homeTeam"));
		assertEquals("Ljungskile SK", deMap.get("guestTeam"));
		assertEquals("Ljungskile SK",   enMap.get("guestTeam"));
	}

//	@Test
//	public void testArrayOfArray() {
//		// a:2:{s:2:"de";a:3:{s:8:"category";s:29:"Internationale Turniere: Uefa";s:8:"homeTeam";s:16:"NK Siroki Brijeg";s:9:"guestTeam";s:12:"FK Qï¿½bï¿½lï¿½";}s:2:"en";a:3:{s:8:"category";s:27:"International Matches: Uefa";s:8:"homeTeam";s:16:"NK Siroki Brijeg";s:9:"guestTeam";s:12:"FK Qï¿½bï¿½lï¿½";}}
//		String s = "a:2:{s:2:\"de\";a:3:{s:8:\"category\";s:29:\"Internationale Turniere: Uefa\";s:8:\"homeTeam\";s:16:\"NK Siroki Brijeg\";s:9:\"guestTeam\";s:12:\"FK Qï¿½bï¿½lï¿½\";}s:2:\"en\";a:3:{s:8:\"category\";s:27:\"International Matches: Uefa\";s:8:\"homeTeam\";s:16:\"NK Siroki Brijeg\";s:9:\"guestTeam\";s:12:\"FK Qï¿½bï¿½lï¿½\";}}";
//		assertNotNull("No unserialization", PhpUnserializer.unserialize(s));
//	}
}
