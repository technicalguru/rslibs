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
package rs.baselib.type;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import rs.baselib.lang.HashCodeUtil;

/**
 * A {@link BigDecimal} configured for monetary operations.
 * <p>The {@link BigDecimal} is configured with a precision scale of 2, {@link RoundingMode#HALF_UP}.</p>
 * @author ralph
 *
 */
public class MonetaryValue implements Serializable, Comparable<MonetaryValue> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/** Banker's rounding */
	public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	/** The {@link MathContext} to be used for monetary values */
	public static final MathContext MATH_CONTEXT = MathContext.UNLIMITED;

	private BigDecimal amount;
	private RoundingMode roundingMode;

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(BigDecimal value) {
		this(value, ROUNDING_MODE);
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(MonetaryValue value) {
		this(value.getAmount(), value.getRoundingMode());
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(BigDecimal value, RoundingMode roundingMode) {
		this.amount = value.setScale(2, roundingMode);
		this.roundingMode = roundingMode;
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(CharSequence value) {
		this(new BigDecimal(value.toString(), MATH_CONTEXT));
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(double value) {
		this(new BigDecimal(value, MATH_CONTEXT));
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(BigInteger value) {
		this(new BigDecimal(value, MATH_CONTEXT));
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(int value) {
		this(new BigDecimal(value, MATH_CONTEXT));
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 */
	public MonetaryValue(long value) {
		this(new BigDecimal(value, MATH_CONTEXT));
	}

	/**
	 * Returns the amount.
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Returns the rounding mode.
	 * @return the rounding mode
	 */
	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	/**
	 * Sets the rounding mode.
	 * @param roundingMode the rounding mode to set
	 */
	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}

	/**
	 * Is value positive?
	 * @return {@code true} when value is a positive number
	 */
	public boolean isPositive() {
		return amount.compareTo(BigDecimal.ZERO) > 0;
	}

	/**
	 * Is value negative?
	 * @return {@code true} when value is a negative number
	 */
	public boolean isNegative() {
		return amount.compareTo(BigDecimal.ZERO) < 0;
	}

	/**
	 * Is value positive?
	 * @return {@code true} when value is a positive number
	 */
	public boolean isZero() {
		return amount.compareTo(BigDecimal.ZERO) == 0;
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(MonetaryValue value) {
		amount = amount.add(value.getAmount(), MATH_CONTEXT).setScale(2, roundingMode);
		return this;
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(BigDecimal value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(int value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(long value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(double value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(BigInteger value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue add(CharSequence value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(MonetaryValue value) {
		amount = amount.subtract(value.getAmount(), MATH_CONTEXT).setScale(2, roundingMode);
		return this;
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(BigDecimal value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(int value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(long value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(double value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(BigInteger value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue subtract(CharSequence value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(BigDecimal value) {
		amount = amount.multiply(value, MATH_CONTEXT).setScale(2, roundingMode);
		return this;
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(int value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(long value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(double value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(BigInteger value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue multiply(CharSequence value) {
		return multiply(new BigDecimal(value.toString().toCharArray()));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(BigDecimal value) {
		amount = amount.divide(value, MATH_CONTEXT).setScale(2, roundingMode);
		return this;
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(int value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(long value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(double value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(BigInteger value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public MonetaryValue divide(CharSequence value) {
		return divide(new BigDecimal(value.toString().toCharArray()));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return this object (for concatenation purposes)
	 */
	public BigDecimal divide(MonetaryValue value) {
		return this.amount.divide(value.getAmount(), MATH_CONTEXT);
	}

	/**
	 * Returns this value as a double.
	 * @return value as double
	 * @see BigDecimal#doubleValue()
	 */
	public double getDouble() {
		return this.amount.doubleValue();
	}

	/**
	 * Returns this value as a float.
	 * @return value as float
	 * @see BigDecimal#floatValue()
	 */
	public float getFloat() {
		return this.amount.floatValue();
	}

	/**
	 * Returns this value as int.
	 * @return value as int
	 * @see BigDecimal#intValue()
	 */
	public int getInt() {
		return this.amount.intValue();
	}

	/**
	 * Returns this value as {@link BigInteger}.
	 * @return value as BigInteger
	 * @see BigDecimal#toBigInteger()
	 */
	public BigInteger getBigInteger() {
		return this.amount.toBigInteger();
	}

	/**
	 * Returns this value as a long.
	 * @return value as long
	 * @see BigDecimal#longValue()
	 */
	public long getLong() {
		return this.amount.longValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(MonetaryValue o) {
		return amount.compareTo(getAmount());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int rc = HashCodeUtil.SEED;
		rc = HashCodeUtil.hash(rc, roundingMode);
		rc = HashCodeUtil.hash(rc, amount);
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other){
		if (this == other) return true;
		if (! (other instanceof MonetaryValue) ) return false;
		MonetaryValue that = (MonetaryValue)other;
		boolean result = (this.amount.equals(that.amount) );
		result = result && (this.roundingMode == that.roundingMode);
		return result;
	}
	
	/** 
	 * The representation of the value as string
	 * @return e.g. "12.00" or "0.00" or "-2345.34" 
	 */
	public String toString() {
		return amount.toPlainString();
	}
}
