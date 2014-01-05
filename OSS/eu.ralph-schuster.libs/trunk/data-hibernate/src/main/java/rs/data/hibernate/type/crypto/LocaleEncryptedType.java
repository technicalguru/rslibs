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

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * Handles Long encryption.
 * @author ralph
 *
 */
public class LocaleEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public LocaleEncryptedType() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return Locale.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) throws UnsupportedEncodingException {
		//return ConverterUtils.toLocale(bytes);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) throws UnsupportedEncodingException {
		//return ConverterUtils.toBytes((Locale)value);
		return null;
	}

}
