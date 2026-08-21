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
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import csv.CsvException;
import csv.util.CSVUtils;

/**
 * Reader for Swing's JTable. 
 * @author ralph
 *
 */
public class JTableReader extends AbstractTableReader {

	private boolean selectedOnly = true;
	private JTable table;
	private int rows[] = null;
	private int currentRowIndex = -1;
	private int rowCount = 0;
	
	/**
	 * Constructor with header row and selected rows only.
	 * @param table table object to read from
	 */
	public JTableReader(JTable table) {
		this(table, true);
	}
	
	/**
	 * Constructor.
	 * @param table table object to read from
	 * @param selectedOnly whether only selected rows shall be read
	 */
	public JTableReader(JTable table, boolean selectedOnly) {
		this.table = table;
		this.selectedOnly = selectedOnly;
		setHasHeaderRow(true);
		myInit();
	}

	/**
	 * Resets all variables to read the table from the beginning
	 */
	private void myInit() {
		rowCount = getTable().getRowCount();
		if (isSelectedOnly()) {
			rows = table.getSelectedRows();
			if ((rows != null) && (rows.length > 0)) rowCount = rows.length;
		}
		currentRowIndex = -1;
	}
	
	/**
	 * Resets the reader.
	 * @see csv.impl.AbstractTableReader#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		myInit();
	}

	/**
	 * Returns true if there are more rows to be delivered.
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return rowCount > currentRowIndex+1;
	}

	/**
	 * Returns the next row.
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Object[] next() {
		if (!hasNext()) throw new CsvException("No more rows");
		currentRowIndex++;
		
		int rowIndex = currentRowIndex;
		if (isSelectedOnly()) rowIndex = rows[currentRowIndex];
		
		TableModel model = getTable().getModel();
		int columnCount = model.getColumnCount();
		List<Object> columns = new ArrayList<Object>();
		for (int i=0; i<columnCount; i++) {
			columns.add(model.getValueAt(rowIndex, i));
		}
		
		incrementRowCount();
		return CSVUtils.convertList(columns, getMinimumColumnCount());
	}

	/**
	 * Removal is not supported.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new CsvException("remove is not supprted");
	}

	/**
	 * Reads the table header.
	 * @see csv.impl.AbstractTableReader#readHeaderRow()
	 */
	@Override
	protected void readHeaderRow() {
		TableColumnModel cModel = getTable().getTableHeader().getColumnModel();
		int colLength = cModel.getColumnCount();
		String row[] = new String[colLength];
		for (int i=0; i<colLength; i++) {
			Object o = cModel.getColumn(i).getHeaderValue();
			if (o != null) row[i] = o.toString();
			else row[i] = null;
		}
		setHeaderRow(row);
	}

	/**
	 * Returns the selectedOnly.
	 * @return the selectedOnly
	 */
	public boolean isSelectedOnly() {
		return selectedOnly;
	}

	/**
	 * Returns the table.
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

}
