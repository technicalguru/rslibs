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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import csv.CsvException;
import csv.util.CSVUtils;

/**
 * Implements Excel reading.
 * <p>
 * This class reads Excel sheets like a stream, meaning
 * delivering rows one by one from the current sheet.
 * Use this reader if you want to load an Excel file by creating a {@link java.io.File}
 * and passing it to the constructor.
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre>
java.io.File f = new java.io.File("excel-test.xls");
ExcelReader in = new ExcelReader(f);
while (in.hasNext()) {
    Object columns[] = in.next();
    // Do something here
}
in.close();
</pre>
 * @author RalphSchuster
 * @see #selectSheet(int)
 * @see #selectSheet(String)
 */
public class ExcelReader extends AbstractStreamTableReader {

	/** The workbook */
	private Workbook workbook;
	/** The evaluator for cell formulas */
	private FormulaEvaluator formulaEvaluator = null;
	/** Whether formulas shall be evaluated or not (default is <code>true</code> = yes) */
	private boolean evaluateFormulas = true;
	/** Whether JavaTime objects shall be returned (default is <code>false</code> = no) */
	private boolean javaTimeEnabled = false;
	/** The sheet we are dealing with */
	private Sheet sheet;
	/** The current row we are reading */
	private Row currentRow;
	/** The row that was delivered by {@link #next()}. */
	private Row lastDeliveredRow;
	/** Index of first row */
	private int firstRow;
	/** Index of last row */
	private int lastRow;
	/** The row currently to read next */
	private int rowNum;
	/** Whether to skip blank rows (not deliver them) */
	private boolean skipBlankRows = true;

	/**
	 * Default constructor.
	 */
	public ExcelReader() {
	}

	/**
	 * Constructor for reading from a file.
	 * @param file file to read from
	 * @throws FileNotFoundException when file does not exist
	 */
	public ExcelReader(File file) throws FileNotFoundException {
		super(file);
	}

	/**
	 * Constructor to read from an existing stream.
	 * @param in input stream to be used
	 */
	public ExcelReader(InputStream in) {
		super(in);
	}

	/**
	 * Constructor to read from an existing workbook.
	 * @param workbook the workbook be used
	 */
	public ExcelReader(Workbook workbook) {
		this.workbook = workbook;
	}

	/**
	 * Constructor for reading from a file.
	 * @param file file to read from
	 * @throws FileNotFoundException when file does not exist
	 */
	public ExcelReader(String file) throws FileNotFoundException {
		super(file);
	}

	/**
	 * Opens the stream by retrieving the workbook and selecting the first sheet.
	 * @see csv.impl.AbstractStreamTableReader#open()
	 */
	@Override
	public void open() {
		super.open();
		try {
			workbook = WorkbookFactory.create(getInputStream());
			selectSheet(0);
		} catch (Exception e) {
			throw new CsvException("Cannot create Excel workbook", e);
		}
	}

	/**
	 * Returns the workbook.
	 * @return workbook
	 */
	public Workbook getWorkbook() {
		if (workbook == null) open();
		return workbook;
	}

	/**
	 * Returns whether blank rows will be skipped or not while reading.
	 * @return <code>true</code> when blank rows are skipped (default), <code>false</code> otherwise
	 */
	public boolean isSkipBlankRows() {
		return skipBlankRows;
	}

	/**
	 * Sets whether blank rows will be skipped or not while reading.
	 * @param skipBlankRows <code>true</code> when blank rows are skipped (default), <code>false</code> otherwise
	 */
	public void setSkipBlankRows(boolean skipBlankRows) {
		this.skipBlankRows = skipBlankRows;
	}


	/**
	 * Computes the max row length of any rows in this sheet.
	 * @return int length
	 */
	public int computeMaxColumnCount() {
		int maxColumnCount = 0;
		for (java.util.Iterator<Row> i = sheet.rowIterator(); i.hasNext();) {
			int length = i.next().getLastCellNum();
			if (length > maxColumnCount) maxColumnCount = length;
		}
		return maxColumnCount;
	}

	/**
	 * Select the given sheet to be read from.
	 * @param name name of sheet
	 * @return sheet selected
	 */
	public Sheet selectSheet(String name) {
		return selectSheet(workbook.getSheet(name));
	}

	/**
	 * Select the given sheet to be read from.
	 * @param sheet sheet to be selected
	 * @return sheet selected
	 */
	public Sheet selectSheet(Sheet sheet) {
		if (this.sheet != sheet) {
			this.sheet = sheet;
			firstRow = sheet.getFirstRowNum();
			rowNum = firstRow;
			lastRow = sheet.getLastRowNum();
			currentRow = null;
		}
		return this.sheet;
	}

	/**
	 * Returns the number of rows in the current sheet.
	 * @return number of rows in sheet or {@code -1} if no sheet is selected
	 * @since 2.9.1
	 */
	public int getNumRows() {
		if (this.sheet != null) {
			return lastRow+1;
		}
		return -1;
	}
	
	/**
	 * Select the given sheet to be read from.
	 * @param index index of sheet
	 * @return sheet selected
	 */
	public Sheet selectSheet(int index) {
		return selectSheet(workbook.getSheetAt(index));
	}

	/**
	 * Returns the current sheet.
	 * @return the current sheet.
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * Returns the last delivered row.
	 * This is the row delivered by last call to {@link #next()}.
	 * @return the last row delivered by {@link #next()}
	 */
	public Row getLastExcelRow() {
		return lastDeliveredRow;
	}

	/**
	 * Resets the reader by resetting the current row index 
	 * @see csv.impl.AbstractStreamTableReader#reset()
	 * @see #getRowCount()
	 */
	@Override
	public void reset() {
		super.reset();
		rowNum = firstRow;
		currentRow = null;
	}

	/**
	 * Returns whether there is a row to be read in the current sheet.
	 * This implementation stops reading when last row from a sheet was read.
	 * You might need to manually select the next sheet if you want to read more
	 * rows from other sheets.
	 * @return true if a row is available in current sheet.
	 * @see java.util.Iterator#hasNext()
	 * @see #selectSheet(int)
	 */
	@Override
	public boolean hasNext() {
		if (currentRow == null) retrieveNextRow();
		return currentRow != null;
	}

	/**
	 * Returns the next row.
	 * This method increases the internal row index and delivers the next row in the sheet.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @return values in row
	 * @see java.util.Iterator#next()
	 * @see #getRowCount()
	 */
	@Override
	public Object[] next() {
		if (hasNext()) {
			Object row[] = getValues(currentRow);
			lastDeliveredRow = currentRow;
			currentRow = null;

			incrementLineCount();
			incrementRowCount();
			return row;
		}
		throw new CsvException("No more rows");
	}

	/**
	 * Returns the row at the given index.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param rowNum row index to read
	 * @return values of row
	 */
	public Object[] getValues(int rowNum) {
		Row row = getSheet().getRow(rowNum);
		return getValues(row);
	}

	/**
	 * Returns the row as Java objects.
	 * Values in the array are Java objects depending on the cell type. If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @return values in row
	 * @param row row to read
	 */
	public Object[] getValues(Row row) {
		if (row == null) return null;
		List<Object> columns = new ArrayList<Object>();
		int colCount = row.getLastCellNum();
		for (int col=0; col<colCount; col++) {
			Cell cell = row.getCell(col);
			columns.add(getValue(cell));
		}

		return CSVUtils.convertList(columns, getMinimumColumnCount());
	}

	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param rownum row index
	 * @param cellNum column index
	 * @return value of cell
	 */
	public Object getValue(int rownum, int cellNum) {
		Row row = getSheet().getRow(rowNum);
		return getValue(row, cellNum);
	}

	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param row row object
	 * @param cellNum column index
	 * @return value of cell
	 */
	public Object getValue(Row row, int cellNum) {
		if (row == null) return null;
		Cell cell = row.getCell(cellNum);
		return getValue(cell);
	}

	/**
	 * Returns the value of the specified cell.
	 * If the cell contained
	 * a formula, the formula is evaluated before returning the row.
	 * @param cell cell object
	 * @return value of cell
	 */
	@SuppressWarnings("incomplete-switch")
	public Object getValue(Cell cell) {
		if (cell == null) return null;

		CellType cellType = cell.getCellType();
		if (cellType == CellType.FORMULA && !isEvaluateFormulas()) {
			cellType = cell.getCachedFormulaResultType();
		}
		
		switch (cellType) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)) {
				return javaTimeEnabled ? cell.getLocalDateTimeCellValue() : cell.getDateCellValue();
			} else {
				return cell.getNumericCellValue();
			}
		case BLANK:
			return null;
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case FORMULA:
			return evaluateCellValue(cell);
		case ERROR:
			return cell.getErrorCellValue();
		}
		return null;
	}

	/**
	 * Returns the evaluated cell content.
	 * This assumes the cell contains a formula.
	 * @param cell cell to evaluate
	 * @return cell value
	 */
	public Object evaluateCellValue(Cell cell) {
		FormulaEvaluator evaluator = getFormulaEvaluator();
		CellValue value = evaluator.evaluate(cell);
		if (value == null) return null;
		switch (value.getCellType()) {
		case STRING:
			return value.getStringValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return DateUtil.getJavaDate(value.getNumberValue());
			} else {
				return value.getNumberValue();
			}
		case BLANK:
			return null;
		case BOOLEAN:
			return value.getBooleanValue();
		case ERROR:
			return value.getErrorValue();
		default:
			System.out.println("type="+cell.getCellType().name());
		}
		return cell.getCellFormula();
	}

	/**
	 * Returns a formula evaluator for the current workbook.
	 * This is for convinience.
	 * @return the formula evaluator
	 */
	public FormulaEvaluator getFormulaEvaluator() {
		if (formulaEvaluator == null) {
			formulaEvaluator = getWorkbook().getCreationHelper().createFormulaEvaluator();
		}
		return formulaEvaluator;
	}


	/**
	 * Returns whether formulas shall be evaluated or not (default is <code>true</code>).
	 * @return <code>true</code> when formulas are evaluated
	 */
	public boolean isEvaluateFormulas() {
		return evaluateFormulas;
	}

	/**
	 * Sets whether formulas shall be evaluated or not (default is <code>true</code>).
	 * @param evaluateFormulas <code>true</code> or <code>false</code>
	 */
	public void setEvaluateFormulas(boolean evaluateFormulas) {
		this.evaluateFormulas = evaluateFormulas;
	}

	/**
	 * Returns whether date/time values shall be returns as LocalDateTime object or simple Dates.
	 * @return whether date/time values shall be returns as LocalDateTime object or simple Dates
	 */
	public boolean isJavaTimeEnabled() {
		return javaTimeEnabled;
	}

	/**
	 * Sets whether date/time values shall be returns as LocalDateTime object or simple Dates.
	 * @param javaTimeEnabled whether date/time values shall be returns as LocalDateTime object or simple Dates
	 */
	public void setJavaTimeEnabled(boolean javaTimeEnabled) {
		this.javaTimeEnabled = javaTimeEnabled;
	}

	/**
	 * Reads the header row from next line.
	 * @see csv.impl.AbstractTableReader#readHeaderRow()
	 */
	@Override
	protected void readHeaderRow() {
		if (hasNext()) {
			setHeaderRow(CSVUtils.convertArray(next(), getMinimumColumnCount()));
		}
	}

	/**
	 * Retrieves the next row from the current sheet.
	 * The row is then internally stored for evaluation of {@link #hasNext()}
	 * and {@link #next()}. Blank rows are skipped when {@linkplain #isSkipBlankRows()} 
	 * return <code>true</code>.
	 */
	protected void retrieveNextRow() {
		while (rowNum <= lastRow) {
			currentRow = getOrCreateRow(rowNum++);
			if (currentRow == null) continue;
			if (isSkipBlankRows() && rowHasOnlyBlankCells(currentRow)) {
				currentRow = null;
			} else {
				break;
			}
		}
	}

	/**
	 * Checks whether row has only blank cells.
	 * The method is called from {@link #retrieveNextRow()}.
	 * @param row the row to check
	 * @return boolean when the row has only blank cells
	 */
	protected boolean rowHasOnlyBlankCells(Row row) {
		boolean blank = true;
		for (Cell cell: row) {
			if (cell.getCellType() != CellType.BLANK) {
				blank = false;
				break;
			}
		}
		return blank;

	}

	/**
	 * Ensures that the sheet contains a row at the given index.
	 * @param rowNum index of row
	 * @return the row from the sheet or a new blank row
	 */
	protected Row getOrCreateRow(int rowNum) {
		Row row = sheet.getRow(rowNum);
		if (!isSkipBlankRows() && (row == null)) {
			row = sheet.createRow(rowNum);
		}
		return row;
	}

}
