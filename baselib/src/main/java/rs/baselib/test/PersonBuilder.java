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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.baselib.io.FileFinder;
import rs.baselib.type.Address;
import rs.baselib.type.City;
import rs.baselib.util.CommonUtils;

/**
 * Provides a way to generate personal information (see {@link Person} randomly.
 * 
 * @author ralph
 *
 */
public class PersonBuilder extends AbstractBuilder<PersonBuilder.Person> {

	public static final int DEFAULT_MIN_AGE = 18;
	public static final int DEFAULT_MAX_AGE = 110;
	
	private List<String> genders;
	private List<String> firstNames;
	private List<String> lastNames;
	private Builder<Integer> ageBuilder;
	private boolean firstNamesSet;
	private boolean lastNamesSet;
	private Builder<String> phoneNumbers;
	private Builder<Address> addresses;
	
	/**
	 * Constructor.
	 */
	public PersonBuilder() {
		this.genders  = CommonUtils.newList("MALE", "FEMALE", "DIVERSE");
		firstNamesSet = false;
		lastNamesSet  = false;
	}

	/**
	 * Build the persons with the given set of genders.
	 * <p>Default set is { "MALE", "FEMALE", "DIVERSE" }.</p>
	 * @param genders - list of genders to randomly pick from ({@code null} will leave gender blank)
	 * @return this builder for method chaining
	 */
	public PersonBuilder withGenders(Collection<String> genders) {
		this.genders = genders != null ? new ArrayList<>(genders) : null;
		return this;
	}
	
	/**
	 * Build the persons without genders.
	 * @return this builder for method chaining
	 */
	public PersonBuilder withoutGenders() {
		this.genders = null;
		return this;
	}
	
	/**
	 * Use these first names when building a person.
	 * @param firstNames - list of names to randomly pick from ({@code null} will leave first names blank)
	 * @return this builder for method chaining
	 */
	public PersonBuilder withFirstNames(Collection<String> firstNames) {
		this.firstNames    = firstNames != null ? new ArrayList<>(firstNames) : null;
		this.firstNamesSet = true;
		return this;
	}
	
	/**
	 * Build persons without first names.
	 * @return this builder for method chaining
	 */
	public PersonBuilder withoutFirstNames() {
		this.firstNames    = null;
		this.firstNamesSet = true;
		return this;
	}
	
	/**
	 * Use the first names from this file (one name per line).
	 * @param firstNamesFilename - filename of file
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withFirstNames(String firstNamesFilename) throws IOException {
		return withFirstNames(FileFinder.find(firstNamesFilename));
	}
	
	/**
	 * Use the first names from this file (one name per line).
	 * @param firstNamesFile - file object
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withFirstNames(File firstNamesFile) throws IOException {
		try {
			return withFirstNames(firstNamesFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Cannot access file", e);
		}
	}
	
	/**
	 * Use the first names from this URL (one name per line).
	 * @param firstNamesUrl - URL to load from
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withFirstNames(URL firstNamesUrl) throws IOException {
		return withFirstNames(loadUrlList(firstNamesUrl));
	}
	
	/**
	 * Use these last names when building a person.
	 * @param lastNames - list of names to randomly pick from ({@code null} will leave last name blank)
	 * @return this builder for method chaining
	 */
	public PersonBuilder withLastNames(Collection<String> lastNames) {
		this.lastNames    = lastNames != null ? new ArrayList<>(lastNames) : null;
		this.lastNamesSet = true;
		return this;
	}
	
	/**
	 * Build persons without last names.
	 * @return this builder for method chaining
	 */
	public PersonBuilder withoutLastNames() {
		this.lastNames    = null;
		this.lastNamesSet = true;
		return this;
	}
	
	/**
	 * Use the last names from this file (one name per line).
	 * @param lastNamesFilename - filename of file to load from
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withLastNames(String lastNamesFilename) throws IOException {
		return withLastNames(FileFinder.find(lastNamesFilename));
	}
	
	/**
	 * Use the last names from this file (one name per line).
	 * @param lastNamesFile - file object
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withLastNames(File lastNamesFile) throws IOException {
		try {
			return withLastNames(lastNamesFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Cannot access file", e);
		}
	}
	
	/**
	 * Use the last names from this URL (one name per line).
	 * @param lastNamesUrl - URL to load from
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public PersonBuilder withLastNames(URL lastNamesUrl) throws IOException {
		return withLastNames(loadUrlList(lastNamesUrl));
	}
	
	/**
	 * Generate person with this builder for the age.
	 * @param ageBuilder - mbuilder for the age.
	 * @return this builder for method chaining
	 */
	public PersonBuilder withAge(Builder<Integer> ageBuilder) {
		this.ageBuilder = ageBuilder;
		return this;
	}
	
	/**
	 * Generate phone numbers.
	 * @param phoneNumbers phone number builder
	 * @return this builder for method chaining
	 */
	public PersonBuilder withPhoneNumbers(Builder<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
		return this;
	}
	
	/**
	 * Generate addresses.
	 * @param address address builder
	 * @return this builder for method chaining
	 */
	public PersonBuilder withAddresses(Builder<Address> addresses) {
		this.addresses = addresses;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Person _build() {
		loadNames();
		
		Person rc = new Person();
		if ((firstNames != null) && !firstNames.isEmpty()) rc.firstName   = firstNames.get(BuilderUtils.RNG.nextInt(0, firstNames.size()));
		if ((lastNames  != null) && !lastNames.isEmpty())  rc.lastName    = lastNames.get(BuilderUtils.RNG.nextInt(0, lastNames.size()));
		if ((genders    != null) && !genders.isEmpty())    rc.gender      = genders.get(BuilderUtils.RNG.nextInt(0, genders.size()));
		rc.address = addresses.build();
		if (addresses instanceof AddressBuilder) {
			City city  = ((AddressBuilder)addresses).getLastCity();
			rc.phoneNumber = city.getCountry().getIdc();
			if (city.getAreaCode() != null) rc.phoneNumber += " (0)"+city.getAreaCode();
			rc.phoneNumber += " "+phoneNumbers.build();
		} else {
			rc.phoneNumber = phoneNumbers.build();
		}
		if (ageBuilder == null) ageBuilder = new IntBuilder().withStart(DEFAULT_MIN_AGE).withEnd(DEFAULT_MAX_AGE).withRandom();
		rc.age           = ageBuilder.build();
		LocalDate firstB = LocalDate.now().minusYears(rc.age+1);  
		rc.birthday      = firstB.plusDays(BuilderUtils.RNG.nextLong(0, 365));
		
		
		return rc;
	}

	/**
	 * Lazy loads names when not set before.
	 */
	private void loadNames() {
		try {
			if (addresses == null) {
				addresses = new AddressBuilder();
				phoneNumbers = new RandomPhoneBuilder().withFormat("${extension}");
			}
			if (phoneNumbers == null) phoneNumbers = new RandomPhoneBuilder().withFormat("${extension}");
			
			if (!firstNamesSet) withFirstNames(FileFinder.find(getClass(), "firstNames.txt"));
			if (!lastNamesSet)  withLastNames(FileFinder.find(getClass(), "lastNames.txt"));
		} catch (IOException e) {
			throw new RuntimeException("Cannot load standard names", e);
		}
	}
	
	/**
	 * Information about a random, fictious person.
	 * 
	 * @author ralph
	 *
	 */
	public static class Person {
		public String    firstName;
		public String    lastName;
		public LocalDate birthday;
		public int       age;
		public String    gender;
		public String    phoneNumber;
		public Address   address;
		
		public Person() {}
		
		public Person(String firstName, String lastName, LocalDate birthday, int age, String gender, String phoneNumber, Address address) {
			this.firstName   = firstName;
			this.lastName    = lastName;
			this.birthday    = birthday;
			this.age         = age;
			this.gender      = gender;
			this.phoneNumber = phoneNumber;
			this.address     = address;
		}

		@Override
		public String toString() {
			return "Person [firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday + ", age="
					+ age + ", gender=" + gender + ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
		}
	}
}
