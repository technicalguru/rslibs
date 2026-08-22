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
import static rs.baselib.test.BuilderUtils.$LocalDateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link LocalDateTimeBuilder}.
 * 
 * @author ralph
 *
 */
public class LocalDateTimeBuilderTest {

	@Test
	public void testDefaultStart() {
		LocalDateTimeBuilder b      = $LocalDateTime();
		LocalDateTime        start  = LocalDateTime.now().minusNanos(1);
		LocalDateTime        actual = b.build();
		LocalDateTime        end    = LocalDateTime.now().plusNanos(1);
		assertTrue(actual.isAfter(start));
		assertTrue(actual.isBefore(end));
	}
	
	@Test
	public void testStart() {
		LocalDateTime        start  = LocalDateTime.now().plusHours(1);
		LocalDateTimeBuilder b      = $LocalDateTime().withStart(start);
		LocalDateTime        actual = b.build();
		assertEquals(start, actual);
	}
	
	@Test
	public void testStep() {
		LocalDateTimeBuilder b      = $LocalDateTime().withStep(Period.ofDays(1), Duration.ofDays(1));
		LocalDateTime        first  = b.build();
		LocalDateTime        actual = b.build();
		assertEquals(first.plusDays(2), actual);
	}
	
	@Test
	public void testStep_withPeriod() {
		LocalDateTimeBuilder b      = $LocalDateTime().withStep(Period.ofDays(1), null);
		LocalDateTime        first  = b.build();
		LocalDateTime        actual = b.build();
		assertEquals(first.plusDays(1), actual);
	}
	
	@Test
	public void testStep_withDuration() {
		LocalDateTimeBuilder b      = $LocalDateTime().withStep(null, Duration.ofDays(1));
		LocalDateTime        first  = b.build();
		LocalDateTime        actual = b.build();
		assertEquals(first.plusDays(1), actual);
	}
	
	@Test
	public void testStep_withMs() {
		LocalDateTimeBuilder b      = $LocalDateTime().withStep(86400000L);
		LocalDateTime        first  = b.build();
		LocalDateTime        actual = b.build();
		assertEquals(first.plusDays(1), actual);
	}
	
	@Test
	public void testRandom() {
		LocalDateTime        now    = LocalDateTime.now();
		LocalDateTime        start  = now.minusDays(10);
		LocalDateTimeBuilder b      = $LocalDateTime().withRandom().withStart(start).withEnd(now);
		for (int i=0; i<100; i++) {
			LocalDateTime        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	
	@Test
	public void testRandom_withStartGreaterThanEnd() {
		LocalDateTime        now    = LocalDateTime.now();
		LocalDateTime        start  = now.minusDays(10);
		LocalDateTimeBuilder b      = $LocalDateTime().withRandom().withStart(now).withEnd(start);
		for (int i=0; i<100; i++) {
			LocalDateTime        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	

}
