/**
 * 
 */
package rsbaselib.util;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Default bean implementation for {@link ValueProvider}.
 * The provider returns the value of the given bean property or - if NULL - the defined NULL value
 * which is NULL by default.
 * The provider will return the bean itself when the bean property is set to NULL.
 * 
 * @author ralph
 *
 */
public class BeanValueProvider implements ValueProvider {

	/** the bean property to be retrieved from an object for display */
	private String beanProperty;
	/** the value to be used when the property is null */
	private Object nullValue = null;

	/**
	 * Constructor.
	 */
	public BeanValueProvider(String beanProperty) {
		setBeanProperty(beanProperty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object o) {
		try {
			if (o == null) return getNullValue();
			Object value = o;
			if (getBeanProperty() != null) value = PropertyUtils.getProperty(o, getBeanProperty());
			if (value == null) value = getNullValue();
			return value;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot retrieve value:", t);
		}
	}

	/**
	 * Returns the beanProperty.
	 * @return the beanProperty
	 */
	public String getBeanProperty() {
		return beanProperty;
	}

	/**
	 * Sets the beanProperty.
	 * @param beanProperty the beanProperty to set
	 */
	public void setBeanProperty(String beanProperty) {
		this.beanProperty = beanProperty;
	}

	/**
	 * Returns the nullValue.
	 * @return the nullValue
	 */
	public Object getNullValue() {
		return nullValue;
	}

	/**
	 * Sets the nullValue.
	 * @param nullValue the nullValue to set
	 */
	public void setNullValue(Object nullValue) {
		this.nullValue = nullValue;
	}

	/**
	 * Helper method to convert list of properties to value providers.
	 * @param beanProperties the bean properties
	 * @return array of value providers for the bean properties
	 */
	public static BeanValueProvider[] getValueProviders(String... beanProperties) {
		BeanValueProvider rc[] = new BeanValueProvider[beanProperties.length];
		for (int i=0; i<rc.length; i++) {
			rc[i] = new BeanValueProvider(beanProperties[i]);
		}
		return rc;
	}

}
