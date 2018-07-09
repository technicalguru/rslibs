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

import java.math.BigDecimal;

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.CommonUtils;

/**
 * Handles BigDecimal encryption.
 * @author ralph
 * @since 1.3.2
 *
 */
public class BigDecimalEncryptedType extends AbstractEncryptionType {

	/**
	 * Constructor.
	 */
	public BigDecimalEncryptedType() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> returnedClass() {
		return BigDecimal.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		try {
			return ConverterUtils.toBigDecimal(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert to BigDecimal: "+CommonUtils.toString(bytes), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		try {
			return ConverterUtils.toBytes((BigDecimal)value);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert BigDecimal: "+value, e);
		}
	}

}
