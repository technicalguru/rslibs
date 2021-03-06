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
package rs.baselib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link rs.baselib.util.WeakMapCache}.
 * @author ralph
 *
 */
public class WeakMapCacheTest {

	private WeakMapCache<Long, Object> cache;
	
	@Before
	public void setUp() throws Exception {
		cache = new WeakMapCache<Long, Object>();
	}

	@Test
	public void testSize() {
		assertEquals("Cache is not empty", 0, cache.size());
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals("Cache is empty", 1, cache.size());	
	}

	@Test
	public void testIsEmpty() {
		assertTrue("Cache is not empty", cache.isEmpty());
	}

	@Test
	public void testContainsKey() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertTrue("containsKey() fails", cache.containsKey(1L));
	}

	@Test
	public void testContainsValue() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertTrue("containsValue() fails", cache.containsValue(testValue));
	}

	@Test
	public void testGet() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals("Cannot get testValue", testValue, cache.get(1L));
	}

	@Test
	public void testPut() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals("Cannot put testValue into cache", 1, cache.size());
	}

	@Test
	public void testRemove() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		assertEquals("Cannot put testValue into cache", 1, cache.size());
		cache.remove(1L);
		assertEquals("Cannot remove testValue from cache", 0, cache.size());
	}

	@Test
	public void testClear() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		cache.clear();
		assertEquals("Cannot clear cache", 0, cache.size());
	}

	@Test
	public void testKeySet() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		Set<Long> keySet = cache.keySet();
		assertEquals("keySet has invalid size", 1, keySet.size());
		assertTrue("keySet does not contain our key", keySet.contains(1L));
	}

	@Test
	public void testValues() {
		String testValue = "TEST_VALUE";
		cache.put(1L, testValue);
		Collection<Object> valueSet = cache.values();
		assertEquals("values() has invalid size", 1, valueSet.size());
		assertTrue("values does not contain our object", valueSet.contains(testValue));
	}

}
