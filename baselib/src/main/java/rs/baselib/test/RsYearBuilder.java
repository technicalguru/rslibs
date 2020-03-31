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

import java.util.TimeZone;

import rs.baselib.util.RsYear;

/**
 * Builder for {@link RsYear} objects.
 * <p>The builder always creates the current year but can be initialized with 
 * a starting year and an offset to be used.</p>
 * @author ralph
 *
 */
public class RsYearBuilder implements Builder<RsYear>{

	/** the time to be used */
	private Long time = null;
	/** the time builder to be used */
	private Builder<Long> timeBuilder = null;
	/** the year to be used */
	private Integer year = null;
	/** the year builder to be used */
	private Builder<Integer> yearBuilder = null;
	/** the timezone to be used */
	private TimeZone timezone = null;

	/**
	 * Constructor.
	 */
	public RsYearBuilder() {
	}

	/**
	 * Create the year with given time.
	 * @param timeInMilliseconds the time to be used for creation (base time)
	 * @return the builder for concatenation
	 */
	public RsYearBuilder withTime(long timeInMilliseconds) {
		this.time = timeInMilliseconds;
		return this;
	}

	/**
	 * Create the year with given time builder.
	 * @param timeBuilder builder for creating the time 
	 * @return the builder for concatenation
	 */
	public RsYearBuilder withTime(Builder<Long> timeBuilder) {
		this.timeBuilder = timeBuilder;
		return this;
	}

	/**
	 * Create the year with given value.
	 * @param year - year to be used
	 * @return the builder for concatenation
	 */
	public RsYearBuilder withYear(int year) {
		this.year  = year;
		return this;
	}

	/**
	 * Create the year with given builder.
	 * @param yearBuilder - year builder to be used
	 * @return the builder for concatenation
	 */
	public RsYearBuilder withYear(Builder<Integer> yearBuilder) {
		this.yearBuilder  = yearBuilder;
		return this;
	}

	/**
	 * Create the year with given timezone.
	 * @param timezone timezone to be used
	 * @return the builder for concatenation
	 */
	public RsYearBuilder withTimezone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsYear build() {
		RsYear rc = null;
		if (year != null) {
			rc = timezone != null ? new RsYear(timezone, year) : new RsYear(year);
		} else if (yearBuilder != null) {
			rc = timezone != null ? new RsYear(timezone, yearBuilder.build()) : new RsYear(yearBuilder.build());
		} else {
			long t = System.currentTimeMillis();
			if (this.time != null) {
				t = this.time.longValue();
			} else if (timeBuilder != null) {
				t = timeBuilder.build();
			}
			if (timezone != null) {
				rc = new RsYear(timezone, t);
			} else {
				rc = new RsYear(t);
			}
		}
		return rc;
	}


}
