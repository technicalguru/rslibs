/*
 * This file is part of RS Library (Data File Library).
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
package rs.data.file.util;


/**
 * A number generator for long.
 * @author ralph
 *
 */
public class LongKeyGenerator implements IKeyGenerator<Long> {

	/**
	 * The last generated ID.
	 */
	private long lastKey = 0L;
	
	/**
	 * Constructor.
	 */
	public LongKeyGenerator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getNewId() {
		lastKey++;
		return lastKey;
	}

	
}
