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
package rs.baselib.licensing;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rs.baselib.util.DefaultComparator;

/**
 * Abstract implementation of a license.
 * @author ralph
 *
 */
public abstract class AbstractLicense implements ILicense {

	/**
	 * Seria ID.
	 */
	private static final long serialVersionUID = 3018690737456535564L;
	
	/** The values */
	private Map<String, Object> properties;

	/**
	 * Constructor.
	 */
	public AbstractLicense() {
		properties = new HashMap<String, Object>();
	}

	/**
	 * Sets the property of the license.
	 */
	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	/**
	 * Sets the property of the license.
	 */
	public Object getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * Returns whether the property is set.
	 */
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	/**
	 * Sets the property of the license.
	 */
	public Object removeProperty(String key) {
		return properties.remove(key);
	}

	/**
	 * Returns all property names.
	 */
	public Iterable<String> getPropertyNames() {
		return Collections.unmodifiableSet(properties.keySet());
	}
	
	/**
	 * Initializes the given property from the context.
	 * @param key key to be used in context and property map
	 * @param context context to initialize from
	 * @return value found
	 */
	protected Object initProperty(String key, ILicenseContext context) {
		if (context.hasKey(key)) {
			Object value = context.get(key);
			setProperty(key, value);
			return value;
		}
		return null;
	}
	
	/**
	 * Verifies that the given value of this license is equal to the property in the context.
	 * @param key key of property
	 * @param context context containing the comparison value
	 * @param failIfMissing fail the verification when this license has no such value
	 */
	protected void verifyEquals(String key, ILicenseContext context, boolean failIfMissing) {
		Object myValue = getProperty(key);
		Object verificationValue = context.get(key);
		if (myValue != null) {
			if (!verificationValue.equals(myValue)) {
				throw new LicenseException(key+" is invalid");
			}
		} else if (failIfMissing) {
			throw new LicenseException(key+" is missing");
		}
	}
	
	/**
	 * Verifies that the given value of this license is greater than the property in the context.
	 * @param key key of property
	 * @param context context containing the comparison value
	 * @param failIfMissing fail the verification when this license has no such value
	 */
	protected void verifyGreaterThan(String key, ILicenseContext context, boolean failIfMissing) {
		Object myValue = getProperty(key);
		Object verificationValue = context.get(key);
		if (myValue != null) {
			if (DefaultComparator.INSTANCE.compare(myValue, verificationValue) <= 0) {
				throw new LicenseException(key+" is invalid");
			}
		} else if (failIfMissing) {
			throw new LicenseException(key+" is missing");
		}
	}
	
	/**
	 * Verifies that the given value of this license is greater than or equal to the property in the context.
	 * @param key key of property
	 * @param context context containing the comparison value
	 * @param failIfMissing fail the verification when this license has no such value
	 */
	protected void verifyEqualGreaterThan(String key, ILicenseContext context, boolean failIfMissing) {
		Object myValue = getProperty(key);
		Object verificationValue = context.get(key);
		if (myValue != null) {
			if (DefaultComparator.INSTANCE.compare(myValue, verificationValue) < 0) {
				throw new LicenseException(key+" is invalid");
			}
		} else if (failIfMissing) {
			throw new LicenseException(key+" is missing");
		}
	}
	
	/**
	 * Verifies that the given value of this license is less than the property in the context.
	 * @param key key of property
	 * @param context context containing the comparison value
	 * @param failIfMissing fail the verification when this license has no such value
	 */
	protected void verifyLessThan(String key, ILicenseContext context, boolean failIfMissing) {
		Object myValue = getProperty(key);
		Object verificationValue = context.get(key);
		if (myValue != null) {
			if (DefaultComparator.INSTANCE.compare(myValue, verificationValue) >= 0) {
				throw new LicenseException(key+" is invalid");
			}
		} else if (failIfMissing) {
			throw new LicenseException(key+" is missing");
		}
	}

	/**
	 * Verifies that the given value of this license is less than or equal to the property in the context.
	 * @param key key of property
	 * @param context context containing the comparison value
	 * @param failIfMissing fail the verification when this license has no such value
	 */
	protected void verifyEqualLessThan(String key, ILicenseContext context, boolean failIfMissing) {
		Object myValue = getProperty(key);
		Object verificationValue = context.get(key);
		if (myValue != null) {
			if (DefaultComparator.INSTANCE.compare(myValue, verificationValue) > 0) {
				throw new LicenseException(key+" is invalid");
			}
		} else if (failIfMissing) {
			throw new LicenseException(key+" is missing");
		}
	}

}
