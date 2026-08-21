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

import csv.impl.AbstractStreamTableReader;
import csv.impl.AbstractStreamTableWriter;
import csv.impl.CSVReader;
import csv.impl.CSVWriter;
import csv.impl.ExcelReader;
import csv.impl.ExcelWriter;
import csv.impl.XExcelWriter;
import csv.impl.XmlReader;
import csv.impl.XmlWriter;


/**
 * Contains information about what reader and writer classes are responsible
 * for a specific MIME type.
 * @author RalphSchuster
 *
 */
public class MimeTypeInfo {

	/**
	 * The default information for CSV files.
	 */
	public static final MimeTypeInfo CSV_INFO = new MimeTypeInfo(
			new String[] {
					"text/csv",
					"text/comma-separated-values"
			}, 
			CSVReader.class, 
			CSVWriter.class
	);
	/**
	 * The default information for classic Excel files.
	 */
	public static final MimeTypeInfo EXCEL_INFO = new MimeTypeInfo(
			new String[] {
					"application/excel",
					"application/vnd.ms-excel",
					"application/x-excel",
					"application/x-msexcel",
			}, 
			ExcelReader.class, 
			ExcelWriter.class
	);
	/**
	 * The default information for classic Excel files.
	 */
	public static final MimeTypeInfo X_EXCEL_INFO = new MimeTypeInfo(
			new String[] {
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
			}, 
			ExcelReader.class, 
			XExcelWriter.class
	);
	/**
	 * The default information for Excel files.
	 */
	public static final MimeTypeInfo XML_INFO = new MimeTypeInfo(
			new String[] {
					"text/xml",
					"application/xml",
			}, 
			XmlReader.class, 
			XmlWriter.class
	);
	private String mimeTypes[];
	private Class<? extends AbstractStreamTableReader> readerClass;
	private Class<? extends AbstractStreamTableWriter> writerClass;
	
	/**
	 * Constructor.
	 * @param mimeType MIME type being registered
	 * @param readerClass class responsible for reading such files
	 * @param writerClass class responsible for writing such files
	 */
	public MimeTypeInfo(String mimeType, Class<? extends AbstractStreamTableReader> readerClass, Class<? extends AbstractStreamTableWriter> writerClass) {
		this(new String[] { mimeType }, readerClass, writerClass);
	}

	/**
	 * Constructor.
	 * @param mimeTypes multiple MIME types being registered
	 * @param readerClass class responsible for reading such files
	 * @param writerClass class responsible for writing such files
	 */
	public MimeTypeInfo(String mimeTypes[], Class<? extends AbstractStreamTableReader> readerClass, Class<? extends AbstractStreamTableWriter> writerClass) {
		this.mimeTypes = mimeTypes;
		this.readerClass = readerClass;
		this.writerClass = writerClass;
	}

	/**
	 * Returns the MIME types that this object provides information for.
	 * @return the mimeTypes
	 */
	public String[] getMimeTypes() {
		return mimeTypes;
	}

	/**
	 * Returns the responsible reader class.
	 * @return the readerClass
	 */
	public Class<? extends AbstractStreamTableReader> getReaderClass() {
		return readerClass;
	}

	/**
	 * Returns the responsible writer class.
	 * @return the writerClass
	 */
	public Class<? extends AbstractStreamTableWriter> getWriterClass() {
		return writerClass;
	}

	
}
