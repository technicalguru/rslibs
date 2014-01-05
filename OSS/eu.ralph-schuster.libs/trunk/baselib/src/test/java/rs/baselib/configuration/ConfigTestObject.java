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
package rs.baselib.configuration;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Test Object for configuration.
 * 
 * @author ralph
 *
 */
public class ConfigTestObject implements IConfigurable {

	private String stringValue;
	private int intValue;

	/**
	 * Constructor.
	 */
	public ConfigTestObject() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		String s = cfg.getString("name(0)", null);
		setStringValue(s);
		int i = cfg.getInt("int(0)", 0);
		setIntValue(i);
	}

	/**
	 * Returns the {@link #stringValue}.
	 * @return the stringValue
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Sets the {@link #stringValue}.
	 * @param stringValue the stringValue to set
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Returns the {@link #intValue}.
	 * @return the intValue
	 */
	public int getIntValue() {
		return intValue;
	}

	/**
	 * Sets the {@link #intValue}.
	 * @param intValue the intValue to set
	 */
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	
}
