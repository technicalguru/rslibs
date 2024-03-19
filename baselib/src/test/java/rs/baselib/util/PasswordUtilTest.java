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
package rs.baselib.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import rs.baselib.util.PasswordUtils.Rule;

/**
 * Tests the {@link PasswordUtils}.
 * 
 * @author ralph
 *
 */
public class PasswordUtilTest {

	@Test
	public void testDigit() {
		assertTrue(PasswordUtils.verify("password1", 8, 20, EnumSet.of(Rule.DIGIT)));
	}

	@Test
	public void testLowerCase() {
		assertTrue(PasswordUtils.verify("PASSWOrD", 8, 20, EnumSet.of(Rule.LOWER_CASE)));
	}

	@Test
	public void testUpperCase() {
		assertTrue(PasswordUtils.verify("passWord", 8, 20, EnumSet.of(Rule.UPPER_CASE)));
	}

	@Test
	public void testSpecial() {
		assertTrue(PasswordUtils.verify("pass.word", 8, 20, EnumSet.of(Rule.SPECIAL_CHAR)));
	}

	@Test
	public void testNoRule() {
		assertTrue(PasswordUtils.verify("password", 8, 20, EnumSet.noneOf(Rule.class)));
	}


	@Test
	public void testComplexRule() {
		assertTrue(PasswordUtils.verify("Pass.Word1", 8, 20, Rule.COMPLEX));
	}
	
	@Test
	public void testDigitFail() {
		assertFalse(PasswordUtils.verify("password", 8, 20, EnumSet.of(Rule.DIGIT)));
	}

	@Test
	public void testLowerCaseFail() {
		assertFalse(PasswordUtils.verify("PASSWORD", 8, 20, EnumSet.of(Rule.LOWER_CASE)));
	}

	@Test
	public void testUpperCaseFail() {
		assertFalse(PasswordUtils.verify("password", 8, 20, EnumSet.of(Rule.UPPER_CASE)));
	}

	@Test
	public void testSpecialFail() {
		assertFalse(PasswordUtils.verify("Password1", 8, 20, Rule.COMPLEX));
	}

	@Test
	public void testSpaceFail1() {
		assertFalse(PasswordUtils.verify("pass wor", 8, 20, EnumSet.noneOf(Rule.class)));
	}

	@Test
	public void testSpaceFail2() {
		assertFalse(PasswordUtils.verify("pass\twor", 8, 20, EnumSet.noneOf(Rule.class)));
	}

	@Test
	public void testComplexRuleFail() {
		assertFalse(PasswordUtils.verify("PassWord1", 8, 20, Rule.COMPLEX));
	}

	@Test
	public void testMinFail() {
		assertFalse(PasswordUtils.verify("Pass.Word1", 12, 20, Rule.COMPLEX));
	}

	@Test
	public void testMaxFail() {
		assertFalse(PasswordUtils.verify("Pass.Word1", 8, 9, Rule.COMPLEX));
	}

}
