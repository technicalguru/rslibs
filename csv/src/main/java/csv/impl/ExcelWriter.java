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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import rs.baselib.type.MonetaryValue;

/**
 * Provides ability to write classic Excel files.
 * <p>
 * The Excel will be written with a call to {@link #close()} only!
 * Please notice that this implementation does not support writing formulas into
 * cells, yet.
 * Example:
 * </p>
<pre>
java.io.File f = new java.io.File("excel-test.xls");
ExcelWriter out = new ExcelWriter(f);
out.printRow(new Object[] { "0:0", new Integer(3), new Date() });
out.printRow(new Object[] { "1:0", new Double(), "another String value" });
out.close();
</pre>
 * @see #close()
 * @author RalphSchuster
 *
 */
public class ExcelWriter extends AbstractStreamTableWriter {

	protected Workbook workbook;
	private Sheet sheet;
	private int rowNum;
	private int maxColumns;
	private Set<ExcelListener> excelListeners = new HashSet<ExcelListener>();
	private ExcelFormatter formatter;
	
	/**
	 * Default constructor.
	 * Please, notice that you are required to set the output stream
	 * before closing the writer.
	 * @see #setOutputStream(OutputStream)
	 */
	public ExcelWriter() {
	}

	/**
	 * Constructor with existing workbook.
	 * You can use this constructor if you wanna write to an existing workbook.
	 * Please, notice that you are required to set the output stream
	 * before closing the writer.
	 * @see #setOutputStream(OutputStream)
	 * @param workbook the workbook to be used
	 */
	public ExcelWriter(Workbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * Constructor with defined output stream.
	 * A new workbook will be created.
	 * @param out output stream to be used.
	 */
	public ExcelWriter(OutputStream out) {
		this(null, out);
	}

	/**
	 * Constructor with existing workbook and defined output stream.
	 * @param workbook the workbook to be used
	 * @param out output stream to be used
	 */
	public ExcelWriter(Workbook workbook, OutputStream out) {
		super(out);
		this.workbook = workbook;
	}

	/**
	 * Constructor for writing into a file.
	 * A new workbook will be created.
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public ExcelWriter(File file) throws IOException {
		this(null, file);
	}

	/**
	 * Constructor with existing workbook that needs to be written to a file.
	 * @param workbook the workbook to be used
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public ExcelWriter(Workbook workbook, File file) throws IOException {
		super(file);
		this.workbook = workbook;
	}

	/**
	 * Constructor for writing into a file.
	 * A new workbook will be created.
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public ExcelWriter(String file) throws IOException {
		this(null, file);
	}

	/**
	 * Constructor with existing workbook that needs to be written to a file.
	 * @param workbook the workbook to be used
	 * @param file output file to be used
	 * @throws IOException when the file cannot be written to
	 */
	public ExcelWriter(Workbook workbook, String file) throws IOException {
		super(file);
		this.workbook = workbook;
	}

	/**
	 * Prints the values to the Excel file.
	 * Please note that this method maintains an internal row counter
	 * and will always start with row index 0 to write to. The method will
	 * automatically increase this internal counter.
	 * You could avoid this by using {@link #printRow(Object[], int)}
	 * @param columns values to be written to the Excel sheet
	 * @see csv.TableWriter#printRow(java.lang.Object[])
	 */
	@Override
	public void printRow(Object[] columns) throws IOException {
		printRow(columns, rowNum);
		rowNum++;
		incrementRowCount();
	}

	/**
	 * Prints the values to the Excel file at the given row.
	 * This method is useful in case you want to write specific rows, e.g.
	 * when writing to an existing workbook.
	 * @param columns values to be written to the Excel sheet
	 * @param rowNum index of row to be written
	 * @throws IOException when an error occurs while writing
	 * @see #printRow(Object[])
	 */
	public void printRow(Object[] columns, int rowNum) throws IOException {
		// Get or create the row
		Row row = getOrCreateRow(rowNum);
		for (int i=0; i<columns.length; i++) {
			setValue(row, i, convert(columns[i]));
		}
	}
	
	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
     * @param row index of row for comment
     * @param column index of column for comment
 	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment, int row, int column) throws IOException {
		/* TODO
		Cell cell = getOrCreateCell(row, column);
		CreationHelper factory = getWorkbook().getCreationHelper();
		Drawing drawing = getSheet().createDrawingPatriarch();
		ClientAnchor anchor = factory.createClientAnchor();
	    Comment commentObj = ((HSSFPatriarch)drawing).createCellComment((HSSFAnchor)anchor);
	    RichTextString str = factory.createRichTextString("Hello, World!");
	    commentObj.setString(str);
	    commentObj.setAuthor("Apache POI");
	    //assign the comment to the cell
	    cell.setCellComment(commentObj);
	    */
	}

	/**
	 * Returns an existing cell or creates one.
	 * @param row row index
	 * @param column column index
	 * @return cell object
	 */
	public Cell getOrCreateCell(int row, int column) {
		Row r = getOrCreateRow(row);
		return getOrCreateCell(r, column);
	}
	
	/**
	 * Returns an existing cell or creates one.
	 * @param row row object
	 * @param column column index
	 * @return cell object
	 */
	public Cell getOrCreateCell(Row row, int column) {
		Cell cell = row.getCell(column);
		if (cell == null) cell = row.createCell(column);
		return cell;
	}
	
	/**
	 * Returns an existing row or creates one.
	 * This method also notifies all {@link ExcelListener}s about a new row.
	 * @param row row index
	 * @return row object
	 */
	public Row getOrCreateRow(int row) {
		Row r = getSheet().getRow(row);
		if (r == null) {
			r = getSheet().createRow(row);
			notifyExcelListeners(r);
		}
		return r;
	}
	
	/**
	 * Sets the value at the specified cell.
	 * @param row row index
	 * @param column column index
	 * @param value value to be set
	 * @see #setValue(Cell, Object)
	 */
	public void setValue(int row, int column, Object value) {
		Row r = getOrCreateRow(row);
		setValue(r, column, value);
	}
	
	/**
	 * Sets the value at the specified cell.
	 * @param row row object
	 * @param column column index
	 * @param value value to be set
	 * @see #setValue(Cell, Object)
	 */
	public void setValue(Row row, int column, Object value) {
		Cell cell = getOrCreateCell(row, column);
		setValue(cell, value);
	}
	
	/**
	 * Sets the value at the specified cell.
	 * This method automatically selects the correct type for the cell
	 * and notifies the {@link ExcelFormatter} to set the correct style
	 * on this cell.
	 * @param cell cell object
	 * @param value value to be set
	 */
	public void setValue(Cell cell, Object value) {
		if (value != null) {
			if (value instanceof Date) {
				cell.setCellValue((Date)value);
			} else if (value instanceof LocalDate) {
				cell.setCellValue((LocalDate)value);
			} else if (value instanceof LocalDateTime) {
				cell.setCellValue((LocalDateTime)value);
			} else if (value instanceof ZonedDateTime) {
				cell.setCellValue(((ZonedDateTime)value).toLocalDateTime());
			} else if (value instanceof MonetaryValue) {
				cell.setCellValue(((MonetaryValue) value).getDouble());
			} else if (value instanceof Number) {
				cell.setCellValue(((Number) value).doubleValue());
			} else if (value instanceof Boolean) {
				cell.setCellValue((Boolean)value);
			} else {
				cell.setCellValue(value.toString());
			}
			
			if (cell.getColumnIndex() > maxColumns) maxColumns = cell.getColumnIndex();
		}
		setStyle(cell, value);
	}
	
	/**
	 * Returns the workbook or creates a fresh one.
	 * @return the workbook
	 */
	public Workbook getWorkbook() {
		if (workbook == null) workbook = new HSSFWorkbook();
		return workbook;
	}

	/**
	 * Returns the current sheet or creates a fresh one.
	 * @return the sheet
	 */
	public Sheet getSheet() {
		if (sheet == null) {
			sheet = createSheet();
			selectSheet(sheet);
		}
		return sheet;
	}

	/**
	 * This method selects the given sheet.
	 * This will reset the internal row counter (see {@link #printRow(Object[])}).
	 * @param sheet sheet to be selected
	 */
	public void selectSheet(Sheet sheet) {
		sheet.setSelected(true);
		rowNum = 0;
		maxColumns = -1;
	}
	
	/**
	 * This method selects the sheet at given index.
	 * If no such sheet exists, it will be created.
	 * This will reset the internal row counter (see {@link #printRow(Object[])}).
	 * @param index sheet index
	 * @return sheet selected
	 */
	public Sheet selectSheet(int index) {
		sheet = getWorkbook().getSheetAt(index);
		if (sheet == null) sheet = createSheet(index);
		selectSheet(sheet);
		return sheet;
	}
	
	/**
	 * Creates a new sheet for the workbook.
	 * @return sheet created
	 */
	public Sheet createSheet() {
		return createSheet(-1);
	}
	
	/**
	 * Creates a new sheet for the workbook at specified index.
	 * @param index of sheet (-1 adds the sheet at the end of all sheet)
	 * @return sheet created
	 */
	public Sheet createSheet(int index) {
		return createSheet(index, "Sheet"+(getWorkbook().getNumberOfSheets()+1));
	}
	
	/**
	 * Creates a new sheet for the workbook at specified index.
	 * @param name name of new sheet
	 * @param index of sheet (-1 adds the sheet at the end of all sheet)
	 * @return sheet created
	 */
	public Sheet createSheet(int index, String name) {
		Sheet rc = getWorkbook().createSheet(name);
		if (index >= 0) getWorkbook().setSheetOrder(name, index);
		return rc;
	}
	
	/**
	 * Closes the writer and writes the Excel to the underlying stream.
	 * Please note that all modifications of an Excel sheet appear in memory
	 * only and need to be written finally by calling this method.
	 * @see csv.impl.AbstractStreamTableWriter#close()
	 */
	@Override
	public void close() {
		try {
			if (formatter != null) formatter.finalize(this);
			getWorkbook().write(getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.close();
	}

	/**
	 * Registers an Excel Listener.
	 * This listener will be informed whenever a new row was created.
	 * @param l the listener
	 */
	public void registerExcelListener(ExcelListener l) {
		excelListeners.add(l);
	}

	/**
	 * Unregisters an ExcelListener.
	 * Registered ExcelListeners will be informed whenever a new row was created.
	 * @param l the listener
	 */
	public void unregisterExcelListener(ExcelListener l) {
		excelListeners.remove(l);
	}

	/**
	 * Notifies all Excel Listeners about the new row.
	 * @param row the row that was created
	 */
	protected void notifyExcelListeners(Row row) {
		for (ExcelListener l : excelListeners) {
			l.rowCreated(this, row);
		}
	}
	
	/**
	 * Returns the formatter set for this ExcelWriter.
	 * @return the formatter
	 */
	public ExcelFormatter getFormatter() {
		return formatter;
	}

	/**
	 * Sets the formatter for this ExcelWriter.
	 * ExcelFormatter are responsible to set the correct style of cells.
	 * The ExcelFormatter will be informed whenever a value in a cell was
	 * modified.
	 * @param formatter the formatter to set
	 * @see ExcelFormatter
	 * @see #setValue(Cell, Object)
	 * @see #setStyle(Cell, Object)
	 */
	public void setFormatter(ExcelFormatter formatter) {
		this.formatter = formatter;
	}

	/**
	 * Sets the style of a cell.
	 * The method is called immediately after a cell was modified. The default implementation
	 * will call {@link ExcelFormatter#setStyle(ExcelWriter, Cell, Object)}.
	 * @param cell cell to be formatted
	 * @param value value that was  set
	 * @see #setFormatter(ExcelFormatter)
	 */
	protected void setStyle(Cell cell, Object value) {
		if (getFormatter() != null) getFormatter().setStyle(this, cell, value);
	}
}
