/*
 * This file is part of CSV/Excel Utility Package.
 *
 *  CSV/Excel Utility Package is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV/Excel Utility Package is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV/Excel Utility Package.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.mapper;

import csv.impl.csv.type.BooleanConversionHandler;
import csv.impl.csv.type.ByteConversionHandler;
import csv.impl.csv.type.CharConversionHandler;
import csv.impl.csv.type.DateConversionHandler;
import csv.impl.csv.type.DoubleConversionHandler;
import csv.impl.csv.type.FloatConversionHandler;
import csv.impl.csv.type.IntegerConversionHandler;
import csv.impl.csv.type.LongConversionHandler;
import csv.impl.csv.type.ShortConversionHandler;
import csv.impl.csv.type.ZonedDateTimeConversionHandler;

/**
 * Mappings required for string-based streams.
 * @author ralph
 *
 */
public class StringMappings extends AbstractMappingCollection {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void init() {
		add(BooleanConversionHandler.INSTANCE);
		add(ByteConversionHandler.INSTANCE);
		add(CharConversionHandler.INSTANCE);
		add(DoubleConversionHandler.INSTANCE);
		add(FloatConversionHandler.INSTANCE);
		add(IntegerConversionHandler.INSTANCE);
		add(LongConversionHandler.INSTANCE);
		add(ShortConversionHandler.INSTANCE);
		add(DateConversionHandler.INSTANCE);
		add(ZonedDateTimeConversionHandler.INSTANCE);
	}

}
