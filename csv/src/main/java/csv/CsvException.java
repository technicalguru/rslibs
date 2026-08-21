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
package csv;

/**
 * Exception that reports a reader/writer problem.
 * @author ralph
 *
 */
public class CsvException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public CsvException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public CsvException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause root cause of exception
	 */
	public CsvException(Throwable cause) {
		super(cause);

	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause of exception
	 */
	public CsvException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause of exception
	 * @param enableSuppression whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	public CsvException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
