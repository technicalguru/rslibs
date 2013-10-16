/**
 * 
 */
package rs.data.file.impl;

import java.util.Collection;
import java.util.Map;

import rs.baselib.type.Address;
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
	public static final String INVOICE_ADDRESS = "invoiceAddress";
	/** Property name for phones */
	public static final String PHONES   = "phones";
	/** Property name for a single phone */
	public static final String PHONE   = "phone";
	/** Property name for all delivery addresses */
	public static final String DELIVERY_ADDRESSES   = "deliveryAddresses";
	/** Property name for a single delivery address */
	public static final String DELIVERY_ADDRESS   = "deliveryAddress";

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
	public Address getInvoiceAddress();
	
	/**
	 * Sets the new address for this customer.
	 * @param address complete new address
	 */
	public void   setInvoiceAddress(Address address);
	
	/**
	 * Returns the phone numbers of this customer.
	 * @return phone numbers
	 */
	public Map<String,String> getPhones();
	
	/**
	 * Sets the phone numbers of this customer.
	 * @param phones new phone numbers
	 */
	public void   setPhones(Map<String,String> phones);

	/**
	 * Adds a new phone number.
	 * @param purpose the purpose of this phone number
	 * @param phone the phone number
	 */
	public void addPhone(String purpose, String phone);
	
	/**
	 * Removes a phone number
	 * @param purpose the phone purpose
	 */
	public void removePhone(String purpose);
	
	/**
	 * Returns all delivery addresses.
	 * @return the delivery addresses
	 */
	public Collection<Address> getDeliveryAddresses();

	/**
	 * Adds a delivery address.
	 * @param address the address to be added
	 */
	public void addDeliveryAddress(Address address);

	/**
	 * Removes a delivery address.
	 * @param address the address to be removed
	 */
	public void removeDeliveryAddress(Address address);
	
	/**
	 * Sets all delivery addresses.
	 * @param addresses the delivery addresses
	 */
	public void setDeliveryAddresses(Collection<Address> addresses);
}
