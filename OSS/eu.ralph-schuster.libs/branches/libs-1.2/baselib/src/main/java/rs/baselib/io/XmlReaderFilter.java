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

import java.io.Reader;

/**
 * Filters invalid XML characters from a stream.
 * @author ralph
 *
 */
public class XmlReaderFilter extends AbstractReaderFilter {

	/**
	 * Constructor.
	 * @param in
	 */
	public XmlReaderFilter(Reader in) {
		super(in);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidChar(char c) {
		return (c == 0x9) ||  (c == 0xA) ||  (c == 0xD) || 
				((c >= 0x20) && (c <= 0xD7FF)) || 
				((c >= 0xE000) && (c <= 0xFFFD));
	}

}
