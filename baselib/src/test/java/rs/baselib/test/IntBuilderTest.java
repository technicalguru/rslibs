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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static rs.baselib.test.BuilderUtils.$Int;
import static rs.baselib.test.BuilderUtils.listOf;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link IntBuilder} class.
 * @author ralph
 *
 */
public class IntBuilderTest {

	@Test
	public void testDefault() {
		IntBuilder b = $Int();
		Integer actual = b.build();
		assertEquals(Integer.valueOf(0), actual);
	}
	
	@Test
	public void testStart() {
		IntBuilder b = $Int().withStart(3);
		Integer actual = b.build();
		assertEquals(Integer.valueOf(3), actual);
	}

	@Test
	public void testOffset() {
		IntBuilder b = $Int().withOffset(3);
		b.build();
		Integer actual = b.build();
		assertEquals(Integer.valueOf(3), actual);
	}

	@Test
	public void testList() {
		Set<Integer> delivered = new HashSet<>();
		for (Integer i : listOf(100, $Int())) {
			assertFalse(delivered.contains(i));
			delivered.add(i);
		}
	}
}
