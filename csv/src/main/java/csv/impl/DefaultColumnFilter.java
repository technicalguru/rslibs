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

import csv.TableReader;

/**
 * Implements a filter based on a given index array.
 * You are able to filter and resort the column in each row.
 * @author ralph
 *
 */
public class DefaultColumnFilter extends AbstractColumnFilter {

	private int indexOrder[];
	
	/**
	 * Default Constructor
	 * @param reader underlying reader to be filtered.
	 * @param indexOrder array of column indices to be returned from underlying reader
	 */
	public DefaultColumnFilter(TableReader reader, int indexOrder[]) {
		super(reader);
		this.indexOrder = indexOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getFilteredIndex(int originalIndex) {
		for (int i=0; i<indexOrder.length; i++) {
			if (indexOrder[i] == originalIndex) return i;
		}
		return -1;
	}

}
