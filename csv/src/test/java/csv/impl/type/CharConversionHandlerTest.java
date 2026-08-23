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

import csv.impl.csv.type.CharConversionHandler;

/**
 * @author ralph
 *
 */
public class CharConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.csv.type.CharConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		CharConversionHandler handler = new CharConversionHandler();
		assertEquals(' ', handler.fromStream(" "));
		assertEquals('A', handler.fromStream("A"));
	}

	/**
	 * Test method for {@link csv.impl.csv.type.CharConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		CharConversionHandler handler = new CharConversionHandler();
		assertEquals(" ", handler.toStream(' '));
		assertEquals("A", handler.toStream('A'));
	}

}
