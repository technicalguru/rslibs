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
package rs.baselib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static rs.baselib.test.BuilderUtils.$City;

import org.junit.jupiter.api.Test;

import rs.baselib.type.City;
import rs.baselib.type.Continent;
import rs.baselib.type.Country;

/**
 * Tests the {@link CityBuilder}.
 * 
 * @author ralph
 *
 */
public class CityBuilderTest {

	@Test
	public void testDefault() {
		CityBuilder b = $City();
		for (int i=0; i<10000; i++) {
			City actual = b.build();
			assertNotNull(actual);
			assertNotNull(actual.getName());
			assertNotNull(actual.getCountry());
		}
	}

	@Test
	public void testWithContinent() {
		CityBuilder b = $City().withContinent(Continent.EUROPE);
		for (int i=0; i<10000; i++) {
			City actual = b.build();
			assertNotNull(actual);
			assertEquals(Continent.EUROPE, actual.getCountry().getContinent());
		}
	}


	@Test
	public void testWithCountry() {
		CityBuilder b = $City().withCountry(Country.GERMANY);
		for (int i=0; i<10000; i++) {
			City actual = b.build();
			assertNotNull(actual);
			assertEquals(Country.GERMANY, actual.getCountry());
		}
	}
}
