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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csv.CommentCallback;
import csv.CsvException;
import csv.TableReader;
import csv.mapper.StreamMapper;

/**
 * Abstract implementation that shall be suitable for most implementations.
 * @author ralph
 *
 */
public abstract class AbstractTableReader implements TableReader {

	private List<CommentCallback> commentCallbacks = new ArrayList<>();
    private int rowCount = 0;
    private int lineCount = 0;
	private boolean hasHeaderRow = false;
	private Object headerRow[] = null;
	private boolean headerRowRead = false;
	private int minimumColumnCount = 0;
	private StreamMapper mapper = null;
	private Map<Integer,Class<?>> columnTypes = new HashMap<>();
	
	/**
	 * Default Constructor.
	 */
	public AbstractTableReader() {
	}

	/**
	 * Returns the mapper.
	 * @return the mapper
	 */
	public StreamMapper getMapper() {
		return mapper;
	}

	/**
	 * Sets the mapper.
	 * @param mapper the mapper to set
	 */
	public void setMapper(StreamMapper mapper) {
		this.mapper = mapper;
	}

	/**
     * Opens the CSV reader.
     */
    @Override
    public void open() {
    	rowCount = 0;
    	lineCount = 0;
    	headerRow = null;
    	setHeaderRowRead(false);
    }
    
    /**
     * Resets the CSV reader and its underlying stream.
     */
    @Override
    public void reset() {
    	rowCount = 0;
    	lineCount = 0;
    	headerRow = null;
    	setHeaderRowRead(false);
    }
    
	/**
	 * Returns the header row.
	 * @return header row if such was defined.
	 */
	@Override
	public Object[] getHeaderRow() {
		if (!hasHeaderRow()) return null;
		if (!isHeaderRowRead()) readHeaderRow();
		return headerRow;
	}

	/**
	 * Reads the header row if required.
	 * This is an empty method. Subclasses must override to correctly read the header row.
	 */
	protected void readHeaderRow() {
	}
	
	/**
	 * Sets the header rows.
	 * @param names names to be set
	 */
	protected void setHeaderRow(String names[]) {
		setHeaderRowRead(true);
		this.headerRow = names;
	}
	
	/**
	 * @param headerRowRead the headerRowRead to set
	 */
	protected void setHeaderRowRead(boolean headerRowRead) {
		this.headerRowRead = headerRowRead;
	}

	
	/**
	 * Returns the value in column with specified name.
	 * Returns null if row has no such column.
	 * @param name name of column (from header row)
	 * @param row row of values
	 * @return value in row for specified column.
	 */
	public Object get(String name, Object row[]) {
		if (row == null) return null;
		int column = getColumnIndex(name);
		if ((column < 0) || (column >= row.length)) return null;
		return row[column];
	}

	/**
	 * Returns the column index of given column name.
	 * The first column with given name will be returned.
	 * @param name name of column
	 * @return index of column or -1 if it does not exist.
	 */
	@Override
	public int getColumnIndex(String name) {
		if (!hasHeaderRow()) throw new CsvException("TableReader has no header row (property hasHeaderRow is false)");
		readHeaderRow();
		if (getHeaderRow() == null) throw new CsvException("Stream is empty");
		for (int i=0; i<headerRow.length; i++) {
			if ((headerRow[i] != null) && headerRow[i].toString().equalsIgnoreCase(name)) return i;
		}
		return -1;
	}
	
	/**
	 * Explicitely set the type of a column.
	 * This information will be used to convert the value of this column.
	 * @param columnIndex index of column
	 * @param type type of column
	 * @see #getTypeConversionHandler(String)
	 * @see #convert(int, String)
	 */
	public void setColumnType(int columnIndex, Class<?> type) {
		columnTypes.put(columnIndex, type);
	}
	
	/**
	 * Returns the type of a column.
	 * This information will be used to convert the value of this column.
	 * @param columnIndex index of column.
	 * @return type of values in column
	 * @see #getTypeConversionHandler(String)
	 * @see #convert(int, String)
	 */
	public Class<?> getColumnType(int columnIndex) {
		Class<?> rc = columnTypes.get(columnIndex);
		if (rc == null) rc = Object.class;
		return rc;
	}
	
	/**
	 * Tells whether the underlying stream has a header row or not
	 * @return true if there is a header row.
	 */
	@Override
	public boolean hasHeaderRow() {
		return hasHeaderRow;
	}

	/**
	 * Tells the reader whether the underlying stream will treat
	 * first row as header row.
	 * @param hasHeaderRow true if there is a header row.
	 */
	@Override
	public void setHasHeaderRow(boolean hasHeaderRow) {
		this.hasHeaderRow = hasHeaderRow;
	}

    /**
     * Adds a comment callback.
     * @param callback the callback
     */
    public void registerCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
        
    /**
     * Removes a comment callback.
     * @param callback the callback
     */
    public void unregisterCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
    }
    
    /**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Object[]> iterator() {
		return this;
	}


	/**
     * Notifies all comment callbacks about a comment.
     * @param s the comment to notify about
     * @param row row number
     * @param cell cell number in row
     */
    protected void notifyComment(String s, int row, int cell) {
    	for (CommentCallback callback : commentCallbacks) {
    		callback.comment(this, s, row, cell);
    	}
    }

    /**
     * Converts the string back to correct object.
     * This method will retrieve the column type from {@link #getColumnType(int)} and then
     * then forward the transformation to {@link #mapper} object.
     * @param columnIndex index of column of this value
     * @param value string representation of object
     * @return object the converted object
     * @see #getColumnType(int)
     */
    protected Object convert(int columnIndex, Object value) {
    	if (mapper == null) return value;
    	return mapper.fromStream(getColumnType(columnIndex), value);
    }
    
    /**
     * Converts the string back to correct object.
     * This method will forward the transformation to {@link #mapper} object.
     * @param type the target type of the return value
     * @param value stream representation of object
     * @return object the converted object
     */
    protected Object convert(Class<?> type, Object value) {
    	if (mapper == null) return value;
    	return mapper.fromStream(type, value);
    }
    
    /**
     * Increases the line count.
     * Line count reflects the lines in an input file.
     * @return lines read so far
     */
    protected int incrementLineCount() {
    	lineCount++;
    	return getLineCount();
    }
    
	/**
     * Line count reflects the lines in an input file.
     * @return lines read so far
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * Increments the row Count.
	 * Row count is the number of netto rows (&lt;= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
    protected int incrementRowCount() {
    	rowCount++;
    	return getRowCount();
    }
    
    /**
     * Returns the row count.
	 * Row count is the number of netto rows (&lt;= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
	public int getRowCount() {
		return rowCount;
	}


	/**
	 * Does nothing
	 * @see csv.TableReader#close()
	 */
	@Override
	public void close() {
	}


	/**
	 * @see csv.TableReader#setMinimumColumnCount(int)
	 */
	@Override
	public void setMinimumColumnCount(int length) {
		this.minimumColumnCount = length;
	}


	/**
	 * @return the minimumLineCount
	 */
	@Override
	public int getMinimumColumnCount() {
		return minimumColumnCount;
	}

    /**
     * Returns an array from the columns.
     * This function exists for convinience to take care of minimum column count.
     * @param columns columns to return
     * @return arrray with column values
     */
    protected Object[] convertArray(List<String> columns) {
        int colcount = columns != null ? columns.size() : 0;
        Object rc[] = new Object[Math.max(colcount, getMinimumColumnCount())];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = convert(i, columns.get(i));
            }
        }
        return rc;
    }
    
    /**
     * Returns an array from the columns.
     * This function exists for convinience to take care of minimum column count.
     * @param columns columns to return
     * @return arrray with column values
     */
    protected Object[] convertArray(String columns[]) {
        int colcount = getMinimumColumnCount();
        if (columns != null) colcount = columns.length;
        Object rc[] = new Object[Math.max(colcount, getMinimumColumnCount())];
        if ((columns != null) && (colcount > 0)) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = convert(i, columns[i]);
            }
        }
        return rc;
    }

	/**
	 * @return the headerRowRead
	 */
	public boolean isHeaderRowRead() {
		return headerRowRead;
	}
    
}
