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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import csv.CsvException;
import csv.mapper.StreamMapper;
import csv.mapper.StringMappings;
import csv.util.CSVUtils;

/**
 * Reads from CSV like streams.
 * <p>
 * Use this reader if you want to load a CSV file by creating a {@link java.io.File}
 * and passing it to the constructor.
 * </p>
 * <p>
 * Example:
 * </p>
 * <pre>
java.io.File f = new java.io.File("csv-test.csv");
CSVReader in = new CSVReader(f);
while (in.hasNext()) {
    Object columns[] = in.next();
    // Do something here
}
in.close();
</pre>
 * @author RalphSchuster
 */
public class CSVReader extends AbstractStreamTableReader {

	private static final int MODE_PRE_DELIM = 0;
	private static final int MODE_DATA_DELIM = 1;
	private static final int MODE_DATA_NODELIM = 2;
	private static final int MODE_POST_DELIM = 3;
	private static final int MODE_COMMENT = 4;

	private String columnDelimiter = "\"";
	private char columnSeparator =  ';';
	private Iterator<Object[]> rowIterator;
	private boolean ignoreComments = true;
	private char commentChars[] = new char[] { '#', ';', '!' };
	private boolean ignoreEmptyLines = true;
	private BufferedReader argReader;

	/**
	 * Default constructor.
	 */
	public CSVReader() {
		init();
	}

	/** 
	 * Creates a new instance of CSVReader.
	 * @param in the object delivering the CSV stream.
	 * 
	 */
	public CSVReader(InputStream in) {
		super(in);
		init();
	}

	/** 
	 * Creates a new instance of CSVReader.
	 * @param file CSV file to read from
	 * @throws FileNotFoundException - when the file could not be found.
	 * 
	 */
	public CSVReader(File file) throws FileNotFoundException {
		super(file);
		init();
	}

	/** 
	 * Creates a new instance of CSVReader.
	 * @param file CSV file to read from
	 * @throws FileNotFoundException - when the file could not be found.
	 * 
	 */
	public CSVReader(String file) throws FileNotFoundException {
		super(file);
		init();
	}

	/** 
	 * Initializes converters.
	 */
	protected void init() {
		setMapper(new StreamMapper(new StringMappings()));
	}
	
	/**
	 * Closes the underlying stream.
	 * @see csv.impl.AbstractStreamTableReader#close()
	 */
	@Override
	public void close() {
		if (argReader != null) try {
			argReader.close();
		} catch (IOException e) {
			throw new CsvException("Cannot close reader", e);
		}
		super.close();
	}

	/**
	 * Returns the underlying reader.
	 * @see csv.impl.AbstractStreamTableReader#getReader()
	 */
	@Override
	protected BufferedReader getReader() {
		if (argReader != null) return argReader;
		return super.getReader();
	}

	/**
	 * Resets the underlying reader.
	 * @see csv.impl.AbstractStreamTableReader#reset()
	 */
	@Override
	public void reset() {
		if (argReader != null) try {
			argReader.reset();
		} catch (IOException e) {
			throw new CsvException("Cannot reset reader", e);
		}
		super.reset();
	}

	/**
	 * Sets the column delimiters to be used. 
	 * The column delimiters can control the length of a column. It is being
	 * used when a column contains special characters (such as the
	 * column separator character or newline). Each character in the given
	 * string can be used to delimit column values.
	 * Default is double-quotes.
	 * @param s new delimiter string
	 */
	public void setColumnDelimiter(String s) {
		columnDelimiter = s;
	}

	/**
	 * Returns the column delimiter to be used.
	 * The column delimiter can control the length of a column. It is being
	 * used when a column contains special characters (such as the
	 * column separator character or newline). Each character in the given
	 * string is being used to delimit column values.
	 * Default is double-quotes.
	 * @return the column delimiter being used
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
	 * @return the column separator character being used.
	 */
	public char getColumnSeparator() {
		return columnSeparator;
	}


	/**
	 * Returns whether comment lines will be ignored.
	 * Default is to ignore comment lines. Ignoing comments means that such lines will not be
	 * delivered as rows but notified via {@link #notifyComment(String, int, int)}.
	 * @return true if comment lines will be ignored
	 */
	public boolean isIgnoreComments() {
		return ignoreComments;
	}

	/**
	 * Controls whether comment lines will be ignored or not.
	 * @param ignoreComments whether comment lines should be ignored
	 */
	public void setIgnoreComments(boolean ignoreComments) {
		this.ignoreComments = ignoreComments;
	}

	/**
	 * Returns the characters that will be regarded as comment starters in first position of a line.
	 * @return String that contains all characters for starting comments
	 */
	public String getCommentChars() {
		return new String(commentChars);
	}

	/**
	 * Sets the characters used to start comment lines.
	 * Comment lines MUST be started at first position in line. Default characters are # and ;
	 * @param commentChars String that contains all characters for comment start
	 */
	public void setCommentChars(String commentChars) {
		this.commentChars = commentChars.toCharArray();
	}

	/**
	 * Returns whether empty lines in a stream will be ignored. Default is TRUE.
	 * @return true if empty lines will be ignored.
	 */
	public boolean isIgnoreEmptyLines() {
		return ignoreEmptyLines;
	}

	/**
	 * Set ignoring of empty lines. Default is TRUE.
	 * @param ignoreEmptyLines controls whether empty lines will be ignored.
	 */
	public void setIgnoreEmptyLines(boolean ignoreEmptyLines) {
		this.ignoreEmptyLines = ignoreEmptyLines;
	}

	/**
	 * Converts a string into its columns according to defined rules.
	 * Returns null if the string is incomplete (not delimited correctly).
	 * @param s String to convert
	 */
	private Object[] convertToColumnArray(StringBuilder s) {
		Object rc[] = null;
		List<String> columns = new ArrayList<String>();
		boolean isIgnoreEmptyLines = isIgnoreEmptyLines();
		
		//System.out.println("=>"+s);
		int len = s.length();
		int i;
		StringBuilder curCol = new StringBuilder();
		StringBuilder comment = new StringBuilder();
		int mode = MODE_PRE_DELIM;
		for (i=0; i<len; i++) {
			char c = s.charAt(i);
			boolean isSeparator = isSeparator(c);
			boolean isLineSeparator = isLineSeparator(c);
			boolean isDelimiter = isDelimiter(c);
			
			switch (mode) {
			case MODE_PRE_DELIM:
				if (isSeparator) {
					columns.add(curCol.toString());
					curCol.setLength(0);
				} else if (isDelimiter) {
					// ignore but set new mode
					mode = MODE_DATA_DELIM;
				} else if (isSpace(c)) {
					// ignore spaces in base mode
				} else if (isCommentChar(c) && (i==0)) {
					// this is a comment
					mode = MODE_COMMENT;
					comment.setLength(0);
				} else if (isLineSeparator) {
					if (i==0) {
						// This is an empty line
						if (!isIgnoreEmptyLines) {
							s.delete(0, i+1);
							return new Object[Math.max(0, getMinimumColumnCount())];
						} 
					} else {
						// an empty value
						columns.add(null);

						// return row now
						rc = convertArray(columns);
						s.delete(0, i+1);
						return rc;
					}
				} else {
					curCol.append(c);
					mode = MODE_DATA_NODELIM;
				}
				break;
			case MODE_POST_DELIM:
				// Wait for separator
				if (isSeparator) {
					columns.add(curCol.toString());
					curCol.setLength(0);
					mode = MODE_PRE_DELIM;
				} else if (isLineSeparator) {
					columns.add(curCol.toString());

					// return row now
					rc = convertArray(columns);
					s.delete(0, i+1);
					return rc;
				}
				break;
			case MODE_DATA_DELIM:
				if (isDelimiter) {
					// next char is delim too?
					if ((i<len-1) && (c == s.charAt(i+1))) {
						// double delim: belongs to value
						curCol.append(c);
						i++;
					} else {
						// single delim: end value
						mode = MODE_POST_DELIM;
					}
				} else {
					// normal character only
					curCol.append(c); 
				}
				break;
			case MODE_DATA_NODELIM:
				if (isSeparator) {
					// end value
					columns.add(curCol.toString().trim());
					curCol.setLength(0);
					mode = MODE_PRE_DELIM;
				} else if (isLineSeparator) {
					// end value
					columns.add(curCol.toString().trim());

					// return row now
					rc = convertArray(columns);
					s.delete(0, i+1);
					return rc;
				} else {
					// normal character only
					curCol.append(c);
				}
				break;
			case MODE_COMMENT:
				if (isLineSeparator) {
					mode = MODE_PRE_DELIM;
					notifyComment(comment.toString().trim(), getLineCount(), -1);
				} else {
					comment.append(c);
				}
				break;
			}
		}

		// Attention! If last column was with delimiter, but did not end with such
		// (not base state again), we need to return null to indicate
		// that more characters are required
		if (mode == MODE_DATA_DELIM) return null;
		columns.add(curCol.toString());

		// return columns
		rc = convertArray(columns);
		s.delete(0, s.length());
		return rc;
	}

	/**
	 * Checks whether given character is a comment character	
	 * @param c character to check
	 * @return true if character is a comment character
	 */
	protected boolean isCommentChar(char c) {
		// Check for comment (";", "!" or "#" at pos 0) and repeat if found
		for (int i=0; i<commentChars.length; i++) {
			if (c == commentChars[i]) return true;
		}
		return false;
	}

	/**
	 * Returns true if character is a separator char.
	 * @param c character to check
	 * @return true if char is separator char
	 */
	protected boolean isSeparator(char c) {
		return (columnSeparator == c);
	}

	/**
	 * Returns true if character is a space char.
	 * @param c character to check
	 * @return true if char is space
	 */
	protected boolean isSpace(char c) {
		return (c == ' ');
	}

	/**
	 * Checks if character is a delimiter character.
	 * @param c character to check.
	 * @return true if character is a delimiter
	 */
	protected boolean isDelimiter(char c) {
		if (columnDelimiter != null) {
			return (columnDelimiter.indexOf(c) >= 0);
		}
		return false;
	}

	/**
	 * Checks if character is a line separator.
	 * @param c character to check.
	 * @return true if character is a line separator
	 */
	protected boolean isLineSeparator(char c) {
		return c == '\n';
	}

	/**
	 * Returns true if there is another CSV row to be read.
	 * @return true if another CSV row is available.
	 */
	@Override
	public boolean hasNext() {
		readHeaderRow();
		return getRowIterator().hasNext();
	}

	/**
	 * Returns next row.
	 * The row is delivered as an array of column string values.
	 * The array will have at least the length defined by
	 * {@link #getMinimumColumnCount()}. 
	 * @return the row as array of columns.
	 */
	public Object[] next() {
		readHeaderRow();
		return getRowIterator().next();
	}

	/**
	 * Reads the header row from next line.
	 * @see csv.impl.AbstractTableReader#readHeaderRow()
	 */
	@Override
	protected void readHeaderRow() {
		if (isHeaderRowRead() || !hasHeaderRow()) return;

		if (getRowIterator().hasNext()) {
			setHeaderRow(CSVUtils.convertArray(getRowIterator().next(), getMinimumColumnCount()));
		}
	}

	/**
	 * Returns the iterator that iterates over rows.
	 * Each row will be returned as an array of strings. 
	 * No conversion will be done.
	 * @return iterator that delivers the CSV rows and columns.
	 */
	private Iterator<Object[]> getRowIterator() {
		if (rowIterator == null) {
			rowIterator = new CSVRowIterator();
		}
		return rowIterator;
	}

	/**
	 * Debugs a string array at standard output stream.
	 * This is for debugging purposes only.
	 * @param idx row index
	 * @param o string array to debug
	 */
	protected static void debug(int idx, String o[]) {
		System.out.print(idx+":");
		for (int i=0; i<o.length; i++) {
			System.out.print(" ["+o[i]+"]");
		}
		System.out.println();
	}

	/**
	 * Iterator that delivers the actual rows and columns.
	 * This private class actually does the meet by reading the underlying
	 * stream, line by line. It separates then rows and columns as
	 * defined by the various parameters.
	 * @author Ralph Schuster
	 *
	 */
	private class CSVRowIterator implements Iterator<Object[]> {

		private ArrayList<String> lineBuffer;

		/**
		 * Constructor.
		 */
		public CSVRowIterator() {
			lineBuffer = new ArrayList<String>();
		}

		/**
		 * Returns true when the buffer has more lines to deliver.
		 * This method also tries to read a new line from the
		 * underlying stream.
		 * @return true when more lines are available.
		 */
		public boolean hasNext() {
			return internalHasNext();
		}

		/**
		 * Internal method to control check of comments.
		 * @param checkComments true if comments must be checked
		 * @return true if a line is available
		 */
		private boolean internalHasNext() {
			if (lineBuffer.size() > 0) return true;

			try {
				boolean doRead = true;
				while (doRead) {
					// This is default
					doRead = false;

					// try to read a line from file
					if (getReader().ready()) {
						String s = getReader().readLine();

						if (s != null) {
							incrementLineCount();
							lineBuffer.add(s);
						}
					}
				}
			} catch (IOException e) {
				throw new CsvException("Cannot read from stream:", e);
			}

			if (lineBuffer.size() > 0) return true;
			return false;
		}


		/**
		 * Delivers the next CSV row.
		 * The method will read from the internal line buffer and
		 * ensures that a single CSV row is complete before it will
		 * be returned.
		 * @return next row of columns
		 */
		public Object[] next() {
			Object o[] = null;
			StringBuilder s = new StringBuilder();
			while (o == null) {
				if (!internalHasNext()) throw new CsvException("No more rows");
				String line = lineBuffer.remove(0);
				//System.out.println("foo=["+line+"], s is now: ["+s+"]");

				// Add the buffer to the line
				s.append(line);
				s.append('\n');

				// Try to get a row from it
				// This method will also take care of comments and strip down the buffer
				// if required
				o = convertToColumnArray(s);
			}
			//System.out.println("returning a row: "+o.length);
			incrementRowCount();
			//debug(getRowCount(), o);
			return o;
		}

		/**
		 * Not suported.
		 */
		public void remove() {
			throw new CsvException("Remove is not supported.");
		}

	}

}
