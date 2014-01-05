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
package rs.baselib.crypto;

/**
 * Exception thrown from signing process.
 * @author ralph
 *
 */
public class SigningException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9041677196642112473L;

	/**
	 * Constructor.
	 */
	public SigningException() {
	}

	/**
	 * Constructor.
	 * @param message error message
	 */
	public SigningException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param cause root cause exception
	 */
	public SigningException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor.
	 * @param message error message
	 * @param cause root cause exception
	 */
	public SigningException(String message, Throwable cause) {
		super(message, cause);
	}

}
