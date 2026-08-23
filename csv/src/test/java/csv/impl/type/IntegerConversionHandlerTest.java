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

import org.junit.jupiter.api.Test;

import csv.impl.csv.type.IntegerConversionHandler;

/**
 * @author ralph
 *
 */
public class IntegerConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.csv.type.IntegerConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		IntegerConversionHandler handler = new IntegerConversionHandler();
		assertEquals(2, handler.fromStream("2"));
		assertEquals(20, handler.fromStream("20"));
		assertEquals(-2, handler.fromStream("-2"));
	}

	/**
	 * Test method for {@link csv.impl.csv.type.IntegerConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		IntegerConversionHandler handler = new IntegerConversionHandler();
		assertEquals("2", handler.toStream(2));
		assertEquals("20", handler.toStream(20));
		assertEquals("-2", handler.toStream(-2));
	}

}
