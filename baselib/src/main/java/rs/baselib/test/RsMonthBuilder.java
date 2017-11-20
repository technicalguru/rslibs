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

import rs.baselib.util.RsMonth;

/**
 * Builder for {@link RsMonth} objects.
 * <p>The builder always creates the current month but can be initialized with 
 * a starting month and an offset to be used.</p>
 * @author ralph
 *
 */
public class RsMonthBuilder implements Builder<RsMonth>{

	/** the time to be used */
	private Long time = null;
	/** the month to be used */
	private Integer month = null;
	/** the year to be used */
	private Integer year = null;
	/** the timezone to be used */
	private TimeZone timezone = null;
	/** the month offset to be used with each build */
	private Integer monthOffset = null;
	/** the counter for offsetting the time */
	private int count;
	
	/**
	 * Constructor.
	 */
	public RsMonthBuilder() {
		this.count = 0;
	}

	/**
	 * Create the month with given values.
	 * @param month - month to be used
	 * @param year - year to be used
	 * @return the builder for concatenation
	 */
	public RsMonthBuilder withMonth(int month, int year) {
		this.month = month;
		this.year  = year;
		return this;
	}

	/**
	 * Create the month with given time.
	 * @param timeInMilliseconds the time to be used for creation (base time)
	 * @return the builder for concatenation
	 */
	public RsMonthBuilder withTime(long timeInMilliseconds) {
		this.time = timeInMilliseconds;
		return this;
	}

	/**
	 * Create the month with given timezone.
	 * @param timezone timezone to be used
	 * @return the builder for concatenation
	 */
	public RsMonthBuilder withTimezone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}
	
	/**
	 * Create each month with another month offset.
	 * @param months - month offset
	 * @return the builder for concatenation
	 */
	public RsMonthBuilder withMonthOffset(int months) {
		this.monthOffset = months;
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsMonth build() {
		RsMonth rc = null;
		if (this.time != null) {
			long time = this.time.longValue();
			rc = new RsMonth(time);
			if (timezone != null) rc.setTimeZone(timezone);
		} else if (month != null) {
			if (timezone != null) rc = new RsMonth(timezone, month, year);
			else rc = new RsMonth(month, year);
		} else {
			rc = new RsMonth();
			if (timezone != null) rc.setTimeZone(timezone);
		}
		if (monthOffset != null) {
			for (int i=0; i<count; i++) {
				if (monthOffset < 0) {
					for (int j=monthOffset; j<0; j++) {
						rc = rc.getPrevious();
					}
				} else {
					for (int j=0; j<monthOffset; j++) {
						rc = rc.getNext();
					}
				}
			}
			count++;
		}			
		if (this.time == null) this.time = rc.getBegin().getTimeInMillis();
		return rc;
	}

	
}
