/**
 * 
 */
package rs.baselib.bean;

/**
 * A BO having a name.
 * @author ralph
 *
 */
public interface NamedObject {

	public static final String PROPERTY_NAME = "name";
	
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
