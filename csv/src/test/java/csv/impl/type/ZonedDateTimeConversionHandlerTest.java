/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.impl.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

import csv.impl.csv.type.ZonedDateTimeConversionHandler;

/**
 * @author ralph
 *
 */
public class ZonedDateTimeConversionHandlerTest {

	private static final String DATES[] = new String[] {
		"31/01/2011 00:00",
		"31.01.2011 00:00:00",
		"31/01/11 00:00:00.0",
		"31.01.11 00:00",
		"2011/01/31 00:00:00",
		"2011.01.31 00:00:00.0",
		"2011-01-31T00:00:00+00:00",
	};
	private static final ZonedDateTime TARGET_DATE = ZonedDateTime.of(2011, 1, 31, 0, 0, 0, 0, ZoneId.of("UTC"));
	
	/**
	 * Test method for {@link csv.impl.csv.type.DateConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		ZonedDateTimeConversionHandler handler = getHandler();
		System.out.println(TARGET_DATE);
		for (String date : DATES) {
			assertEquals(TARGET_DATE.toInstant().toEpochMilli(), ((ZonedDateTime)handler.fromStream(date)).toInstant().toEpochMilli());
		}
	}

	/**
	 * Test method for {@link csv.impl.csv.type.DateConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		ZonedDateTimeConversionHandler handler = getHandler();
		assertEquals("31.01.2011 00:00:00", handler.toStream(TARGET_DATE));
	}

	protected ZonedDateTimeConversionHandler getHandler() {
		ZonedDateTimeConversionHandler handler = new ZonedDateTimeConversionHandler();
		handler.setPrintFormat("dd.MM.yyyy HH:mm:ss");
		handler.setZoneId(ZoneId.of("UTC"));
		return handler;
	}
}
