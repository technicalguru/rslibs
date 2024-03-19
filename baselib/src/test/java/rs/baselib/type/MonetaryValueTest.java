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
package rs.baselib.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Currency;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link MonetaryValue} class.
 * @author ralph
 *
 */
public class MonetaryValueTest {

	@Test
	public void testToString1() {
		MonetaryValue value = new MonetaryValue("12.23", Currency.getInstance("EUR"));
		assertEquals("12.23 €", value.toString());
	}
	
	@Test
	public void testToString2() {
		MonetaryValue value = new MonetaryValue("12.233", Currency.getInstance("EUR"));
		assertEquals("12.23 €", value.toString());
	}
	
	@Test
	public void testToString3() {
		MonetaryValue value = new MonetaryValue("-12.233", Currency.getInstance("EUR"));
		assertEquals("-12.23 €", value.toString());
	}
	
	@Test
	public void testToString4() {
		MonetaryValue value = new MonetaryValue("-0.233", Currency.getInstance("EUR"));
		assertEquals("-0.23 €", value.toString());
	}
	
	@Test
	public void testToString5() {
		MonetaryValue value = new MonetaryValue("0.00", Currency.getInstance("EUR"));
		assertEquals("0.00 €", value.toString());
	}
	
	@Test
	public void testToString6() {
		MonetaryValue value = MonetaryValue.zero(Currency.getInstance("EUR"));
		assertEquals("0.00 €", value.toString());
	}
	
	@Test
	public void testToString7() {
		MonetaryValue value = new MonetaryValue(3.0, Currency.getInstance("EUR"));
		assertEquals("3.00 €", value.toString());
	}
	
	@Test
	public void testToString8() {
		MonetaryValue value = new MonetaryValue(-2.3, Currency.getInstance("EUR"));
		assertEquals("-2.30 €", value.toString());
	}
	
	@Test
	public void testToString9() {
		MonetaryValue value = new MonetaryValue(-0.011, Currency.getInstance("EUR"));
		assertEquals("-0.01 €", value.toString());
	}
	
	@Test
	public void testAdd() {
		MonetaryValue value = new MonetaryValue(5.23, Currency.getInstance("EUR")).add(0.005);
		assertEquals(new MonetaryValue(5.24, Currency.getInstance("EUR")), value);
	}

	
	@Test
	public void testSubtract() {
		MonetaryValue value = new MonetaryValue(5.23, Currency.getInstance("EUR")).subtract(0.005);
		assertEquals(new MonetaryValue(5.22, Currency.getInstance("EUR")), value);
	}

	@Test
	public void testMultiply() {
		MonetaryValue value = new MonetaryValue(5.23, Currency.getInstance("EUR")).multiply(0.5);
		assertEquals(new MonetaryValue(2.62, Currency.getInstance("EUR")), value);
	}

	@Test
	public void testDivide() {
		MonetaryValue value = new MonetaryValue(5.23, Currency.getInstance("EUR")).divide(2);
		assertEquals(new MonetaryValue(2.62, Currency.getInstance("EUR")), value);
	}

}
