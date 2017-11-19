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

import java.util.ArrayList;
import java.util.List;

/**
 * Helps using the builder pattern.
 * @author ralph
 *
 */
public class BuilderUtils {


	/**
	 * Return any object.
	 * @param builder the builder to be used
	 * @return the object built
	 */
	public static <T> T some(Builder<T> builder) {
		return builder.build();
	}

	/**
	 * Returns a list of objects.
	 * @param count the number of objects to create
	 * @param builder the builder to be used
	 * @return the list of objects built
	 */
	public static <T> List<T> listOf(int count, Builder<T> builder) {
		List<T> rc = new ArrayList<>();
		for (int i=0; i<count; i++) {
			rc.add(builder.build());
		}
		return rc;
	}
	
	/**
	 * Returns the String Builder factory.
	 * @return the builder for strings.
	 */
	public static StringBuilder $String() {
		return new StringBuilder();
	}

	/**
	 * Returns the Integer Builder factory.
	 * @return the builder for integers.
	 */
	public static IntBuilder $Int() {
		return new IntBuilder();
	}

	/**
	 * Returns the Long Builder factory.
	 * @return the builder for long integers.
	 */
	public static LongBuilder $Long() {
		return new LongBuilder();
	}

	/**
	 * Returns the {@link rs.baselib.util.RsDate} Builder factory.
	 * @return the builder for {@link rs.baselib.util.RsDate}
	 */
	public static RsDateBuilder $RsDate() {
		return new RsDateBuilder();
	}

	/**
	 * Returns the {@link rs.baselib.util.RsDay} Builder factory.
	 * @return the builder for {@link rs.baselib.util.RsDay}
	 */
	public static RsDayBuilder $RsDay() {
		return new RsDayBuilder();
	}

	/**
	 * Returns the {@link rs.baselib.util.RsMonth} Builder factory.
	 * @return the builder for {@link rs.baselib.util.RsMonth}
	 */
	public static RsMonthBuilder $RsMonth() {
		return new RsMonthBuilder();
	}

	/**
	 * Returns the {@link rs.baselib.util.RsYear} Builder factory.
	 * @return the builder for {@link rs.baselib.util.RsYear}
	 */
	public static RsYearBuilder $RsYear() {
		return new RsYearBuilder();
	}

}
