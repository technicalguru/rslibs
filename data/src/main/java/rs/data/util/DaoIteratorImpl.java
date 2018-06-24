/*
 * This file is part of RS Library (Data Base Library).
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
package rs.data.util;

import java.util.Iterator;

/**
 * An iterator implementation wrapping another iterator.
 * @author ralph
 *
 */
public class DaoIteratorImpl<E> implements IDaoIterator<E> {

	private Iterator<E> iterator;
	
	/**
	 * Constructor.
	 * @param iterator the iterator to be used in the background
	 */
	public DaoIteratorImpl(Iterator<E> iterator) {
		this.iterator = iterator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next() {
		return iterator.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		iterator.remove();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		if (iterator instanceof IDaoIterator) {
			((IDaoIterator<E>)iterator).close();
		}
	}

	
}
