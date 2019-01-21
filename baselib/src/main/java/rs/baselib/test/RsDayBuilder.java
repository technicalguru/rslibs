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

import rs.baselib.util.RsDay;

/**
 * Builder for {@link RsDay} objects.
 * <p>The builder always create the current day but can be initialized with 
 * a starting time and an offset to be used.</p>
 * @author ralph
 *
 */
public class RsDayBuilder implements Builder<RsDay>{

	/** the first time to be used */
	private Long time = null;
	/** the time builder to be used */
	private Builder<Long> timeBuilder = null;
	/** the timezone to be used */
	private TimeZone timezone = null;

	/**
	 * Constructor.
	 */
	public RsDayBuilder() {
	}

	/**
	 * Create the day with given time.
	 * @param timeInMilliseconds the time to be used for creation (base time)
	 * @return the builder for concatenation
	 */
	public RsDayBuilder withTime(long timeInMilliseconds) {
		this.time = timeInMilliseconds;
		return this;
	}

	/**
	 * Create the day with given time builder.
	 * @param timeBuilder builder for creating the time 
	 * @return the builder for concatenation
	 */
	public RsDayBuilder withTime(Builder<Long> timeBuilder) {
		this.timeBuilder = timeBuilder;
		return this;
	}

	/**
	 * Create the day with given timezone.
	 * @param timezone timezone to be used
	 * @return the builder for concatenation
	 */
	public RsDayBuilder withTimezone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDay build() {
		RsDay rc = null;
		if (this.time != null) {
			rc = new RsDay(this.time);
		} else if (timeBuilder != null) {
			rc = new RsDay(timeBuilder.build());
		} else {
			rc = new RsDay();
		}
		if (timezone != null) rc.setTimeZone(timezone);
		return rc;
	}
}