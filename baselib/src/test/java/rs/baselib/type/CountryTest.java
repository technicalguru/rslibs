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

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Simple Test to ensure that continental sets are produced correctly.
 * @author ralph
 *
 */
public class CountryTest {

	@Test
	public void testContinents() {
		assertFalse(Country.COUNTRIES_AFRICA.isEmpty());
		assertFalse(Country.COUNTRIES_ANTARCTICA.isEmpty());
		assertFalse(Country.COUNTRIES_ASIA.isEmpty());
		assertFalse(Country.COUNTRIES_AUSTRALIA.isEmpty());
		assertFalse(Country.COUNTRIES_EUROPE.isEmpty());
		assertFalse(Country.COUNTRIES_NORTH_AMERICA.isEmpty());
		assertFalse(Country.COUNTRIES_SOUTH_AMERICA.isEmpty());
	}
}
