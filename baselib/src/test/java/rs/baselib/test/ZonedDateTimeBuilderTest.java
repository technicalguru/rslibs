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
import static rs.baselib.test.BuilderUtils.$ZonedDateTime;

import java.time.Duration;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link ZonedDateTimeBuilder}.
 * 
 * @author ralph
 *
 */
public class ZonedDateTimeBuilderTest {

	@Test
	public void testDefaultStartAndZone() {
		ZonedDateTimeBuilder b      = $ZonedDateTime();
		ZonedDateTime        start  = ZonedDateTime.now().minusNanos(1);
		ZonedDateTime        actual = b.build();
		ZonedDateTime        end    = ZonedDateTime.now().plusNanos(1);
		assertEquals(start.getZone(), actual.getZone());
		assertTrue(actual.isAfter(start));
		assertTrue(actual.isBefore(end));
	}
	
	@Test
	public void testZone() {
		ZoneId               zone   = ZoneId.of("UTC");
		ZonedDateTimeBuilder b      = $ZonedDateTime().withZoneId(zone);
		ZonedDateTime        actual = b.build();
		assertEquals(zone, actual.getZone());
	}
	
	@Test
	public void testZoneFromStart() {
		ZoneId               zone   = ZoneId.of("UTC");
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStart(ZonedDateTime.now().withZoneSameInstant(zone));
		ZonedDateTime        actual = b.build();
		assertEquals(zone, actual.getZone());
	}
	
	@Test
	public void testStart() {
		ZonedDateTime        start  = ZonedDateTime.now().plusHours(1);
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStart(start);
		ZonedDateTime        actual = b.build();
		assertEquals(start, actual);
	}
	
	@Test
	public void testStep() {
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStep(Period.ofDays(1), Duration.ofDays(1));
		ZonedDateTime        first  = b.build();
		ZonedDateTime        actual = b.build();
		assertEquals(Duration.ofDays(2).getSeconds(), actual.toInstant().getEpochSecond() - first.toInstant().getEpochSecond());
	}
	
	@Test
	public void testStep_withPeriod() {
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStep(Period.ofDays(1), null);
		ZonedDateTime        first  = b.build();
		ZonedDateTime        actual = b.build();
		assertEquals(Duration.ofDays(1).getSeconds(), actual.toInstant().getEpochSecond() - first.toInstant().getEpochSecond());
	}
	
	@Test
	public void testStep_withDuration() {
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStep(null, Duration.ofDays(1));
		ZonedDateTime        first  = b.build();
		ZonedDateTime        actual = b.build();
		assertEquals(Duration.ofDays(1).getSeconds(), actual.toInstant().getEpochSecond() - first.toInstant().getEpochSecond());
	}

	@Test
	public void testStep_withMs() {
		ZonedDateTimeBuilder b      = $ZonedDateTime().withStep(86400000L);
		ZonedDateTime        first  = b.build();
		ZonedDateTime        actual = b.build();
		assertEquals(Duration.ofDays(1).getSeconds(), actual.toInstant().getEpochSecond() - first.toInstant().getEpochSecond());
	}
	
	@Test
	public void testRandom() {
		ZonedDateTime        now    = ZonedDateTime.now();
		ZonedDateTime        start  = now.minusDays(10);
		ZonedDateTimeBuilder b      = $ZonedDateTime().withRandom().withStart(start).withEnd(now);
		for (int i=0; i<100; i++) {
			ZonedDateTime        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	
	@Test
	public void testRandom_withStartGreaterThanEnd() {
		ZonedDateTime        now    = ZonedDateTime.now();
		ZonedDateTime        start  = now.minusDays(10);
		ZonedDateTimeBuilder b      = $ZonedDateTime().withRandom().withStart(now).withEnd(start);
		for (int i=0; i<100; i++) {
			ZonedDateTime        actual = b.build();
			assertTrue(start.isEqual(actual) || start.isBefore(actual));
			assertTrue(now.isAfter(actual));
		}
	}
	

}
