/**
 * 
 */
package csv.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import csv.impl.DefaultExcelFormatter;

/**
 * Help with the styles in a workbook.
 * <p>Use this helper when u need different styles in a workbook and refer to them by name.</p>
 * @see #getStyle(String)
 * @see #createStyle(String)
 * 
 * @author ralph
 *
 */
public class WorkbookCellStyles {

	private Workbook workbook;
	private Map<String,CellStyle> styles;
	private DataFormat dataFormat;
	private Font boldFont;
	private Font plainFont;
	
	/**
	 * Constructor.
	 * @param workbook - the workbook this helper is attached to.
	 */
	public WorkbookCellStyles(Workbook workbook) {
		this.workbook   = workbook;
		this.styles     = new HashMap<>();
		this.dataFormat = workbook.createDataFormat();
	}

	/**
	 * Returns the cached cell style of the given name.
	 * <p>The method will call {@link #createStyle(String)} when the style
	 *    was not created yet.</
	 * @param name - name of the style.
	 * @return the style of the given name or {@code null} if it cannot be found or created.
	 * @see #createStyle(String)
	 */
	public CellStyle getStyle(String name) {
		CellStyle rc = styles.get(name);
		if (rc == null) {
			rc = createStyle(name);
			if (rc != null) styles.put(name, rc);
		}
		return rc;
	}
	
	/**
	 * Set the style in the given sheet cell according to the ceel style name.
	 * @param sheet - sheet that the cell belongs to
	 * @param row - row index of cell
	 * @param col - column index of cell
	 * @param name - name of style to be set
	 * @return the cell style that was applied.
	 * @see #setStyle(Row, int, String)
	 */
	public CellStyle setStyle(Sheet sheet, int row, int col, String name) {
		if (sheet != null) return setStyle(sheet.getRow(row), col, name);
		return getStyle(name);
	}
	
	/**
	 * Set the style in the given row cell according to the cell style name.
	 * @param row - row the cell belongs to
	 * @param col - column index of cell
	 * @param name - name of style to be set
	 * @return the cell style that was applied.
	 * @see #setStyle(Cell, String)
	 */
	public CellStyle setStyle(Row row, int col, String name) {
		if (row != null) return setStyle(row.getCell(col), name);
		return getStyle(name);
	}
	
	/**
	 * Set the style in the given cell according to the cell style name.
	 * @param cell - the sell to be modified
	 * @param name - name of style to be set
	 * @return the cell style that was applied.
	 */
	public CellStyle setStyle(Cell cell, String name) {
		if (cell != null) cell.setCellStyle(getStyle(name));
		return getStyle(name);
	}
	
	/**
	 * Creates a style of the given name.
	 * <p>Descendants <b>must</b> override this method to create the cell style
	 *    for their specific usage.</p>
	 * @param name - name of the cell style to be created
	 * @return the cell style of the given name or {@code null}
	 * @see #getBoldFont()
	 * @see #getPlainFont()
	 * @see #getWorkbook()
	 * @see #getDataFormat()
	 */
	protected CellStyle createStyle(String name) {
		return null;
	}
	
	/**
	 * Returns the workbook.
	 * @return the workbook.
	 */
	public Workbook getWorkbook() {
		return workbook;
	}
	
	/**
	 * Returns the default bold font.
	 * @return bold font
	 * @see DefaultExcelFormatter#DEFAULT_FONT_NAME
	 * @see DefaultExcelFormatter#DEFAULT_FONT_SIZE
	 */
	public Font getBoldFont() {
		if (boldFont == null) {
			boldFont = workbook.createFont();
			boldFont.setBold(true);
			boldFont.setFontHeightInPoints(DefaultExcelFormatter.DEFAULT_FONT_SIZE);
			boldFont.setFontName(DefaultExcelFormatter.DEFAULT_FONT_NAME);
		}
		return boldFont;
	}

	/**
	 * Returns the default bold font.
	 * @return bold font
	 * @see DefaultExcelFormatter#DEFAULT_FONT_NAME
	 * @see DefaultExcelFormatter#DEFAULT_FONT_SIZE
	 */
	public Font getPlainFont() {
		if (plainFont == null) {
			plainFont = workbook.createFont();
			plainFont.setFontHeightInPoints(DefaultExcelFormatter.DEFAULT_FONT_SIZE);
			plainFont.setFontName(DefaultExcelFormatter.DEFAULT_FONT_NAME);
		}
		return plainFont;
	}

	/**
	 * Returns the data format object of the workbook.
	 * @return the data format object of the workbook.
	 */
	public DataFormat getDataFormat() {
		return dataFormat;
	}

}
