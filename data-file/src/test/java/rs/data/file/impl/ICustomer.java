/**
 * 
 */
package rs.data.file.impl;

import rs.baselib.bean.NamedObject;
import rs.baselib.type.Address;
import rs.data.api.bo.ILongBO;

/**
 * Reference interface of a BO.
 * @author ralph
 *
 */
public interface ICustomer extends ILongBO, NamedObject {

	public static final String PROPERTY_ADDRESS = "address";
	
	public Address getAddress();
	public void setAddress(Address address);
}
