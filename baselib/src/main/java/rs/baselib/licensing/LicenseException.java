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
package rs.baselib.licensing;

/**
 * A License problem.
 * @author ralph
 *
 */
public class LicenseException extends RuntimeException {

	/**  */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public LicenseException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message - error message
	 * @param cause - cause of error
	 */
	public LicenseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor.
	 * @param message - error message
	 */
	public LicenseException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause - cause of error
	 */
	public LicenseException(Throwable cause) {
		super(cause);
	}


}
