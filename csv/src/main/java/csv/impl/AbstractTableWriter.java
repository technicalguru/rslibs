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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import csv.TableWriter;
import csv.mapper.StreamMapper;

/**
 * Abstract implementation of writer interface.
 * The interface provides basic functionality being needed regardless of underlying medium
 * to be written to.
 * @author ralph
 *
 */
public abstract class AbstractTableWriter implements TableWriter {

    private int rowCount;
	private StreamMapper mapper = null;
    
	/**
	 * General initialization.
	 * This implementation does nothing.
	 */
	protected void init() {
		rowCount = 0;
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
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment) throws IOException {
	}
	
	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
     * @param row index of row for comment
     * @param column index of column for comment
 	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment, int row, int column) throws IOException {
	}
	
	/**
	 * Closes the writer.
	 * This implementation does nothing.
	 */
	public void close() {
	}
	
	/**
	 * Returns the rows written.
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Increments the row count.
	 * @return current row count
	 */
	protected int incrementRowCount() {
		rowCount++;
		return rowCount;
	}
	
    /**
     * Converts the value to its stream representation.
     * @param value object
     * @return stream representation
     */
    protected Object convert(Object value) {
    	if (mapper == null) return value;
    	return mapper.toStream(value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void printRow(Collection<?> columns) throws IOException {
        printRow(columns.iterator(), columns.size());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void printRow(Iterator<?> columns, int size) throws IOException {
        Object o[] = new Object[size];
        int i = 0;
        
        while (columns.hasNext() && (i < size)) {
            o[i] = columns.next();
            i++;
        }
        
        printRow(o);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void printRow(Iterator<?> columns) throws IOException {
        ArrayList<Object> o = new ArrayList<Object>();
        while (columns.hasNext()) {
            o.add(columns.next());
        }
        printRow(o.toArray());
    }
    

}
