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

import org.apache.commons.text.StringEscapeUtils;

/**
 * Provides implementation for writing HTML table.
 * The HTML table written is controlled by separate templates for each individual tags.
 * <p>
 * <strong>Please note!</strong> This class is mainly for convinience when aou need to create
 * static HTML files. You'd rather use JSP pages when you are in a JSP environment (unless you
 * have good reasons to use this class).
 * </p>
 * @author ralph
 *
 */
public class HtmlWriter extends AbstractStreamTableWriter {

	public static final String DEFAULT_TABLE_TEMPLATE = "<table>\n|</table>\n";
	
	// Header templates
	public static final String DEFAULT_THEAD_TEMPLATE = "\t<thead>\n|\t</thead>\n";
	public static final String DEFAULT_THEAD_TR_TEMPLATE = "\t\t<tr>\n|\t\t</tr>\n";
	public static final String DEFAULT_THEAD_TH_TEMPLATE = "\t\t\t<th class=\"even\">|</th>\n";
	public static final String DEFAULT_THEAD_TH_TEMPLATE2 = "\t\t\t<th class=\"odd\">|</th>\n";
	
	// Body templates
	public static final String DEFAULT_TBODY_TEMPLATE = "\t<tbody>\n|\t</tbody>\n";
	public static final String DEFAULT_TBODY_TR_TEMPLATE = "\t\t<tr class=\"even\">\n|\t\t</tr>\n";
	public static final String DEFAULT_TBODY_TD_TEMPLATE = "\t\t\t<td class=\"even\">|</td>\n";
	public static final String DEFAULT_TBODY_TR_TEMPLATE2 = "\t\t<tr class=\"odd\">\n|\t\t</tr>\n";
	public static final String DEFAULT_TBODY_TD_TEMPLATE2 = "\t\t\t<td class=\"odd\">|</td>\n";

	/** General template (table) */
	private String tableTemplate = DEFAULT_TABLE_TEMPLATE;
	
	/** Header Template (thead) */
	private String headerTemplate = DEFAULT_THEAD_TEMPLATE;
	/** Header Row Template (tr/even rows) */
	private String headerRowTemplate = DEFAULT_THEAD_TR_TEMPLATE;
	/** Header Column Template (th/even columns) */
	private String headerColumnTemplate = DEFAULT_THEAD_TH_TEMPLATE;
	/** Header Column Template (th/odd columns) */
	private String headerColumnTemplate2 = DEFAULT_THEAD_TH_TEMPLATE2;
	
	/** Header Template (tbody) */
	private String bodyTemplate = DEFAULT_TBODY_TEMPLATE;
	/** Header Row Template (tr/even rows) */
	private String bodyRowTemplate = DEFAULT_TBODY_TR_TEMPLATE;
	/** Header Row Template (tr/odd rows) */
	private String bodyRowTemplate2 = DEFAULT_TBODY_TR_TEMPLATE2;
	/** Header Column Template (td/even columns) */
	private String dataColumnTemplate = DEFAULT_TBODY_TD_TEMPLATE;
	/** Header Column Template (th/odd columns) */
	private String dataColumnTemplate2 = DEFAULT_TBODY_TD_TEMPLATE2;
	
	/** whether table header was written */
	private boolean tableHeaderWritten = false;
	/** whether table header was written */
	private boolean tableBodyHeaderWritten = false;
	/** current row index */
	private int currentRowIndex = 0;
	/** has header row? */
	private boolean hasHeaderRow = true;
	
	/**
	 * Default Constructor.
	 */
	public HtmlWriter() {
	}

	/**
	 * @param out output stream
	 */
	public HtmlWriter(OutputStream out) {
		super(out);
	}

	/**
	 * @param file output file
	 * @throws IOException when the file cannot be written
	 */
	public HtmlWriter(File file) throws IOException {
		super(file);
	}

	/**
	 * @param file output filename
	 * @throws IOException when the file cannot be written
	 */
	public HtmlWriter(String file) throws IOException {
		super(file);
	}

	
	/**
	 * Closes the writer by printing the footer of the table.
	 * @see csv.impl.AbstractStreamTableWriter#close()
	 */
	@Override
	public void close() {
		printTableBodyFooter();
		printTableFooter();
		super.close();
	}

	/**
	 * @see csv.impl.AbstractTableWriter#init()
	 */
	@Override
	protected void init() {
		currentRowIndex = 0;
		tableHeaderWritten = false;
		super.init();
	}

	/**
	 * Prints the row.
	 * if the writer was configured to have a header row then first call will write a HTML header row
	 * @param columns columns to be written
	 * @see csv.TableWriter#printRow(java.lang.Object[])
	 */
	@Override
	public void printRow(Object[] columns) throws IOException {
		printTableHeader();
		if ((getRowCount() == 0) && isHasHeaderRow()) {
			// print the header row
			printTableHeadHeader();
			printHeaderRow(columns);
			printTableHeadFooter();
		} else {
			printTableBodyHeader();
			printDataRow(columns, currentRowIndex);
			
			currentRowIndex++;
		}
		incrementRowCount();
		getWriter().flush();
	}

	/**
	 * Prints the header row into the stream.
	 * @param columns columns to be written
	 */
	public void printHeaderRow(Object columns[]) {
		printTableHeadRowHeader();
		for (int i=0; i<columns.length; i++) {
			printTableHeadColumnHeader(i);
			printColumnContent(columns[i], -1, i);
			printTableHeadColumnFooter(i);
		}
		printTableHeadRowFooter();
	}
	
	/**
	 * Prints a data row to the underlying stream
	 * @param columns the column values to be written
	 * @param rowIndex index of data row (not including header row)
	 */
	public void printDataRow(Object columns[], int rowIndex) {
		printTableBodyRowHeader(rowIndex);
		for (int i=0; i<columns.length; i++) {
			printTableDataColumnHeader(rowIndex, i);
			printColumnContent(columns[i], rowIndex, i);
			printTableDataColumnFooter(rowIndex, i);
		}
		printTableBodyRowFooter(rowIndex);
	}
	
	/**
	 * Prints the value of this object only without any enclosing tags.
	 * @param o object to be written
	 * @param rowIndex index of data row (not including header row)
	 * @param columnIndex index of column
	 */
	public void printColumnContent(Object o, int rowIndex, int columnIndex) {
		if (o == null) o = "";
		String s = convert(o).toString();
		
		// HTML cleanup of content
		s = encodeHtml(s);
		if ((s == null) || (s.length() == 0)) s = "&nbsp;";
		
		getWriter().print(s);
	}
	
	/**
	 * Prints the table header into the underlying stream.
	 */
	public void printTableHeader() {
		if (isTableHeaderWritten()) return;
		getWriter().print(getHeader(getTableTemplate()));
		tableHeaderWritten = true;
	}

	/**
	 * Prints the table footer into the underlying stream.
	 */
	public void printTableFooter() {
		if (!tableHeaderWritten) return;
		getWriter().print(getFooter(getTableTemplate()));
		getWriter().flush();
	}

	/**
	 * Prints the table head (thead) header into the underlying stream.
	 */
	public void printTableHeadHeader() {
		getWriter().print(getHeader(getTheadTemplate()));
	}

	/**
	 * Prints the table head (thead) footer into the underlying stream.
	 */
	public void printTableHeadFooter() {
		getWriter().print(getFooter(getTheadTemplate()));
	}
	
	/**
	 * Prints the table body (tbody) header into the underlying stream.
	 */
	public void printTableBodyHeader() {
		if (tableBodyHeaderWritten) return;
		getWriter().print(getHeader(getTbodyTemplate()));
		tableBodyHeaderWritten = true;
	}

	/**
	 * Prints the table body (tbody) footer into the underlying stream.
	 */
	public void printTableBodyFooter() {
		if (!tableBodyHeaderWritten) return;
		getWriter().print(getFooter(getTbodyTemplate()));
	}
	
	/**
	 * Prints the table header row (tr) header into the underlying stream.
	 */
	public void printTableHeadRowHeader() {
		getWriter().print(getHeader(getTheadTrTemplate()));
	}

	/**
	 * Prints the table header row (tr) footer into the underlying stream.
	 */
	public void printTableHeadRowFooter() {
		getWriter().print(getFooter(getTheadTrTemplate()));
	}

	/**
	 * Prints the table header column (th) header into the underlying stream.
	 * @param columnIndex index of column
	 */
	public void printTableHeadColumnHeader(int columnIndex) {
		boolean even = columnIndex % 2 == 0;
		getWriter().print(getHeader(even ? getTheadThTemplate() : getTheadThTemplate2()));
	}

	/**
	 * Prints the table header column (th) footer into the underlying stream.
	 * @param columnIndex index of column
	 */
	public void printTableHeadColumnFooter(int columnIndex) {
		boolean even = columnIndex % 2 == 0;
		getWriter().print(getFooter(even ? getTheadThTemplate() : getTheadThTemplate2()));
	}
	
	/**
	 * Prints the table body row (tr) header into the underlying stream.
	 * @param rowIndex index of data row (not including header row)
	 */
	public void printTableBodyRowHeader(int rowIndex) {
		boolean even = rowIndex % 2 == 0;
		getWriter().print(getHeader(even ? getTbodyTrTemplate() : getTbodyTrTemplate2()));
	}

	/**
	 * Prints the table body row (tr) footer into the underlying stream.
	 * @param rowIndex index of data row (not including header row)
	 */
	public void printTableBodyRowFooter(int rowIndex) {
		boolean even = rowIndex % 2 == 0;
		getWriter().print(getFooter(even ? getTbodyTrTemplate() : getTbodyTrTemplate2()));
	}
	
	/**
	 * Prints the table body data cell (td) header into the underlying stream.
	 * @param rowIndex index of data row (not including header row)
	 * @param columnIndex index of column
	 */
	public void printTableDataColumnHeader(int rowIndex, int columnIndex) {
		boolean even = columnIndex % 2 == 0;
		getWriter().print(getHeader(even ? getTbodyTdTemplate() : getTbodyTdTemplate2()));
	}

	/**
	 * Prints the table body data cell (td) footer into the underlying stream.
	 * @param rowIndex index of data row (not including header row)
	 * @param columnIndex index of column
	 */
	public void printTableDataColumnFooter(int rowIndex, int columnIndex) {
		boolean even = columnIndex % 2 == 0;
		getWriter().print(getFooter(even ? getTbodyTdTemplate() : getTbodyTdTemplate2()));
	}
	
	
	/**
	 * Returns the main table template.
	 * @return the tableTemplate
	 */
	public String getTableTemplate() {
		return tableTemplate;
	}

	/**
	 * Sets the main table template.
	 * @param tableTemplate the tableTemplate to set
	 */
	public void setTableTemplate(String tableTemplate) {
		this.tableTemplate = tableTemplate;
	}

	/**
	 * Returns the header template.
	 * @return the headerTemplate
	 */
	public String getTheadTemplate() {
		return headerTemplate;
	}

	/**
	 * Sets the header template.
	 * @param headerTemplate the headerTemplate to set
	 */
	public void setTheadTemplate(String headerTemplate) {
		this.headerTemplate = headerTemplate;
	}

	/**
	 * Returns the header row template.
	 * @return the headerRowTemplate
	 */
	public String getTheadTrTemplate() {
		return headerRowTemplate;
	}

	/**
	 * Sets the header row template.
	 * @param headerRowTemplate the headerRowTemplate to set
	 */
	public void setTheadTrTemplate(String headerRowTemplate) {
		this.headerRowTemplate = headerRowTemplate;
	}

	/**
	 * Returns the header cell template for even columns.
	 * @return the headerColumnTemplate
	 */
	public String getTheadThTemplate() {
		return headerColumnTemplate;
	}

	/**
	 * Sets the header cell template for even columns.
	 * @param headerColumnTemplate the headerColumnTemplate to set
	 */
	public void setTheadThTemplate(String headerColumnTemplate) {
		this.headerColumnTemplate = headerColumnTemplate;
	}

	/**
	 * Returns the header cell template for odd columns.
	 * @return the headerColumnTemplate2
	 */
	public String getTheadThTemplate2() {
		return headerColumnTemplate2;
	}

	/**
	 * Sets the header cell template for odd columns.
	 * @param headerColumnTemplate2 the headerColumnTemplate2 to set
	 */
	public void setTheadThTemplate2(String headerColumnTemplate2) {
		this.headerColumnTemplate2 = headerColumnTemplate2;
	}

	/**
	 * Returns the table body template.
	 * @return the bodyTemplate
	 */
	public String getTbodyTemplate() {
		return bodyTemplate;
	}

	/**
	 * Sets the body template.
	 * @param bodyTemplate the bodyTemplate to set
	 */
	public void setTbodyTemplate(String bodyTemplate) {
		this.bodyTemplate = bodyTemplate;
	}

	/**
	 * Returns the row template for even data rows.
	 * @return the bodyRowTemplate
	 */
	public String getTbodyTrTemplate() {
		return bodyRowTemplate;
	}

	/**
	 * Sets the row template for even data rows.
	 * @param bodyRowTemplate the bodyRowTemplate to set
	 */
	public void setTbodyTrTemplate(String bodyRowTemplate) {
		this.bodyRowTemplate = bodyRowTemplate;
	}

	/**
	 * Returns the row template for odd data rows.
	 * @return the bodyRowTemplate2
	 */
	public String getTbodyTrTemplate2() {
		return bodyRowTemplate2;
	}

	/**
	 * Sets the row template for odd data rows.
	 * @param bodyRowTemplate2 the bodyRowTemplate2 to set
	 */
	public void setTbodyTrTemplate2(String bodyRowTemplate2) {
		this.bodyRowTemplate2 = bodyRowTemplate2;
	}

	/**
	 * Returns the data cell template for even columns.
	 * @return the dataColumnTemplate
	 */
	public String getTbodyTdTemplate() {
		return dataColumnTemplate;
	}

	/**
	 * Sets the data cell template for even columns.
	 * @param dataColumnTemplate the dataColumnTemplate to set
	 */
	public void setTbodyTdTemplate(String dataColumnTemplate) {
		this.dataColumnTemplate = dataColumnTemplate;
	}

	/**
	 * Returns the data cell template for odd columns.
	 * @return the dataColumnTemplate2
	 */
	public String getTbodyTdTemplate2() {
		return dataColumnTemplate2;
	}

	/**
	 * Sets the data cell template for odd columns.
	 * @param dataColumnTemplate2 the dataColumnTemplate2 to set
	 */
	public void setTbodyTdTemplate2(String dataColumnTemplate2) {
		this.dataColumnTemplate2 = dataColumnTemplate2;
	}

	/**
	 * Returns whether the table header (table tag) was written.
	 * @return the tableHeaderWritten
	 */
	public boolean isTableHeaderWritten() {
		return tableHeaderWritten;
	}

	/**
	 * Returns the current data row index.
	 * @return the currentRowIndex
	 */
	public int getCurrentRowIndex() {
		return currentRowIndex;
	}

	/**
	 * Returns true if this writer will create a header row.
	 * @return the hasHeaderRow
	 */
	public boolean isHasHeaderRow() {
		return hasHeaderRow;
	}

	/**
	 * Sets whether writer will create a header row.
	 * @param hasHeaderRow the hasHeaderRow to set
	 */
	public void setHasHeaderRow(boolean hasHeaderRow) {
		this.hasHeaderRow = hasHeaderRow;
	}

	/**
	 * Makes HTML encoding for the string.
	 * @param s the string ready to be used in HTML
	 * @return HTML encoded string
	 */
	public static String encodeHtml(String s) {
		if (s == null) return null;
		return StringEscapeUtils.escapeHtml4(s);
	}
	
	/**
	 * Returns the header part of the given template
	 * @param template template divided into two parts separated by a pipe character (|)
	 * @return header part of template
	 */
	protected static String getHeader(String template) {
		if (template == null) return null;
		String s[] = template.split("\\|", 2);
		if (s.length == 0) return null;
		return s[0];
	}
	
	/**
	 * Returns the footer part of the given template
	 * @param template template divided into two parts separated by a pipe character (|)
	 * @return footer part of template
	 */
	protected static String getFooter(String template) {
		if (template == null) return null;
		String s[] = template.split("\\|", 2);
		if (s.length == 0) return null;
		if (s.length == 1) return "";
		return s[1];
	}
}
