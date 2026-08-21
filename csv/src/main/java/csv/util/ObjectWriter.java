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

import java.io.IOException;

import csv.CsvException;
import csv.TableWriter;

/**
 * Writes Objects into a table.
 * <p>This class is different to {@link BeanWriter} as it asks a converter to convert the object to a row first.</p>
 * <pre>
 * // Create an instance of your table writer 
 * TableWriter tableWriter = ...; 
 * 
 * // Get an instance of your object converter
 * ObjectConverter&lt;MyClass&gt; converter = new MyClassConverter();
 * 
 * // Get an instance of your table header provider (if you need it)
 * TableHeaderProvider headerProvider = new MyTableHeaderProvider();
 * 
 * // Now write to the table stream
 * ObjectWriter&lt;MyClass&gt; writer = new ObjectWriter(tableWriter, converter, headerProvider);
 * writer.write(object1);
 * writer.write(object2);
 * writer.write(new MyClass[] { object1, object2, object 3 });
 * 
 * // Close the writer
 * writer.close();
 * </pre>
 * 
 * @author ralph
 *
 */
public class ObjectWriter<T> {

	protected TableWriter writer;
	protected ObjectConverter<T> converter;
	protected TableHeaderProvider headerProvider;
	protected boolean rowsWritten;
	
	/**
	 * Constructor.
	 * @param writer - the writer to write to
	 * @param converter - the converter to be used
	 */
	public ObjectWriter(TableWriter writer, ObjectConverter<T> converter) {
		this(writer, converter, null);
	}
	
	/**
	 * Constructor.
	 * @param writer - the writer to write to
	 * @param converter - the converter to be used
	 * @param headerProvider - the header provider (can be null)
	 */
	public ObjectWriter(TableWriter writer, ObjectConverter<T> converter, TableHeaderProvider headerProvider) {
		this.writer    = writer;
		this.converter = converter;
		if ((headerProvider == null) && (converter instanceof TableHeaderProvider)) {
			headerProvider = (TableHeaderProvider) converter;
		}
		this.headerProvider = headerProvider;
	}

	/**
	 * Returns the row converter.
	 * @return the row converter
	 */
	public ObjectConverter<T> getConverter() {
		return converter;
	}

	/**
	 * Sets the row converter.
	 * @param converter the row converter to be used
	 */
	public void setConverter(ObjectConverter<T> converter) {
		this.converter = converter;
		if ((headerProvider == null) && (converter instanceof TableHeaderProvider)) {
			headerProvider = (TableHeaderProvider) converter;
		}
	}

	/**
	 * Returns the table header provider.
	 * @return the table header provider
	 */
	public TableHeaderProvider getHeaderProvider() {
		return headerProvider;
	}

	/**
	 * Sets the table header provider.
	 * @param headerProvider the table header provider to be used.
	 */
	public void setHeaderProvider(TableHeaderProvider headerProvider) {
		this.headerProvider = headerProvider;
	}

	/**
	 * Returns the table writer.
	 * @return the table writer
	 */
	public TableWriter getWriter() {
		return writer;
	}
	
	/**
	 * Writes the header row.
	 */
	public void writeHeader(String[] headerRow) {
		if (!rowsWritten && (headerRow != null)) {
			try {
				writer.printRow(headerRow);
			} catch (IOException e) {
				throw new CsvException("Cannot write to table writer", e);
			}
			rowsWritten = true;
		}
	}

	/**
	 * Writes multiple objects to the stream.
	 */
	public void write(T objects[]) {
		for (T o : objects) write(o);
	}

	/**
	 * Writes multiple objects to the stream.
	 */
	public void write(Iterable<? extends T> objects) {
		for (T o : objects) write(o);
	}

	/**
	 * Writes the object to the stream.
	 */
	public void write(T object) {
		if (object != null) {
			Object row[] = converter.convert(object);
			if (!rowsWritten) {
				if ((headerProvider == null) && (object instanceof TableHeaderProvider)) {
					headerProvider = (TableHeaderProvider)object;
				}
				if (headerProvider != null) {
					writeHeader(headerProvider.getTableHeader());
				}
			}
			try {
				writer.printRow(row);
			} catch (IOException e) {
				throw new CsvException("Cannot write to table writer", e);
			}
		}
	}
	
	/**
	 * Closes the stream.
	 */
	public void close() {
		writer.close();
	}
}
