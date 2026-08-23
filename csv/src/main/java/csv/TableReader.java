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
package csv;

import java.util.Iterator;

/**
 * Introduces an interface for other implementations
 * of table reading interfaces.
 * The Reader interface defines an important rule: header rows are never delivered
 * by {@link #next()} but by {@link #getHeaderRow()} instead. All implementations
 * of this class must follow this rule.
 * @author RalphSchuster
 *
 */
public interface TableReader extends Iterator<Object[]>, Iterable<Object[]> {

	/**
	 * Tells the reader whether the underlying stream will treat
	 * first row as header row.
	 * @param hasHeaderRow true if there is a header row.
	 */
	public void setHasHeaderRow(boolean hasHeaderRow);
	
	/**
	 * Tells whether the underlying stream has a header row or not
	 * @return true if there is a header row.
	 */
	public boolean hasHeaderRow();
	
	/**
	 * Returns the header row.
	 * Please note that header rows are never delivered through {@link #next()}.
	 * @return header row if such was defined.
	 */
	public Object[] getHeaderRow();
	
	/**
	 * Returns the column index of given column name.
	 * The first column with given name will be returned.
	 * @param name name of column
	 * @return index of column or -1 if it does not exist.
	 */
	public int getColumnIndex(String name);

	/**
	 * Opens the reader or resets it.
	 */
	public void open();
	
	/**
	 * Resets the reader.
	 */
	public void reset();
	
	/**
	 * Closes the reader.
	 */
	public void close();
	
	/**
	 * Registers a comment callback.
	 * The callback will be executed when a comment is detected in input.
	 * Note that not all implementations actually support comments.
	 * @param callback callback to be registered
	 */
	public void registerCommentCallBack(CommentCallback callback);
	
	/**
	 * Unregisters a comment callback.
	 * Note that not all implementations actually support comments.
	 * @param callback callback to be unregistered
	 */
	public void unregisterCommentCallBack(CommentCallback callback);
	
	/**
	 * Sets the minimum number of columns to be returned by {@link #next()}.
	 * @param length number of columns
	 */
	public void setMinimumColumnCount(int length);
	
	/**
	 * Returns the minimum number of columns to be returned by {@link #next()}.
	 * @return length number of columns
	 */
	public int getMinimumColumnCount();
}
