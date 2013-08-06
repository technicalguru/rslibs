/**
 * 
 */
package rs.data.example.api;

import rs.data.api.bo.ILongBO;

/**
 * Exmaple customer interface.
 * @author ralph
 *
 */
public interface Customer extends ILongBO {
	
	/** Property name for name */
	public static final String NAME    = "name";
	/** Property name for address */
	public static final String ADDRESS = "address";
	/** Property name for phone */
	public static final String PHONE   = "phone";

	/**
	 * Returns the name of this customer.
	 * @return name of customer
	 */
	public String getName();
	
	/**
	 * Sets the name of this customer.
	 * @param name new name
	 */
	public void   setName(String name);
	
	/**
	 * Returns the complete address of the customer.
	 * @return the current address
	 */
	public String getAddress();
	
	/**
	 * Sets the new address for this customer.
	 * @param address complete new address
	 */
	public void   setAddress(String address);
	
	/**
	 * Returns the phone number of this customer.
	 * @return phone number
	 */
	public String getPhone();
	
	/**
	 * Sets the phone number of this customer.
	 * @param phone new phone number
	 */
	public void   setPhone(String phone);

}
