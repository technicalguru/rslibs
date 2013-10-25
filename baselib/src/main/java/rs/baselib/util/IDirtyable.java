/**
 * 
 */
package rs.baselib.util;

import javax.persistence.Transient;

/**
 * Tells whether an object has changed or not.
 * @author ralph
 *
 */
public interface IDirtyable {

	/** Property name */
	public static final String DIRTY = "dirty";
	
	/**
	 * Returns whether this object has changed.
	 * @return true or false
	 */
	@Transient
	public boolean isDirty();
	

}
