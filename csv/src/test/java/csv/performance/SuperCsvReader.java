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

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Reader for SuperCsv.
 * <p>Attention</p> SuperCsv is not able to read the original test file correctly. This might
 * be a mis-configuration only and needs to be revisited. There is no KPI figure at the moment.
 * @author ralph
 *
 */
public class SuperCsvReader implements IReader {

	/**
	 * Constructor.
	 */
	public SuperCsvReader() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "SuperCsv";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(File file, String charset) throws Exception {
		if (charset == null) charset = Charset.defaultCharset().name();
		ICsvListReader listReader = new CsvListReader(new InputStreamReader(new FileInputStream(file), charset), CsvPreference.STANDARD_PREFERENCE);

		listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
		final CellProcessor[] processors = getProcessors();

		int count = 0;
		try {
			while (listReader.read(processors) != null) {
				count++;
			}
		} finally {
			listReader.close();
		}
		return count;
	}

	private static CellProcessor[] getProcessors() {

		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
		};

		return processors;
	}
}
