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
package csv.performance;

import java.io.File;

/**
 * Interface for all reader performance tests.
 * @author ralph
 *
 */
public interface IReader {

	/**
	 * Returns the name of the library under test by this class
	 * @return name of library
	 */
	public String getName();
	
	/**
	 * Performs reading all CSV data of given file.
	 * @param file the file that shall be read
	 * @param charset the character encoding of the file (can be <code>null</code>)
	 * @return the number of data rows read (might be different from number of lines)
	 * @throws Exception in case a problem occurs
	 */
	public int read(File file, String charset) throws Exception;
	
}
