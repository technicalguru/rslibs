/**
 * 
 */
package rs.baselib.util;

/**
 * Tells whether an object has changed or not.
 * @author ralph
 *
 */
public interface IDirtyable {

	/**
	 * Returns whether this object has changed.
	 * @return true or false
	 */
	public boolean isDirty();
	

}
