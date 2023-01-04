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
	 * @param <T> type of object to be built
	 * @return the object built
	 */
	public static <T> T some(Builder<T> builder) {
		return builder.build();
	}

	/**
	 * Returns a list of objects.
	 * @param count the number of objects to create
	 * @param builder the builder to be used
	 * @param <T> type of objects to be built
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


}
