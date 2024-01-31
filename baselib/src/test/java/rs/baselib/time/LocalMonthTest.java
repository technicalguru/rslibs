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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

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
		assertEquals(ld.getYear(), lm.getYear());
		assertEquals(ld.getMonthValue(), lm.getMonthValue());
	}

	@Test
	public void testNow_ZoneId() {
		ZoneId zone = ZoneId.of("UTC");
		LocalDate ld = LocalDate.now(zone);
		LocalMonth lm = LocalMonth.now(zone);
		assertEquals(ld.getYear(), lm.getYear());
		assertEquals(ld.getMonthValue(), lm.getMonthValue());
	}
	
	@Test
	public void testOf_IntInt() {
		LocalMonth lm = LocalMonth.of(2023, 1);
		assertEquals(2023, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testOf_IntMonth() {
		LocalMonth lm = LocalMonth.of(2023, Month.JANUARY);
		assertEquals(2023, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testOfEpochDay() {
		LocalMonth lm = LocalMonth.ofEpochDay(35);
		assertEquals(1970, lm.getYear());
		assertEquals(2, lm.getMonthValue());
	}
	
	@Test
	public void testOfEpochMonth() {
		LocalMonth lm = LocalMonth.ofEpochMonth(0);
		assertEquals(1970, lm.getYear());
		assertEquals(1, lm.getMonthValue());
		lm = LocalMonth.ofEpochMonth(11);
		assertEquals(1970, lm.getYear());
		assertEquals(12, lm.getMonthValue());
	}
	
	@Test
	public void testOfInstant() {
		LocalMonth lm = LocalMonth.ofInstant(Instant.ofEpochSecond(86400), ZoneId.of("UTC"));
		assertEquals(1970, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testOfKey() {
		LocalMonth lm = LocalMonth.ofKey("197311");
		assertEquals(1973, lm.getYear());
		assertEquals(11, lm.getMonthValue());
	}
	
	@Test
	public void testOfLocalDate() {
		LocalMonth lm = LocalMonth.ofLocalDate(LocalDate.of(1970, 1, 15));
		assertEquals(1970, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testOfYearDay() {
		LocalMonth lm = LocalMonth.ofYearDay(1973, 45);
		assertEquals(1973, lm.getYear());
		assertEquals(2, lm.getMonthValue());
	}
	
	@Test
	public void testAtEndOfMonth() {
		LocalDateTime ld = LocalMonth.ofYearDay(1973, 45).atEndOfMonth();
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(28, ld.getDayOfMonth());
		assertEquals(23, ld.getHour());
		assertEquals(59, ld.getMinute());
		assertEquals(59, ld.getSecond());
	}
	
	@Test
	public void testAtEndOfMonth_ZoneId() {
		ZonedDateTime ld = LocalMonth.ofYearDay(1973, 45).atEndOfMonth(ZoneId.of("UTC"));
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(28, ld.getDayOfMonth());
		assertEquals(23, ld.getHour());
		assertEquals(59, ld.getMinute());
		assertEquals(59, ld.getSecond());
	}
	
	@Test
	public void testAtStartOfMonth() {
		LocalDateTime ld = LocalMonth.ofYearDay(1973, 45).atStartOfMonth();
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(1, ld.getDayOfMonth());
		assertEquals(0, ld.getHour());
		assertEquals(0, ld.getMinute());
		assertEquals(0, ld.getSecond());
	}
	
	@Test
	public void testAtStartOfMonth_ZoneId() {
		ZonedDateTime ld = LocalMonth.ofYearDay(1973, 45).atStartOfMonth(ZoneId.of("UTC"));
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(1, ld.getDayOfMonth());
		assertEquals(0, ld.getHour());
		assertEquals(0, ld.getMinute());
		assertEquals(0, ld.getSecond());
	}
	
	@Test
	public void testGetFirstDay() {
		LocalDate ld = LocalMonth.ofYearDay(1973, 45).getFirstDay();
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(1, ld.getDayOfMonth());
	}
	
	@Test
	public void testGetLastDay() {
		LocalDate ld = LocalMonth.ofYearDay(1973, 45).getLastDay();
		assertEquals(1973, ld.getYear());
		assertEquals(2, ld.getMonthValue());
		assertEquals(28, ld.getDayOfMonth());
	}

	@Test
	public void testGetKey() {
		LocalMonth lm = LocalMonth.ofYearDay(1973, 32);
		assertEquals("197302", lm.getKey());
	}
	
	@Test
	public void testIsLeapYear() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertTrue(lm.isLeapYear());
		lm = LocalMonth.of(1999, 2);
		assertFalse(lm.isLeapYear());
	}
	
	@Test
	public void testLengthOfMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertEquals(29, lm.lengthOfMonth());
	}
	
	@Test
	public void testLengthOfYear() {
		LocalMonth lm = LocalMonth.of(1972, 2);
		assertEquals(366, lm.lengthOfYear());
	}
	
	@Test
	public void testMinusMonths() {
		LocalMonth lm = LocalMonth.of(1972, 2).minusMonths(25);
		assertEquals(1970, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testMinusYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).minusYear(2);
		assertEquals(1970, lm.getYear());
		assertEquals(2, lm.getMonthValue());
	}
	
	@Test
	public void testPlusMonths() {
		LocalMonth lm = LocalMonth.of(1972, 2).plusMonths(25);
		assertEquals(1974, lm.getYear());
		assertEquals(3, lm.getMonthValue());
	}
	
	@Test
	public void testPlusYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).plusYear(2);
		assertEquals(1974, lm.getYear());
		assertEquals(2, lm.getMonthValue());
	}
	
	@Test
	public void testNextMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).nextMonth();
		assertEquals(1972, lm.getYear());
		assertEquals(3, lm.getMonthValue());
	}
	
	@Test
	public void testPreviousMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).previousMonth();
		assertEquals(1972, lm.getYear());
		assertEquals(1, lm.getMonthValue());
	}
	
	@Test
	public void testMonthsUntil() {
		List<LocalMonth> l = LocalMonth.of(1972, 2).monthsUntil(LocalMonth.of(1972, 5)).collect(Collectors.toList());
		assertEquals(3, l.size());
		assertEquals("197202", l.get(0).getKey());
		assertEquals("197203", l.get(1).getKey());
		assertEquals("197204", l.get(2).getKey());
	}
	
	@Test
	public void testToEpochMonth() {
		assertEquals(0, LocalMonth.of(1970, 1).toEpochMonth());
		assertEquals(12, LocalMonth.of(1971, 1).toEpochMonth());
		assertEquals(-1, LocalMonth.of(1969, 12).toEpochMonth());
	}
	
	@Test
	public void testWithMonth() {
		LocalMonth lm = LocalMonth.of(1972, 2).withMonth(5);
		assertEquals(1972, lm.getYear());
		assertEquals(5, lm.getMonthValue());
	}
	
	@Test
	public void testWithYear() {
		LocalMonth lm = LocalMonth.of(1972, 2).withYear(2023);
		assertEquals(2023, lm.getYear());
		assertEquals(2, lm.getMonthValue());
	}

}
