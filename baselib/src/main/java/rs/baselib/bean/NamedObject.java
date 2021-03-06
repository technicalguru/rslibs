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
package rs.baselib.bean;

/**
 * A BO having a name.
 * @author ralph
 *
 */
public interface NamedObject {

	/** 
	 * Property name of name.
	 * @since 1.2.9 
	 */
	public static final String NAME = "name";
	/** 
	 * Property name of name.
	 * @deprecated As of release 1.2.9, replaced by {@link #NAME}.
	 */
	@Deprecated
	public static final String PROPERTY_NAME = NAME;
	
	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Sets the name of this object.
	 * @param name new name
	 */
	public void setName(String name);
}
