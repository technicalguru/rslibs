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

import org.junit.Test;

/**
 * Tests the {@link LevenshteinDistance}.
 * @author ralph
 *
 */
public class LevenshteinDistanceTest {

	@Test
	public void test1() {
		assertEquals("Invalid distance", 3, LevenshteinDistance.INSTANCE.getDistance("kitten", "sitting"));
	}
	
	@Test
	public void test2() {
		assertEquals("Invalid distance", 3, LevenshteinDistance.INSTANCE.getDistance("saturday", "sunday"));
	}
	
	@Test
	public void test3() {
		assertEquals("Invalid distance", 8, LevenshteinDistance.INSTANCE.getDistance("rosettacode", "raisethysword"));
	}
	
	@Test
	public void test4() {
		assertEquals("Invalid distance", 1, LevenshteinDistance.INSTANCE.getDistance("kitten", "sitten"));
	}
	

}
