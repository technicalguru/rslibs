/*
 * This file is part of RS Library (Base Library).
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
package rs.baselib.type;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class to store basic information about a city.
 * @author ralph
 *
 */
public class City implements Serializable {

	/** Serial UID */
	private static final long serialVersionUID = 1L;

	/** Name of city */
	private String name;
	/** The country */
	private Country country;
	/** The area code */
	private String areaCode;
	/** The state */
	private String state;
	
	/**
	 * Default Constructor.
	 */
	public City() {}

	/**
	 * Constructor.
	 * @param name name of city
	 * @param country the country
	 * @param areaCode the area code (without leading zero)
	 * @param state state within country
	 */
	public City(String name, Country country, String areaCode, String state) {
		this.name = name;
		this.country = country;
		this.areaCode = areaCode;
		this.state = state;
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the country.
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Sets the country.
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Returns the areaCode.
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * Sets the areaCode.
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * Returns the state.
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state.
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(areaCode, country, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(areaCode, other.areaCode) && country == other.country && Objects.equals(name, other.name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "City [name=" + name + ", country=" + country + ", areaCode=" + areaCode + ", state=" + state + "]";
	}
	
}
