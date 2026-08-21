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
package csv.performance;

import java.io.File;

import csv.impl.CSVReader;

/**
 * Performs reading with CSV/Excel Utility.
 * @author ralph
 *
 */
public class CsvExcelUtilityReader implements IReader {

	/**
	 * Constructor.
	 */
	public CsvExcelUtilityReader() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "eu.ralph-schuster.csv";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(File file, String charset) throws Exception {
		CSVReader reader = new CSVReader(file);
		if (charset != null) reader.setCharset(charset);
		reader.open();
		int lineCount = 0;
		while (reader.hasNext()) {
			reader.next();
			lineCount++;
		}
		reader.close();
		return lineCount;
	}

}
