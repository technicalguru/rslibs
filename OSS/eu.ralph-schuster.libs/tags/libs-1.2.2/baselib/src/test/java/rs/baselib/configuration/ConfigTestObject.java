/**
 * 
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
