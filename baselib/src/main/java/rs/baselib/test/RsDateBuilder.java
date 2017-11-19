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

import rs.baselib.util.RsDate;

/**
 * Builder for {@link RsDate} objects.
 * <p>The builder create the current time always but can be initialized with 
 * a starting time and an offset to be used.</p>
 * @author ralph
 *
 */
public class RsDateBuilder implements Builder<RsDate>{

	/** the time to be used */
	private Long time = null;
	/** the timezone to be used */
	private TimeZone timezone = null;
	/** the time offset to be used with each build */
	private Long timeOffset = null;
	/** the counter for offsetting the time */
	private int count;
	
	/**
	 * Constructor.
	 */
	public RsDateBuilder() {
		this.count = 0;
	}

	/**
	 * Create the date with given time.
	 * @param timeInMilliseconds the time to be used for creation (base time)
	 * @return the builder for concatenation
	 */
	public RsDateBuilder withTime(long timeInMilliseconds) {
		this.time = timeInMilliseconds;
		return this;
	}
	
	/**
	 * Create the date with given timezone.
	 * @param timezone timezone to be used
	 * @return the builder for concatenation
	 */
	public RsDateBuilder withTimezone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}
	
	/**
	 * Create each date with another time offset.
	 * @param timeInMilliseconds
	 * @return the builder for concatenation
	 */
	public RsDateBuilder withTimeOffset(long timeOffsetInMilliseconds) {
		this.timeOffset = timeOffsetInMilliseconds;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate build() {
		long time = System.currentTimeMillis();
		if (this.time != null) time = this.time.longValue();
		if (timeOffset != null) {
			time += count*timeOffset;
			count++;
		}
		RsDate rc = new RsDate(time);
		if (timezone != null) rc.setTimeZone(timezone);
		return rc;
	}

	
}
