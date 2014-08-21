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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.baselib.type.IIdObject;

/**
 * Some data utilities.
 * @author ralph
 *
 */
public class DataUtil {

	/**
	 * Returns the IDs of all objects as a list.
	 * @param businessObjects collection of business objects.
	 * @return list of IDs.
	 */
	public static <K extends Serializable> List<K> idList(Collection<? extends IIdObject<K>> businessObjects) {
		List<K> rc = new ArrayList<K>();
		for (IIdObject<K> bo : businessObjects) {
			if (bo != null) {
				rc.add(bo.getId());
			}
		}
		return rc;
	}
	
	/**
	 * Returns the IDs of all objects as a list.
	 * @param businessObjects collection of business objects.
	 * @return list of IDs.
	 */
	@SuppressWarnings("unchecked")
	public static <K extends Serializable> K[] idArray(Collection<? extends IIdObject<K>> businessObjects, Class<K> keyClass) {
		List<K> list = idList(businessObjects);
		return list.toArray((K[])Array.newInstance(keyClass, list.size()));
	}
	
}
