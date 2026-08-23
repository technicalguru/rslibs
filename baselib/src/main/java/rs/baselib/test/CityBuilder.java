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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import rs.baselib.io.FileFinder;
import rs.baselib.type.City;
import rs.baselib.type.Continent;
import rs.baselib.type.Country;
import rs.baselib.util.CommonUtils;

/**
 * Provides a way to generate cities (see {@link City} randomly.
 * @author ralph
 *
 */
public class CityBuilder extends AbstractBuilder<City> {

	private List<City> cities;
	private boolean citiesFiltered;
	private Country    country;
	private Continent  continent;
	
	/**
	 * Constructor.
	 */
	public CityBuilder() {
		this.cities         = null;
		this.citiesFiltered = false;
	}
	
	/**
	 * Use these cities when building.
	 * @param cities - list of cities to randomly pick from
	 * @return this builder for method chaining
	 */
	public CityBuilder withCities(Collection<City> cities) {
		this.cities    = cities != null ? new ArrayList<>(cities) : null;
		return this;
	}
	
	/**
	 * Use the cities from this file (one city per line).
	 * <p>Format is basic CSV (without quotation marks, using comma): &lt;name&gt;,&lt;countryCode&lt;areaCode&gt;,&lt;state&gt;
	 * <p>The least information available must be the city. All others are optional, but position must be respected, e.g.:
	 * <pre>
	 *    Frankfurt,DE,69,Hessen
	 *    Frankfurt,DE,69
	 *    Frankfurt,DE,69,
	 *    Frankfurt,49,,Hessen
	 *    Frankfurt
	 * </pre>
	 * @param citiesFilename - filename of file
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public CityBuilder withCities(String citiesFilename) throws IOException {
		return withCities(FileFinder.find(citiesFilename));
	}
	
	/**
	 * Use the cities from this file (one city per line).
	 * <p>Format is basic CSV (without quotation marks, using comma): &lt;name&gt;,&lt;countryCode&lt;areaCode&gt;,&lt;state&gt;
	 * <p>The least information available must be the city. All others are optional, but position must be respected, e.g.:
	 * <pre>
	 *    Frankfurt,DE,69,Hessen
	 *    Frankfurt,DE,69
	 *    Frankfurt,DE,69,
	 *    Frankfurt,49,,Hessen
	 *    Frankfurt
	 * </pre>
	 * @param citiesFile - file object
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public CityBuilder withCities(File citiesFile) throws IOException {
		try {
			return withCities(citiesFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Cannot access file", e);
		}
	}
	
	/**
	 * Use the cities from this file (one city per line).
	 * <p>Format is basic CSV (without quotation marks, using comma): &lt;name&gt;,&lt;countryCode&lt;areaCode&gt;,&lt;state&gt;
	 * <p>The least information available must be the city. All others are optional, but position must be respected, e.g.:
	 * <pre>
	 *    Frankfurt,DE,69,Hessen
	 *    Frankfurt,DE,69
	 *    Frankfurt,DE,69,
	 *    Frankfurt,49,,Hessen
	 *    Frankfurt
	 * </pre>
	 * @param citiesUrl - URL to load from
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public CityBuilder withCities(URL citiesUrl) throws IOException {
		return withCities(loadCityList(citiesUrl));
	}
	
	/**
	 * Only return cities in given country.
	 * <p>If set, the builder will ignore the continent setting
	 * @param country the country to be used
	 * @return this builder for method chaining
	 */
	public CityBuilder withCountry(Country country) {
		this.country = country;
		return this;
	}
	
	/**
	 * Only return cities in given continent.
	 * <p>will be ignored when the city was set
	 * @param continent the continent to be used
	 * @return this builder for method chaining
	 */
	public CityBuilder withContinent(Continent continent) {
		this.continent = continent;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected City _build() {
		loadCities();
		return cities.get(BuilderUtils.RNG.nextInt(cities.size()));
	}

	/**
	 * Lazy loads names when not set before.
	 */
	private void loadCities() {
		try {
			if (cities == null) withCities(FileFinder.find(getClass(), "cities.csv"));
			if (!citiesFiltered) {
				if ((country != null) || (continent != null)) {
					CollectionUtils.filter(cities, c -> {
						if (country != null) return country.equals(c.getCountry());
						if (c.getCountry() != null)	return continent.equals(c.getCountry().getContinent());
						return false;
					});
				}
				citiesFiltered = true;
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot load standard cities", e);
		}
	}

	/**
	 * Load a list of string from a URL.
	 * @param url - URL to be loaded from
	 * @return the collection of strings loaded
	 * @throws IOException - when the content cannot be loaded
	 */
	protected List<City> loadCityList(URL url) throws IOException {
		List<String> lines = loadUrlList(url);
		List<City> rc = new ArrayList<>();
		for (String line : lines) {
			if (!CommonUtils.isEmpty(line)) {
				String infos[] = line.split("\\s*,\\s*");
				City city = new City();
				city.setName(infos[0]);
				if ((infos.length > 1) && !CommonUtils.isEmpty(infos[1])) city.setZip(infos[1].trim());
				if (infos.length > 2) city.setCountry(findCountry(infos[2].trim()));
				if ((infos.length > 3) && !CommonUtils.isEmpty(infos[3])) city.setAreaCode(infos[3].trim());
				if ((infos.length > 4) && !CommonUtils.isEmpty(infos[4])) city.setState(infos[4].trim());
				rc.add(city);
			}
		}
		return rc;
	}

	/**
	 * Finds the given country.
	 * The country will be identified by either ISO 2 country code (DE), ISO 3 country code (DEU), 
	 * International dial code (+49) or Top Level Domain (.de)
	 * @param countryCode
	 * @return
	 */
	protected static Country findCountry(String countryCode) {
		for (Country country : Country.values()) {
			if (countryCode.equalsIgnoreCase(country.getIso2Code())) return country;
			if (countryCode.equalsIgnoreCase(country.getIso3Code())) return country;
			if (("+"+countryCode).equalsIgnoreCase(country.getIdc())) return country;
			if (countryCode.equalsIgnoreCase(country.getTld())) return country;
		}
		return null;
	}
}
