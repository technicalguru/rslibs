/**
 * 
 */
package rs.data.util;

import java.io.Serializable;

/**
 * The Class ID for caching mechanism.
 * @author ralph
 *
 */
public class CID implements Serializable {
	
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -9209131720989751209L;
	private Class<?> clazz;
	private Object id;
	
	public CID(Class<?> clazz, Object id) {
		this.clazz = clazz;
		this.id = id;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int rc = 31 * clazz.hashCode();
		if (id != null) rc += (id.hashCode() >>> 32);
		return rc;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof CID)) return false;
		CID cid = (CID)o;
		if (!clazz.equals(cid.clazz)) return false;
		if ((id == null) || (cid.id == null)) return false;
		return cid.id.equals(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "CID["+clazz.getSimpleName()+";"+id+"]";
	}
}

