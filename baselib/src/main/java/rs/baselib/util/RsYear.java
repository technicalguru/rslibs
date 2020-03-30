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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A year.
 * @author Ralph
 *
 */
public class RsYear extends RsDate {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;
	
	private static SimpleDateFormat KEY_PARSER() {
		return new SimpleDateFormat("yyyy");
	}
	
	/**
	 * Constructor.
	 * @since 1.2.8
	 */
	public RsYear() {
		this(TimeZone.getDefault());
	}

	/**
	 * Constructor.
	 * @param timezone - init with timezone
	 */
	public RsYear(TimeZone timezone) {
		super(timezone != null ? timezone : TimeZone.getDefault());
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param date - init with date
	 */
	public RsYear(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with timezone
	 * @param date - init with date
	 */
	public RsYear(TimeZone timezone, Date date) {
		super(timezone, date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timeInMillis - init with timestamp
	 */
	public RsYear(long timeInMillis) {
		super(timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param timezone - init with timezone
	 * @param timeInMillis - init with timestamp
	 */
	public RsYear(TimeZone timezone, long timeInMillis) {
		super(timezone, timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param calendar - init with calendar time
	 */
	public RsYear(Calendar calendar) {
		super(calendar);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param year - year value
	 */
	public RsYear(int year) {
		this(TimeZone.getDefault(), year);
	}
	
	/**
	 * Constructor.
	 * @param timezone - init with timezone
	 * @param year - year value
	 * @since 1.2.8
	 */
	public RsYear(TimeZone timezone, int year) {
		super(timezone, 0);
		set(YEAR, year);
		ensureBegin();
	}

	/**
	 * Adjusts the time stamp to the start of the year.
	 */
	protected void ensureBegin() {
		set(MONTH, 0);
		set(DAY_OF_MONTH, 1);
		set(HOUR_OF_DAY, 0);
		set(MINUTE, 0);
		set(SECOND, 0);
		set(MILLISECOND, 0);
		getKey();
	}
	
	/**
	 * Returns the key of this year.
	 * @return the key
	 */
	public String getKey() {
		StringBuffer rc = new StringBuffer();
		rc.append(get(YEAR));
		return rc.toString();
	}
	
	/**
	 * Returns the key of this year.
	 * @return the year
	 */
	public int getIntKey() {
		return get(YEAR);
	}
	
	/**
	 * Returns the begin of this year.
	 * @return begin of year
	 */
	public RsDate getBegin() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(DAY_OF_MONTH, 1);
		rc.set(MONTH, 0);
		rc.set(HOUR_OF_DAY, 0);
		rc.set(MINUTE, 0);
		rc.set(SECOND, 0);
		rc.set(MILLISECOND, 0);
		return rc;
	}
	
	/**
	 * Returns the end of this year.
	 * @return begin of year
	 */
	public RsDate getEnd() {
		RsDate rc = new RsDate(getTimeZone(), getTimeInMillis());
		rc.set(DAY_OF_MONTH, rc.getActualMaximum(DAY_OF_MONTH));
		rc.set(MONTH, rc.getActualMaximum(MONTH));
		rc.set(HOUR_OF_DAY, rc.getActualMaximum(HOUR_OF_DAY));
		rc.set(MINUTE, rc.getActualMaximum(MINUTE));
		rc.set(SECOND, rc.getActualMaximum(SECOND));
		rc.set(MILLISECOND, rc.getActualMaximum(MILLISECOND));
		return rc;
	}
	
	/**
	 * Returns the next year.
	 * @return next year
	 */
	public RsYear getNext() {
		RsYear rc = new RsYear(getTimeZone(), getTimeInMillis());
		rc.add(YEAR, 1);
		return rc;
	}
	
	/**
	 * Returns the previous year.
	 * @return previous year
	 */
	public RsYear getPrevious() {
		RsYear rc = new RsYear(getTimeZone(), getTimeInMillis());
		rc.add(YEAR, -1);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof RsYear)) return false;
		
		RsYear m = (RsYear)o;
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
	 * Returns a year object for the given key.
	 * @param key key of year
	 * @return year object
	 */
	public static RsYear getYear(String key) {
		try {
			return new RsYear(KEY_PARSER().parse(key));
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse month: "+key, e);
		}
	}
	
	/**
	 * Returns a year object for the given key.
	 * @param year year number
	 * @return year object
	 */
	public static RsYear getYear(int year) {
		return new RsYear(year);
	}
}
