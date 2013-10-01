/**
 * 
 */
package rs.baselib.type;

import java.io.Serializable;

import rs.baselib.bean.AbstractBean;

/**
 * A location address.
 * @author ralph
 *
 */
public class Address extends AbstractBean implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/** Property name of street1 */
	public static final String STREET1 = "street1";
	/** Property name of street2 */
	public static final String STREET2 = "street2";
	/** Property name of city */
	public static final String CITY = "city";
	/** Property name of zipCode */
	public static final String ZIP_CODE = "zipCode";
	/** Property name of state */
	public static final String STATE = "state";
	/** Property name of country */
	public static final String COUNTRY = "country";
	
	/** First line of street address */
	private String street1;
	/** Seconds line of street address */
	private String street2;
	/** Name of city */
	private String city;
	/** ZIP-Code of city */
	private String zipCode;
	/** State of city */
	private String state;
	/** Country */
	private Country country;
	
	/**
	 * Constructor.
	 */
	public Address() {
	}

	/**
	 * Constructor.
	 * @param street1 street
	 * @param city name of city
	 * @param zipCode ZIP code of city
	 * @param country country
	 */
	public Address(String street1, String city, String zipCode, Country country) {
		this(street1, null, city, zipCode, null, country);
	}

	/**
	 * Constructor.
	 * @param street1 street (1st line)
	 * @param city name of city
	 * @param zipCode ZIP code of city
	 * @param state state of city
	 * @param country country
	 */
	public Address(String street1, String city, String zipCode, String state, Country country) {
		this(street1, null, city, zipCode, state, country);
	}

	/**
	 * Constructor.
	 * @param street1 street (1st line)
	 * @param street2 street (2nd line)
	 * @param city name of city
	 * @param zipCode ZIP code of city
	 * @param state state of city
	 * @param country country
	 */
	public Address(String street1, String street2, String city, String zipCode, String state, Country country) {
		setStreet1(street1);
		setStreet2(street2);
		setCity(city);
		setZipCode(zipCode);
		setState(state);
		setCountry(country);
	}

	/**
	 * Returns the {@link #street1}.
	 * @return the street1
	 */
	public String getStreet1() {
		return street1;
	}

	/**
	 * Sets the {@link #street1}.
	 * @param street1 the street1 to set
	 */
	public void setStreet1(String street1) {
		String oldValue = getStreet1();
		this.street1 = street1;
		firePropertyChange(STREET1, oldValue, street1);
	}

	/**
	 * Returns the {@link #street2}.
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}

	/**
	 * Sets the {@link #street2}.
	 * @param street2 the street2 to set
	 */
	public void setStreet2(String street2) {
		String oldValue = getStreet2();
		this.street2 = street2;
		firePropertyChange(STREET2, oldValue, street2);
	}

	/**
	 * Returns the {@link #city}.
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the {@link #city}.
	 * @param city the city to set
	 */
	public void setCity(String city) {
		String oldValue = getCity();
		this.city = city;
		firePropertyChange(CITY, oldValue, city);
	}

	/**
	 * Returns the {@link #zipCode}.
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * Sets the {@link #zipCode}.
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		String oldValue = getZipCode();
		this.zipCode = zipCode;
		firePropertyChange(ZIP_CODE, oldValue, zipCode);
	}

	/**
	 * Returns the {@link #state}.
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the {@link #state}.
	 * @param state the state to set
	 */
	public void setState(String state) {
		String oldValue = getState();
		this.state = state;
		firePropertyChange(STATE, oldValue, state);
	}

	/**
	 * Returns the {@link #country}.
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Sets the {@link #country}.
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		Country oldValue = getCountry();
		this.country = country;
		firePropertyChange(COUNTRY, oldValue, country);
	}

}
