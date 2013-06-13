/**
 * 
 */
package rs.baselib.util;

import java.io.Serializable;

/**
 * A period of time.
 * @author ralph
 *
 */
public class DateTimePeriod implements Serializable {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7921353873601015818L;
	
	private RsDate from;
	private RsDate until;
	
	/**
	 * Constructor.
	 */
	public DateTimePeriod() {
	}

	
	/**
	 * Constructor.
	 */
	public DateTimePeriod(RsDate from, RsDate until) {
		setFrom(from);
		setUntil(until);
	}

	/**
	 * Returns the from.
	 * @return the from
	 */
	public RsDate getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 * @param from the from to set
	 */
	public void setFrom(RsDate from) {
		this.from = from;
	}

	/**
	 * Returns the until.
	 * @return the until
	 */
	public RsDate getUntil() {
		return until;
	}

	/**
	 * Sets the until.
	 * @param until the until to set
	 */
	public void setUntil(RsDate until) {
		this.until = until;
	}

	/**
	 * Tells whether date is in this range.
	 * @param date date to be checked
	 * @return true when date falls in this period
	 */
	public boolean isIn(RsDate date) {
		RsDate from = getFrom();
		RsDate until = getUntil();
		if ((from == null) && (until == null)) return true;
		if (until == null) return from.before(date);
		if (from == null) return until.after(date);
		return from.before(date) && until.after(date);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((until == null) ? 0 : until.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DateTimePeriod)) {
			return false;
		}
		DateTimePeriod other = (DateTimePeriod) obj;
		if (getFrom() == null) {
			if (other.getFrom() != null) {
				return false;
			}
		} else if (!getFrom().equals(other.getFrom())) {
			return false;
		}
		if (getUntil() == null) {
			if (other.getUntil() != null) {
				return false;
			}
		} else if (!getUntil().equals(other.getUntil())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns a deep copy of this period.
	 * @return deep copy
	 */
	public DateTimePeriod deepCopy() {
		RsDate from = getFrom();
		if (from != null) from = (RsDate)from.clone();
		RsDate until = getUntil();
		if (until != null) until = (RsDate)until.clone();
		return new DateTimePeriod(from, until);
	}
}
