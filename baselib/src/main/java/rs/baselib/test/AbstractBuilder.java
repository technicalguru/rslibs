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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import rs.baselib.util.CommonUtils;

/**
 * Abstract implementation to ease development.
 * <p>Handles the last element and provides additional methods.
 * 
 * @author ralph
 *
 */
public abstract class AbstractBuilder<T> implements Builder<T> {

	private Builder<T> parentBuilder;

	private T last;

	/**
	 * No parent constructor.
	 */
	protected AbstractBuilder() {
		this(null);
	}
	
	/**
	 * Constructor with parent builder
	 * @param parentBuilder the parent builder to be called
	 */
	protected AbstractBuilder(Builder<T> parentBuilder) {
		this.parentBuilder = parentBuilder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T build() {
		last = parentBuilder != null ? _build(parentBuilder.build()) : _build();
		return last;
	}

	/**
	 * The actual specific builder method (when no parent builder is defined).
	 * @return the built value
	 */
	protected abstract T _build();
	
	/**
	 * Method when a parent built an object already.
	 * <p>The default implementation return the given object.
	 * @param value the object as built by the parent.
	 * @return the built object
	 */
	protected T _build(T value) {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T last() {
		return last;
	}
	
	/**
	 * Load a list of string from a URL.
	 * @param url - URL to be loaded from
	 * @return the collection of strings loaded
	 * @throws IOException - when the content cannot be loaded
	 */
	protected List<String> loadUrlList(URL url) throws IOException {
		String content = CommonUtils.loadContent(url, StandardCharsets.UTF_8).trim();
		return CommonUtils.newList(content.split("\n\r*"));
	}
	

}
