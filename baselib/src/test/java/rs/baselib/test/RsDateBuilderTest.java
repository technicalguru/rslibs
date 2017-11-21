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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static rs.baselib.test.BuilderUtils.$Long;
import static rs.baselib.test.BuilderUtils.$RsDate;
import static rs.baselib.test.BuilderUtils.listOf;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Test;

import rs.baselib.util.RsDate;

/**
 * Tests the {@link RsDateBuilder} class.
 * @author ralph
 *
 */
public class RsDateBuilderTest {

	@Test
	public void testDefault() {
		RsDateBuilder b = $RsDate();
		long start = System.currentTimeMillis();
		RsDate actual = b.build();
		long end = System.currentTimeMillis();
		long time = actual.getTimeInMillis();
		assertTrue("RsDateBuilder not initialized correctly", time >= start);
		assertTrue("RsDateBuilder not initialized correctly", time <= end);
	}
	
	@Test
	public void testWithTimeLong() {
		long time = 1000000L;
		RsDateBuilder b = $RsDate().withTime(time);
		RsDate actual = b.build();
		assertEquals("RsDateBuilder not initialized correctly", time, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimeBuilder() {
		LongBuilder builder = $Long().withStart(100000L).withOffset(1000L);
		RsDateBuilder b = $RsDate().withTime(builder);
		RsDate actual = b.build();
		assertEquals("RsDateBuilder not initialized correctly", 100000L, actual.getTimeInMillis());
		actual = b.build();
		assertEquals("RsDateBuilder not initialized correctly", 101000L, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimeOffset() {
		RsDateBuilder b = $RsDate().withTimeOffset(100000L);
		RsDate first = b.build();
		RsDate actual = b.build();
		assertEquals("RsDateBuilder not initialized correctly", first.getTimeInMillis()+100000L, actual.getTimeInMillis());
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsDateBuilder b = $RsDate().withTimezone(tz);
		RsDate actual = b.build();
		assertEquals("RsDateBuilder not initialized correctly", tz, actual.getTimeZone());
	}

	@Test
	public void testList() {
		Set<RsDate> delivered = new HashSet<>();
		for (RsDate s : listOf(100, $RsDate().withTimeOffset(1L))) {
			assertFalse("RsDateBuilder does not produce unique dates", delivered.contains(s));
			delivered.add(s);
		}
	}
}
