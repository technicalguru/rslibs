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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import csv.impl.AbstractStreamTableReader;
import csv.impl.AbstractStreamTableWriter;
import jakarta.activation.MimetypesFileTypeMap;

/**
 * This factory returns correct reader and writer implementations
 * for given files.
<pre>
java.io.File f1 = new java.io.File("csv-test.csv");
java.io.File f2 = new java.io.File("excel-test.xls");
java.io.File f3 = new java.io.File("excel-test.xlsx");
java.io.File f4 = new java.io.File("xml-test.xml");

// Returns implementation for CSV file
TableReader in1  = CSVFactory.getFactory().getReader(f1);
TableWriter out1 = CSVFactory.getFactory().getWriter(f1);

// Returns implementation for classic Excel files
TableReader in2  = CSVFactory.getFactory().getReader(f2);
TableWriter out2 = CSVFactory.getFactory().getWriter(f2);

// Returns implementation for XML-based Excel files
TableReader in3  = CSVFactory.getFactory().getReader(f3);
TableWriter out3 = CSVFactory.getFactory().getWriter(f3);

// Returns implementation for XML files
TableReader in4  = CSVFactory.getFactory().getReader(f4);
TableWriter out4 = CSVFactory.getFactory().getWriter(f4);

</pre>
 * @author RalphSchuster
 *
 */
public class CSVFactory {

	private Map<String, Class<? extends AbstractStreamTableReader>> readers;
	private Map<String, Class<? extends AbstractStreamTableWriter>> writers;
	
	private static CSVFactory factory = null;
	
	/**
	 * Returns the factory for rading/writing tables.
	 * @return factory factory object (singleton)
	 */
	public static CSVFactory getFactory() {
		if (factory == null) {
			factory = new CSVFactory();
		}
		return factory;
	}
	
	/**
	 * Creates the factory and initializes.
	 */
	protected CSVFactory() {
		init();
	}

	/**
	 * Initializes the factory.
	 */
	protected void init() {
		initReaderMap();
		initWriterMap();
		register(MimeTypeInfo.CSV_INFO);
		register(MimeTypeInfo.EXCEL_INFO);
		register(MimeTypeInfo.X_EXCEL_INFO);
		register(MimeTypeInfo.XML_INFO);
	}
	
	/**
	 * Initializes the reader map.
	 */
	protected void initReaderMap() {
		if (readers != null) return;
		readers = new HashMap<String, Class<? extends AbstractStreamTableReader>>();
	}
	
	/**
	 * Initializes the writer map.
	 */
	protected void initWriterMap() {
		if (writers != null) return;
		writers = new HashMap<String, Class<? extends AbstractStreamTableWriter>>();
	}
	
	/**
	 * Registers a new MIME type.
	 * @param mimeTypeInfo the info to register
	 */
	public void register(MimeTypeInfo mimeTypeInfo) {
		String types[] = mimeTypeInfo.getMimeTypes();
		Class<? extends AbstractStreamTableReader> reader = mimeTypeInfo.getReaderClass();
		Class<? extends AbstractStreamTableWriter> writer = mimeTypeInfo.getWriterClass();
		for (int i=0; i<types.length; i++) {
			if (reader != null) readers.put(types[i], reader);
			if (writer != null) writers.put(types[i], writer);
		}
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file filename
	 * @return reader class instance to be used
	 * @throws IOException when the file cannot be read
	 */
	public TableReader getReader(String file)  throws IOException {
		return getReader(new File(file));
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file file
	 * @return reader class instance to be used
	 * @throws IOException when the file cannot be read
	 */
	public TableReader getReader(File file) throws IOException {
		String mimeType = getMimeType(file);
		if (mimeType == null) throw new CsvException("No MIME type found: "+file.getAbsolutePath());
		AbstractStreamTableReader reader = getMimeTypeReader(mimeType);
		reader.setInputStream(new FileInputStream(file));
		return reader;
	}
	
	/**
	 * Returns a reader for the given MIME type.
	 * @param mimeType MIME type
	 * @return reader to be used
	 */
	public AbstractStreamTableReader getMimeTypeReader(String mimeType) {
		if (mimeType == null) throw new CsvException("NULL MIME type");
		Class<? extends AbstractStreamTableReader> readerClass = readers.get(mimeType);
		if (readerClass == null) throw new CsvException("Cannot find reader class for: "+mimeType);
		try {
			AbstractStreamTableReader reader = readerClass.getConstructor().newInstance();
			return reader;
		} catch (Exception e) {
			throw new CsvException("Cannot create reader instance: ", e);
		}		
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file filename
	 * @return reader class instance to be used
	 * @throws IOException when the file cannot be written
	 */
	public AbstractStreamTableWriter getWriter(String file) throws IOException {
		return getWriter(new File(file));
	}
	
	/**
	 * Returns the correct reader for the given file.
	 * @param file file
	 * @return reader class instance to be used
	 * @throws IOException when the file cannot be written
	 */
	public AbstractStreamTableWriter getWriter(File file) throws IOException {
		String mimeType = getMimeType(file);
		if (mimeType == null) throw new CsvException("No MIME type found: "+file.getAbsolutePath());
		AbstractStreamTableWriter writer = getMimeTypeWriter(mimeType);
		writer.setOutputStream(new FileOutputStream(file));
		return writer;
	}
	
	/**
	 * Returns a writer for the given MIME type.
	 * @param mimeType MIME type
	 * @return writer to be used
	 */
	public AbstractStreamTableWriter getMimeTypeWriter(String mimeType) {
		if (mimeType == null) throw new CsvException("NULL MIME type");
		Class<? extends AbstractStreamTableWriter> writerClass = writers.get(mimeType);
		if (writerClass == null) throw new CsvException("Cannot find writer class for: "+mimeType);
		try {
			AbstractStreamTableWriter writer = writerClass.getConstructor().newInstance();
			return writer;
		} catch (Exception e) {
			throw new CsvException("Cannot create writer instance: ", e);
		}		
	}
	
	/**
	 * Returns the MIME type for the given file.
	 * @param file file to check
	 * @return MIME type of file
	 */
	public String getMimeType(File file) {
		String filename = file.getName().toLowerCase();
		if (filename.endsWith(".xlsx")) return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		if (filename.endsWith(".xls"))  return "application/vnd.ms-excel";
		if (filename.endsWith(".csv"))  return "text/csv";
		// Return any other
		return new MimetypesFileTypeMap().getContentType(file);
	}
}
