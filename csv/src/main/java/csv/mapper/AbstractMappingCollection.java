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
package csv.mapper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Implements basic functionality to define a mapping collection.
 * @author ralph
 *
 */
public abstract class AbstractMappingCollection implements MappingCollection {

	protected Collection<TypeConverter> collection = new HashSet<>();
	
	/**
	 * Constructor.
	 */
	public AbstractMappingCollection() {
		init();
	}
	
	/**
	 * Override this method to init your collection.
	 */
	protected abstract void init();
	
	/**
	 * Add another converter to this collection.
	 * @param converter converter to be added
	 */
	public void add(TypeConverter converter) {
		collection.add(converter);
	}
	
	/**
	 * Remove a converter from this collection.
	 * @param converter converter to be removed
	 */
	public void remove(TypeConverter converter) {
		collection.remove(converter);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<TypeConverter> iterator() {
		return collection.iterator();
	}

	
}
