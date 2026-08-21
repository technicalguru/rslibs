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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Reader for Commons CSV.
 * @author ralph
 *
 */
public class CommonsCsvReader implements IReader {

	/**
	 * Constructor.
	 */
	public CommonsCsvReader() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Commons CSV";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(File file, String charset) throws Exception {
		if (charset == null) charset = Charset.defaultCharset().name();
		CSVParser parser = CSVFormat.RFC4180.parse(new InputStreamReader(new FileInputStream(file), charset));
		int count = 0;
		Iterator<CSVRecord> i = parser.iterator();
		while (i.hasNext()) {
			i.next();
			count++;
		}
		parser.close();
		return count;
	}

}
