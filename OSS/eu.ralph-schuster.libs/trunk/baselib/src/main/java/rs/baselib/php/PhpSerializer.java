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

import java.nio.charset.Charset;
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
		return serialize(object, null);
	}
	
	/**
	 * Serializes the specified object.
	 *
	 * @param object object to serialize
	 * @param charset to be used
	 * @return The serialized data
	 * @since 1.2.6
	 */
	public static String serialize(Object object, Charset charset) {
		StringBuilder buffer;
		if (charset == null) charset = Charset.forName("UTF-8");

		buffer = new StringBuilder();
		serializeObject(object, buffer, charset);
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
	private static void serializeObject(Object object, StringBuilder buffer, Charset charset) {
		if (object == null) {
			serializeNull(buffer, charset);
		} else if (object instanceof String) {
			serializeString((String) object, buffer, charset);
		} else if (object instanceof Character) {
			serializeCharacter((Character) object, buffer, charset);
		} else if (object instanceof Integer) {
			serializeInteger(((Integer) object).intValue(), buffer, charset);
		} else if (object instanceof Short) {
			serializeInteger(((Short) object).intValue(), buffer, charset);
		} else if (object instanceof Byte) {
			serializeInteger(((Byte) object).intValue(), buffer, charset);
		} else if (object instanceof Long) {
			serializeLong(((Long) object).longValue(), buffer, charset);
		} else if (object instanceof Double) {
			serializeDouble(((Double) object).doubleValue(), buffer, charset);
		} else if (object instanceof Float) {
			serializeDouble(((Float) object).doubleValue(), buffer, charset);
		} else if (object instanceof Boolean) {
			serializeBoolean((Boolean) object, buffer, charset);
		} else if (object instanceof Object[]) {
			serializeArray((Object[]) object, buffer, charset);
		} else if (object instanceof Collection<?>) {
			serializeArray(((Collection<?>) object).toArray(), buffer, charset);
		} else if (object instanceof Map<?, ?>) {
			serializeMap((Map<?, ?>) object, buffer, charset);
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
	private static void serializeString(String string, StringBuilder buffer, Charset charset) {
		String decoded = PhpUnserializer.decode(string, charset);

		buffer.append("s:");
		buffer.append(decoded.length());
		buffer.append(":\"");
		buffer.append(string);
		buffer.append("\"");
	}

	/**
	 * Serializes the specified character and appends it to the serialization
	 * buffer.
	 *
	 * @param value The value to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeCharacter(Character value, StringBuilder buffer, Charset charset) {
		buffer.append("s:1:\"");
		buffer.append(value);
		buffer.append("\"");
	}

	/**
	 * Adds a serialized NULL to the serialization buffer.
	 *
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeNull(StringBuilder buffer, Charset charset) {
		buffer.append("N");
	}

	/**
	 * Serializes the specified integer number and appends it to the
	 * serialization buffer.
	 *
	 * @param number The integer number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeInteger(int number, StringBuilder buffer, Charset charset) {
		buffer.append("i:");
		buffer.append(number);
	}

	/**
	 * Serializes the specified lonf number and appends it to the serialization
	 * buffer.
	 *
	 * @param number The long number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeLong(long number, StringBuilder buffer, Charset charset) {
		if ((number >= Integer.MIN_VALUE) && (number <= Integer.MAX_VALUE)) {
			buffer.append("i:");
		} else {
			buffer.append("d:");
		}
		buffer.append(number);
	}

	/**
	 * Serializes the specfied double number and appends it to the serialization
	 * buffer.
	 *
	 * @param number The number to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeDouble(double number, StringBuilder buffer, Charset charset) {
		buffer.append("d:");
		buffer.append(number);
	}

	/**
	 * Serializes the specfied boolean and appends it to the serialization
	 * buffer.
	 *
	 * @param value The value to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeBoolean(Boolean value, StringBuilder buffer, Charset charset) {
		buffer.append("b:");
		buffer.append(value.booleanValue() ? 1 : 0);
	}

	/**
	 * Serializes the specfied array and appends it to the serialization
	 * buffer.
	 *
	 * @param array The array to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeArray(Object[] array, StringBuilder buffer, Charset charset) {
		int max;

		buffer.append("a:");
		max = array.length;
		buffer.append(max);
		buffer.append(":{");
		for (int i = 0; i < max; i++) {
			if (i>0) buffer.append(';');
			serializeObject(Integer.valueOf(i), buffer, charset);
			buffer.append(';');
			serializeObject(array[i], buffer, charset);
		}
		buffer.append('}');
	}

	/**
	 * Serializes the specfied map and appends it to the serialization buffer.
	 *
	 * @param map The map to serialize
	 * @param buffer The string buffer to append serialized data to
	 */
	private static void serializeMap(Map<?, ?> map, StringBuilder buffer, Charset charset) {
		buffer.append("a:");
		buffer.append(map.size());
		buffer.append(":{");
		boolean isFirst = true;
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (isFirst) isFirst = false;
			else buffer.append(';');
			serializeObject(entry.getKey(), buffer, charset);
			buffer.append(';');
			serializeObject(entry.getValue(), buffer, charset);
		}
		buffer.append('}');
	}

}