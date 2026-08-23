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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import csv.CsvException;
import csv.util.CSVUtils;

/**
 * Implements a table reader to read JDBC ResultSet rows.
 * @author ralph
 *
 */
public class JdbcReader extends AbstractTableReader {

	private ResultSet resultSet;
	private Object nextRow[] = null;
	private int columnCount = 0;
	private boolean closeResultSet = false;
	
	/**
	 * Default constructor.
	 * @param resultSet the JDBC result set to read data from
	 */
	public JdbcReader(ResultSet resultSet) {
		setResultSet(resultSet);
		setHasHeaderRow(true);
	}

	/**
	 * Default constructor.
	 * @param statement JDBC statement ready to be executed
	 * @throws SQLException when the statement cannot be used
	 */
	public JdbcReader(Statement statement) throws SQLException {
		this(statement.getResultSet());
	}

	/**
	 * Sets the JDBC result set for this reader.
	 * @param resultSet the resultSet to set
	 */
	protected void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * Returns the JDBC result set of this reader.
	 * @return the resultSet
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * Closes the reader.
	 * If the result set was created within this class then it will be closed, too.
	 * @see csv.TableReader#close()
	 */
	@Override
	public void close() {
		try {
			if (!resultSet.isClosed() && isCloseResultSet()) resultSet.close();
		} catch (SQLException e) {
			throw new CsvException("Error while closing result set", e);
		}
		super.close();
	}

	/**
	 * Opens the reader.
	 * @see csv.TableReader#open()
	 */
	@Override
	public void open() {
		super.open();
		nextRow = null;
		columnCount = 0;
	}

	/**
	 * Resets the result set cursor before the first row.
	 * @see csv.TableReader#reset()
	 */
	@Override
	public void reset() {
		try {
			resultSet.beforeFirst();
		} catch (SQLException e) {
			throw new CsvException("Cannot relocate cursor", e);
		}
		nextRow = null;
		columnCount = 0;
		super.reset();
	}

	/**
	 * Reads the header row.
	 */
	@Override
	protected void readHeaderRow() {
		try {
			ResultSetMetaData meta = resultSet.getMetaData();
			int colcount = meta.getColumnCount();
			String l[] = new String[colcount];
			for (int i=0; i<colcount; i++) {
				l[i] = meta.getColumnName(i+1);
			}
			setHeaderRow(CSVUtils.extendArray(l, getMinimumColumnCount()));
		} catch (SQLException e) {
			throw new CsvException("Cannot read column names", e);
		}
	}

	/**
	 * Returns the number of columns the result set returns.
	 * @return number of columns
	 */
	public int getColumnCount() {
		if (columnCount <= 0) {
			try {
				ResultSetMetaData meta = resultSet.getMetaData();
				columnCount = meta.getColumnCount();
			} catch (SQLException e) {
				throw new CsvException("Cannot read column names", e);
			}
		}
		return columnCount;
	}
	
	/**
	 * Returns true if there are more rows to be delivered.
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
		Object rc[] = nextRow;
		nextRow = null;
		incrementRowCount();
		return rc;
	}

	/**
	 * Reads the next row from the result set.
	 */
	protected void readNextRow() {
		// The current row must be read before
		if (nextRow != null) return;
		try {
			// Read the column names first if required
			readHeaderRow();
			
			// Read the data row
			if (resultSet.next()) {
				nextRow = new Object[getColumnCount()];
				for (int i=0; i<nextRow.length; i++) {
					nextRow[i] = resultSet.getObject(i+1);
				}
			}
		} catch (SQLException e) {
			throw new CsvException("Cannot read next row", e);
		}
	}
	
	/**
	 * Forwards to JDBC result set.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		try {
			resultSet.deleteRow();
		} catch (SQLException e) {
			throw new CsvException("Cannot delete row", e);
		}
	}

	/**
	 * Returns true if a call to {@link #close()} will also close the result set.
	 * @return the closeResultSet
	 */
	public boolean isCloseResultSet() {
		return closeResultSet;
	}

	/**
	 * Sets whether the result set will be closed.
	 * @param closeResultSet the closeResultSet to set
	 */
	public void setCloseResultSet(boolean closeResultSet) {
		this.closeResultSet = closeResultSet;
	}

}
