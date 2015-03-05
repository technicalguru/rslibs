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
 * @since 1.2.9
 */
public class MonetaryValue implements Serializable, Comparable<MonetaryValue> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/** Banker's rounding */
	public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
	/** The {@link MathContext} to be used for monetary values */
	public static final MathContext MATH_CONTEXT = MathContext.DECIMAL128;
	/** The default scale to be used for monetary values */
	public static final int DEFAULT_SCALE = 2;
	/** 0 */
	public static final MonetaryValue ZERO = new MonetaryValue(0);
	
	private BigDecimal amount;
	private RoundingMode roundingMode;
	private int scale;
	private MathContext mathContext;
	
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
	 * @param roundingMode the rounding mode to be used
	 */
	public MonetaryValue(BigDecimal value, RoundingMode roundingMode) {
		this(value, roundingMode, MATH_CONTEXT, DEFAULT_SCALE);
	}

	/**
	 * Constructor.
	 * @param value the monetary value
	 * @param roundingMode the rounding mode to be used
	 * @param mathContext the {@link MathContext} to be used
	 * @param scale the scale to be used
	 */
	public MonetaryValue(BigDecimal value, RoundingMode roundingMode, MathContext mathContext, int scale) {
		this.amount = value.setScale(scale, roundingMode);
		this.roundingMode = roundingMode;
		this.scale = scale;
		this.mathContext = mathContext;
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
	 * Returns the scale.
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Sets the scale.
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * Returns the {@link MathContext}.
	 * @return the mathContext
	 */
	public MathContext getMathContext() {
		return mathContext;
	}

	/**
	 * Sets the {@link MathContext}.
	 * @param mathContext the mathContext to set
	 */
	public void setMathContext(MathContext mathContext) {
		this.mathContext = mathContext;
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
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(MonetaryValue value) {
		return new MonetaryValue(amount.add(value.getAmount(), getMathContext()), getRoundingMode(), getMathContext(), getScale());
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(BigDecimal value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(int value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(long value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(double value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(BigInteger value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Adds another value to this object
	 * @param value value to be added
	 * @return new monetary value holding the result
	 */
	public MonetaryValue add(CharSequence value) {
		return add(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(MonetaryValue value) {
		return new MonetaryValue(amount.subtract(value.getAmount(), getMathContext()), getRoundingMode(), getMathContext(), getScale());
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(BigDecimal value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(int value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(long value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(double value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(BigInteger value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Subtracts another value from this object.
	 * @param value value to be subtracted
	 * @return new monetary value holding the result
	 */
	public MonetaryValue subtract(CharSequence value) {
		return subtract(new MonetaryValue(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(BigDecimal value) {
		return new MonetaryValue(amount.multiply(value, getMathContext()), getRoundingMode(), getMathContext(), getScale());
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(int value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(long value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(double value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(BigInteger value) {
		return multiply(new BigDecimal(value));
	}

	/**
	 * Multiplies another value with this object.
	 * @param value value to be multiplied with
	 * @return new monetary value holding the result
	 */
	public MonetaryValue multiply(CharSequence value) {
		return multiply(new BigDecimal(value.toString().toCharArray()));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(BigDecimal value) {
		return new MonetaryValue(amount.divide(value, getMathContext()), getRoundingMode(), getMathContext(), getScale());
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(int value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(long value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(double value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(BigInteger value) {
		return divide(new BigDecimal(value));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return new monetary value holding the result
	 */
	public MonetaryValue divide(CharSequence value) {
		return divide(new BigDecimal(value.toString().toCharArray()));
	}

	/**
	 * Divides this object by another value.
	 * @param value value to be divided by
	 * @return the result of the operation
	 */
	public BigDecimal divide(MonetaryValue value) {
		return this.amount.divide(value.getAmount(), getMathContext());
	}

	/**
	 * Negates this value.
	 * @return new monetary value holding the result
	 */
	public MonetaryValue negate() {
		return new MonetaryValue(amount.negate(getMathContext()), getRoundingMode(), getMathContext(), getScale());
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
		return amount.compareTo(o.getAmount());
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
