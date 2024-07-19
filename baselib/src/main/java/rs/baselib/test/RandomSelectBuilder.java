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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rs.baselib.util.CommonUtils;

/**
 * Provides values randomly from a given list.
 * 
 * @author ralph
 *
 */
public class RandomSelectBuilder<T> implements Builder<T> {

	private List<T> values;
	
	/**
	 * Constructor.
	 */
	public RandomSelectBuilder() {
		values = new ArrayList<>();
	}

	/**
	 * Use the given list as values.
	 * @param values the value list to pick from
	 * @return the builder for method chaining
	 */
	public RandomSelectBuilder<T> withValues(Collection<T> values) {
		if ((values == null) || values.isEmpty()) throw new RuntimeException("values cannot be empty");
		this.values = new ArrayList<>(values);
		return this;
	}

	/**
	 * Use the given array as values.
	 * @param values the array list to pick from
	 * @return the builder for method chaining
	 */
	public RandomSelectBuilder<T> withValues(T values[]) {
		if ((values == null) || (values.length == 0)) throw new RuntimeException("values cannot be empty");
		this.values = CommonUtils.newList(values);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T build() {
		return values.get(BuilderUtils.RNG.nextInt(0, values.size()));
	}
	
	
}
