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

import csv.mapper.StreamMapper;
import csv.mapper.StringMappings;

/**
 * Writer implementation for XML streams.
 * @author ralph
 *
 */
public class XmlWriter extends AbstractStreamTableWriter {

	private String topLevelTagName = "table";
	private String rowTagName = "record";
	private String columnTagName = "column";
	private boolean useColumnNameTags = false;
	private String columnNameAttribute = "name";
	private String columnTypeAttribute = "type";
	private boolean headerWritten = false;
	private String encoding = "UTF-8";
	private Object headerColumns[] = null;
	private boolean writeHeaderRow = false;
	
	/**
	 * Constructor.
	 */
	public XmlWriter() {
	}

	/**
	 * Constructor.
	 * @param out output stream
	 */
	public XmlWriter(OutputStream out) {
		this(out, false);
	}

	/**
	 * Constructor.
	 * @param out output stream
	 * @param writeHeaderRow whether a dedicated header row will be received as first row
	 */
	public XmlWriter(OutputStream out, boolean writeHeaderRow) {
		super(out);
		setWriteHeaderRow(writeHeaderRow);
	}

	/**
	 * Constructor.
	 * @param file output file
	 * @throws IOException if exception occurs
	 */
	public XmlWriter(File file) throws IOException {
		this(file, false);
	}

	/**
	 * Constructor.
	 * @param file output file
	 * @param writeHeaderRow whether a dedicated header row will be received as first row
	 * @throws IOException if exception occurs
	 */
	public XmlWriter(File file, boolean writeHeaderRow) throws IOException {
		super(file);
		setWriteHeaderRow(writeHeaderRow);
	}

	/**
	 * Constructor.
	 * @param file file to write
	 * @throws IOException if exception occurs
	 */
	public XmlWriter(String file) throws IOException {
		this(file, false);
	}

	/**
	 * Constructor.
	 * @param file file to write
	 * @param writeHeaderRow whether a dedicated header row will be received as first row
	 * @throws IOException if exception occurs
	 */
	public XmlWriter(String file, boolean writeHeaderRow) throws IOException {
		super(file);
		setWriteHeaderRow(writeHeaderRow);
	}

	/**
	 * Prints the closing of the XML file before closing the stream.
	 * @see csv.impl.AbstractStreamTableWriter#close()
	 */
	@Override
	public void close() {
		printFooter();
		super.close();
	}

	/**
	 * Initializes the writer.
	 * @see csv.impl.AbstractTableWriter#init()
	 */
	@Override
	protected void init() {
		super.init();
		headerWritten = false;
		headerColumns = null;
		setMapper(new StreamMapper(new StringMappings()));
	}

	/**
	 * Prints the row into the stream.
	 * The first row must contain the column names if {@link #setWriteHeaderRow(boolean)} was set.
	 * @see csv.TableWriter#printRow(java.lang.Object[])
	 */
	@Override
	public void printRow(Object[] columns) throws IOException {
		if (!headerWritten) printHeader();
		
		if (isWriteHeaderRow() && (getRowCount() == 0)) {
			setHeaderColumns(columns);
		} else {
			getWriter().println("\t<"+getRowTagName()+">");
			for (int i=0; i<columns.length; i++) {
				getWriter().print("\t\t"+getColumnStartTag(i, columns[i]));
				if (columns[i] != null) {
					getWriter().print(encode(convert(columns[i])));
					getWriter().println(getColumnEndTag(i));
				}
			}
			getWriter().println("\t</"+getRowTagName()+">");
		}
		incrementRowCount();
	}

	/**
	 * Returns the complete start tag for the column.
	 * @param column index of column
	 * @param value value in column
	 * @return start tag, will include combined closing tag if value is null
	 */
	protected String getColumnStartTag(int column, Object value) {
		String tagName = getColumnTagName(column);
		String attributes = getColumnTagAttributes(column, value);
		StringBuilder rc = new StringBuilder();
		rc.append('<');
		rc.append(tagName);
		if (attributes != null) rc.append(attributes);
		if (value == null) rc.append('/');
		rc.append('>');
		return rc.toString();
	}
	
	/**
	 * Returns column's closing tag
	 * @param column column index
	 * @return closing column tag
	 */
	protected String getColumnEndTag(int column) {
		String tagName = getColumnTagName(column);
		StringBuilder rc = new StringBuilder();
		rc.append("</");
		rc.append(tagName);
		rc.append('>');
		return rc.toString();
	}

	/**
	 * Returns correct column name.
	 * @param column index of column
	 * @return column name
	 * @see #isUseColumnNameTags()
	 */
	protected String getColumnTagName(int column) {
		if (!isUseColumnNameTags()) return getColumnTagName();
		return getColumnName(column);
	}
	
	/**
	 * Returns all attributes that need to be set for a column opening tag.
	 * @param column index of column
	 * @param value value of column
	 * @return all attributes
	 */
	protected String getColumnTagAttributes(int column, Object value) {
		StringBuilder rc = new StringBuilder();
		
		// Name of column
		if (!isUseColumnNameTags()) {
			rc.append(' ');
			rc.append(getColumnNameAttribute());
			rc.append("=\"");
			rc.append(encode(getColumnName(column)));
			rc.append("\"");
		}
		
		// Type of column
		if (value != null) {
			rc.append(' ');
			rc.append(getColumnTypeAttribute());
			rc.append("=\"");
			rc.append(encode(getColumnType(value)));
			rc.append("\"");
		}
		if (rc.length() > 0) return rc.toString();
		return null;
	}
	
	/**
	 * Returns the type of the value to be set as attribute in column opening tag.
	 * @param value value of column
	 * @return column type
	 */
	protected String getColumnType(Object value) {
		return value.getClass().getName();
	}
	
	/**
	 * Prints the comment into the stream.
	 * @see csv.impl.AbstractTableWriter#printComment(java.lang.String)
	 */
	@Override
	public void printComment(String comment) throws IOException {
		getWriter().println("<!--"+comment+"-->");
	}


	/**
	 * Prints the comment at the given location.
	 * @see csv.impl.AbstractTableWriter#printComment(java.lang.String, int, int)
	 */
	@Override
	public void printComment(String comment, int row, int column) throws IOException {
		printComment(comment);
	}

	/**
	 * Prints the header.
	 */
	protected void printHeader() {
		if (headerWritten) return;
		getWriter().println("<?xml version=\"1.0\" encoding=\""+getEncoding()+"\"?>");
		getWriter().println("<"+getTopLevelTagName()+">");
		headerWritten = true;
	}
	
	/**
	 * Prints the footer.
	 */
	protected void printFooter() {
		if (!headerWritten) printHeader();
		getWriter().println("</"+getTopLevelTagName()+">");
	}
	
	/**
	 * Encodes the object for usage in XML file.
	 * @param s object to encode
	 * @return XML encoded string
	 */
	protected String encode(Object s) {
		return StringEscapeUtils.escapeXml11(s.toString());
	}
	
	/**
	 * Returns the column name at given index.
	 * The method will pass on to {@link #getDefaultColumnName(int)} if no column header can be found.
	 * @param index index of column
	 * @return column name
	 */
	protected String getColumnName(int index) {
		if ((headerColumns == null) || (headerColumns.length <= index) || (headerColumns[index] == null)) return getDefaultColumnName(index);
		return headerColumns[index].toString();
	}
	
	/**
	 * Creates a default name for the column.
	 * The name is "column" plus the index
	 * @param index index of column
	 * @return default column name
	 */
	protected String getDefaultColumnName(int index) {
		return "column"+index;
	}
	
	/**
	 * Returns the XML root tag name.
	 * @return the topLevelTagName
	 */
	public String getTopLevelTagName() {
		return topLevelTagName;
	}

	/**
	 * Sets the XML root tag name.
	 * @param topLevelTagName the topLevelTagName to set
	 */
	public void setTopLevelTagName(String topLevelTagName) {
		this.topLevelTagName = topLevelTagName;
	}

	/**
	 * Returns the name of the row tag.
	 * @return the rowTagName
	 */
	public String getRowTagName() {
		return rowTagName;
	}

	/**
	 * Set the name of the row tag.
	 * @param rowTagName the rowTagName to set
	 */
	public void setRowTagName(String rowTagName) {
		this.rowTagName = rowTagName;
	}

	/**
	 * Returns the name of the column tag.
	 * This property will be used only when {@link #setUseColumnNameTags(boolean)} was not set.
	 * @return the columnTagName
	 */
	public String getColumnTagName() {
		return columnTagName;
	}

	/**
	 * Sets the name of teh column tag.
	 * This property will be used only when {@link #setUseColumnNameTags(boolean)} was not set.
	 * @param columnTagName the columnTagName to set
	 */
	public void setColumnTagName(String columnTagName) {
		this.columnTagName = columnTagName;
	}

	/**
	 * Returns whether column tag names will be equal to column names.
	 * @return the useColumnNameTags
	 */
	public boolean isUseColumnNameTags() {
		return useColumnNameTags;
	}

	/**
	 * Sets whether column tag names will be equal to column names.
	 * @param useColumnNameTags the useColumnNameTags to set
	 */
	public void setUseColumnNameTags(boolean useColumnNameTags) {
		this.useColumnNameTags = useColumnNameTags;
	}

	/**
	 * Returns the name of the attribute within column tag that contains the column name.
 	 * This property will be used only when {@link #setUseColumnNameTags(boolean)} was not set.
	 * @return the columnNameAttribute
	 */
	public String getColumnNameAttribute() {
		return columnNameAttribute;
	}

	/**
	 * Sets the name of the attribute within column tag that contains the column name.
	 * This property will be used only when {@link #setUseColumnNameTags(boolean)} was not set.
	 * @param columnNameAttribute the columnNameAttribute to set
	 */
	public void setColumnNameAttribute(String columnNameAttribute) {
		this.columnNameAttribute = columnNameAttribute;
	}

	
	/**
	 * Returns the name of the attribute in the column tag that contains the Java type of the value.
	 * @return the columnTypeAttribute
	 */
	public String getColumnTypeAttribute() {
		return columnTypeAttribute;
	}

	/**
	 * Sets the name of the attribute in the column tag that contains the Java type of the value.
	 * @param columnTypeAttribute the columnTypeAttribute to set
	 */
	public void setColumnTypeAttribute(String columnTypeAttribute) {
		this.columnTypeAttribute = columnTypeAttribute;
	}

	/**
	 * Returns the encoding string for XML header.
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * Sets the encoding string for XML header.
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * Returns the header column names.
	 * @return the headerColumns
	 */
	public Object[] getHeaderColumns() {
		return headerColumns;
	}

	/**
	 * Sets the header column names explicitely.
	 * @param headerColumns the headerColumns to set
	 */
	public void setHeaderColumns(Object[] headerColumns) {
		this.headerColumns = headerColumns;
	}

	/**
	 * Returns true when the first row being written contains the column names.
	 * @return the writeHeaderRow
	 */
	public boolean isWriteHeaderRow() {
		return writeHeaderRow;
	}

	/**
	 * Sets whether the first row being written contains the column names.
	 * @param writeHeaderRow the writeHeaderRow to set
	 */
	public void setWriteHeaderRow(boolean writeHeaderRow) {
		this.writeHeaderRow = writeHeaderRow;
	}

	
}
