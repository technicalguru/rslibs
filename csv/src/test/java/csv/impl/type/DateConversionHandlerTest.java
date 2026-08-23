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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

import csv.impl.csv.type.DateConversionHandler;

/**
 * @author ralph
 *
 */
public class DateConversionHandlerTest {

	private static final String DATES[] = new String[] {
		"31/01/2011",
		"31.01.2011",
		"31/01/11",
		"31.01.11",
		"2011/01/31",
		"2011.01.31"
	};
	private static final Date TARGET_DATE = new Date(1296432000000L);
	
	/**
	 * Test method for {@link csv.impl.csv.type.DateConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		DateConversionHandler handler = new DateConversionHandler();
		handler.setTimezone(TimeZone.getTimeZone("UTC"));
		System.out.println(TARGET_DATE);
		for (String date : DATES) {
			assertEquals(TARGET_DATE, handler.fromStream(date));
		}
	}

	/**
	 * Test method for {@link csv.impl.csv.type.DateConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		DateConversionHandler handler = new DateConversionHandler();
		handler.setTimezone(TimeZone.getTimeZone("UTC"));
		assertEquals(DATES[0], handler.toStream(TARGET_DATE));
	}

}
