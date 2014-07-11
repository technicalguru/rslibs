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
package rs.baselib.php;

import java.util.Collection;
import java.util.Map;

/**
 * Serializes Java objects in a PHP serializer format string.
 * <p>Implementation based on pherialize by Klaus Reimer:
 *  https://github.com/kayahr/pherialize/blob/master</p>
 * @author ralph
 * @since 1.2.6
 */
public class PhpSerializer {

	/**
	 * Serializes the specified object.
	 *
	 * @param object object to serialize
	 * @return The serialized data
	 * @since 1.2.6
	 */
	public static String serialize(Object object) {
		StringBuilder buffer;

		buffer = new StringBuilder();
		serializeObject(object, buffer);
		return buffer.toString();
	}

	/**
	 * This method is used internally for recursively scanning the object while
	 * serializing. It just calls the other serializeObject method defaulting
	 * to allowing references.
	 *
	 * @param object The object to serialize
	 * @param buffer he string buffer to append serialized data to
	 */
	private static void serializeObject(Object object, StringBuilder buffer) {
		if (object == null) {
			serializeNull(buffer);
		} else if (object instanceof String) {
			serializeString((String) object, buffer);
		} else if (object instanceof Character) {
			serializeCharacter((Character) object, buffer);
		} else if (object instanceof Integer) {
			serializeInteger(((Integer) object).intValue(), buffer);
		} else if (object instanceof Short) {
			serializeInteger(((Short) object).intValue(), buffer);
		} else if (object instanceof Byte) {
			serializeInteger(((Byte) object).intValue(), buffer);
		} else if (object instanceof Long) {
			serializeLong(((Long) object).longValue(), buffer);
		} else if (object instanceof Double) {
			serializeDouble(((Double) object).doubleValue(), buffer);
		} else if (object instanceof Float) {
			serializeDouble(((Float) object).doubleValue(), buffer);
		} else if (object instanceof Boolean) {
			serializeBoolean((Boolean) object, buffer);
		} else if (object instanceof Object[]) {
			serializeArray((Object[]) object, buffer);
		} else if (object instanceof Collection<?>) {
			serializeArray(((Collection<?>) object).toArray(), buffer);
		} else if (object instanceof Map<?, ?>) {
			serializeMap((Map<?, ?>) object, buffer);
		} else {
			throw new PhpSerializeException("Unable to serialize " + object.getClass().getName());
		}
	}

	/**
	 * Serializes the specified string and appends it to the serialization
	 * buffer.
	 *
	 * @param string The string to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeString(String string, StringBuilder buffer) {
		buffer.append("s:");
		buffer.append(string.length());
		buffer.append(":\"");
		buffer.append(string);
		buffer.append("\";");
	}

	/**
	 * Serializes the specified character and appends it to the serialization
	 * buffer.
	 *
	 * @param value The value to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeCharacter(Character value, StringBuilder buffer) {
		buffer.append("s:1:\"");
		buffer.append(value);
		buffer.append("\";");
	}

	/**
	 * Adds a serialized NULL to the serialization buffer.
	 *
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeNull(StringBuilder buffer) {
		buffer.append("N;");
	}

	/**
	 * Serializes the specified integer number and appends it to the
	 * serialization buffer.
	 *
	 * @param number The integer number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeInteger(int number, StringBuilder buffer) {
		buffer.append("i:");
		buffer.append(number);
		buffer.append(";");
	}

	/**
	 * Serializes the specified lonf number and appends it to the serialization
	 * buffer.
	 *
	 * @param number The long number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeLong(long number, StringBuilder buffer) {
		if ((number >= Integer.MIN_VALUE) && (number <= Integer.MAX_VALUE)) {
			buffer.append("i:");
		} else {
			buffer.append("d:");
		}
		buffer.append(number);
		buffer.append(";");
	}

	/**
	 * Serializes the specfied double number and appends it to the serialization
	 * buffer.
	 *
	 * @param number The number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeDouble(double number, StringBuilder buffer) {
		buffer.append("d:");
		buffer.append(number);
		buffer.append(";");
	}

	/**
	 * Serializes the specfied boolean and appends it to the serialization
	 * buffer.
	 *
	 * @param value The value to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeBoolean(Boolean value, StringBuilder buffer) {
		buffer.append("b:");
		buffer.append(value.booleanValue() ? 1 : 0);
		buffer.append(";");
	}

	/**
	 * Serializes the specfied array and appends it to the serialization
	 * buffer.
	 *
	 * @param array The array to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeArray(Object[] array, StringBuilder buffer) {
		int max;

		buffer.append("a:");
		max = array.length;
		buffer.append(max);
		buffer.append(":{");
		for (int i = 0; i < max; i++) {
			serializeObject(Integer.valueOf(i), buffer);
			serializeObject(array[i], buffer);
		}
		buffer.append('}');
	}

	/**
	 * Serializes the specfied map and appends it to the serialization buffer.
	 *
	 * @param map The map to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeMap(Map<?, ?> map, StringBuilder buffer) {
		buffer.append("a:");
		buffer.append(map.size());
		buffer.append(":{");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			serializeObject(entry.getKey(), buffer);
			serializeObject(entry.getValue(), buffer);
		}
		buffer.append('}');
	}

}