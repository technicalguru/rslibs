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
package rs.data.impl.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A DTO using a Map object as storage.
 * @author ralph
 *
 */
public class MapDTO<K extends Serializable> extends GeneralDTO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/** The properties object */
	private Map<String,Object> map;
	
	/**
	 * Constructor.
	 */
	public MapDTO() {
		map = new HashMap<String, Object>();
	}

	/**
	 * Returns a property.
	 * @param name key of property.
	 * @return value of property
	 */
	public Object getProperty(String name) {
		return map.get(name);
		
	}

	/**
	 * Sets a property.
	 * @param name key of property
	 * @param value new value of property
	 */
	public void setProperty(String name, Object value) {
		map.put(name, value);
	}

	/**
	 * Returns the {@link #map}.
	 * @return the map
	 */
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(map);
	}
	
	
}
