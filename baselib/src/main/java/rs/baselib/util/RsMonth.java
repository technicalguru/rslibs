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
package rs.baselib.util;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A month.
 * @author Ralph
 *
 */
public class RsMonth extends RsDate {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private static SimpleDateFormat KEY_PARSER() {
		return new SimpleDateFormat("yyyyMM");
	}
	
	/**
	 * Constructor.
	 */
	public RsMonth() {
		this(TimeZone.getDefault());
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @since 1.2.8
	 */
	public RsMonth(TimeZone timezone) {
		super(timezone != null ? timezone : TimeZone.getDefault());
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param date - init with date
	 */
	public RsMonth(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param date - init with date
	 */
	public RsMonth(TimeZone timezone, Date date) {
		super(timezone, date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timeInMillis - init with timestamp
	 */
	public RsMonth(long timeInMillis) {
		super(timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param timeInMillis - init with timestamp
	 */
	public RsMonth(TimeZone timezone, long timeInMillis) {
		super(timezone, timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param calendar - init with calendar value
	 */
	public RsMonth(Calendar calendar) {
		super(calendar);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param month month value (0-11)
	 * @param year year value
	 */
	public RsMonth(int month, int year) {
		this(TimeZone.getDefault(), month, year);
	}

	/**
	 * Constructor.
	 * @param timezone - init with this timezone
	 * @param month month value (0-11)
	 * @param year year value
	 * @since 1.2.8
	 */
	public RsMonth(TimeZone timezone, int month, int year) {
		super(timezone, 0);
		set(MONTH, month);
		set(YEAR, year);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timestamp the zoned date time object from JavaTime API
	 * @since 3.0.0
	 */
	public RsMonth(ZonedDateTime timestamp) {
		this(TimeZone.getTimeZone(timestamp.getZone()), timestamp.getMonthValue()-1, timestamp.getYear());
	}
	
	/**
	 * Adjusts the time stamp to the start of the month.
	 */
	protected void ensureBegin() {
		set(DAY_OF_MONTH, 1);
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
		getKey();
	}
	
	/**
	 * Returns the key of this month.
	 * @return the key of this month
	 */
	public String getKey() {
		StringBuffer rc = new StringBuffer();
		rc.append(get(YEAR));
		int m = get(MONTH);
		if (m < 9) rc.append('0');
		rc.append(m+1);
		return rc.toString();
	}
	
	/**
	 * Returns the begin of this month.
	 * @return begin of month
	 */
	public RsDate getBegin() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(DAY_OF_MONTH, 1);
		rc.set(HOUR_OF_DAY, 0);
		rc.set(MINUTE, 0);
		rc.set(SECOND, 0);
		rc.set(MILLISECOND, 0);
		return rc;
	}
	
	/**
	 * Returns the end of this month.
	 * @return begin of month
	 */
	public RsDate getEnd() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(DAY_OF_MONTH, rc.getActualMaximum(DAY_OF_MONTH));
		rc.set(HOUR_OF_DAY, rc.getActualMaximum(HOUR_OF_DAY));
		rc.set(MINUTE, rc.getActualMaximum(MINUTE));
		rc.set(SECOND, rc.getActualMaximum(SECOND));
		rc.set(MILLISECOND, rc.getActualMaximum(MILLISECOND));
		return rc;
	}
	
	/**
	 * Returns the next month.
	 * @return next month
	 */
	public RsMonth getNext() {
		return roll(1);
	}
	
	/**
	 * Returns the n-th next month.
	 * @param n the number of months to roll
	 * @return n-th next month
	 * @since 3.0.0
	 */
	public RsMonth getNext(int n) {
		return roll(n);
	}
	
	/**
	 * Returns the previous month.
	 * @return previous month
	 */
	public RsMonth getPrevious() {
		return roll(-1);
	}
	
	/**
	 * Returns the n-th previous month.
	 * @param n the number of months to roll
	 * @return n-th previous month
	 * @since 3.0.0
	 */
	public RsMonth getPrevious(int n) {
		return roll(-n);
	}
	
	/**
	 * Returns a month by rolling back or forth this month
	 * @param months months to roll (can be negative)
	 * @return the n-th month before or after this month
	 * @since 3.0.0
	 */
	public RsMonth roll(int months) {
		RsMonth rc = new RsMonth(getTimeZone(), getTimeInMillis());
		rc.add(MONTH, months);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof RsMonth)) return false;
		
		RsMonth m = (RsMonth)o;
		return getKey().equals(m.getKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/**
	 * Returns a month object for the given key.
	 * @param key key of month
	 * @return month object
	 */
	public static RsMonth getMonth(String key) {
		try {
			return new RsMonth(KEY_PARSER().parse(key));
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse month: "+key, e);
		}
	}
	
	/**
	 * Returns a month object for the given timestamp.
	 * @param timestamp any timestamp of JavaTime API
	 * @return month object
	 * @since 3.0.0
	 */
	public static RsMonth from(ZonedDateTime timestamp) {
		return new RsMonth(timestamp);
	}

}
