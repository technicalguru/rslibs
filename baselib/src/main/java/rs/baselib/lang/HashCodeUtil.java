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
package rs.baselib.lang;

import java.lang.reflect.Array;

/**
 * Collected methods which allow easy implementation of <tt>hashCode</tt>.
 * <p>Implementation is based on the recommendations of Effective Java, by Joshua Bloch.</p>
 * Example use case:
 * <pre>
 *  public int hashCode(){
 *    int result = HashCodeUtil.SEED;
 *    //collect the contributions of various fields
 *    result = HashCodeUtil.hash(result, fPrimitive);
 *    result = HashCodeUtil.hash(result, fObject);
 *    result = HashCodeUtil.hash(result, fArray);
 *    return result;
 *  }
 * </pre>
 * @author ralph
 *
 */
public final class HashCodeUtil {

	/**
	 * An initial value for a <tt>hashCode</tt>, to which is added contributions
	 * from fields. Using a non-zero value decreases collisons of <tt>hashCode</tt>
	 * values.
	 */
	public static final int SEED = 23;

	/** 
	 * Hash booleans.  
	 * @param aSeed - a previous seed
	 * @param aBoolean - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed, boolean aBoolean) {
		return firstTerm( aSeed ) + (aBoolean ? 1 : 0);
	}

	/** 
	 * Hash chars.  
	 * @param aSeed - a previous seed
	 * @param aChar - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed, char aChar) {
		return firstTerm(aSeed) + (int)aChar;
	}

	/** 
	 * Hash ints.  
	 * @param aSeed - a previous seed
	 * @param aInt - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed , int aInt) {
		/*
		 * Implementation Note
		 * Note that byte and short are handled by this method, through
		 * implicit conversion.
		 */
		return firstTerm(aSeed) + aInt;
	}

	/** 
	 * Hash longs.  
	 * @param aSeed - a previous seed
	 * @param aLong - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed , long aLong) {
		return firstTerm(aSeed)  + (int)(aLong ^ (aLong >>> 32));
	}

	/** 
	 * Hash floats.  
	 * @param aSeed - a previous seed
	 * @param aFloat - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed , float aFloat) {
		return hash(aSeed, Float.floatToIntBits(aFloat));
	}

	/** 
	 * Hash doubles. 
	 * @param aSeed - a previous seed
	 * @param aDouble - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed , double aDouble) {
		return hash( aSeed, Double.doubleToLongBits(aDouble) );
	}

	/**
	 * <tt>aObject</tt> is a possibly-null object field, and possibly an array.
	 *
	 * If <tt>aObject</tt> is an array, then each element may be a primitive
	 * or a possibly-null object.
	 * @param aSeed - a previous seed
	 * @param aObject - the value to combine in hash
	 * @return the combined hash code
	 */
	public static int hash(int aSeed , Object aObject) {
		int result = aSeed;
		if (aObject == null){
			result = hash(result, 0);
		}
		else if (!isArray(aObject)){
			result = hash(result, aObject.hashCode());
		}
		else {
			int length = Array.getLength(aObject);
			for (int idx = 0; idx < length; ++idx) {
				Object item = Array.get(aObject, idx);
				//if an item in the array references the array itself, prevent infinite looping
				if(! (item == aObject))  
					//recursive call!
					result = hash(result, item);
			}
		}
		return result;
	}  

	// PRIVATE 
	private static final int fODD_PRIME_NUMBER = 37;

	private static int firstTerm(int aSeed){
		return fODD_PRIME_NUMBER * aSeed;
	}

	private static boolean isArray(Object aObject){
		return aObject.getClass().isArray();
	}

} 