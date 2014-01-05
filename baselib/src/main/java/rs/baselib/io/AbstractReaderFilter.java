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
package rs.baselib.io;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Abstract implementation of a reader that can filter characters.
 * <p>
 * Descendants shall override {@link #isValidChar(char)} only.
 * </p>
 * 
 * @author ralph
 *
 */
public abstract class AbstractReaderFilter extends FilterReader {

	/**
	 * Constructor.
	 */
	public AbstractReaderFilter(Reader in) {
		super(in);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		char[] buf = new char[1];
		int result = read(buf, 0, 1);
		if (result == -1) {
			return -1;
		} 
		return (int) buf[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int count = 0;
		while (count == 0) {
			count = super.read(cbuf, off, len);
			if (count == -1) return -1;

			int last = off;
			for (int i=off; i<off+count; i++) {
				if(isValidChar(cbuf[i])) {
					cbuf[last++] = cbuf[i];
				}
			}

			count = last-off;
		}
		return count;	
	}

	/**
	 * Tells the filter reader whether the given character is valid or not.
	 * @param c the character to be validated
	 * @return <code>true</code> when the character can be delivered
	 */
	public abstract boolean isValidChar(char c);
}
