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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import csv.CsvException;
import csv.util.CSVUtils;

/**
 * An abstract implementation of TableWriter.
 * This implementation is dedicated for I/O streams as underlying medium.
 * @author RalphSchuster
 *
 */
public abstract class AbstractStreamTableWriter extends AbstractTableWriter {

    private PrintWriter writer;
    private OutputStream outputStream;
    private CharsetEncoder charsetEncoder = CSVUtils.getDefaultCharset().newEncoder();
    
	/**
	 * Default Constructor.
	 */
	public AbstractStreamTableWriter() {
		init();
	}

    /**
     * Create a new instance from an existing OutputStream.
     * @param out - the stream to write to.
     */
    public AbstractStreamTableWriter(OutputStream out) {
        setOutputStream(out);
		init();
    }
    
    /**
     * Create a new instance from a file object.
     * @param file - file to write data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractStreamTableWriter(File file) throws IOException {
        this(new FileOutputStream(file));
    }
        
    /**
     * Create a new instance froma file name.
     * @param file - file to write data to.
     * @throws IOException - when the file could not be created
     */
    public AbstractStreamTableWriter(String file) throws IOException {
        this(new FileOutputStream(file));
    }
    
	/**
	 * Sets the underlying stream.
	 * This implementation throws an exception when the stream was already set.
	 * @param out the output stream to be used
	 */
	public void setOutputStream(OutputStream out) {
		if (outputStream != null) throw new CsvException("Output stream already set");
		this.outputStream = out;
	}

	/**
	 * Returns a writer object for convinience.
	 * The writer will be created only when required.
	 * @return the writer object
	 */
	public PrintWriter getWriter() {
		if (writer == null) writer = createWriter();
		return writer;
	}

	/**
	 * Creates the print writer for the output.
     * The method will call {@link #createOutputStreamWriter(OutputStream)}
     * for the creation of the underlying writer.
	 * @return the output writer
	 */
	protected PrintWriter createWriter() {
		return new PrintWriter(createOutputStreamWriter(getOutputStream()));
	}
	
	/**
	 * Creates the stream writer for the printer.
	 * @param out the underlying output stream
	 * @return the stream writer
	 */
	protected OutputStreamWriter createOutputStreamWriter(OutputStream out) {
		return new OutputStreamWriter(out, getCharsetEncoder());
	}
	
	/**
	 * Returns the encoder being used for the output.
	 * @return the encoder being used.
	 */
	public CharsetEncoder getCharsetEncoder() {
		return charsetEncoder;
	}
		
	/**
	 * Sets the charset encoder being used.
	 * @param charsetEncoder the charset encoder to set
	 */
	public void setCharsetEncoder(CharsetEncoder charsetEncoder) {
		this.charsetEncoder = charsetEncoder;
	}

	/**
	 * Sets the charset being used for output.
	 * @param charset the charset to set
	 */
	public void setCharset(Charset charset) {
		setCharsetEncoder(charset.newEncoder());
	}

	/**
	 * Sets the charset being used for output.
	 * @param charset the charset to set
	 */
	public void setCharset(String charset) {
		setCharset(Charset.forName(charset));
	}

	/**
	 * Returns the underlying output stream
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Closes the writer and its underlying streams.
	 */
	public void close() {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (getOutputStream() != null) {
				getOutputStream().flush();
				getOutputStream().close();
			}
		} catch (IOException e) {
			throw new CsvException("Cannot close writer", e);
		}
		super.close();
	}
	
}
