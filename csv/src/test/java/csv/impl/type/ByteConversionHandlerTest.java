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

import csv.impl.csv.type.ByteConversionHandler;

/**
 * @author ralph
 *
 */
public class ByteConversionHandlerTest {

	/**
	 * Test method for {@link csv.impl.csv.type.ByteConversionHandler#fromStream(java.lang.String)}.
	 */
	@Test
	public void testToObject() {
		ByteConversionHandler handler = new ByteConversionHandler();
		assertEquals((byte)'\u0020', handler.fromStream("32"));
		assertEquals((byte)'A', handler.fromStream("65"));
	}

	/**
	 * Test method for {@link csv.impl.csv.type.ByteConversionHandler#toStream(java.lang.Object)}.
	 */
	@Test
	public void testToStringObject() {
		ByteConversionHandler handler = new ByteConversionHandler();
		assertEquals("65", handler.toStream((byte)'A'));
		assertEquals("32", handler.toStream((byte)'\u0020'));
	}

}
