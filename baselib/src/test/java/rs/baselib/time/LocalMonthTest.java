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
package rs.baselib.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * Tests the {@link LocalMonth} class.
 * 
 * @author ralph
 *
 */
public class LocalMonthTest {

	@Test
	public void testNow() {
		LocalDate ld = LocalDate.now();
		LocalMonth lm = LocalMonth.now();
		assertEquals("Year in now() incorrect", ld.getYear(), lm.getYear());
		assertEquals("Month in now() incorrect", ld.getMonthValue(), lm.getMonthValue());
	}

	@Test
	public void testNow_ZoneId() {
		ZoneId zone = ZoneId.of("UTC");
		LocalDate ld = LocalDate.now(zone);
		LocalMonth lm = LocalMonth.now(zone);
		assertEquals("Year in now(ZoneId) incorrect", ld.getYear(), lm.getYear());
		assertEquals("Month in now(ZoneId) incorrect", ld.getMonthValue(), lm.getMonthValue());
	}
	
	@Test
	public void testOf_IntInt() {
		LocalMonth lm = LocalMonth.of(2023, 1);
		assertEquals("Year in of(int,int) incorrect", 2023, lm.getYear());
		assertEquals("Month in of(int,int) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testOf_IntMonth() {
		LocalMonth lm = LocalMonth.of(2023, Month.JANUARY);
		assertEquals("Year in of(int,Month) incorrect", 2023, lm.getYear());
		assertEquals("Month in of(int,Month) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testOfEpochDay() {
		LocalMonth lm = LocalMonth.ofEpochDay(35);
		assertEquals("Year in ofEpochDay(long) incorrect", 1970, lm.getYear());
		assertEquals("Month in ofEpochDay(long) incorrect", 2, lm.getMonthValue());
	}
	
	@Test
	public void testOfEpochMonth() {
		LocalMonth lm = LocalMonth.ofEpochMonth(0);
		assertEquals("Year in ofEpochMonth(int) incorrect", 1970, lm.getYear());
		assertEquals("Month in ofEpochMonth(int) incorrect", 1, lm.getMonthValue());
		lm = LocalMonth.ofEpochMonth(11);
		assertEquals("Year in ofEpochMonth(int) incorrect", 1970, lm.getYear());
		assertEquals("Month in ofEpochMonth(int) incorrect", 12, lm.getMonthValue());
	}
	
	@Test
	public void testOfInstant() {
		LocalMonth lm = LocalMonth.ofInstant(Instant.ofEpochSecond(86400), ZoneId.of("UTC"));
		assertEquals("Year in ofInstant(Instant,ZoneId) incorrect", 1970, lm.getYear());
		assertEquals("Month in ofInstant(Instant,ZoneId) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testOfKey() {
		LocalMonth lm = LocalMonth.ofKey("197311");
		assertEquals("Year in ofKey(String) incorrect", 1973, lm.getYear());
		assertEquals("Month in ofKey(String) incorrect", 11, lm.getMonthValue());
	}
	
	@Test
	public void testOfLocalDate() {
		LocalMonth lm = LocalMonth.ofLocalDate(LocalDate.of(1970, 1, 15));
		assertEquals("Year in ofLocalDate(LocalDate) incorrect", 1970, lm.getYear());
		assertEquals("Month in ofLocalDate(LocalDate) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testOfYearDay() {
		LocalMonth lm = LocalMonth.ofYearDay(1973, 45);
		assertEquals("Year in ofYearDay(int,int) incorrect", 1973, lm.getYear());
		assertEquals("Month in ofYearDay(int,int) incorrect", 2, lm.getMonthValue());
	}
	
	@Test
	public void testAtEndOfMonth() {
		LocalDateTime ld = LocalMonth.ofYearDay(1973, 45).atEndOfMonth();
		assertEquals("Year in atEndOfMonth() incorrect", 1973, ld.getYear());
		assertEquals("Month in atEndOfMonth() incorrect", 2, ld.getMonthValue());
		assertEquals("Day in atEndOfMonth() incorrect", 28, ld.getDayOfMonth());
		assertEquals("Hour in atEndOfMonth() incorrect", 23, ld.getHour());
		assertEquals("Minute in atEndOfMonth() incorrect", 59, ld.getMinute());
		assertEquals("Second in atEndOfMonth() incorrect", 59, ld.getSecond());
	}
	
	@Test
	public void testAtEndOfMonth_ZoneId() {
		ZonedDateTime ld = LocalMonth.ofYearDay(1973, 45).atEndOfMonth(ZoneId.of("UTC"));
		assertEquals("Year in atEndOfMonth(ZoneId) incorrect", 1973, ld.getYear());
		assertEquals("Month in atEndOfMonth(ZoneId) incorrect", 2, ld.getMonthValue());
		assertEquals("Day in atEndOfMonth(ZoneId) incorrect", 28, ld.getDayOfMonth());
		assertEquals("Hour in atEndOfMonth(ZoneId) incorrect", 23, ld.getHour());
		assertEquals("Minute in atEndOfMonth(ZoneId) incorrect", 59, ld.getMinute());
		assertEquals("Second in atEndOfMonth(ZoneId) incorrect", 59, ld.getSecond());
	}
	
	@Test
	public void testAtStartOfMonth() {
		LocalDateTime ld = LocalMonth.ofYearDay(1973, 45).atStartOfMonth();
		assertEquals("Year in atStartOfMonth() incorrect", 1973, ld.getYear());
		assertEquals("Month in atStartOfMonth() incorrect", 2, ld.getMonthValue());
		assertEquals("Day in atStartOfMonth() incorrect", 1, ld.getDayOfMonth());
		assertEquals("Hour in atStartOfMonth() incorrect", 0, ld.getHour());
		assertEquals("Minute in atStartOfMonth() incorrect", 0, ld.getMinute());
		assertEquals("Second in atStartOfMonth() incorrect", 0, ld.getSecond());
	}
	
	@Test
	public void testAtStartOfMonth_ZoneId() {
		ZonedDateTime ld = LocalMonth.ofYearDay(1973, 45).atStartOfMonth(ZoneId.of("UTC"));
		assertEquals("Year in atStartOfMonth(ZoneId) incorrect", 1973, ld.getYear());
		assertEquals("Month in atStartOfMonth(ZoneId) incorrect", 2, ld.getMonthValue());
		assertEquals("Day in atStartOfMonth(ZoneId) incorrect", 1, ld.getDayOfMonth());
		assertEquals("Hour in atStartOfMonth(ZoneId) incorrect", 0, ld.getHour());
		assertEquals("Minute in atStartOfMonth(ZoneId) incorrect", 0, ld.getMinute());
		assertEquals("Second in atStartOfMonth(ZoneId) incorrect", 0, ld.getSecond());
	}
	
	@Test
	public void testGetFirstDay() {
		LocalDate ld = LocalMonth.ofYearDay(1973, 45).getFirstDay();
		assertEquals("Year in getFirstDay() incorrect", 1973, ld.getYear());
		assertEquals("Month in getFirstDay() incorrect", 2, ld.getMonthValue());
		assertEquals("Day in getFirstDay() incorrect", 1, ld.getDayOfMonth());
	}
	
	@Test
	public void testGetLastDay() {
		LocalDate ld = LocalMonth.ofYearDay(1973, 45).getLastDay();
		assertEquals("Year in getLastDay() incorrect", 1973, ld.getYear());
		assertEquals("Month in getLastDay() incorrect", 2, ld.getMonthValue());
		assertEquals("Day in getLastDay() incorrect", 28, ld.getDayOfMonth());
	}

	@Test
	public void testGetKey() {
		LocalMonth lm = LocalMonth.ofYearDay(1973, 32);
		assertEquals("Key incorrect", "197302", lm.getKey());
	}
	
	@Test
	public void testIsLeapYear() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertTrue("isLeapYear is incorrect", lm.isLeapYear());
		lm = LocalMonth.of(1999, 2);
		assertFalse("isLeapYear is incorrect", lm.isLeapYear());
	}
	
	@Test
	public void testLengthOfMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertEquals("Day in lengthOfMonth() incorrect", 29, lm.lengthOfMonth());
	}
	
	@Test
	public void testLengthOfYear() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertEquals("Day in lengthOfYear() incorrect", 366, lm.lengthOfYear());
	}
	
	@Test
	public void testMinusMonths() {
		LocalMonth lm = LocalMonth.of(1972, 2).minusMonths(25);
		assertEquals("Year in minusMonths(int) incorrect", 1970, lm.getYear());
		assertEquals("Month in minusMonths(int) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testMinusYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).minusYear(2);
		assertEquals("Year in minusYear(int) incorrect", 1970, lm.getYear());
		assertEquals("Month in minusYear(int) incorrect", 2, lm.getMonthValue());
	}
	
	@Test
	public void testPlusMonths() {
		LocalMonth lm = LocalMonth.of(1972, 2).plusMonths(25);
		assertEquals("Year in plusMonths(int) incorrect", 1974, lm.getYear());
		assertEquals("Month in plusMonths(int) incorrect", 3, lm.getMonthValue());
	}
	
	@Test
	public void testPlusYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).plusYear(2);
		assertEquals("Year in plusYear(int) incorrect", 1974, lm.getYear());
		assertEquals("Month in plusYear(int) incorrect", 2, lm.getMonthValue());
	}
	
	@Test
	public void testNextMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).nextMonth();
		assertEquals("Year in plusYear(int) incorrect", 1972, lm.getYear());
		assertEquals("Month in plusYear(int) incorrect", 3, lm.getMonthValue());
	}
	
	@Test
	public void testPreviousMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).previousMonth();
		assertEquals("Year in plusYear(int) incorrect", 1972, lm.getYear());
		assertEquals("Month in plusYear(int) incorrect", 1, lm.getMonthValue());
	}
	
	@Test
	public void testMonthsUntil() {
		List<LocalMonth> l = LocalMonth.of(1972, 2).monthsUntil(LocalMonth.of(1972, 5)).collect(Collectors.toList());
		assertEquals("monthUntil(LocalMonth) delivers wrong months", 3, l.size());
		assertEquals("monthUntil(LocalMonth) delivers wrong months", "197202", l.get(0).getKey());
		assertEquals("monthUntil(LocalMonth) delivers wrong months", "197203", l.get(1).getKey());
		assertEquals("monthUntil(LocalMonth) delivers wrong months", "197204", l.get(2).getKey());
	}
	
	@Test
	public void testToEpochMonth() {
		assertEquals("toEpochMonth() incorrect", 0, LocalMonth.of(1970, 1).toEpochMonth());
		assertEquals("toEpochMonth() incorrect", 12, LocalMonth.of(1971, 1).toEpochMonth());
		assertEquals("toEpochMonth() incorrect", -1, LocalMonth.of(1969, 12).toEpochMonth());
	}
	
	@Test
	public void testWithMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).withMonth(5);
		assertEquals("Year in withMonth(int) incorrect", 1972, lm.getYear());
		assertEquals("Month in withMonth(int) incorrect", 5, lm.getMonthValue());
	}
	
	@Test
	public void testWithYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).withYear(2023);
		assertEquals("Year in withYear(int) incorrect", 2023, lm.getYear());
		assertEquals("Month in withYear(int) incorrect", 2, lm.getMonthValue());
	}

}
