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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import csv.CsvException;
import csv.mapper.StreamMapper;
import csv.mapper.StringMappings;
import rs.baselib.util.CommonUtils;

/**
 * Reads from a XML file.
 * @author ralph
 *
 */
public class XmlReader extends AbstractStreamTableReader {

	protected static final int END_OF_FILE_INDEX = -1;
	
	private ParserThread parserThread = null;
	private Object nextRow[] = null;
	private String rowTagName = "record";
	private String columnTagName = "column";
	private boolean useColumnNameTags = false;
	private String columnNameAttribute = "name";
	private String columnTypeAttribute = "type";
	
	/**
	 * Constructor.
	 */
	public XmlReader() {
		init();
	}

	/**
	 * Constructor.
	 * @param in stream to read
	 */
	public XmlReader(InputStream in) {
		super(in);
		init();
	}

	/**
	 * Constructor.
	 * @param file fiel to read
	 * @throws FileNotFoundException when the file cannot be found
	 */
	public XmlReader(File file) throws FileNotFoundException {
		super(file);
		init();
	}

	/**
	 * Constructor.
	 * @param file file to read
	 * @throws FileNotFoundException when the file cannot be found
	 */
	public XmlReader(String file) throws FileNotFoundException {
		super(file);
		init();
	}

	/**
	 * Initializes reader.
	 */
	protected void init() {
		setHasHeaderRow(true);
		setMapper(new StreamMapper(new StringMappings()));
	}
	
	/**
	 * Closes the stream.
	 * @see csv.impl.AbstractStreamTableReader#close()
	 */
	@Override
	public void close() {
		// deliver remaining comments
		if (parserThread != null) {
			deliverComments(END_OF_FILE_INDEX);
		}
		super.close();
	}

	/**
	 * Resets the reader. 
	 * @see csv.impl.AbstractStreamTableReader#reset()
	 */
	@Override
	public void reset() {
		if (parserThread != null) {
			parserThread.stopParsing();
			parserThread = null;
		}
		super.reset();
	}

	/**
	 * Returns true if there are more rows.
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (nextRow == null) readNextRow();
		return nextRow != null;
	}

	/**
	 * Returns the next row.
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Object[] next() {
		if (nextRow == null) readNextRow();
		if (nextRow == null) throw new CsvException("End of XML stream reached");
		
		// Before delivering make sure that comments are delivered
		deliverComments();
		
		// Deliver current row
		Object rc[] = nextRow;
		nextRow = null;
		incrementRowCount();
		return rc;
	}

	/**
	 * Reads the next row.
	 */
	protected void readNextRow() {
		if (nextRow != null) return;
		
		// Initialize parser
		if (parserThread == null) initParser();
		
		// Read the next row 
		nextRow = parserThread.next();
	}
	
	/**
	 * Reads the header row.
	 * @see csv.impl.AbstractTableReader#readHeaderRow()
	 */
	@Override
	protected void readHeaderRow() {
		if (parserThread == null) initParser();
		if (isHeaderRowRead()) return;
		
		// Parsing thread has information about column names
		setHeaderRow(parserThread.getColumnNames());
		return;
	}

	/**
	 * Initializes the XML parser thread.
	 */
	protected void initParser() {
		if (parserThread == null) {
			parserThread = new ParserThread();
			parserThread.start();
		}
	}
	
	/**
	 * Handles all comments that were registered for current row.
	 */
	protected void deliverComments() {
		deliverComments(getRowCount());
	}
	
	/**
	 * Handles all comments that were registered for current row.
	 * @param rowIndex the index of the row to be processed
	 */
	protected void deliverComments(int rowIndex) {
		List<String> comments = parserThread.deliverComments(rowIndex);
		if (comments != null) {
			for (String comment : comments) {
				notifyComment(comment, rowIndex, 0);
			}
		}
	}
	
	/**
	 * Returns the row tag name.
	 * @return the rowTagName
	 */
	public String getRowTagName() {
		return rowTagName;
	}

	/**
	 * Sets the row tag name.
	 * @param rowTagName the rowTagName to set
	 */
	public void setRowTagName(String rowTagName) {
		this.rowTagName = rowTagName;
	}

	/**
	 * Returns the column tag name.
	 * @return the columnTagName
	 */
	public String getColumnTagName() {
		return columnTagName;
	}

	/**
	 * Sets the column tag name.
	 * @param columnTagName the columnTagName to set
	 */
	public void setColumnTagName(String columnTagName) {
		this.columnTagName = columnTagName;
	}

	/**
	 * Returns whether column names will be used as column tag names.
	 * @return the useColumnNameTags
	 */
	public boolean isUseColumnNameTags() {
		return useColumnNameTags;
	}

	/**
	 * Sets whether column names will be used as column tag names.
	 * @param useColumnNameTags the useColumnNameTags to set
	 */
	public void setUseColumnNameTags(boolean useColumnNameTags) {
		this.useColumnNameTags = useColumnNameTags;
	}

	/**
	 * Returns the attribute name of column tag that will contain the attribute name.
	 * This information is required only when {@link #isUseColumnNameTags()} returns false.
	 * @return the columnNameAttribute
	 */
	public String getColumnNameAttribute() {
		return columnNameAttribute;
	}

	/**
	 * Sets the attribute name of column tag that will contain the attribute name.
	 * This information is required only when {@link #isUseColumnNameTags()} returns false.
	 * @param columnNameAttribute the columnNameAttribute to set
	 */
	public void setColumnNameAttribute(String columnNameAttribute) {
		this.columnNameAttribute = columnNameAttribute;
	}

	
	/**
	 * Returns the attribute name of column tag that contains the type of value.
	 * The type is the value class and usually being used for type conversion.
	 * @return the columnTypeAttribute
	 */
	public String getColumnTypeAttribute() {
		return columnTypeAttribute;
	}

	/**
	 * Sets the attribute name of column tag that contains the type of value.
	 * The type is the value class and usually being used for type conversion.
	 * @param columnTypeAttribute the columnTypeAttribute to set
	 */
	public void setColumnTypeAttribute(String columnTypeAttribute) {
		this.columnTypeAttribute = columnTypeAttribute;
	}


	/**
	 * The thread doing the XML parse (pushes the handler).
	 * We use reader/writer synchronization pattern to enable pull pattern
	 * for main class.
	 * @author ralph
	 *
	 */
	private class ParserThread extends Thread {
		
		private SAXParser parser = null;
		private CsvException parsingError = null;
		private List<Object[]> availableObjects = new ArrayList<Object[]>();
		private String columnNames[] = null;
		private boolean parsingStopped = false;
		private XmlHandler handler = null;
		private Map<Integer, List<String>> comments = new HashMap<Integer, List<String>>();
		
		public ParserThread() {
		}
		
		/**
		 * Runs the parser.
		 */
		public void run() {
			try {
				SAXParserFactory factory   = SAXParserFactory.newInstance();
				parser = factory.newSAXParser();
				handler = new XmlHandler(this);
				parser.setProperty ("http://xml.org/sax/properties/lexical-handler", handler);
				parser.parse(getInputStream(), handler);
			} catch (Exception e) {
				parsingError = new CsvException(e);
			}

		}
		
		/**
		 * Stops the parser.
		 */
		public void stopParsing() {
			// Actually, the parser cannot be stopped (no way of interacting with SAX)
		}
		
		/**
		 * Returns the column names.
		 * @return array of names
		 */
		public String[] getColumnNames() {
			if (columnNames == null) {
				// get at least one row (but do not remove from queue)
				next(false);
			
				columnNames = handler.getColumnNames();
			}
			return columnNames;
		}
		
		/**
		 * Returns the parsing exception that occurred.
		 * @return parsing exception
		 */
		public CsvException getParsingError() {
			return parsingError;
		}
		
		/**
		 * Delivers the rows registered for given row index
		 * @param rowIndex row index
		 * @return list of comments for this row
		 */
		public List<String> deliverComments(int rowIndex) {
			if (rowIndex == END_OF_FILE_INDEX) {
				// Deliver all comments
				List<String> rc = new ArrayList<String>();
				for (Map.Entry<Integer, List<String>> entry : comments.entrySet()) {
					rc.addAll(entry.getValue());
				}
				comments.clear();
				if (rc.size() > 0) return rc;
				return null;
			}
			return comments.remove(rowIndex);
		}
		
		/**
		 * Registers a comment for given row index.
		 * @param rowIndex index of row
		 * @param comment comment to register
		 */
		public void registerComment(int rowIndex, String comment) {
			List<String> l = comments.get(rowIndex);
			if (l == null) {
				l = new ArrayList<String>();
				comments.put(rowIndex, l);
			}
			l.add(comment);
		}
		
		/**
		 * Delivers the next row.
		 * Used by the {@link XmlReader#readNextRow()}. Method blocks if rows need to be read from stream.
		 */
		public Object[] next() {
			return next(true);
		}
		
		/**
		 * Delivers the next row.
		 * Used by the {@link XmlReader#readNextRow()}. Method blocks if rows need to be read from stream.
		 */
		private synchronized Object[] next(boolean doRemove) {
			Object[] rc = null;
		 
			if (getParsingError() != null) throw getParsingError();
			
			// Return null if parsing was done and no more objects available
			
			while (availableObjects.isEmpty() && !parsingStopped) {
				try {
					wait();
				} catch (InterruptedException e) { }
			}
			if (!availableObjects.isEmpty()) {
				rc = doRemove ? availableObjects.remove(0) : availableObjects.get(0);
			}
			
			notify();
			return rc;
		}

		/**
		 * Adds a new object to the list of available objects.
		 * Used by the {@link XmlHandler}.
		 * @param o Row to be added (null if parsing stopped)
		 */
		public synchronized void addObject(Object o[]) {

			while (availableObjects.size() >= 20) {
				try {
					wait();
				} catch (InterruptedException e) {}
			}
			if (o != null) {
				availableObjects.add(o);
			} else {
				// Parsing stopped
				parsingStopped = true;
			}
			notify();
		}
	}
	
	/**
	 * SAXParser handler.
	 * Handling class for XML tags.
	 * @author ralph
	 *
	 */
	private class XmlHandler extends DefaultHandler2 {

		private ParserThread thread = null;
		private boolean isRow = false;
		private boolean isCollectingChars = false;
		private StringBuilder charBuf = new StringBuilder();
		private String columnName = null;
		private Class<?> columnType = null;
		private List<String> columnNames;
		private Map<String, Object> columnValues;
		private int rowCount;
		
		public XmlHandler(ParserThread thread) {
			this.thread = thread;
		}
		
		/**
		 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
		 */
		@Override
		public void startDocument() throws SAXException {
			columnNames = new ArrayList<String>();
			columnValues = new HashMap<String, Object>();
			rowCount = 0;
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (isRow) {
				// Column starts, save column name and type
				columnName = getColumnName(qName, attributes);
				columnType = getColumnType(attributes);
				
				// start collecting chars 
				isCollectingChars = true;
				charBuf = new StringBuilder();
			} else if (qName.equals(getRowTagName())){
				// Row starts
				isRow = true;
				columnValues.clear();
			}
		}
		
		/**
		 * Returns the correct column name according to configuration
		 * @param tagName tag name being evaluated
		 * @param attr attributes being evaluated
		 * @return correct column name
		 */
		protected String getColumnName(String tagName, Attributes attr) {
			if (isUseColumnNameTags()) return tagName;
			return attr.getValue(getColumnNameAttribute());
		}
		
		/**
		 * Returns the correct column type
		 * @param attr attributes being evaluated
		 * @return correct column type
		 */
		protected Class<?> getColumnType(Attributes attr) throws SAXException {
			try {
				String name = attr.getValue(getColumnTypeAttribute());
				if (CommonUtils.isEmpty(name)) name = "java.lang.String";
				return Class.forName(name);
			} catch (ClassNotFoundException e) {
				throw new SAXException("Cannot find target class", e);
			}
		}
		
		/**
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (isCollectingChars) {
				charBuf.append(ch, start, length);
			}
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			// stop collecting chars when column starts
			if (isCollectingChars) {
				isCollectingChars = false;
				addColumnValue(columnName, columnType, charBuf.toString());
			} else if (isRow) {
				isRow = false;
				thread.addObject(getValueArray());
				rowCount++;
			}
		}
		
		/**
		 * Returns the array for columns to be delivered.
		 * This method ensures that columns are always delievered in same sequence
		 * regardless how they appear in XML stream.
		 * @return array of values for current row
		 */
		protected Object[] getValueArray() {
			Object rc[] = new Object[columnNames.size()];
			
			for (int i=0; i<rc.length; i++) {
				rc[i] = columnValues.get(columnNames.get(i));
			}
			
			return rc;
		}
		
		/**
		 * Adds the value for given column for current row.
		 * @param columnName name of column
		 * @param value value in column
		 */
		protected void addColumnValue(String columnName, Class<?> columnType, String value) {
			Object v = null;
			
			// add the column name to list of known columns
			if (!columnNames.contains(columnName)) columnNames.add(columnName);
			
			// convert the value
			if ((columnType == null) && ((value == null) || (value.length() == 0))) v = null;
			else v = convert(columnType, value);
			
			// Add the value
			columnValues.put(columnName, v);
		}
		
		/**
		 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
		 */
		@Override
		public void endDocument() throws SAXException {
			// Signal end of parsing
			thread.addObject(null);
		}

		/**
		 * Returns the column names from XML
		 * @return names of columns
		 */
		public String[] getColumnNames() {
			String rc[] = new String[columnNames.size()];
			columnNames.toArray(rc);
			return rc;
		}
		
		/**
		 * @see org.xml.sax.ext.DefaultHandler2#comment(char[], int, int)
		 */
		@Override
		public void comment(char[] ch, int start, int length) throws SAXException {
			thread.registerComment(rowCount, new String(ch, start, length));
		}


		
	}
}
