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
import java.io.Reader;
import java.nio.charset.Charset;

import org.skife.csv.CSVReader;
import org.skife.csv.ReaderCallback;
import org.skife.csv.SimpleReader;

/**
 * Reader for Skife CSV.
 * @author ralph
 *
 */
public class SkifeCsvReader implements IReader {

	/**
	 * Constructor.
	 */
	public SkifeCsvReader() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "Skife CSV";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(File file, String charset) throws Exception {
		CSVReader reader = new SimpleReader();
		if (charset == null) charset = Charset.defaultCharset().name();

		Reader in = null;
		final int[] count = {0};
		try {
			in = new InputStreamReader(new FileInputStream(file), charset);

			reader.parse(in, new ReaderCallback() {
				public void onRow(String[] fields) {
					count[0]++;
				}
			});
		} finally {
			in.close();
		}
		return count[0];
	}

}
