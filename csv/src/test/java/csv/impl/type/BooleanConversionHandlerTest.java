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

import csv.impl.csv.type.BooleanConversionHandler;

/**
 * @author ralph
 *
 */
public class BooleanConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.csv.type.BooleanConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		BooleanConversionHandler handler = new BooleanConversionHandler();
		assertEquals(true, handler.fromStream("true"));
		assertEquals(false, handler.fromStream("yes"));
		assertEquals(false, handler.fromStream("false"));
	}

	/**
	 * Test method for {@link csv.impl.csv.type.BooleanConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		BooleanConversionHandler handler = new BooleanConversionHandler();
		assertEquals("true", handler.toStream(Boolean.TRUE));
		assertEquals("false", handler.toStream(Boolean.FALSE));
		
	}

}
