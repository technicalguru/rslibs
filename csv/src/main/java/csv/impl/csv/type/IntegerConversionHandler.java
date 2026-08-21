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
 * Conversion Handler for int.
 * @author ralph
 *
 */
public class IntegerConversionHandler implements TypeConverter {

	public static final TypeConverter INSTANCE = new IntegerConversionHandler();
	
	/**
	 * Constructor.
	 */
	public IntegerConversionHandler() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getTypes() {
		return new Class<?>[] { Integer.TYPE, Integer.class };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object fromStream(Object s) {
		return Integer.parseInt(s.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object toStream(Object o) {
		return o.toString();
	}

}
