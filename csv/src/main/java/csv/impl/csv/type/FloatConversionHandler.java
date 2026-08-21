/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.impl.csv.type;

import csv.mapper.TypeConverter;

/**
 * Conversion Handler for float.
 * @author ralph
 *
 */
public class FloatConversionHandler implements TypeConverter {

	public static final TypeConverter INSTANCE = new FloatConversionHandler();
	
	/**
	 * Constructor.
	 */
	public FloatConversionHandler() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Float.TYPE, Float.class };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object fromStream(Object s) {
		return Float.parseFloat(s.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object toStream(Object o) {
		return o.toString();
	}

}
