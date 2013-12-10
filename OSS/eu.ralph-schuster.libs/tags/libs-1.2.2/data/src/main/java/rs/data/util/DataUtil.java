/**
 * 
 */
package rs.data.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.data.api.bo.IGeneralBO;

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
	public static <K extends Serializable> List<K> idList(Collection<? extends IGeneralBO<K>> businessObjects) {
		List<K> rc = new ArrayList<K>();
		for (IGeneralBO<K> bo : businessObjects) {
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
	public static <K extends Serializable> K[] idArray(Collection<? extends IGeneralBO<K>> businessObjects, Class<K> keyClass) {
		List<K> list = idList(businessObjects);
		return list.toArray((K[])Array.newInstance(keyClass, list.size()));
	}
	
}
