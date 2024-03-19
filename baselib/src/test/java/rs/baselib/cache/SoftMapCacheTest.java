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
package rs.baselib.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests the {@link rs.baselib.cache.SoftMapCache}.
 * @author ralph
 *
 */
public class SoftMapCacheTest {

	private SoftMapCache<Long, Object> cache;
	
	@BeforeEach
	public void setUp() throws Exception {
		cache = new SoftMapCache<Long, Object>();
	}

	@Test
	public void testSize() {
		assertEquals(0, cache.size());
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals(1, cache.size());	
	}

	@Test
	public void testIsEmpty() {
		assertTrue(cache.isEmpty());
	}

	@Test
	public void testContainsKey() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertTrue(cache.containsKey(1L));
	}

	@Test
	public void testContainsValue() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertTrue(cache.containsValue(testValue));
	}

	@Test
	public void testGet() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals(testValue, cache.get(1L));
	}

	@Test
	public void testPut() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals(1, cache.size());
	}

	@Test
	public void testRemove() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals(1, cache.size());
		cache.remove(1L);
		assertEquals(0, cache.size());
	}

	@Test
	public void testClear() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		cache.clear();
		assertEquals(0, cache.size());
	}

	@Test
	public void testKeySet() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		Set<Long> keySet = cache.keySet();
		assertEquals(1, keySet.size());
		assertTrue(keySet.contains(1L));
	}

	@Test
	public void testValues() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		Collection<Object> valueSet = cache.values();
		assertEquals(1, valueSet.size());
		assertTrue(valueSet.contains(testValue));
	}

}
