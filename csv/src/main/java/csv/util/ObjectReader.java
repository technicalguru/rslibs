/*
 * This file is part of CSV/Excel Utility Package.
 *
 *  CSV/Excel Utility Package is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV/Excel Utility Package is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV/Excel Utility Package.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv.util;

import java.util.Iterator;

import csv.TableReader;

/**
 * Reads objects from a table.
 * <p>This class is different to {@link BeanReader} as it asks a converter to convert the row.</p>
 * <pre>
 * // Create an instance of your table reader 
 * TableReader tableReader = ...; 
 * 
 * // Get an instance of your row converter
 * RowConverter&lt;MyClass&gt; converter = new MyClassConverter();
 * 
 * // Now read from the table stream
 * ObjectReader&lt;MyClass&gt; reader = new ObjectReader(tableReader, converter, true);
 * Object tableHeader[] = reader.getTableHeader();
 * while (reader.hasNext()) {
 *    MyClass myObject = reader.next();
 *    
 *    // do something...
 * }
 * 
 * // Close the reader
 * reader.close();
 * </pre>
 * @author ralph
 *
 */
public class ObjectReader<T> implements Iterator<T>, Iterable<T> {

	protected TableReader        reader;
	protected Iterator<Object[]> iterator;
	protected RowConverter<T>    converter;
	private   boolean            hasHeaderRow;
	protected Object             tableHeader[];
	
	/**
	 * Constructor.
	 * @param reader the underlying table reader
	 * @param converter the converter to be used
	 * @param hasHeaderRow whether the table has a table header to read
	 */
	public ObjectReader(TableReader reader, RowConverter<T> converter, boolean hasHeaderRow) {
		this.reader       = reader;
		this.converter    = converter;
		this.hasHeaderRow = hasHeaderRow;
		this.tableHeader  = null;
		this.iterator     = reader.iterator();
		readHeaderRow();
	}
	
	/**
	 * Reads the header row if required.
	 */
	private void readHeaderRow() {
		if (hasHeaderRow && (tableHeader == null) ) {
			if (reader.hasNext()) tableHeader = reader.next();
		}
	}
	
	/**
	 * Returns the header row that was read.
	 * @return the header row or null if no such row was read.
	 */
	public Object[] getTableHeader() {
		return tableHeader;
	}

	/**
	 * Returns the row converter.
	 * @return the row converter
	 */
	public RowConverter<T> getConverter() {
		return converter;
	}

	/**
	 * Sets the row converter.
	 * @param converter the row converter to be used
	 */
	public void setConverter(RowConverter<T> converter) {
		this.converter = converter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		return converter.convert(iterator.next());
	}

	/**
	 * Closes the stream.
	 */
	public void close() {
		reader.close();
	}

}
