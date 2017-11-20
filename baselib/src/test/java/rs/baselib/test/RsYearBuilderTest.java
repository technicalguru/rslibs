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
import static rs.baselib.test.BuilderUtils.$RsYear;
import static rs.baselib.test.BuilderUtils.listOf;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Test;

import rs.baselib.util.RsYear;

/**
 * Tests the {@link RsYearBuilder} class.
 * @author ralph
 *
 */
public class RsYearBuilderTest {

	@Test
	public void testDefault() {
		RsYearBuilder b = $RsYear();
		String expected = new SimpleDateFormat("yyyy").format(new Date());
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", expected, actual.getKey());
	}
	
	@Test
	public void testWithTime() {
		long time = 1000000L;
		RsYearBuilder b = $RsYear().withTime(time);
		String expected = new SimpleDateFormat("yyyy").format(new Date(time));
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", expected, actual.getKey());
	}

	@Test
	public void testWithTimeOffset() {
		RsYearBuilder b = $RsYear().withYearOffset(1);
		RsYear first = b.build();
		int year = first.getIntKey();
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", year+1, actual.getIntKey());
	}

	@Test
	public void testWithTimezone() {
		TimeZone tz = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
		RsYearBuilder b = $RsYear().withTimezone(tz);
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", tz, actual.getTimeZone());
	}

	@Test
	public void testWithMonth() {
		RsYearBuilder b = $RsYear().withYear(2015);
		RsYear actual = b.build();
		assertEquals("RsYearBuilder not initialized correctly", "2015", actual.getKey());
	}

	@Test
	public void testList() {
		Set<String> delivered = new HashSet<>();
		for (RsYear s : listOf(100, $RsYear().withYearOffset(1))) {
			assertFalse("RsYearBuilder does not produce unique dates", delivered.contains(s.getKey()));
			delivered.add(s.getKey());
		}
	}
}
