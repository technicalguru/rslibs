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

import org.hibernate.MappingException;

import rs.baselib.io.ConverterUtils;
import rs.baselib.lang.LangUtils;

/**
 * Reads enumerations.
 * @author ralph
 *
 */
public class EnumEncryptedType extends AbstractEncryptionType {

	/**
	 * The class implementing the enumeration
	 */
	@SuppressWarnings("rawtypes")
	private Class<Enum> clazz = null; 

	/**
	 * Constructor
	 */
	public EnumEncryptedType() { 
	} 

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Class<?> returnedClass() { 
		return clazz; 
	} 

	/**
	 * 
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object convert(byte[] bytes) throws Exception {
		return Enum.valueOf(clazz, ConverterUtils.toString(bytes));
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws Exception {
		return ConverterUtils.toBytes(((Enum<?>)value).name());
	}

	/**
	 * {@inheritDoc}
	 */
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	 public void setParameterValues(Properties parameters) {
		 super.setParameterValues(parameters);
		 String className = (String)parameters.get("class");
		 if (className == null) {
			 throw new MappingException("class parameter not specified");
		 }

		 try {
			 clazz = (Class<Enum>)LangUtils.forName(className);
		 } catch (ClassNotFoundException e) {
			 throw new MappingException("Cannot find class: "+className, e);
		 }
	 }

}
