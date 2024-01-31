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
import static rs.baselib.test.BuilderUtils.$Long;
import static rs.baselib.test.BuilderUtils.listOf;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link LongBuilder} class.
 * @author ralph
 *
 */
public class LongBuilderTest {

	@Test
	public void testDefault() {
		LongBuilder b = $Long();
		Long actual = b.build();
		assertEquals(Long.valueOf(0), actual);
	}
	
	@Test
	public void testStart() {
		LongBuilder b = $Long().withStart(3);
		Long actual = b.build();
		assertEquals(Long.valueOf(3), actual);
	}

	@Test
	public void testOffset() {
		LongBuilder b = $Long().withOffset(3);
		b.build();
		Long actual = b.build();
		assertEquals(Long.valueOf(3), actual);
	}

	@Test
	public void testList() {
		Set<Long> delivered = new HashSet<>();
		for (Long i : listOf(100, $Long())) {
			assertFalse(delivered.contains(i));
			delivered.add(i);
		}
	}
}
