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

import org.apache.commons.lang3.RandomUtils;

/**
 * A Long builder.
 * @author ralph
 *
 */
public class LongBuilder implements Builder<Long> {

	/** the uniqueness count */
	private long count;
	/** the offset */
	private long offset;
	/** end number (for random only) */
	private long end;
	/** whether to create random numbers */
	private boolean random = false;

	/**
	 * Constructor.
	 */
	public LongBuilder() {
		this.count  = 0;
		this.offset = 1;
	}

	/**
	 * Start the build with a given long.
	 * @param start - the first number to produce
	 * @return this builder for concatenation
	 */
	public LongBuilder withStart(long start) {
		this.count   = start;
		return this;
	}

	/**
	 * Set a given increment/decrement for each build.
	 * @param offset - the increment/decrement to produce
	 * @return this builder for concatenation
	 */
	public LongBuilder withOffset(long offset) {
		this.offset   = offset;
		return this;
	}

	/**
	 * Set a given max number (for random numbers only).
	 * @param end - the max number to use
	 * @return this builder for concatenation
	 */
	public LongBuilder withEnd(long end) {
		this.end   = end;
		return this;
	}

	/**
	 * Set random creation.
	 * @return this builder for concatenation
	 */
	public LongBuilder withRandom() {
		this.random   = true;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long build() {
		Long rc = 0L;
		if (!random) {
			rc = Long.valueOf(count);
			count += offset;
		} else {
			long l = RandomUtils.nextLong(0, count);
			while (l > end) {
				l = RandomUtils.nextLong(0, count);
			}
			rc = Long.valueOf(l);
		}
		return rc;
	}


}
