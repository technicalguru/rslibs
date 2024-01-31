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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link PhpSerializer}.
 * @author ralph
 *
 */
public class PhpSerializerTest {

	@Test
	public void testString() {
		String s = PhpSerializer.serialize("a test object");
		assertEquals("s:13:\"a test object\";", s);
	}

	@Test
	public void testCharacter() {
		String s = PhpSerializer.serialize('c');
		assertEquals("s:1:\"c\";", s);
	}

	@Test
	public void testNull() {
		String s = PhpSerializer.serialize(null);
		assertEquals("N;", s);
	}

	@Test
	public void testInteger() {
		String s = PhpSerializer.serialize(15);
		assertEquals("i:15;", s);
	}

	@Test
	public void testLong() {
		String s = PhpSerializer.serialize(15L);
		assertEquals("i:15;", s);
		s = PhpSerializer.serialize(Long.MAX_VALUE);
		assertEquals("d:9223372036854775807;", s);
	}

	@Test
	public void testDouble() {
		String s = PhpSerializer.serialize(15d);
		assertEquals("d:15.0;", s);
	}

	@Test
	public void testBoolean() {
		String s = PhpSerializer.serialize(true);
		assertEquals("b:1;", s);
		s = PhpSerializer.serialize(false);
		assertEquals("b:0;", s);
	}
	
	@Test
	public void testArray() {
		String s = PhpSerializer.serialize(new Object[] { 15, 15L, 15d, true, "test string" });
		assertEquals("a:5:{i:0;i:15;i:1;i:15;i:2;d:15.0;i:3;b:1;i:4;s:11:\"test string\";}", s);
	}
	
	@Test
	public void testMap() {
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		map.put(15d, true);
		map.put("key", "test string");
		map.put(15,  15L);
		String s = PhpSerializer.serialize(map);
		assertEquals("a:3:{d:15.0;b:1;s:3:\"key\";s:11:\"test string\";i:15;i:15;}", s);
	}
	
	@Test
	public void testArrayOfArrays() {
		Map<String,String> deMap = new LinkedHashMap<String, String>();
		Map<String,String> enMap = new LinkedHashMap<String, String>();
		deMap.put("category", "Schweden: Superettan");
		deMap.put("homeTeam", "Varbergs BOIS FC");
		deMap.put("guestTeam", "Ljungskile SK");
		enMap.put("category", "Sweden: Superettan");
		enMap.put("homeTeam", "Varbergs BOIS FC");
		enMap.put("guestTeam", "Ljungskile SK");
		Map<String,Map<String,String>> map = new LinkedHashMap<String, Map<String,String>>();
		map.put("de", deMap);
		map.put("en", enMap);
		String expected1 = "a:2:{s:2:\"de\";a:3:{s:8:\"category\";s:20:\"Schweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}s:2:\"en\";a:3:{s:8:\"category\";s:18:\"Sweden: Superettan\";s:8:\"homeTeam\";s:16:\"Varbergs BOIS FC\";s:9:\"guestTeam\";s:13:\"Ljungskile SK\";}}";
		String actual = PhpSerializer.serialize(map);
		assertEquals(expected1, actual);
	}
}
