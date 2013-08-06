/**
 * 
 */
package rs.data.impl.dto;

import java.io.Serializable;
import java.util.Properties;

/**
 * A DTO using a Properties object as storage.
 * @author ralph
 *
 */
public class PropertiesDTO<K extends Serializable> extends GeneralDTO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/** The properties object */
	private Properties properties;
	
	/**
	 * Constructor.
	 */
	public PropertiesDTO() {
	}

	/**
	 * Returns a property.
	 * @param name key of property.
	 * @return value of property
	 */
	public String getProperty(String name) {
		return properties.getProperty(name);
		
	}

	/**
	 * Sets a property.
	 * @param name key of property
	 * @param value new value of property
	 */
	public void setProperty(String name, String value) {
		properties.setProperty(name, value);
	}

	/**
	 * Returns the {@link #properties}.
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}
	
	
}
