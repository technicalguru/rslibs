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

import rs.baselib.io.ConverterUtils;
import rs.baselib.util.CommonUtils;

/**
 * Handles Float encryption.
 * @author ralph
 *
 */
public class CharEncryptedType extends AbstractLangEncryptionType {

	/**
	 * Constructor.
	 */
	public CharEncryptedType() {
		super(Character.class, Character.TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object convert(byte[] bytes) {
		try {
			return ConverterUtils.toCharObject(bytes);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert to character: "+CommonUtils.toString(bytes), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected byte[] convert(Object value) {
		try {
			return ConverterUtils.toBytes((Character)value);
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert string: "+value, e);
		}
	}

}
