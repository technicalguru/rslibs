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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.baselib.io.FileFinder;

/**
 * JUnit Test for Excel.
 * @author RalphSchuster
 *
 */
public class ExcelWriterReaderTest {
	private static final String FILE_NAME= "excel-test.xls";
	private static final String FILE_MULTISHEET_XLS = "multisheet.xls";
	private static final String FILE_SKIPPEDLINES_XLSX = "skippedlines.xlsx";
	private static final String FILE_EXTERNAL_SHEET_REF = "external_sheet_ref.xls";

	private static final String TEST_HEADER[] = new String[] {
		"Column-0", "Column-1", "Column-2", "Column-3"
	};

	private static final String TEST_VALUES[][]= new String[][]{
		// normal row
		{"0:0", "0:1", "0:2", "0:3"},
		// col delimiter
		{"1:0", "1:1", "1;2", "1:3"},
		// mixed quotes
		{"2:0 \"End of first row\"", "2:1 \"", " \" 2:2\" ", " \"\"\"2:3 "},
		// row delimiter
		{"\n3:0", "3:1\n2.Zeile", "3:2\n\n3.Zeile", "\n\n3:3\n4.Zeile"},
		// encoding
		{"š  ť  ž  ľ  č  ě ď ň ř ů ĺ Š Ť Ž Ľ Č Ě Ď Ň Ř Ů Ĺ", "ł ą ż ę ć ń ś ź Ł Ą Ż Ę Ć Ń Ś Ź", "Ă ă Ş ş Ţ ţ", " š č ž ć đ  Š Č Ž Ć Đ", " Ő ő Ű ű", "Ä, ä, Ö, ö, Ü, ü, ß",
			"абвгдеёжзийклмно прстуфхцчшчьыъэюя АБВГДЕЁЖЗИЙ КЛМНОПРСТУФ ХЦЧШЩЬЫЪЭЮЯ", "Ў ў Є є Ґ ґ", "Ђ Љ Њ Ћ Џ ђ љ њ ћ џ"}};

	private File fFile;
	private URL multisheetUrl;
	private URL skippedLinesUrl;
	private URL externalSheetRefUrl;

	/**
	 * Initializes file.
	 */
	@BeforeEach
	public void init() {
		fFile= new File(FILE_NAME);
		multisheetUrl = FileFinder.find(FILE_MULTISHEET_XLS);
		skippedLinesUrl = FileFinder.find(FILE_SKIPPEDLINES_XLSX);
		externalSheetRefUrl = FileFinder.find(FILE_EXTERNAL_SHEET_REF);
	}

	/**
	 * Removes the file.
	 */
	@AfterEach
	public void done() {
		if ((fFile != null) && fFile.exists()) fFile.deleteOnExit();
	}

	/**
	 * The test method writes a CSV file.
	 * Test {@link #testWrittenValues(boolean)} will
	 * compare the results with the original values.
	 */
	@Test
	public void testPlainReadWrite() {
		testWriteFile(false);
		testWrittenValues(false);
	}

	/**
	 * Writes the test file.
	 * @param withHeader whether header shall be written.
	 */
	protected void testWriteFile(boolean withHeader) {
		ExcelWriter out = null;
		try {
			// Write the file
			out = new ExcelWriter(fFile);
			if (withHeader) out.printRow(TEST_HEADER);
			for (int row= 0; row < TEST_VALUES.length; row++) {
				out.printRow(TEST_VALUES[row]);
			}
			out.close();
			assertTrue(fFile.exists());
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	/**
	 * Checks whether written data is equal to test data.
	 * @param withHeader whether header must be checked too
	 */
	protected void testWrittenValues(boolean withHeader) {
		ExcelReader in = null;
		try {
			in = new ExcelReader(fFile);
			in.setHasHeaderRow(withHeader);
			int row = 0;

			// Test the header
			if (withHeader) {
				Object header[] = in.getHeaderRow();
				testRow(TEST_HEADER, header);
			}

			// Test the data
			while (in.hasNext()) {
				Object columns[]= in.next();

				testRow(TEST_VALUES[row], columns);

				row++;
			}

			// compare size of rows
			assertEquals(TEST_VALUES.length, row);

		} catch (FileNotFoundException e) {
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

	/**
	 * This method checks if a header can be written and read.
	 */
	@Test
	public void testHeader() {
		testWriteFile(true);
		testWrittenValues(true);
	}

	/**
	 * This method checks that a blank sheet with no rows and a row range from 0 to 1 can be read.
	 * 
	 * @throws IOException - when the file cannot be opened/read
	 */
	@Test
	public void testBlankTab_DoesNotNPEInhasNext() throws IOException {
		ExcelReader in = new ExcelReader(multisheetUrl.openStream());
		in.selectSheet(2);
		assertFalse(in.hasNext());
	}

	/**
	 * This method checks that a sheet with blank lines can be read.
	 * 
	 * @throws IOException - when the file cannot be opened/read
	 */
	@Test
	public void testBlankLines_DoesNotNPEAndDummiesUpLinesWhenAsked() throws IOException {
		ExcelReader in = new ExcelReader(skippedLinesUrl.openStream());

		in.getWorkbook().setMissingCellPolicy(MissingCellPolicy.CREATE_NULL_AS_BLANK);
		in.setSkipBlankRows(false);
		in.setMinimumColumnCount(in.computeMaxColumnCount());
		assertEquals(in.getMinimumColumnCount(), 2);

		assertTrue(in.hasNext()); testRow(new String[]{"header", "header2"}, in.next());
		assertTrue(in.hasNext()); testRow(new String[]{null,      null    }, in.next());
		assertTrue(in.hasNext()); testRow(new String[]{null,      null    }, in.next());
		assertTrue(in.hasNext()); testRow(new String[]{"val1",    null    }, in.next());
		assertTrue(in.hasNext()); testRow(new String[]{"val2",    "val2-2"}, in.next());
		assertFalse(in.hasNext());
	}

	/**
	 * This method checks that when we have external sheet references in our formulas, for example,
	 * we can avoid formula evaluation and just use the cached value.
	 * 
	 * @throws IOException - when the file cannot be opened/read
	 */
	@Test
	public void testEvaluateFormulasFalse() throws IOException {
		ExcelReader in = new ExcelReader(externalSheetRefUrl.openStream());
		in.setEvaluateFormulas(false);

		assertTrue(in.hasNext()); 
		testRow(new Double[]{99945.0}, in.next());
		assertFalse(in.hasNext());
	}
}
