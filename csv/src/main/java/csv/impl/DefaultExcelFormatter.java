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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import rs.baselib.type.MonetaryValue;

/**
 * Default implementation of an ExcelFormatter.
 * This class provides a default implementation that provides some basic
 * functionality to emphasize the header row in a sheet by a bold font and
 * formatting hyperlinks in cells.
 * You can derive from this implementation to change formatting, e.g. just
 * setting another color or font size.
 * @author RalphSchuster
 *
 */
public class DefaultExcelFormatter implements ExcelFormatter {

	/** Arial font name */
	public static final String DEFAULT_FONT_NAME = "Arial";
	/** Color Black */
	public static final short DEFAULT_FONT_COLOR = IndexedColors.BLACK.getIndex();
	/** Color Blue */
	public static final short HYPERLINK_FONT_COLOR = IndexedColors.BLUE.getIndex();
	/** Font Size 10 */
	public static final short DEFAULT_FONT_SIZE = 10;
	/** datetime format "dd.mm.yyyy hh:mm" */
	public static final String DEFAULT_DATE_FORMAT = "dd.mm.yyyy";
	/** datetime format "dd.mm.yyyy hh:mm" */
	public static final String DEFAULT_DATETIME_FORMAT = "dd.mm.yyyy hh:mm";
	/** integer format "0" */
	public static final String DEFAULT_INTEGER_FORMAT = "0";
	/** real format "0.00" */
	public static final String DEFAULT_REAL_FORMAT = "0.00";

	private boolean emphasizeFirstRow;
	private boolean autofilter;
	
	private Font defaultBoldFont;
	private Font defaultPlainFont;
	private Font defaultHyperlinkFont;
	private String defaultFontName;
	private short defaultFontSize;
	private short defaultFontColor;
	private short defaultHyperlinkColor;
	private Map<String, Short> dateFormat;
	private Map<String, Short> intFormat;
	private Map<String, Short> currencyFormat;
	private Map<String, Short> realFormat;
	private Map<StyleDescription, CellStyle> styles;
	private Short borderColor;
	private BorderStyle borderThickness;

	/**
	 * Default constructor.
	 * This is without any formatting.
	 */
	public DefaultExcelFormatter() {
		this(false, false);
	}

	/**
	 * Constructor for defining the emphasizing of header rows.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow) {
		this(emphasizeFirstRow, false);
	}
	
	/**
	 * Constructor for defining the emphasizing of header rows.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @param autofilter whether row 0 shall have autofilter activated
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow, boolean autofilter) {
		this(emphasizeFirstRow, autofilter, null, null, null, null);
	}

	/**
	 * Constructor for defining the various properties.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @param defaultFontName font name of default font
	 * @param defaultFontSize font size to be used
	 * @param defaultFontColor color to be used for font
	 * @param defaultHyperlinkColor color for hyperlinks to be used
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow, String defaultFontName, Short defaultFontSize, Short defaultFontColor, Short defaultHyperlinkColor) {
		this(emphasizeFirstRow, false, defaultFontName, defaultFontSize, defaultFontColor, defaultHyperlinkColor);
	}
	
	/**
	 * Constructor for defining the various properties.
	 * @param emphasizeFirstRow whether row 0 shall be set in bold font
	 * @param autofilter whether row 0 shall have autofilter activated
	 * @param defaultFontName font name of default font
	 * @param defaultFontSize font size to be used
	 * @param defaultFontColor color to be used for font
	 * @param defaultHyperlinkColor color for hyperlinks to be used
	 * @see #getFont(ExcelWriter, int, int, Object)
	 */
	public DefaultExcelFormatter(boolean emphasizeFirstRow, boolean autofilter, String defaultFontName, Short defaultFontSize, Short defaultFontColor, Short defaultHyperlinkColor) {
		this.emphasizeFirstRow     = emphasizeFirstRow;
		this.autofilter            = autofilter;
		this.defaultFontName       = defaultFontName != null ? defaultFontName : DEFAULT_FONT_NAME;
		this.defaultFontSize       = defaultFontSize != null ? defaultFontSize : DEFAULT_FONT_SIZE;
		this.defaultFontColor      = defaultFontColor != null ? defaultFontColor : DEFAULT_FONT_COLOR;
		this.defaultHyperlinkColor = defaultHyperlinkColor != null ? defaultHyperlinkColor : HYPERLINK_FONT_COLOR;
		init();
	}

	/**
	 * Initializes the formatter.
	 */
	public void init() {
		defaultBoldFont      = null;
		defaultPlainFont     = null;
		defaultHyperlinkFont = null;
		dateFormat           = new HashMap<>();
		intFormat            = new HashMap<>();
		realFormat           = new HashMap<>();
		currencyFormat       = new HashMap<>();
		styles               = new HashMap<>();
	}

	/**
	 * Returns whether row 0 shall have autofilter activated.
	 * @return whether row 0 shall have autofilter activated
	 */
	public boolean isAutofilter() {
		return autofilter;
	}

	/**
	 * Sets whether row 0 shall have autofilter activated.
	 * @param autofilter whether row 0 shall have autofilter activated
	 */
	public void setAutofilter(boolean autofilter) {
		this.autofilter = autofilter;
	}

	/**
	 * Sets the cell style.
	 * This implementations calls various other methods to define
	 * the style of the cell.
	 * @param writer writer that requires the information
	 * @param cell cell to be formatted
	 * @param value value in cell
	 * @see #getFormat(ExcelWriter, int, int, Object)
	 * @see #getBackgroundColor(ExcelWriter, int, int, Object)
	 * @see #getFillPattern(ExcelWriter, int, int, Object)
	 * @see #getForegroundColor(ExcelWriter, int, int, Object)
	 * @see #getFont(ExcelWriter, int, int, Object)
	 * @see #getAlign(ExcelWriter, int, int, Object)
	 * @see #getHyperlink(ExcelWriter, int, int, Object)
	 */
	@Override
	public void setStyle(ExcelWriter writer, Cell cell, Object value) {
		int row = cell.getRowIndex();
		int column = cell.getColumnIndex();

		StyleDescription desc = new StyleDescription();

		// Collect cell style and check if we already had it before

		// data format
		desc.setFormat(getFormat(writer, row, column, value));
		desc.setFgColor(getForegroundColor(writer, row, column, value));
		desc.setFillPattern(getFillPattern(writer, row, column, value));
		desc.setBgColor(getBackgroundColor(writer, row, column, value));

		// Font
		desc.setFont(getFont(writer, row, column, value));

		// Borders
		desc.setTopBorderColor(getTopBorderColor(writer, row, column, value));
		desc.setLeftBorderColor(getLeftBorderColor(writer, row, column, value));
		desc.setRightBorderColor(getRightBorderColor(writer, row, column, value));
		desc.setBottomBorderColor(getBottomBorderColor(writer, row, column, value));
		desc.setTopBorderThickness(getTopBorderThickness(writer, row, column, value));
		desc.setLeftBorderThickness(getLeftBorderThickness(writer, row, column, value));
		desc.setRightBorderThickness(getRightBorderThickness(writer, row, column, value));
		desc.setBottomBorderThickness(getBottomBorderThickness(writer, row, column, value));
		desc.setTextWrap(isTextWrap(writer, row, column, value));

		// Alignment
		desc.setAlignment(getAlign(writer, row, column, value));

		if (!desc.isDefault()) {
			CellStyle style = styles.get(desc);
			if (style == null) {
				style = writer.getWorkbook().createCellStyle();
				styles.put(desc, style);
				desc.applyStyle(style);
			}

			desc.applyStyle(style);

			// set style
			cell.setCellStyle(style);
		}

		// Set a hyperlink
		Hyperlink link = getHyperlink(writer, row, column, value);
		if (link != null) cell.setHyperlink(link);

	}

	/**
	 * Returns a hyperlink object when the given cell shall be linked.
	 * Notice that you should return a blue underlined font in {@link #getFont(ExcelWriter, int, int, Object)}
	 * when you return a hyperlink here.
	 * @param writer the calling writer
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return hyperlink object for the cell
	 */
	public Hyperlink getHyperlink(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Finalizes the workbook.
	 * This method is called immediately before the {@link ExcelWriter} writes the
	 * complete workbook to the underlying output stream.
	 * This implementation just sets all columns to auto fit.
	 * @param writer the calling writer
	 * @param rowCount the number of rows in the selected sheet
	 * @param columnCount the number of columns modified in the selected sheet
	 * @deprecated Use {@link #finalize(ExcelWriter)} instead.
	 */
	@Override
	@Deprecated
	public void finalize(ExcelWriter writer, int rowCount, int columnCount) {
		finalize(writer);
	}

	/**
	 * {@inheritDoc}
	 * @see #finalizeSheet(ExcelWriter, Workbook, Sheet, int)
	 * @see #finalizeFirstRow(ExcelWriter, Workbook, Sheet, int, Row, Cell, int)
	 * 
	 */
	@Override
	public void finalize(ExcelWriter writer) {
		Workbook workbook = writer.getWorkbook();
		Iterator<Sheet> i = workbook.sheetIterator();
		int index = 0;
		while (i.hasNext()) {
			Sheet sheet = i.next();
			finalizeSheet(writer, workbook, sheet, index);
			index++;
		}
	}

	/**
	 * Finalizes the given sheet in the workbook.
	 * The method is automatically called by {@link #finalize(ExcelWriter)} and takes care
	 * of finalizing the first row in a sheet by calling {@link #finalizeFirstRow(ExcelWriter, Workbook, Sheet, int, Row, Cell, int)}.
	 * @param writer - writer the calling writer
	 * @param workbook - the workbook to be finalized
	 * @param sheet - the sheet to be finalized
	 * @param sheetIndex - the index of the sheet
	 * @see #finalize(ExcelWriter)
	 * @see #finalizeFirstRow(ExcelWriter, Workbook, Sheet, int, Row, Cell, int)
	 * @since 4.1
	 */
	public void finalizeSheet(ExcelWriter writer, Workbook workbook, Sheet sheet, int sheetIndex) {
		int currentWorksheet = workbook.getActiveSheetIndex();
		workbook.setActiveSheet(sheetIndex);
		
		// Set Autosize column
		Row row = sheet.getRow(0);
		if (row != null) {
			row.getFirstCellNum();
			row.getLastCellNum();
			for (int i=row.getFirstCellNum(); i<=row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i);
				if (cell != null) {
					finalizeFirstRow(writer, workbook, sheet, sheetIndex, row, cell, i);
				}
			}
			if (isAutofilter()) applyAutofilter(workbook, sheet, row);
		}
		workbook.setActiveSheet(currentWorksheet);
	}
	
	/**
	 * Sets the autofilter property in this row.
	 * @param workbook - the workbook to be finalized
	 * @param sheet - the sheet to be finalized
	 * @param row - the row
	 */
	public void applyAutofilter(Workbook workbook, Sheet sheet, Row row) {
		sheet.setAutoFilter(new CellRangeAddress(0, sheet.getLastRowNum(), 0, row.getLastCellNum()));
	}
	
	/**
	 * Finalized the first row of the given sheet by setting the autosize of the column.
	 * @param writer - writer the calling writer
	 * @param workbook - the workbook to be finalized
	 * @param sheet - the sheet to be finalized
	 * @param sheetIndex - the index of the sheet
	 * @param row - the first row
	 * @param cell - the cell in first row to finalize
	 * @param cellIndex - the cell index
	 * @see #finalize(ExcelWriter)
	 * @see #finalizeSheet(ExcelWriter, Workbook, Sheet, int)
	 * @since 4.1
	 */
	public void finalizeFirstRow(ExcelWriter writer, Workbook workbook, Sheet sheet, int sheetIndex, Row row, Cell cell, int cellIndex) {
		sheet.autoSizeColumn(cellIndex);
		if (isAutofilter()) {
			CellStyle style = workbook.createCellStyle();
			style.setFont(getDefaultBoldFont(workbook));
			style.setWrapText(false);
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * Returns the alignment to be used.
	 * This implementation returns null.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return alignment index for Excel or null if no alignment is required
	 */
	public HorizontalAlignment getAlign(ExcelWriter writer, int row, int column, Object value) {
		if ((value instanceof Number)) {
			return HorizontalAlignment.RIGHT;
		}
		return null;	
	}

	/**
	 * Returns the display format.
	 * The format of the value. This implementation sets format for dates and numbers.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return format index for Excel or null if no formatting is required
	 * @see #getDateFormat(int, int, Object)
	 * @see #getIntegerFormat(int, int, Object)
	 * @see #getRealFormat(int, int, Object)
	 */
	public Short getFormat(ExcelWriter writer, int row, int column, Object value) {
		if ((value instanceof Date) || (value instanceof LocalDateTime) || (value instanceof ZonedDateTime)) {
			return getDateFormat(writer, getDateTimeFormat(row, column, value));
		}
		if (value instanceof LocalDate) {
			return getDateFormat(writer, getDateFormat(row, column, value));
		}
		if (value instanceof MonetaryValue) {
			return getCurrencyFormat(writer, getCurrencyFormat(row, column, value));
		}
		if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Short)) {
			return getIntegerFormat(writer, getIntegerFormat(row, column, value));
		}
		if ((value instanceof Double) || (value instanceof Float)) {
			return getRealFormat(writer, getRealFormat(row, column, value));
		}

		return null;
	}

	/**
	 * Returns the ID of the format or creates a new one if required.
	 * @param writer writer that provides the workbook
	 * @param format format to be used
	 * @return ID of format
	 */
	protected Short getDateFormat(ExcelWriter writer, String format) {
		Short rc = dateFormat.get(format);
		if (rc == null) {
			DataFormat formatHelper = writer.getWorkbook().createDataFormat();
			rc = formatHelper.getFormat(format);
			dateFormat.put(format, rc);
		}
		return rc;
	}

	/**
	 * Returns the ID of the format or creates a new one if required.
	 * @param writer writer that provides the workbook
	 * @param format format to be used
	 * @return ID of format
	 */
	protected Short getCurrencyFormat(ExcelWriter writer, String format) {
		Short rc = currencyFormat.get(format);
		if (rc == null) {
			DataFormat formatHelper = writer.getWorkbook().createDataFormat();
			rc = formatHelper.getFormat(format);
			currencyFormat.put(format, rc);
		}
		return rc;
	}

	/**
	 * Returns the ID of the format or creates a new one if required.
	 * @param writer writer that provides the workbook
	 * @param format format to be used
	 * @return ID of format
	 */
	protected Short getIntegerFormat(ExcelWriter writer, String format) {
		Short rc = intFormat.get(format);
		if (rc == null) {
			DataFormat formatHelper = writer.getWorkbook().createDataFormat();
			rc = formatHelper.getFormat(format);
			intFormat.put(format, rc);
		}
		return rc;
	}

	/**
	 * Returns the ID of the format or creates a new one if required.
	 * @param writer writer that provides the workbook
	 * @param format format to be used
	 * @return ID of format
	 */
	protected Short getRealFormat(ExcelWriter writer, String format) {
		Short rc = intFormat.get(format);
		if (rc == null) {
			DataFormat formatHelper = writer.getWorkbook().createDataFormat();
			rc = formatHelper.getFormat(format);
			realFormat.put(format, rc);
		}
		return rc;
	}

	/**
	 * Returns the default format for dates.
	 * This implementation returns {@link #DEFAULT_DATETIME_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_DATETIME_FORMAT
	 */
	public String getDateTimeFormat(int row, int column, Object value) {
		return DEFAULT_DATETIME_FORMAT;
	}

	/**
	 * Returns the default format for currencies.
	 * This implementation returns "#,##0.00\\ _" plus the symbol of the currency.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return currency formats
	 */
	public String getCurrencyFormat(int row, int column, Object value) {
		Currency currency = (value instanceof MonetaryValue) ? ((MonetaryValue)value).getCurrency() : Currency.getInstance(Locale.getDefault());
		return "#,##0.00\\ \""+currency.getSymbol()+"\"_";
	}
	
	/**
	 * Returns the default format for dates.
	 * This implementation returns {@link #DEFAULT_DATE_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_DATE_FORMAT
	 */
	public String getDateFormat(int row, int column, Object value) {
		return DEFAULT_DATE_FORMAT;
	}

	/**
	 * Returns the default format for shorts, integers and longs.
	 * This implementation returns {@link #DEFAULT_INTEGER_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_INTEGER_FORMAT
	 */
	public String getIntegerFormat(int row, int column, Object value) {
		return DEFAULT_INTEGER_FORMAT;
	}

	/**
	 * Returns the default format for real and float numbers.
	 * This implementation returns {@link #DEFAULT_REAL_FORMAT}.
	 * @param row the row that this format will be used for
	 * @param column the column that this format will be used for
	 * @param value the value that this format will be used for
	 * @return date formats
	 * @see #DEFAULT_REAL_FORMAT
	 */
	public String getRealFormat(int row, int column, Object value) {
		return DEFAULT_REAL_FORMAT;
	}
	/**
	 * Returns the background color for the specified cell.
	 * This implementation returns null (default background color).
	 * You can use IndexedColors.LIGHT_GREEN.getIndex() to return the color.
	 * Notice that background colors is somehow misleading as foreground and background
	 * color build up a cell's background (behind the text itself).
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return color index for Excel or null
	 */
	public Short getBackgroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the fill pattern for the background.
	 * This implementation returns CellStyle.SOLID_FOREGROUND if a foreground color was set.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return the fill pattern or null if default pattern shall be applied.
	 */
	public FillPatternType getFillPattern(ExcelWriter writer, int row, int column, Object value) {
		if (getForegroundColor(writer, row, column, value) != null) return FillPatternType.SOLID_FOREGROUND;
		return null;
	}

	/**
	 * Returns the foreground color for the specified cell.
	 * This implementation returns null (default foreground color).
	 * You can use IndexedColors.LIGHT_GREEN.getIndex() to return the color.
	 * This is the correct implementation if you want to set the cell's color.
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return color index for Excel or null
	 */
	public Short getForegroundColor(ExcelWriter writer, int row, int column, Object value) {
		return null;
	}

	/**
	 * Returns the correct font for the cell.
	 * This implementation will return bold font for the first row if required and
	 * hyperlink fonts for hyperlink cells.
	 * An overwritten implementation could look like this:
	 * <pre>
	 * font = writer.getWorkbook().createFont();
	 * font.setBoldweight(Font.BOLDWEIGHT_BOLD);
	 * font.setColor(IndexedColors.BLACK.getIndex());
	 * font.setFontHeightInPoints((short)10);
	 * font.setFontName("Arial");
	 * </pre>
	 * @param writer writer that requires the information
	 * @param row row index
	 * @param column column index
	 * @param value value in cell
	 * @return correct font or null if no special font is required.
	 * @see #getBoldFont(Workbook, int, int, Object)
	 * @see #getPlainFont(Workbook, int, int, Object)
	 * @see #getHyperlinkFont(Workbook, int, int, Object)
	 */
	public Font getFont(ExcelWriter writer, int row, int column, Object value) {
		if (emphasizeFirstRow && (row ==0)) return getBoldFont(writer.getWorkbook(), row, column, value);
		if (getHyperlink(writer, row, column, value) != null) {
			return getHyperlinkFont(writer.getWorkbook(), row, column, value);
		}
		return getPlainFont(writer.getWorkbook(), row, column, value);
	}

	/**
	 * Returns the font size to be used.
	 * @return the default font size
	 * @see #DEFAULT_FONT_SIZE
	 */
	public short getDefaultFontSize() {
		return defaultFontSize;
	}

	/**
	 * Returns the font color to be used in non-hyperlink cells.
	 * @return the font color
	 * @see #DEFAULT_FONT_COLOR
	 */
	public short getDefaultFontColor() {
		return defaultFontColor;
	}

	/**
	 * Returns the font color to be used for hyperlinks.
	 * @return the hyperlink color
	 */
	public short getDefaultHyperlinkColor() {
		return defaultHyperlinkColor;
	}

	/**
	 * Returns the font name to be used.
	 * @return the font name
	 */
	public String getDefaultFontName() {
		return defaultFontName;
	}

	/**
	 * Returns the setting of emphasizing the header row.
	 * @return the emphasizeFirstRow
	 */
	public boolean isEmphasizeFirstRow() {
		return emphasizeFirstRow;
	}

	/**
	 * Sets the property of emphasizing header rows.
	 * @param emphasizeFirstRow the emphasizeFirstRow to set
	 */
	public void setEmphasizeFirstRow(boolean emphasizeFirstRow) {
		this.emphasizeFirstRow = emphasizeFirstRow;
	}

	/**
	 * Returns the bold font used for header rows.
	 * This implementation returns the font returned by 
	 * {@link #getDefaultBoldFont(Workbook)}.
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @param workbook the workbook for creation
	 * @return the bold Font for this cell
	 * @see #getDefaultBoldFont(Workbook)
	 */
	public Font getBoldFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultBoldFont(workbook);
	}

	/**
	 * Returns the default bold font.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultFontColor()} with bold weight.
	 * @param workbook workbook object for creation
	 * @return default bold font
	 */
	public Font getDefaultBoldFont(Workbook workbook) {
		if (defaultBoldFont == null) {
			defaultBoldFont = workbook.createFont();
			defaultBoldFont.setBold(true);
			defaultBoldFont.setColor(getDefaultFontColor());
			defaultBoldFont.setFontHeightInPoints(getDefaultFontSize());
			defaultBoldFont.setFontName(getDefaultFontName());
		}
		return defaultBoldFont;
	}

	/**
	 * Returns the default font used for normal cells.
	 * This implementation returns the font defined by ,
	 * {@link #getDefaultPlainFont(Workbook)}.
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @param workbook the workbook for creation
	 * @return the font for this cell
	 */
	public Font getPlainFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultPlainFont(workbook);
	}

	/**
	 * Returns the default font used for normal cells.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultFontColor()} with normal weight.
	 * @param workbook the workbook for creation
	 * @return the default plain font
	 */
	public Font getDefaultPlainFont(Workbook workbook) {
		if (defaultPlainFont == null) {
			defaultPlainFont = workbook.createFont();
			defaultPlainFont.setBold(false);
			defaultPlainFont.setColor(getDefaultFontColor());
			defaultPlainFont.setFontHeightInPoints(getDefaultFontSize());
			defaultPlainFont.setFontName(getDefaultFontName());
		}
		return defaultPlainFont;
	}

	/**
	 * Returns the font to be used for hyperlinks.
	 * This implementation returns the font defined by 
	 * {@link #getDefaultHyperlinkFont(Workbook)}. 
	 * @param workbook the workbook for creation
	 * @param row the row that this font will be used for
	 * @param column the column that this font will be used for
	 * @param value the value that this font will be used for
	 * @return font object
	 */
	public Font getHyperlinkFont(Workbook workbook, int row, int column, Object value) {
		return getDefaultHyperlinkFont(workbook);
	}

	/**
	 * Returns the font to be used for hyperlinks.
	 * This implementation returns the font defined by {@link #getDefaultFontName()},
	 * {@link #getDefaultFontSize()} and {@link #getDefaultHyperlinkColor()}. This font will
	 * be underlined with normal weight.
	 * @param workbook the workbook for creation
	 * @return font object for hyperlinks
	 */
	public Font getDefaultHyperlinkFont(Workbook workbook) {
		if (defaultHyperlinkFont == null) {
			defaultHyperlinkFont = workbook.createFont();
			defaultHyperlinkFont.setBold(false);
			defaultHyperlinkFont.setColor(getDefaultHyperlinkColor());
			defaultHyperlinkFont.setFontHeightInPoints(getDefaultFontSize());
			defaultHyperlinkFont.setFontName(getDefaultFontName());
			defaultHyperlinkFont.setUnderline(Font.U_SINGLE);
		}
		return defaultHyperlinkFont;
	}

	public Short getTopBorderColor(ExcelWriter writer, int row, int column, Object value) {
		return borderColor;
	}

	public Short getLeftBorderColor(ExcelWriter writer, int row, int column, Object value) {
		return borderColor;
	}

	public Short getRightBorderColor(ExcelWriter writer, int row, int column, Object value) {
		return borderColor;
	}

	public Short getBottomBorderColor(ExcelWriter writer, int row, int column, Object value) {
		return borderColor;
	}

	public BorderStyle getTopBorderThickness(ExcelWriter writer, int row, int column, Object value) {
		return borderThickness;
	}

	public BorderStyle getLeftBorderThickness(ExcelWriter writer, int row, int column, Object value) {
		return borderThickness;
	}

	public BorderStyle getRightBorderThickness(ExcelWriter writer, int row, int column, Object value) {
		return borderThickness;
	}

	public BorderStyle getBottomBorderThickness(ExcelWriter writer, int row, int column, Object value) {
		return borderThickness;
	}

	/**
	 * @param borderColor the borderColor to set
	 */
	public void setDefaultBorderColor(Short borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @param borderThickness the borderThickness to set
	 */
	public void setDefaultBorderThickness(BorderStyle borderThickness) {
		this.borderThickness = borderThickness;
	}

	public boolean isTextWrap(ExcelWriter writer, int row, int column, Object value) {
		return value instanceof String;
	}

	/** Describes a style for a cell. */
	protected static class StyleDescription {

		private Short format;
		private Short fgColor;
		private FillPatternType fillPattern;
		private Short bgColor;
		private Font font;
		private HorizontalAlignment alignment;
		private Short topBorderColor;
		private BorderStyle topBorderThickness;
		private Short leftBorderColor;
		private BorderStyle leftBorderThickness;
		private Short rightBorderColor;
		private BorderStyle rightBorderThickness;
		private Short bottomBorderColor;
		private BorderStyle bottomBorderThickness;
		private boolean textWrap;

		/**
		 * Constructor.
		 */
		public StyleDescription() {
			this (null, null, null, null, null, null, null, null, null, null, null, null, null, null, false);
		}

		/**
		 * Constructor.
		 * @param format - ID of format
		 * @param fgColor - ID of foreground color
		 * @param fillPattern - ID of fill pattern
		 * @param bgColor - ID of background color
		 * @param font - ID of font
		 * @param alignment - ID of alignment
		 * @param topBorderColor - ID of color for top border
		 * @param topBorderThickness - ID of thickness for top border
		 * @param leftBorderColor - ID of color for left border
		 * @param leftBorderThickness - ID of thickness for left border
		 * @param rightBorderColor - ID of color for right border
		 * @param rightBorderThickness - ID of thickness for right border
		 * @param bottomBorderColor - ID of color for bottom border
		 * @param bottomBorderThickness - ID of thickness for bottom border
		 * @param textWrap - ID of wrapping style
		 */
		public StyleDescription(Short format, Short fgColor, FillPatternType fillPattern,
				Short bgColor, Font font, HorizontalAlignment alignment,
				Short topBorderColor, BorderStyle topBorderThickness,
				Short leftBorderColor, BorderStyle leftBorderThickness,
				Short rightBorderColor, BorderStyle rightBorderThickness,
				Short bottomBorderColor, BorderStyle bottomBorderThickness,
				boolean textWrap) {
			super();
			this.format = format;
			this.fgColor = fgColor;
			this.fillPattern = fillPattern;
			this.bgColor = bgColor;
			this.font = font;
			this.alignment = alignment;
			this.topBorderColor = topBorderColor;
			this.topBorderThickness = topBorderThickness;
			this.leftBorderColor = leftBorderColor;
			this.leftBorderThickness = leftBorderThickness;
			this.rightBorderColor = rightBorderColor;
			this.rightBorderThickness = rightBorderThickness;
			this.bottomBorderColor = bottomBorderColor;
			this.bottomBorderThickness = bottomBorderThickness;
			this.textWrap = textWrap;
		}

		/**
		 * Returns the format.
		 * @return the format
		 */
		public Short getFormat() {
			return format;
		}

		/**
		 * Sets the format.
		 * @param format the format to set
		 */
		public void setFormat(Short format) {
			this.format = format;
		}

		/**
		 * Returns the foreground color.
		 * @return the fgColor
		 */
		public Short getFgColor() {
			return fgColor;
		}

		/**
		 * Sets the foreground color.
		 * @param fgColor the fgColor to set
		 */
		public void setFgColor(Short fgColor) {
			this.fgColor = fgColor;
		}

		/**
		 * Returns the fill pattern.
		 * @return the fillPattern
		 */
		public FillPatternType getFillPattern() {
			return fillPattern;
		}

		/**
		 * Sets the fill pattern.
		 * @param fillPattern the fillPattern to set
		 */
		public void setFillPattern(FillPatternType fillPattern) {
			this.fillPattern = fillPattern;
		}

		/**
		 * Returns the background color.
		 * @return the bgColor
		 */
		public Short getBgColor() {
			return bgColor;
		}

		/**
		 * Sets the background color.
		 * @param bgColor the bgColor to set
		 */
		public void setBgColor(Short bgColor) {
			this.bgColor = bgColor;
		}

		/**
		 * Returns the text font.
		 * @return the font
		 */
		public Font getFont() {
			return font;
		}

		/**
		 * Sets the text font.
		 * @param font the font to set
		 */
		public void setFont(Font font) {
			this.font = font;
		}

		/**
		 * Returns the alignment.
		 * @return the alignment
		 */
		public HorizontalAlignment getAlignment() {
			return alignment;
		}

		/**
		 * Sets the alignment.
		 * @param alignment the alignment to set
		 */
		public void setAlignment(HorizontalAlignment alignment) {
			this.alignment = alignment;
		}

		/**
		 * Returns the color of the top border.
		 * @return the topBorderColor
		 */
		public Short getTopBorderColor() {
			return topBorderColor;
		}

		/**
		 * Sets the color of the top border.
		 * @param topBorderColor the topBorderColor to set
		 */
		public void setTopBorderColor(Short topBorderColor) {
			this.topBorderColor = topBorderColor;
		}

		/**
		 * Returns the thickness of the top border.
		 * @return the topBorderThickness
		 */
		public BorderStyle getTopBorderThickness() {
			return topBorderThickness;
		}

		/**
		 * Sets the thickness of the top border.
		 * @param topBorderThickness the topBorderThickness to set
		 */
		public void setTopBorderThickness(BorderStyle topBorderThickness) {
			this.topBorderThickness = topBorderThickness;
		}

		/**
		 * Returns the color of the left border.
		 * @return the leftBorderColor
		 */
		public Short getLeftBorderColor() {
			return leftBorderColor;
		}

		/**
		 * Sets the color of the left border.
		 * @param leftBorderColor the leftBorderColor to set
		 */
		public void setLeftBorderColor(Short leftBorderColor) {
			this.leftBorderColor = leftBorderColor;
		}

		/**
		 * Returns the thickness of the left border.
		 * @return the leftBorderThickness
		 */
		public BorderStyle getLeftBorderThickness() {
			return leftBorderThickness;
		}

		/**
		 * Sets the thickness of the left border.
		 * @param leftBorderThickness the leftBorderThickness to set
		 */
		public void setLeftBorderThickness(BorderStyle leftBorderThickness) {
			this.leftBorderThickness = leftBorderThickness;
		}

		/**
		 * Sets the color of the right border.
		 * Returns the color of the right border.
		 * @return the rightBorderColor
		 */
		public Short getRightBorderColor() {
			return rightBorderColor;
		}

		/**
		 * @param rightBorderColor the rightBorderColor to set
		 */
		public void setRightBorderColor(Short rightBorderColor) {
			this.rightBorderColor = rightBorderColor;
		}

		/**
		 * Returns the thickness of the right border.
		 * @return the rightBorderThickness
		 */
		public BorderStyle getRightBorderThickness() {
			return rightBorderThickness;
		}

		/**
		 * Sets the thickness of the right border.
		 * @param rightBorderThickness the rightBorderThickness to set
		 */
		public void setRightBorderThickness(BorderStyle rightBorderThickness) {
			this.rightBorderThickness = rightBorderThickness;
		}

		/**
		 * Returns the color of the bottom border.
		 * @return the bottomBorderColor
		 */
		public Short getBottomBorderColor() {
			return bottomBorderColor;
		}

		/**
		 * Sets the color of the bottom border.
		 * @param bottomBorderColor the bottomBorderColor to set
		 */
		public void setBottomBorderColor(Short bottomBorderColor) {
			this.bottomBorderColor = bottomBorderColor;
		}

		/**
		 * Returns the thickness of the bottom border.
		 * @return the bottomBorderThickness
		 */
		public BorderStyle getBottomBorderThickness() {
			return bottomBorderThickness;
		}

		/**
		 * Sets the thickness of the bottom border.
		 * @param bottomBorderThickness the bottomBorderThickness to set
		 */
		public void setBottomBorderThickness(BorderStyle bottomBorderThickness) {
			this.bottomBorderThickness = bottomBorderThickness;
		}


		/**
		 * Returns whether text is wrapped in cell.
		 * @return the textWrap
		 */
		public boolean isTextWrap() {
			return textWrap;
		}

		/**
		 * Sets whether text is wrapped in cell.
		 * @param textWrap the textWrap to set
		 */
		public void setTextWrap(boolean textWrap) {
			this.textWrap = textWrap;
		}

		/**
		 * Applies the style to the cell.
		 * @param style the style to be set from this description
		 */
		public void applyStyle(CellStyle style) {
			if (format != null) style.setDataFormat(format);

			if (fgColor != null) {
				style.setFillForegroundColor(fgColor);		
				if (fillPattern != null) style.setFillPattern(fillPattern);
			}
			if (bgColor != null) style.setFillBackgroundColor(bgColor);
			if (font != null) style.setFont(font);
			if (alignment != null) style.setAlignment(alignment);
			// Borders;
			if (topBorderColor != null) style.setTopBorderColor(topBorderColor);
			if (leftBorderColor != null) style.setLeftBorderColor(leftBorderColor);
			if (rightBorderColor != null) style.setRightBorderColor(rightBorderColor);
			if (bottomBorderColor != null) style.setBottomBorderColor(bottomBorderColor);
			if (topBorderThickness != null) style.setBorderTop(topBorderThickness);
			if (leftBorderThickness != null) style.setBorderLeft(leftBorderThickness);
			if (rightBorderThickness != null) style.setBorderRight(rightBorderThickness);
			if (bottomBorderThickness != null) style.setBorderBottom(bottomBorderThickness);
			style.setWrapText(isTextWrap());
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;
			return obj.toString().equals(toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			StringBuilder rc = new StringBuilder();
			rc.append(getFormat()); ; rc.append(':');
			rc.append(getFgColor()); rc.append(':');
			rc.append(getFillPattern()); rc.append(':');
			rc.append(getBgColor()); rc.append(':');
			Font font = getFont();
			if (font != null) {
				rc.append(font.getIndex());
			} else {
				rc.append("null");
			}
			rc.append(':');
			rc.append(getAlignment()); rc.append(':');
			rc.append(getTopBorderColor()); rc.append(':');
			rc.append(getTopBorderThickness()); rc.append(':');
			rc.append(getLeftBorderColor()); rc.append(':');
			rc.append(getLeftBorderThickness()); rc.append(':');
			rc.append(getRightBorderColor()); rc.append(':');
			rc.append(getRightBorderThickness()); rc.append(':');
			rc.append(getBottomBorderColor()); rc.append(':');
			rc.append(getBottomBorderThickness()); ; rc.append(':');
			rc.append(isTextWrap());
			return rc.toString();
		}

		protected static final String DEFAULT_DESC = "null:null:null:null:null:null:null:null:null:null:null:null:null:null:false";

		public boolean isDefault() {
			return toString().equalsIgnoreCase(DEFAULT_DESC);

		}
	}
}
