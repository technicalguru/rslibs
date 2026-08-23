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

import static rs.baselib.test.BuilderUtils.$Int;
import static rs.baselib.test.BuilderUtils.$RandomString;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.baselib.io.FileFinder;

/**
 * Produces street names.
 * 
 * @author ralph
 *
 */
public class StreetBuilder extends AbstractBuilder<String> {

	private List<String>     streetNames;
	private Builder<Integer> numberBuilder;
	private boolean          numbersSet;
	private Builder<String>  numberExtensionBuilder;
	private boolean          numberExtensionsSet;
	private boolean          englishFormat;
	
	
	/**
	 * Constructor.
	 */
	public StreetBuilder() {
		this.englishFormat = true;
	}

	/**
	 * Use these names when building street names.
	 * <p>Unset or NULL will use default list.
	 * @param streetNames - list of names to randomly pick from.
	 * @return this builder for method chaining
	 */
	public StreetBuilder withStreetNames(Collection<String> streetNames) {
		this.streetNames    = streetNames != null ? new ArrayList<>(streetNames) : null;
		return this;
	}
	
	/**
	 * Use the street names from this file (one name per line).
	 * @param streetNamesFilename - filename of file
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public StreetBuilder withStreetNames(String streetNamesFilename) throws IOException {
		return withStreetNames(FileFinder.find(streetNamesFilename));
	}
	
	/**
	 * Use the street names from this file (one name per line).
	 * @param streetNamesFilename - file object
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public StreetBuilder withStreetNames(File streetNamesFilename) throws IOException {
		try {
			return withStreetNames(streetNamesFilename.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Cannot access file", e);
		}
	}
	
	/**
	 * Use the street names from this URL (one name per line).
	 * @param streetNamesUrl - URL to load from
	 * @return this builder for method chaining
	 * @throws IOException - when the content cannot be loaded
	 */
	public StreetBuilder withStreetNames(URL streetNamesUrl) throws IOException {
		return withStreetNames(loadUrlList(streetNamesUrl));
	}
	
	/**
	 * Use this builder for the numbers.
	 * <p>Leave unset to use default number generator.
	 * @param numberBuilder - builder that generates the house numbers.
	 * @return this builder for method chaining
	 */
	public StreetBuilder withNumberBuilder(Builder<Integer> numberBuilder) {
		this.numberBuilder = numberBuilder;
		this.numbersSet    = true;
		return this;
	}
	
	/**
	 * Do not generate house numbers.
	 * @return this builder for method chaining
	 */
	public StreetBuilder withoutNumbers() {
		return withNumberBuilder(null);
	}
	
	/**
	 * Use this builder for the number extensions (the letters attached to house numbers, e.g. 13a).
	 * <p>Only ~10% of generated house numbers will have an extension attached.
	 * <p>Leave unset to use default generator. Set NULL if you don't want extensions to be generated.
	 * <p>The generator will be ignored when numberBuilder was set to NULL.
	 * @param numberBuilder - builder that generates the house numbers.
	 * @return this builder for method chaining
	 */
	public StreetBuilder withNumberExtensionBuilder(Builder<String> numberExtensionBuilder) {
		this.numberExtensionBuilder = numberExtensionBuilder;
		this.numberExtensionsSet    = true;
		return this;
	}

	/**
	 * Do not generate house number extensions.
	 * @return this builder for method chaining
	 */
	public StreetBuilder withoutNumberExtensions() {
		return withNumberExtensionBuilder(null);
	}
	
	/**
	 * Defines whether the house number goes first (english format: "123 Kent Road") or last ("Kent Road 123").
	 * @param englishFormat true when house number shall be first in returned built street 
	 * @return this builder for method chaining
	 */
	public StreetBuilder withEnglishFormat(boolean englishFormat) {
		this.englishFormat = englishFormat;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String _build() {
		loadNames();
		initNumbers();
		
		String rc = streetNames.get(BuilderUtils.RNG.nextInt(0, streetNames.size()));
		String number = buildNumber();
		if (number != null) {
			if (englishFormat) rc = number + " "+rc;
			else               rc = rc + " " + number;
		}
		return rc;
	}

	/**
	 * Builds a house number.
	 * @return a house number
	 */
	public String buildNumber() {
		if (numberBuilder != null) {
			String rc = numberBuilder.build().toString();
			if ((numberExtensionBuilder != null) && (BuilderUtils.RNG.nextInt(0, 9) == 0)) {
				rc += numberExtensionBuilder.build();
			}
			return rc;
		}
		return null;
	}
	
	/**
	 * Lazy loads names when not set before.
	 */
	private void loadNames() {
		try {
			if (streetNames == null) withStreetNames(FileFinder.find(getClass(), "streets.txt"));
		} catch (IOException e) {
			throw new RuntimeException("Cannot load standard names", e);
		}
	}
	
	/**
	 * Lazy Init the number builders when not set yet.
	 */
	private void initNumbers() {
		if (!numbersSet) {
			if (numberBuilder == null) numberBuilder = $Int().withStart(1).withEnd(150).withRandom();
		}
		if (!numberExtensionsSet) {
			if (numberExtensionBuilder == null) numberExtensionBuilder = $RandomString().withChars("abcdefgh").withLength(1);
		}
	}
	
}
