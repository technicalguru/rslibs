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
	
	private static SimpleDateFormat KEY_FORMATTER() {
		return new SimpleDateFormat("yyyy");
	}
	
	/**
	 * Constructor.
	 */
	public RsYear() {
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsYear(Date date) {
		super(date);
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsYear(long timeInMillis) {
		super(timeInMillis);
		ensureBegin();
	}

	/**
	 * Constructor.
	 * @param calendar
	 */
	public RsYear(Calendar calendar) {
		super(calendar);
		ensureBegin();
	}

	/**
	 * Constructor.
	 */
	public RsYear(int year) {
		super(0);
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
	}
	
	/**
	 * Returns the key of this month.
	 * @return the key
	 */
	public String getKey() {
		return KEY_FORMATTER().format(getTime());
	}
	
	/**
	 * Returns the key of this month.
	 * @return the year
	 */
	public int getIntKey() {
		return get(YEAR);
	}
	
	/**
	 * Returns the begin of this month.
	 * @return begin of month
	 */
	public RsDate getBegin() {
		RsDate rc = new RsDate(getTimeInMillis());
		rc.set(DAY_OF_MONTH, 1);
		rc.set(MONTH, 0);
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
		RsDate rc = new RsDate(getTimeInMillis());
		rc.set(DAY_OF_MONTH, rc.getActualMaximum(DAY_OF_MONTH));
		rc.set(MONTH, rc.getActualMaximum(MONTH));
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
	public RsYear getNext() {
		RsYear rc = new RsYear(getTimeInMillis());
		rc.add(YEAR, 1);
		return rc;
	}
	
	/**
	 * Returns the previous month.
	 * @return previous month
	 */
	public RsYear getPrevious() {
		RsYear rc = new RsYear(getTimeInMillis());
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
			return new RsYear(KEY_FORMATTER().parse(key));
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
