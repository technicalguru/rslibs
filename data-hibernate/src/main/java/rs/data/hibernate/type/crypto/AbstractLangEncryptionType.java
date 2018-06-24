/*
 * This file is part of RS Library (Data Hibernate Library).
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
package rs.data.hibernate.type.crypto;

import java.util.Properties;

/**
 * Handles automatically object and simple language types.
 * @author ralph
 *
 */
public abstract class AbstractLangEncryptionType extends AbstractEncryptionType {

	private Class<?> objectType;
	private Class<?> simpleType;
	private boolean simple;
	
	/**
	 * Constructor.
	 * @param objectType - The object type of the simple type
	 * @param simpleType - the simple type
	 */
	public AbstractLangEncryptionType(Class<?> objectType, Class<?> simpleType) {
		this.objectType = objectType;
		this.simpleType = simpleType;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return simple ? simpleType : objectType;
	}

	/**
	 * Returns true if this is a simple type mapper.
	 * @return true or false
	 */
	protected boolean isSimple() {
		return simple;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameterValues(Properties parameters) {
		super.setParameterValues(parameters);
		String s = null;
		if (parameters != null) parameters.getProperty("simple");
		if (s == null) s = "true";
		simple = Boolean.parseBoolean(s);
	}
}
