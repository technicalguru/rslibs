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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the {@link StreetBuilder}
 * 
 * @author ralph
 *
 */
public class StreetBuilderTest {

	@Test
	public void testDefault() {
		StreetBuilder b = new StreetBuilder();
		for (int i=0; i<1000; i++) {
			String s = b.build();
			assertTrue(isEnglishFormat(s));
			assertTrue(s.indexOf(" ") > 0);
		}
	}
	
	@Test
	public void testFormat() {
		StreetBuilder b = new StreetBuilder().withEnglishFormat(false);
		for (int i=0; i<1000; i++) {
			String s = b.build();
			assertTrue(isGermanFormat(s));
		}
	}

	@Test
	public void testExtensions() {
		StreetBuilder b = new StreetBuilder().withEnglishFormat(false);
		int cnt = 0;
		for (int i=0; i<1000; i++) {
			String s = b.build();
			if (!Character.isDigit(s.charAt(s.length()-1))) cnt++;
		}
		assertTrue(cnt > 10);
		assertTrue(cnt < 200);
	}
	
	@Test
	public void testWithoutExtensions() {
		StreetBuilder b = new StreetBuilder().withEnglishFormat(false).withoutNumberExtensions();
		for (int i=0; i<1000; i++) {
			String s = b.build();
			assertTrue(Character.isDigit(s.charAt(s.length()-1)));
		}
	}
	
	@Test
	public void testWithoutNumbers() {
		StreetBuilder b = new StreetBuilder().withoutNumbers();
		for (int i=0; i<1000; i++) {
			String s = b.build();
			assertTrue(!Character.isDigit(s.charAt(0)));
		}
	}
	
	private static boolean isEnglishFormat(String s) {
		return Character.isDigit(s.charAt(0));
	}
	
	private static boolean isGermanFormat(String s) {
		return Character.isDigit(s.charAt(s.lastIndexOf(" ")+1));
	}
}
