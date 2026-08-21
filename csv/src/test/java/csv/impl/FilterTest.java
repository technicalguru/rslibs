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
package csv.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import csv.TableReader;

/**
 * JUnit Test for JavaBeans.
 * @author RalphSchuster
 *
 */
public class FilterTest {
	private static final String FILE_NAME= "filter-test.csv";

	private static final int ROW_FILTER[] = new int[]{ 1, 3 };
	private static final int COLUMN_FILTER[] = new int[]{ 3, 0, 2 };
	
	private static final String TEST_HEADER[] = new String[] {
		"Column-0", "Column-1", "Column-2", "Column-3"
	};
	
	private static final String TEST_VALUES[][]= new String[][]{
		{"0:0", "0:1", "0:2", "0:3"},
		{"1:0", "1:1", "1:2", "1:3"},
		{"2:0", "2:1", "2:2", "2:3"},
		{"3:0", "3:1", "3:2", "3:3"},
	};
	
	private File fFile;

	/**
	 * Initializes file.
	 */
	@BeforeEach
	public void init() {
		fFile = new File(FILE_NAME);
	}

	/**
	 * Removes the file.
	 */
	@AfterEach
	public void done() {
		if ((fFile != null) && fFile.exists()) fFile.deleteOnExit();
	}
	
	/**
	 * The test method writes a XML file with beans.
	 * Test {@link #testRowFilter()} will
	 * compare the results with the original values.
	 */
	@Test
	public void testPlainReadWrite() {
		testWriteFile(false);
		testColumnFilter(false);
		testRowFilter();
	}
	
	protected void testWriteFile(boolean writeHeader) {
		CSVWriter out = null;
		try {
			// Write the file
			out = new CSVWriter(fFile);
			
			// Write Header
			if (writeHeader) out.printRow(TEST_HEADER);
			
			// Write Data
			for (int row= 0; row < TEST_VALUES.length; row++) {
				out.printRow(TEST_VALUES[row]);
			}
			assertTrue(fFile.exists());

		} catch (IOException e) {
			fail("IO exception: " + e.getMessage());
		} finally {
			out.close();
		}
	}

	/**
	 * This method checks if only filtered rows are returned.
	 */
	public void testRowFilter() {
		TableReader in = null;
		try {
			// Reader
			CSVReader reader = new CSVReader(fFile);
			reader.setHasHeaderRow(false);
			in = new IndexRowFilter(reader, ROW_FILTER);
			int row = 0;
			while (in.hasNext()) {
				Object columns[]= in.next();

				testRow(TEST_VALUES[ROW_FILTER[row]], columns);

				row++;
			}

			// compare size of rows
			assertTrue(row == ROW_FILTER.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail("May be a previous test has failed: " + e.getMessage());
		} finally {
			in.close();
		}
	}

	/**
	 * Checks that master and copy are equal
	 * @param master master data array
	 * @param copy copy data array to be compared
	 */
	protected void testRow(Object master[], Object copy[]) {
		// compare size of columns
		assertEquals(master.length, copy.length);
		for (int col=0; col<copy.length; col++) {
			if ((master[col] != null)) {
				assertNotNull(copy[col]);
			}
			if ((copy[col] != null)) {
				assertNotNull(master[col]);
			}
			assertEquals(master[col], copy[col]);
		}
	}

	protected void testColumnFilter(boolean hasHeader) {
		TableReader in = null;
		try {
			// Reader
			CSVReader reader = new CSVReader(fFile);
			reader.setHasHeaderRow(hasHeader);
			in = new DefaultColumnFilter(reader, COLUMN_FILTER);
			int row = 0;
			
			// Test header
			if (hasHeader) {
				Object headers[] = in.getHeaderRow();
				testColumnFilteredRow(TEST_HEADER, headers);
			}
			
			while (in.hasNext()) {
				Object columns[]= in.next();

				testColumnFilteredRow(TEST_VALUES[row], columns);

				row++;
			}

			// compare size of rows
			assertTrue(row == TEST_VALUES.length);

		} catch (Exception e) {
			e.printStackTrace();
			fail("May be a previous test has failed: " + e.getMessage());
		} finally {
			in.close();
		}
	}

	/**
	 * Checks that master and copy are equal
	 * @param master master data array
	 * @param copy copy data array to be compared
	 */
	protected void testColumnFilteredRow(Object master[], Object copy[]) {
		// compare size of columns
		assertEquals(master.length, copy.length); // Length is always original length
		for (int col=0; col<COLUMN_FILTER.length; col++) { // until column filter length only
			if ((master[COLUMN_FILTER[col]] != null)) {
				assertNotNull(copy[col]);
			}
			if ((copy[col] != null)) {
				assertNotNull(master[COLUMN_FILTER[col]]);
			}
			assertEquals(master[COLUMN_FILTER[col]], copy[col]);
		}
	}

	/**
	 * This method checks if a header can be written and read.
	 */
	@Test
	public void testHeader() {
		testWriteFile(true);
		testColumnFilter(true);
	}
	
}
