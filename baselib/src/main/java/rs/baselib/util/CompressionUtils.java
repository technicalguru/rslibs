/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.baselib.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Supports compression.
 * @author ralph
 *
 */
public class CompressionUtils {

	/** 
	 * Compresses a byte array .
	 * @param data data to be compressed
	 * @return compressed version
	 * @throws IOException when compression fails
	 */
	public static byte[] compress(byte[] data) throws IOException {  
		Deflater deflater = new Deflater();  
		deflater.setLevel(Deflater.BEST_COMPRESSION);
		deflater.setInput(data);  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   

		deflater.finish();  
		byte[] buffer = new byte[1024];   
		while (!deflater.finished()) {  
			int count = deflater.deflate(buffer); // returns the generated code... index  
			outputStream.write(buffer, 0, count);   
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		deflater.end();

		return output;  
	}

	/** Uncompresses a byte array 
	 * @param data data to be uncompressed
	 * @return uncompressed version
	 * @throws IOException when uncompression fails
	 * @throws DataFormatException if compression format is invalid
	 */
	public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
		Inflater inflater = new Inflater();   
		inflater.setInput(data);  

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[1024];  
		while (!inflater.finished()) {  
			int count = inflater.inflate(buffer);  
			outputStream.write(buffer, 0, count);  
		}  
		outputStream.close();  
		byte[] output = outputStream.toByteArray();  

		inflater.end();

		return output;  
	}  
}
