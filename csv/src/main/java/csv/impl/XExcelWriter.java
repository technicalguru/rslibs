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
package csv.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Provides ability to write XML-based Excel files.
 * <p>
 * The Excel will be written with a call to {@link #close()} only!
 * Please notice that this implementation does not support writing formulas into
 * cells, yet.
 * Example:
 * </p>
<pre>
java.io.File f = new java.io.File("excel-test.xlsx");
ExcelWriter out = new XExcelWriter(f);
out.printRow(new Object[] { "0:0", new Integer(3), new Date() });
out.printRow(new Object[] { "1:0", new Double(), "another String value" });
out.close();
</pre>
 * @see #close()
 * @author RalphSchuster
 *
 */
public class XExcelWriter extends ExcelWriter {

	public static final int STREAMING_ACCESS_WINDOW_SIZE = 100;
	
	private boolean useStreaming = false;
	
	/**
	 * Default constructor.
	 * Please, notice that you are required to set the output stream
	 * before closing the writer.
	 * @see #setOutputStream(OutputStream)
	 */
	public XExcelWriter() {
	}

	/**
	 * Constructor with existing workbook.
	 * You can use this constructor if you wanna write to an existing workbook.
	 * Please, notice that you are required to set the output stream
	 * before closing the writer.
	 * @see #setOutputStream(OutputStream)
	 * @param workbook the workbook to be used
	 */
	public XExcelWriter(Workbook workbook) {
		super(workbook);

	}

	/**
	 * Constructor with defined output stream.
	 * A new workbook will be created.
	 * @param out output stream to be used.
	 */
	public XExcelWriter(OutputStream out) {
		super(out);

	}

	/**
	 * Constructor with existing workbook and defined output stream.
	 * @param workbook the workbook to be used
	 * @param out output stream to be used
	 */
	public XExcelWriter(Workbook workbook, OutputStream out) {
		super(workbook, out);

	}

	/**
	 * Constructor for writing into a file.
	 * A new workbook will be created.
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public XExcelWriter(File file) throws IOException {
		super(file);

	}

	/**
	 * Constructor with existing workbook that needs to be written to a file.
	 * @param workbook the workbook to be used
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public XExcelWriter(Workbook workbook, File file) throws IOException {
		super(workbook, file);

	}

	/**
	 * Constructor for writing into a file.
	 * A new workbook will be created.
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public XExcelWriter(String file) throws IOException {
		super(file);

	}

	/**
	 * Constructor with existing workbook that needs to be written to a file.
	 * @param workbook the workbook to be used
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public XExcelWriter(Workbook workbook, String file) throws IOException {
		super(workbook, file);

	}

	/**
	 * Default constructor.
	 * Please, notice that you are required to set the output stream
	 * before closing the writer.
	 * @param useStreaming {@code true} when streaming version is used
	 * @see #setOutputStream(OutputStream)
	 */
	public XExcelWriter(boolean useStreaming) {
		setUseStreaming(useStreaming);
	}

	/**
	 * Constructor with defined output stream.
	 * A new workbook will be created.
	 * @param out output stream to be used.
	 * @param useStreaming {@code true} when streaming version is used
	 */
	public XExcelWriter(OutputStream out, boolean useStreaming) {
		super(out);
		setUseStreaming(useStreaming);
	}

	/**
	 * Constructor for writing into a file.
	 * A new workbook will be created.
	 * @param file output file to be used
	 * @param useStreaming {@code true} when streaming version is used
	 * @throws IOException when the file cannot be written to
	 */
	public XExcelWriter(File file, boolean useStreaming) throws IOException {
		super(file);
		setUseStreaming(useStreaming);
	}

	/**
	 * Returns whether the excel fiel uses streaming version (default is false).
	 * @return {@code true} when streaming version is used
	 */
	public boolean isUseStreaming() {
		return useStreaming;
	}

	/**
	 * Sets whether the excel fiel uses streaming version (default is false).
	 * <p><b>You will need to set this property immediately after construction to be in effect or
	 *       better use the correct constructor version.</b>
	 * @param useStreaming {@code true} when streaming version is used
	 */
	public void setUseStreaming(boolean useStreaming) {
		this.useStreaming = useStreaming;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Workbook getWorkbook() {
		if (workbook == null) {
			workbook = isUseStreaming() ? createStreamingWorkbook() : createNonStreamingWorkbook();
		}
		return workbook;
	}

	/**
	 * Creates a streaming workbook using {@link #STREAMING_ACCESS_WINDOW_SIZE}.
	 * @return the workbook created
	 */
	public SXSSFWorkbook createStreamingWorkbook() {
		SXSSFWorkbook rc = new SXSSFWorkbook(STREAMING_ACCESS_WINDOW_SIZE);
		rc.setCompressTempFiles(true);
		return rc;
	}
	
	/**
	 * Creates a standard non-streaming workbook.
	 * @return the workbook created
	 */
	public XSSFWorkbook createNonStreamingWorkbook() {
		return new XSSFWorkbook();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row getOrCreateRow(int row) {		
		if (isUseStreaming() && (workbook instanceof SXSSFWorkbook)) {
			if (getRowCount() == ((SXSSFWorkbook)workbook).getRandomAccessWindowSize()) {
				ExcelFormatter formatter = getFormatter();
				if ((formatter != null) && (formatter instanceof DefaultExcelFormatter)) {
					DefaultExcelFormatter f = (DefaultExcelFormatter)formatter;
					f.finalizeSheet(this, workbook, getSheet(), workbook.getActiveSheetIndex());
				}
			}
		}
		return super.getOrCreateRow(row);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sheet createSheet(int index, String name) {
		Sheet rc = super.createSheet(index, name);
		if (rc instanceof SXSSFSheet) {
			SXSSFSheet sSheet = (SXSSFSheet)rc;
			sSheet.trackAllColumnsForAutoSizing();
		}
		return rc;
	}
	
}
