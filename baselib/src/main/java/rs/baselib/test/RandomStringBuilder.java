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

import rs.baselib.crypto.EncryptionUtils;

/**
 * A random string builder.
 * @author ralph
 *
 */
public class RandomStringBuilder implements Builder<String> {

	private String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private int length   = 10;
	
	/**
	 * Constructor.
	 */
	public RandomStringBuilder() {
	}

	/**
	 * Set the allowed chars (default [A-Za-z0-9])
	 * @param chars - the allowed chars
	 * @return this object for concatenation.
	 */
	public RandomStringBuilder withChars(String chars) {
		this.chars = chars;
		return this;
	}
	
	/**
	 * Set the length of the strings to return.
	 * @param length - the length
	 * @return this object for concatenation.
	 */
	public RandomStringBuilder withLength(int length) {
		this.length = length;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String build() {
		return EncryptionUtils.generatePassword(chars, System.currentTimeMillis(), length);
	}

}
