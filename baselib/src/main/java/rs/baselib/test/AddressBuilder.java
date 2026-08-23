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
package rs.baselib.test;

import rs.baselib.type.Address;
import rs.baselib.type.City;

/**
 * Produces Addresses.
 * 
 * @author ralph
 *
 */
public class AddressBuilder extends AbstractBuilder<Address> {

	private Builder<String> street1;
	private Builder<String> street2;
	private Builder<City>   cityBuilder;
	private City            lastCity;
	
	/**
	 * Use the builder to generate street1 fields.
	 * <p>Unset or NULL will use the default {@link StreetBuilder}.
	 * @param street1 builder for street1 field
	 * @return the builder for method chaining
	 */
	public AddressBuilder withStreet1(Builder<String> street1) {
		this.street1 = street1;
		return this;
	}
	
	/**
	 * Use the builder to generate street2 fields.
	 * <p>Unset or NULL will not fill this field
	 * @param street2 builder for street2 field
	 * @return the builder for method chaining
	 */
	public AddressBuilder withStreet2(Builder<String> street2) {
		this.street2 = street2;
		return this;
	}
	
	/**
	 * Use the builder to generate city information (city, zip code, state, country).
	 * <p>Unset or NULL will use the default {@link CityBuilder}.
	 * @param street1 builder for city
	 * @return the builder for method chaining
	 */
	public AddressBuilder withCity(Builder<City> city) {
		this.cityBuilder = city;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Address _build() {
		if (street1     == null) street1     = new StreetBuilder();
		if (cityBuilder == null) cityBuilder = new CityBuilder();
		
		Address rc = new Address();
		rc.setStreet1(street1.build());
		if (street2 != null) rc.setStreet2(street2.build());
		City city = cityBuilder.build();
		rc.setCity(city.getName());
		rc.setZipCode(city.getZip());
		rc.setState(city.getState());
		rc.setCountry(city.getCountry());
		
		lastCity = city;
		return rc;
	}

	/**
	 * Returns the city information of last build.
	 * @return the city information
	 */
	public City getLastCity() {
		return lastCity;
	}

	
}
