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
import java.io.PrintWriter;

import csv.mapper.StreamMapper;
import csv.mapper.StringMappings;

/**
 * Implements functionality for writing CSV streams.
 * <p>
 * Example:
 * </p>
<pre>
java.io.File f = new java.io.File("csv-test.csv");
CSVWriter out = new CSVWriter(f);
out.printRow(new Object[] { "0:0", "0:1", "0:2" });
out.printRow(new Object[] { "1:0", "1:1", "1:2" });
out.close();
</pre>
 * @author RalphSchuster
 */
public class CSVWriter extends AbstractStreamTableWriter {
    
    private String columnDelimiter = "\"";
    private char columnSeparator = ';';
    private boolean delimiterRequired = false;
    private String rowSeparator = "\n";
    private int columnCount = 0;
    private char commentChar = '#';
    private PrintWriter argWriter;
    
    /**
     * Default constructor.
     */
    public CSVWriter() {
    }
    
    /**
     * Constructor for writing into a file.
     * @param file file
     * @throws IOException when an exception occurs
     */
	public CSVWriter(File file) throws IOException {
		super(file);
	}

    /**
     * Constructor for writing into a stream.
     * @param out output stream
     */
	public CSVWriter(OutputStream out) {
		super(out);
	}

    /**
     * Constructor for writing into a file.
     * @param file file
     * @throws IOException when an exception occurs
     */
	public CSVWriter(String file) throws IOException {
		super(file);
	}


	/**
	 * Initializes the writer.
	 * @see csv.impl.AbstractStreamTableWriter#init()
	 */
	@Override
	protected void init() {
		super.init();
		columnCount = 0;
		setMapper(new StreamMapper(new StringMappings()));
	}


	/**
	 * Returns the underlying stream.
	 * @see csv.impl.AbstractStreamTableWriter#getWriter()
	 */
	@Override
	public PrintWriter getWriter() {
		if (argWriter != null) return argWriter;
		return super.getWriter();
	}


	/**
	 * Closes the underlying stream.
	 * @see csv.impl.AbstractStreamTableWriter#close()
	 */
	@Override
	public void close() {
		if (argWriter != null) argWriter.close();
		super.close();
	}


	/**
     * Sets the column delimiter to be used. 
     * Default are double-quotes. The usage of the delimiters
     * is defined by {@link #isColumnDelimiterRequired()}.
     * @param s the new delimiter
     */
    public void setColumnDelimiter(String s) {
        columnDelimiter = s;
    }
    
    /**
     * Returns the column delimiter to be used.
     * Default are double-quotes.
     * @return the column delimiter being used.
     */
    public String getColumnDelimiter() {
        return columnDelimiter;
    }
    
    /**
     * Sets the column separator to be used. 
     * Default is semi-colon.
     * @param s new separator character
     */
    public void setColumnSeparator(char s) {
        columnSeparator = s;
    }
    
    /**
     * Returns the column separator to be used. 
     * Default is semi-colon.
     * @return the column separator character being used
     */
    public char getColumnSeparator() {
        return columnSeparator;
    }
    
    /**
     * Sets the row separator to be used.
     * Actually you should never change this character as it will break
     * the definition of a CSV stream. 
     * Default is newline character.
     * @param s new separator
     */
    public void setRowSeparator(String s) {
        rowSeparator = s;
    }
    
    /**
     * Returns the row separator to be used. 
     * Default is a newline character.
     * @return the row separator character.
     */
    public String getRowSeparator() {
        return rowSeparator;
    }
    
    /**
     * Sets if column separators are always required or not.
     * Usually, column delimiters are written only when the
     * column value contains special characters, such as
     * the delimiters for columns and rows.
     * Default value is false.
     * @param b true when delimiters shall always be written.
     */
    public void setColumnDelimiterRequired(boolean b) {
        delimiterRequired = b;
    }
    
    /**
     * Returns whether column delimiters are always required.
     * Default value is false.
     * @return true if column delimiters are written on each column.
     */
    public boolean isColumnDelimiterRequired() {
        return delimiterRequired;
    }
    
    /**
     * Returns the character used for indicating comments
	 * @return the commentChar
	 */
	public char getCommentChar() {
		return commentChar;
	}

	/**
	 * Sets the character to be used for indicating comments
	 * @param commentChar the commentChar to set
	 */
	public void setCommentChar(char commentChar) {
		this.commentChar = commentChar;
	}

	/**
	 * Formats a row for CSV output.
	 * @param columns columns to be prepared
	 * @return string ready to be printed in CSV
	 */
	public String prepareRow(Object columns[]) {
        int i;
        StringBuilder rc = new StringBuilder();
        
        for (i=0; i<columns.length; i++) {
            Object o = columns[i];
            
            if (i != 0) rc.append(getColumnSeparator());
            rc.append(prepareColumn(o));
            
        }
        if (columnCount < columns.length) columnCount = columns.length;
        rc.append(rowSeparator);
        return rc.toString();
	}
	
	/**
     * Prints a new row into the CSV file.
     * This is the method where an actual CSV row will be printed.
     * The columns are prepared to follow the CSV syntax rules
     * and definitions. The underlying stream in flushed immediately.
     * @param columns array of column values.
     */
    public void printRow(Object[] columns) throws IOException {
        getWriter().print(prepareRow(columns));
        getWriter().flush();
        incrementRowCount();
    }
    
    /**
     * Formats a comment for printing
     * @param comment comment to be printed
     * @return string ready to be written to CSV
     */
    public String prepareComment(String comment ) {
    	StringBuilder s = new StringBuilder();
    	s.append(getCommentChar());
    	s.append(' ');
    	s.append(comment);
    	s.append(getRowSeparator());
    	return s.toString();
    }
        
    /**
     * Prints a comment into the CSV stream.
     * @param comment comment to write
     * @throws IOException when an exception occurs
     */
    @Override
    public void printComment(String comment) throws IOException {
    	printComment(comment, 0, 0);
    }
    
    /**
     * Prints a comment into the stream.
     * Note that not all implementations support comments.
     * @param comment comment to write
     * @param row index of row for comment
     * @param column index of column for comment
     * @throws IOException when an exception occurs
     */
    @Override
    public void printComment(String comment, int row, int column) throws IOException {
    	getWriter().print(prepareComment(comment));
    	getWriter().flush();
    }
    
    /**
     * Prepares a column value for output.
     * This function wraps delimiting character if necessary and makes any
     * required replacements within the value.
     * @param s value to wrap and parse
     * @param colIndex index of column
     * @return column value to write to output
     */
    private String prepareColumn(Object o) {
        String rc = "";
        if (o == null) return rc;
        String s = convert(o).toString();
        if ((columnDelimiter != null) && columnNeedsDelimiting(s)) {
            rc = columnDelimiter;
            rc += prepareColumnValue(s);
            rc += columnDelimiter;
        } else {
            rc = s;
        }
        return rc;
    }
    
    /**
     * Decides if a column value needs to be wrapped with delimiters.
     * @param s column value to check
     * @return true if value must be wrapped in output
     */
    protected boolean columnNeedsDelimiting(String s) {
    	// always delimited
        if (delimiterRequired) return true;
        // empty value
        if ((s == null) || (s.length() == 0)) return false;
        // spaces before and/or after the value
        if (!s.trim().equals(s)) return true;
        // column 0 starts with comment
        if (s.charAt(0) == getCommentChar()) return true;
        // value contains row separator
        if (s.indexOf(rowSeparator) >= 0) return true;
        // value contains column separator
        if (s.indexOf(columnSeparator) >= 0) return true;
        // Special Excel fix
        if (s.startsWith("ID") && (getRowCount() == 0)) return true;
        // value contains column delimiter
        return (s.indexOf(columnDelimiter) >= 0);
    }
    
    /**
     * Replaces all occurrences of the delimiter by doubling it.
     * @param s column value to parse
     * @return value with replaced delimiters.
     */
    protected String prepareColumnValue(String s) {
        if (columnDelimiter != null) {
            s = s.replaceAll(columnDelimiter, columnDelimiter+columnDelimiter);
        }
        return s;
    }
    
	/**
	 * Returns the columns written to the stream.
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

    
}
