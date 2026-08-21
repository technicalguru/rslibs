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

import java.util.Iterator;

import csv.CommentCallback;
import csv.CsvException;
import csv.TableReader;

/**
 * Filters rows from underlying table reader.
 * This is an abstract implementation only that eases filtering. 
 * @author ralph
 *
 */
public abstract class AbstractRowFilter implements TableReader {

	private TableReader reader;
	private Object nextRow[] = null;
	private int rawRowIndex = 0;
	private int rowIndex = 0;
	
	/**
	 * Constructor.
	 * @param reader the reader this filter shall process
	 */
	public AbstractRowFilter(TableReader reader) {
		this.reader = reader;
	}

	/**
	 * Returns the underlying reader.
	 * @return the reader
	 */
	protected TableReader getReader() {
		return reader;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		getReader().close();
	}

	/**
	 * Forwarded to underlying reader.
	 * @param name name of column (from header row)
	 * @param row row of values	 
	 * @return the value in the respective column
	 * @see AbstractTableReader#get(String, Object[])
	 */
	public Object get(String name, Object[] row) {
		if (getReader() instanceof AbstractTableReader) {
			return ((AbstractTableReader)getReader()).get(name, row);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getColumnIndex(String name) {
		return getReader().getColumnIndex(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getHeaderRow() {
		return getReader().getHeaderRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinimumColumnCount() {
		return getReader().getMinimumColumnCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasHeaderRow() {
		return getReader().hasHeaderRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void open() {
		getReader().open();
		nextRow = null;
		rawRowIndex = 0;
		rowIndex = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Object[]> iterator() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerCommentCallBack(CommentCallback callback) {
		getReader().registerCommentCallBack(callback);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		getReader().reset();
		nextRow = null;
		rawRowIndex = 0;
		rowIndex = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHasHeaderRow(boolean hasHeaderRow) {
		getReader().setHasHeaderRow(hasHeaderRow);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMinimumColumnCount(int length) {
		getReader().setMinimumColumnCount(length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterCommentCallBack(CommentCallback callback) {
		getReader().unregisterCommentCallBack(callback);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (nextRow == null) findNextRow();
		return nextRow != null;
	}

	/**
	 * Read from underlying reader until there is a row not filtered away.
	 */
	protected void findNextRow() {
		while ((nextRow == null) && isMoreRowsExpected() && getReader().hasNext()) {
			Object row[] = getReader().next();
			if (isValidRow(row)) nextRow = row;
			rawRowIndex++;
		}
	}
	
	/**
	 * Tells whether the row can be delivered or will be filtered away.
	 * @param row row to be checked
	 * @return whether row is a valid row
	 * @see #getRawRowIndex()
	 * @see #getRowIndex()
	 */
	protected abstract boolean isValidRow(Object row[]);
	
	/**
	 * Tells whether more rows will be expected after current row.
	 * This method always returns true so all rows from underlying reader
	 * will be checked. However, you should override this method if you
	 * want to avoid checking more rows because you already know that
	 * no row will match your criteria anymore.
	 * @return true if another valid row can be expected
	 */
	protected boolean isMoreRowsExpected() {
		return true;
	}
	
	/**
	 * Returns the row index from the underlying reader (raw row index)
	 * @return the rawRowIndex
	 */
	protected int getRawRowIndex() {
		return rawRowIndex;
	}

	/**
	 * Returns the index of current row (delivered rows only)
	 * @return the rowIndex
	 */
	protected int getRowIndex() {
		return rowIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] next() {
		if (!hasNext()) throw new CsvException("No more rows available");
		rowIndex++;
		Object rc[] = nextRow;
		nextRow = null;
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		getReader().remove();
	}

}
