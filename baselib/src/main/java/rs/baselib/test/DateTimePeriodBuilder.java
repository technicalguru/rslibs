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

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;

/**
 * Builder for {@link DateTimePeriod}s.
 * @author ralph
 *
 */
public class DateTimePeriodBuilder implements Builder<DateTimePeriod> {

	/** Fixed from date */
	private RsDate from = null;
	/** Fixed until date */
	private RsDate until = null;
	/** From builder */
	private Builder<RsDate> fromBuilder  = null;
	/** Until builder */
	private Builder<RsDate> untilBuilder = null;
	/**
	 * Constructor.
	 */
	public DateTimePeriodBuilder() {
	}

	/**
	 * Create dates using the given from date.
	 * @param from the date
	 * @return this object for concatenation
	 */
	public DateTimePeriodBuilder withFrom(RsDate from) {
		this.from = from;
		return this;
	}
	
	/**
	 * Create dates using the given from date builder.
	 * @param fromBuilder the date builder
	 * @return this object for concatenation
	 */
	public DateTimePeriodBuilder withFrom(Builder<RsDate> fromBuilder) {
		this.fromBuilder = fromBuilder;
		return this;
	}
	
	/**
	 * Create dates using the given until date.
	 * @param until the date
	 * @return this object for concatenation
	 */
	public DateTimePeriodBuilder withUntil(RsDate until) {
		this.until = until;
		return this;
	}
	
	/**
	 * Create dates using the given until date builder.
	 * @param untilBuilder the date builder
	 * @return this object for concatenation
	 */
	public DateTimePeriodBuilder withUntil(Builder<RsDate> untilBuilder) {
		this.untilBuilder = untilBuilder;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTimePeriod build() {
		// Build from
		RsDate from = null;
		if (this.from != null) {
			from = this.from;
		} else if (fromBuilder != null) {
			from = fromBuilder.build();
		}
		// Build until
		RsDate until = null;
		if (this.until != null) {
			until = this.until;
		} else if (untilBuilder != null) {
			until = untilBuilder.build();
		}
		
		return new DateTimePeriod(from, until);
	}

	
}
