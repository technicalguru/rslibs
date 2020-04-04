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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests the {@link IgnoreChecks} class if it correctly identifies
 * {@link IgnoreTest} annotations.
 * 
 * @author ralph
 *
 */
public class IgnoreChecksTest {

	/**
	 * Tests that a method is not annotated for any ignore.
	 * @see IgnoreChecks#ignoreTest(java.lang.reflect.AnnotatedElement, java.lang.String[])
	 */
	@Test
	public void testNoIgnoreTest() {
		try {
			Map<String,Method> m = getTestMethods();
			assertFalse("Method shall not be annotated", IgnoreChecks.ignoreTest(m.get("notAnnotated")));
			assertFalse("Method shall not be annotated", IgnoreChecks.ignoreTest(m.get("notAnnotated"), "first"));
			assertFalse("Method shall not be annotated", IgnoreChecks.ignoreTest(m.get("notAnnotated"), "first", "second"));
			assertFalse("Method shall not be annotated", IgnoreChecks.ignoreTest(m.get("notAnnotated"), "third"));
		} catch (NoSuchMethodException e) {
			fail("Cannot test");
		}
	}

	/**
	 * Tests that a method is annotated for all tests.
	 * @see IgnoreChecks#ignoreTest(java.lang.reflect.AnnotatedElement, java.lang.String[])
	 */
	@Test
	public void testAllIgnoreTest() {
		try {
			Map<String,Method> m = getTestMethods();
			assertTrue("Method shall be fully annotated", IgnoreChecks.ignoreTest(m.get("fullyAnnotated")));
			assertTrue("Method shall be fully annotated", IgnoreChecks.ignoreTest(m.get("fullyAnnotated"), "first"));
			assertTrue("Method shall be fully annotated", IgnoreChecks.ignoreTest(m.get("fullyAnnotated"), "first", "second"));
			assertTrue("Method shall be fully annotated", IgnoreChecks.ignoreTest(m.get("fullyAnnotated"), "third"));
		} catch (NoSuchMethodException e) {
			fail("Cannot test");
		}
	}

	/**
	 * Tests that a method is annotated for a single test "first".
	 * @see IgnoreChecks#ignoreTest(java.lang.reflect.AnnotatedElement, java.lang.String[])
	 */
	@Test
	public void testSingleIgnoreTest() {
		try {
			Map<String,Method> m = getTestMethods();
			assertFalse("Method shall be annotated for \"first\" only", IgnoreChecks.ignoreTest(m.get("ignoreFirstAnnotated")));
			assertTrue("Method shall be annotated for \"first\" only", IgnoreChecks.ignoreTest(m.get("ignoreFirstAnnotated"), "first"));
			assertTrue("Method shall be annotated for \"first\" only", IgnoreChecks.ignoreTest(m.get("ignoreFirstAnnotated"), "first", "second"));
			assertFalse("Method shall be annotated for \"first\" only", IgnoreChecks.ignoreTest(m.get("ignoreFirstAnnotated"), "third"));
		} catch (NoSuchMethodException e) {
			fail("Cannot test");
		}
	}

	/**
	 * Tests that a method is annotated for two tests, "first" and "second".
	 * @see IgnoreChecks#ignoreTest(java.lang.reflect.AnnotatedElement, java.lang.String[])
	 */
	@Test
	public void testMultipleIgnoreTest() {
		try {
			Map<String,Method> m = getTestMethods();
			assertFalse("Method shall be annotated for \"first\" and \"second\"", IgnoreChecks.ignoreTest(m.get("ignoreFirstSecondAnnotated")));
			assertTrue("Method shall be annotated for \"first\" and \"second\"", IgnoreChecks.ignoreTest(m.get("ignoreFirstSecondAnnotated"), "first"));
			assertTrue("Method shall be annotated for \"first\" and \"second\"", IgnoreChecks.ignoreTest(m.get("ignoreFirstSecondAnnotated"), "second"));
			assertTrue("Method shall be annotated for \"first\" and \"second\"", IgnoreChecks.ignoreTest(m.get("ignoreFirstSecondAnnotated"), "first", "second"));
			assertFalse("Method shall be annotated for \"first\" and \"second\"", IgnoreChecks.ignoreTest(m.get("ignoreFirstSecondAnnotated"), "third"));
		} catch (NoSuchMethodException e) {
			fail("Cannot test");
		}
	}

	/**
	 * Returns all methods defined for annotation test.
	 * @return the four test methods
	 * @throws NoSuchMethodException if some problem occurs
	 */
	protected Map<String, Method> getTestMethods() throws NoSuchMethodException {
		Map<String,Method> rc = new HashMap<String, Method>();
		rc.put("notAnnotated", IgnoreChecksTest.class.getMethod("notAnnotated"));
		rc.put("fullyAnnotated", IgnoreChecksTest.class.getMethod("fullyAnnotated"));
		rc.put("ignoreFirstAnnotated", IgnoreChecksTest.class.getMethod("ignoreFirstAnnotated"));
		rc.put("ignoreFirstSecondAnnotated", IgnoreChecksTest.class.getMethod("ignoreFirstSecondAnnotated"));
		return rc;
	}
	
	/**
	 * Method not annotated.
	 * The method is used for unit-testing the annotation.
	 */
	public void notAnnotated() {
		
	}

	/**
	 * Method annotated to ignore all tests.
	 * The method is used for unit-testing the annotation.
	 */
	@IgnoreTest
	public void fullyAnnotated() {
		
	}
	
	/**
	 * Method annotated to ignore the first test.
	 * The method is used for unit-testing the annotation.
	 */
	@IgnoreTest("first")
	public void ignoreFirstAnnotated() {
		
	}
	
	
	/**
	 * Method annotated to ignore the first and second test.
	 * The method is used for unit-testing the annotation.
	 */
	@IgnoreTest({"first", "second"})
	public void ignoreFirstSecondAnnotated() {
		
	}
	
	
}
