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

import java.lang.reflect.AnnotatedElement;

/**
 * Provides check methods for the {@link IgnoreTest} annotation.
 * 
 * @author ralph
 *
 */
public class IgnoreChecks {

	/**
	 * Returns true when the element has been annotated to be ignored
	 * for any of the mentioned test types.
	 * @param element the element to be checked
	 * @param testTypes the type of tests that shall be ignored (any must match). If null then
	 *                  the method will check if all tests are allowed.
	 * @return true when the element shall be ignored for given test types.
	 */
	public static boolean ignoreTest(AnnotatedElement element, String... testTypes) {
		return ignoreTest(element.getAnnotation(IgnoreTest.class), testTypes);
	}
	
	/**
	 * Returns true when the given annotation contains the any of the given test
	 * types. 
	 * @param ignoreTest the annotation
	 * @param testTypes test types that shall be checked. If null then the annotation
	 *                  will be checked if it ignores all tests.
	 * @return true if the annotation defines ignoring the given tests
	 */
	public static boolean ignoreTest(IgnoreTest ignoreTest, String... testTypes) {
		// There is no such annotation
		if (ignoreTest == null) return false;
		
		String annotationTypes[] = ignoreTest.value();
		
		// Check if all tests are ignored
		if ((annotationTypes == null) || (annotationTypes.length == 0)) return true;
		
		
		// Check if there is no specific test asked for (but annotation limits scope)
		if ((testTypes == null) || (testTypes.length == 0)) return false;
		
		// Now find just one match
		for (String s1 : annotationTypes) {
			for (String s2 : testTypes) {
				if (s1.equals(s2)) return true;
			}
		}
		
		// No match found.
		return false;
	}
}
