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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rs.baselib.test.BuilderUtils.$LocalTime;

import java.time.Duration;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link LocalTimeBuilder}.
 * 
 * @author ralph
 *
 */
public class LocalTimeBuilderTest {

	@Test
	public void testDefaultStart() {
		LocalTimeBuilder b      = $LocalTime();
		LocalTime        start  = LocalTime.now().minusNanos(1);
		LocalTime        actual = b.build();
		LocalTime        end    = LocalTime.now().plusNanos(1);
		assertTrue(actual.isAfter(start), "start="+start+" actual="+actual);
		assertTrue(actual.isBefore(end));
	}
	
	@Test
	public void testStart() {
		LocalTime        start  = LocalTime.now().plusHours(1);
		LocalTimeBuilder b      = $LocalTime().withStart(start);
		LocalTime        actual = b.build();
		assertEquals(start, actual);
	}
	
	@Test
	public void testStep() {
		LocalTimeBuilder b      = $LocalTime().withStep(Duration.ofHours(1));
		LocalTime        last  = b.build();
		// Ensure that the time starts again at the beginning
		for (int i=0; i<30; i++) {
			LocalTime actual = b.build();
			int expectedHour = last.getHour()+1;
			if (expectedHour > 23) expectedHour = 0;
			assertEquals(expectedHour, actual.getHour());
			last = actual;
		}
	}
	
	@Test
	public void testRandom() {
		LocalTime        start = LocalTime.of(10, 0, 0, 0);
		LocalTime        end   = LocalTime.of(14, 0, 0, 0);
		LocalTimeBuilder b      = $LocalTime().withRandom().withStart(start).withEnd(end);
		for (int i=0; i<100; i++) {
			LocalTime        actual = b.build();
			assertTrue(start.equals(actual) || start.isBefore(actual));
			assertTrue(end.isAfter(actual));
		}
	}
	
	@Test
	public void testRandom_withStartGreaterThanEnd() {
		LocalTime        start = LocalTime.of(10, 0, 0, 0);
		LocalTime        end   = LocalTime.of(14, 0, 0, 0);
		LocalTimeBuilder b      = $LocalTime().withRandom().withStart(end).withEnd(start);
		for (int i=0; i<100; i++) {
			LocalTime        actual = b.build();
			assertTrue(start.equals(actual) || start.isBefore(actual));
			assertTrue(end.isAfter(actual));
		}
	}
	

}
