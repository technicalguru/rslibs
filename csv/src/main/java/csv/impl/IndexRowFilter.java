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

import java.util.Arrays;

import csv.TableReader;

/**
 * Implements a row filter based on row indices.
 * @author ralph
 *
 */
public class IndexRowFilter extends AbstractRowFilter {

	private int validRowIndices[];
	private int validIndex = 0;
	
	/**
	 * Constructor.
	 * @param reader underlying table reader
	 * @param validRowIndices all row indices that are valid for delivering
	 */
	public IndexRowFilter(TableReader reader, int validRowIndices[]) {
		super(reader);
		this.validRowIndices = validRowIndices;
		Arrays.sort(this.validRowIndices);
	}

	/**
	 * Filters rows according to raw row index.
	 * @see csv.impl.AbstractRowFilter#isValidRow(java.lang.Object[])
	 */
	@Override
	protected boolean isValidRow(Object[] row) {
		if (validIndex < validRowIndices.length) {
			if (getRawRowIndex() == validRowIndices[validIndex]) {
				validIndex++;
				return true;
			}
		}
		return false;
	}

	/**
	 * This method returns false when all valid rows were delivered.
	 * @see csv.impl.AbstractRowFilter#isMoreRowsExpected()
	 */
	@Override
	protected boolean isMoreRowsExpected() {
		return validIndex < validRowIndices.length;
	}

	/**
	 * @see csv.impl.AbstractRowFilter#open()
	 */
	@Override
	public void open() {
		super.open();
		validIndex = 0;
	}

	/**
	 * @see csv.impl.AbstractRowFilter#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		validIndex = 0;
	}

	
}
