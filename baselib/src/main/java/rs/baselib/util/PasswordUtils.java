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

import java.util.EnumSet;

/**
 * Provide some methods for password rule verifications.
 * 
 * @author ralph
 *
 */
public class PasswordUtils {

	/**
	 * Passord pattern rules (minimum occurrance of a type of character).
	 * 
	 * @author ralph
	 *
	 */
	public static enum Rule {
		/** Must contain at least one digit */
		DIGIT("(?=.*[0-9])"),
		/** Must contain at least one lower-case letter */
		LOWER_CASE("(?=.*[a-z])"),
		/** Must contain at least one upper-case letter */
		UPPER_CASE("(?=.*[A-Z])"),
		/** Must contain at least one special char of: {@code @ # $ % ^ & + = : ; , . &gt; &lt; " ' / ( ) [ ] { } * ~ - _ } */
		SPECIAL_CHAR("(?=.*[@#$%^&+=:;,\\.\\>\\<\"'\\/\\(\\)\\[\\]\\{\\}\\*\\~\\-_])");
		
		/** Complex rule set: at least 1 digit, 1 lower-case letter, 1 upper-case letter, 1 special char */
		public static EnumSet<Rule> COMPLEX = EnumSet.allOf(Rule.class);
		
		private String regex;
		
		private Rule(String regex) {
			this.regex = regex;
		}
		
		/**
		 * Returns the regular expression to verify the rule.
		 * @return the regular expression
		 */
		public String getRegex() {
			return regex;
		}
	}
	
	/**Regular expression to check that there is no whitespace */
    public static String REGEX_NO_SPACE     = "(?=\\S+$)";
    
    /**
     * Verify password rules with complex structure, 8-20 chars.
     * @param passwd the password to be verified
     * @return true when password fulfills these rules
     */
    public static boolean verify(String passwd) {
    	return passwd.matches(getPattern(8, 20, Rule.COMPLEX));
    }
    
    /**
     * Verify password rules with given rules, 8-20 chars.
     * @param passwd the password to be verified
     * @param rules the rules to apply
     * @return true when password fulfills these rules
     */
    public static boolean verify(String passwd, EnumSet<Rule> rules) {
    	return passwd.matches(getPattern(8, 20, rules));
    }
    
    /**
     * Verify password rules with complex structure, max. 20 chars.
     * @param passwd the password to be verified
     * @param min minimum number of characters (applied when greather than 0)
     * @return true when password fulfills these rules
     */
    public static boolean verify(String passwd, int min) {
    	return passwd.matches(getPattern(min, Math.max(min, 20), Rule.COMPLEX));
    }
    
    /**
     * Verify password rules with complex structure.
     * @param passwd the password to be verified
     * @param min minimum number of characters (applied when greather than 0)
     * @param max maximum number of characters (must be greater then min)
     * @return true when password fulfills these rules
     */
    public static boolean verify(String passwd, int min, int max) {
    	return passwd.matches(getPattern(min, max, Rule.COMPLEX));
    }
    
    /**
     * Verify password rules.
     * @param passwd the password to be verified
     * @param min minimum number of characters (applied when greather than 0)
     * @param max maximum number of characters (must be greater then min)
     * @param rules the rules to apply
     * @return true when password fulfills these rules
     */
    public static boolean verify(String passwd, int min, int max, EnumSet<Rule> rules) {
    	return passwd.matches(getPattern(min, max, rules));
    }
    
    /**
     * Returns regular expression to verify all rules for a password.
     * @param min minimum number of characters (applied when greather than 0)
     * @param max maximum number of characters (must be greater then min)
     * @param rules the rules to apply
     * @return
     */
    public static String getPattern(int min, int max, EnumSet<Rule> rules) {
    	String minMax = "";
    	if (min > 0) {
    		minMax = ".{" + min + ",";
        	if (max > min) minMax += max;
        	minMax += "}";
    	}
    	String pattern = "";
    	for (Rule rule : rules) pattern += rule.getRegex();
    	pattern += REGEX_NO_SPACE + minMax;
    	return pattern;
    }
}
