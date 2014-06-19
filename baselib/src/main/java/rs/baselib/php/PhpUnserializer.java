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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Unerializes PHP format string to Java objects.
 * <p>Implementation based on pherialize by Klaus Reimer: 
 * {@link https://github.com/kayahr/pherialize/blob/master/}</p>
 * @author ralph
 * @since 1.2.6
 */
public class PhpUnserializer {

	/**
	 * Unserializes string to an object.
	 * @param data the data string
	 * @return The unserialized object
	 * @since 1.2.6
	 */
	public static Object unserialize(String data) {	
		return unserialize(data, (Charset)null);
	}

	/**
	 * Unserializes string to an object.
	 * @param data the data string
	 * @param charset the charset to use
	 * @return The unserialized object
	 * @since 1.2.6
	 */
	public static Object unserialize(String data, Charset charset) {	
		return unserialize(data, new ParseInfo(charset));
	}

	/**
	 * Unserializes the next object in the data stream.
	 *
	 * @return The unserialized object
	 */
	private static Object unserialize(String data, ParseInfo parseInfo) {

		char type;
		Object result;

		type = data.charAt(parseInfo.pos);
		switch (type) {
		case 's':
			result = unserializeString(data, parseInfo);
			break;
		case 'i':
			result = unserializeInteger(data, parseInfo);
			break;
		case 'd':
			result = unserializeDouble(data, parseInfo);
			break;
		case 'b':
			result = unserializeBoolean(data, parseInfo);
			break;
		case 'N':
			result = unserializeNull(data, parseInfo);
			break;
		case 'a':
			result = unserializeArray(data, parseInfo);
			break;
		default:
			throw new PhpUnserializeException("Unable to unserialize unknown type " + type);
		}

		return result;
	}

	/**
	 * Unserializes the next object in the data stream into a String.
	 *
	 * @return The unserialized String
	 */
	private static String unserializeString(String data, ParseInfo parseInfo) {
		int pos, length;

		pos = data.indexOf(':', parseInfo.pos + 2);
		length = Integer.parseInt(data.substring(parseInfo.pos + 2, pos));
		parseInfo.pos = pos + length + 3;
		String unencoded = data.substring(pos + 2, pos + 2 + length);
		return encode(unencoded, parseInfo.charset);
	}

	/**
	 * Unserializes the next object in the data stream into an Integer.
	 *
	 * @return The unserialized Integer
	 */
	private static Number unserializeInteger(String data, ParseInfo parseInfo) {
		int pos;

		pos = getNumberEndPosition(data, parseInfo.pos+2);
		String s = data.substring(parseInfo.pos + 2, pos);
		parseInfo.pos = pos ;
		// try int
		try { return Integer.parseInt(s); } catch (NumberFormatException e) { } // Ignore
		// try long
		try { return Long.parseLong(s); } catch (NumberFormatException e) { } // Ignore
		// do double
		return Double.parseDouble(s);
	}

	/**
	 * Unserializes the next object in the data stream into an Double.
	 *
	 * @return The unserialized Double
	 */
	private static Number unserializeDouble(String data, ParseInfo parseInfo) {
		int pos;

		pos = getNumberEndPosition(data, parseInfo.pos+2);
		String s = data.substring(parseInfo.pos + 2, pos);
		parseInfo.pos = pos;
		
		if (s.indexOf('.') < 0) {
			// try int
			try { return Integer.parseInt(s); } catch (NumberFormatException e) { } // Ignore
			
			// try long
			try { return Long.parseLong(s); } catch (NumberFormatException e) { } // Ignore
		}
		// do double
		return Double.parseDouble(s);
	}

	/**
	 * Unserializes the next object in the data stream into a Boolean.
	 *
	 * @return The unserialized Boolean
	 */
	private static boolean unserializeBoolean(String data, ParseInfo parseInfo) {
		Boolean result;

		result = Boolean.valueOf(data.charAt(parseInfo.pos + 2) == '1');
		parseInfo.pos += 3;
		return result;
	}

	/**
	 * Unserializes the next object in the data stream into a Null
	 *
	 * @return The unserialized Null
	 */
	private static Object unserializeNull(String data, ParseInfo parseInfo) {
		parseInfo.pos += 1;
		return null;
	}

	/**
	 * Unserializes the next object in the data stream into an array. This
	 * method returns an ArrayList if the unserialized array has numerical
	 * keys starting with 0 or a HashMap otherwise.
	 *
	 * @return The unserialized array
	 */
	private static Object unserializeArray(String data, ParseInfo parseInfo) {
		Map<Object,Object> result = new HashMap<Object, Object>();
		boolean decimalSequence = true;

		int pos;
		int max;
		int i;
		Object key, value;

		pos = data.indexOf(':', parseInfo.pos + 2);
		max = Integer.parseInt(data.substring(parseInfo.pos + 2, pos));
		parseInfo.pos = pos + 2;
		for (i=0; i<max; i++) {
			key = unserialize(data, parseInfo);
			parseInfo.pos++;
			value = unserialize(data, parseInfo);
			parseInfo.pos++;
			result.put(key, value);
			if (decimalSequence) {
				if (!(key instanceof Integer)) {
					decimalSequence = false;
				} else {
					int keyNum = Integer.parseInt(key.toString());
					if (keyNum != i) {
						decimalSequence = false;
					}
				}
			}
		}
		parseInfo.pos++;
		
		// Convert now to array if possible
		if (decimalSequence) {
			Object rc[] = new Object[max];
			for (i=0; i<max; i++) {
				rc[i] = result.get(i);
			}
			return rc;
		}
		return result;
	}

	protected static int getNumberEndPosition(String data,  int pos) {
		for (int i=pos; i<data.length(); i++) {
			char c = data.charAt(i);
			if (!Character.isDigit(c) && c != '.') return i;
		}
		return data.length();
	}

	public static String decode(String encoded, Charset charset) {
		try {
			return new String(encoded.getBytes(charset), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return encoded;
		}
	}


	public static String encode(String decoded, Charset charset) {
		try {
			return new String(decoded.getBytes("ISO-8859-1"), charset);
		} catch (UnsupportedEncodingException e) {
			return decoded;
		}
	}

	/**
	 * Helper class for parsing.
	 * @author ralph
	 *
	 */
	private static class ParseInfo {
		int pos = 0;
		Charset charset;

		public ParseInfo(Charset charset) {
			pos = 0;
			if (charset == null) charset = Charset.forName("UTF-8");
			this.charset = charset;

		}
	}
}
