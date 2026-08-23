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

import java.util.HashMap;
import java.util.Map;

/**
 * The stream mapper is the central place to convert column objects from or into a table stream.
 * <p>
 * It has a register of type converters that can be used with a table reader or writer. Text-based
 * table readers/writers use the {@link StringMappings} converters by default. But you can change
 * or replace them by your own mappings:
 * </p>
 * <pre>
 *   ExcelReader in = new ExcelReader(file);
 *   StreamMapper mapper = new StreamMapper(myMappings);
 *   mapper.add(otherMappings);
 *   mapper.remove(anyConverter);
 *   in.setMapper(myMapper);
 *   
 *   // start using the reader now...
 *   
 *   ExcelWriter out = new ExcelWriter(file);
 *   StreamMapper mapper = new StreamMapper(myMappings);
 *   mapper.add(otherMappings);
 *   mapper.remove(anyConverter);
 *   out.setMapper(myMapper);
 *   
 *   // start using the writer now...
 * </pre>
 * @author ralph
 *
 */
public class StreamMapper {

	protected Map<Class<?>,TypeConverter> converters = new HashMap<>();

	/**
	 * Default constructor.
	 */
	public StreamMapper() {		
	}
	
	/**
	 * Constructor to ease construction of mapper objects.
	 * @param collections - collections to be added to this mapper.
	 */
	public StreamMapper(MappingCollection ...collections) {
		for (MappingCollection c : collections) {
			add(c);
		}
	}
	
	/**
	 * Constructor to ease construction of mapper objects.
	 * @param converters - converters to be added to this mapper.
	 */
	public StreamMapper(TypeConverter ...converters) {
		for (TypeConverter c : converters) {
			add(c);
		}
	}
	
	/**
	 * Add the mappings of the given collection.
	 * @param collection the collection to be added.
	 */
	public void add(MappingCollection collection) {
		for (TypeConverter c : collection) {
			add(c);
		}
	}
	
	/**
	 * Remove the mappings of the given collection.
	 * @param collection the collection to be removed.
	 */
	public void remove(MappingCollection collection) {
		for (TypeConverter c : collection) {
			remove(c);
		}
	}
	
	/**
	 * Add another converter to this collection.
	 * @param converter converter to be added
	 */
	public void add(TypeConverter converter) {
		for (Class<?> type : converter.getTypes()) {
			this.converters.put(type, converter);
		}
	}
	
	/**
	 * Remove a converter from this collection.
	 * @param converter converter to be removed
	 */
	public void remove(TypeConverter converter) {
		for (Class<?> type : converter.getTypes()) {
			this.converters.remove(type);
		}
	}
	
	/**
	 * Remove a converter from this collection.
	 * @param type type to be removed
	 */
	public void remove(Class<?> type) {
		this.converters.remove(type);
	}
	
    /**
     * Returns a type conversion handler for the given type.
     * @param type type to get a handler for
     * @return conversion handler
     */
    protected TypeConverter getConverter(Class<?> type) {
    	return converters.get(type);
    }
    
    /**
     * Converts the value to its stream representation.
     * @param value object
     * @return stream representation
     */
    public Object toStream(Object value) {
    	if (value == null) return null;
    	TypeConverter handler = getConverter(value.getClass());
    	if (handler != null) return handler.toStream(value);
    	return value;
    }
    
    /**
     * Converts the value from its stream representation.
     * @param targetClass - the target class to be returned
     * @param value object - stream representation
     * @return client representation
     */
    public Object fromStream(Class<?> targetClass, Object value) {
    	if (value == null) return null;
    	if (targetClass == null) return value;
    	TypeConverter handler = getConverter(targetClass);
    	if (handler != null) return handler.fromStream(value);
    	return value;
    }
    

}
