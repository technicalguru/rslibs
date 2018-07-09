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

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.NoCopy;
import rs.baselib.util.RsDate;

/**
 * Reflection and language Utils.
 * @author ralph
 *
 */
public class LangUtils {

	private static Logger log = LoggerFactory.getLogger(LangUtils.class);
	
	/** The Java version we are running in */
	private static String javaVersion = null;
	
	/**
	 * Load a class by first checking the thread's class loader and then the caller's class loader.
	 * @param className name of cass to be loaded
	 * @return the class found
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public static Class<?> forName(String className) throws ClassNotFoundException {
		return forName(className, null);
	}
	
	/**
	 * Load a class by directly specifying a class loader.
	 * @param className name of cass to be loaded
	 * @param classLoader the class loader to be used - if null the thread's class loader will be  used first
	 * @return the class found
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
		if (classLoader == null) try {
			// Check the thread's class loader
			classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null) {
				return Class.forName(className, true, classLoader);
			}
		} catch (ClassNotFoundException e) {
			// not found, use the class' loader
			classLoader = null;
		}
		if (classLoader != null) {
			return Class.forName(className, true, classLoader);
		}
		return Class.forName(className);
	}
	
	/**
	 * Instantiates a class object from class name.
	 * This is a shortcut method for <code>{@link #forName(String)}.newInstance()</code>.
	 * @param className the name of class
	 * @return an instance of the named class
	 * @throws ClassNotFoundException when class cannot be found
	 * @throws IllegalAccessException when constructor cannot be called
	 * @throws InstantiationException when constructor throws errors
	 */
	public static Object newInstance(String className) throws ReflectiveOperationException {
		return newInstance(className, null);
	}
	
	/**
	 * Instantiates a class object from class name.
	 * This is a shortcut method for <code>{@link #forName(String,ClassLoader)}.newInstance()</code>.
	 * @param className the name of class
	 * @param classLoader the class loader to be used - if null the thread's class loader will be  used first
	 * @return an instance of the named class
	 * @throws ClassNotFoundException when class cannot be found
	 * @throws IllegalAccessException when constructor cannot be called
	 * @throws InstantiationException when constructor throws errors
	 */
	public static Object newInstance(String className, ClassLoader classLoader) throws ReflectiveOperationException {
		Class<?> clazz = forName(className, classLoader);
		return clazz.getConstructor().newInstance();
	}
	
	/**
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * @param type the type
	 * @return the underlying class
	 * @deprecated see {@link ReflectionUtils#getClass(Type)}
	 */
	@Deprecated
	public static Class<?> getClass(Type type) {
		return ReflectionUtils.getClass(type);
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base class.
	 *
	 * @param baseClass the base class
	 * @param childClass the child class
	 * @param <T> type of base class
	 * @return a list of the raw classes for the actual type arguments.
	 * @deprecated see {@link ReflectionUtils#getTypeArguments(Class, Class)}
	 */
	@Deprecated
	public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
		return ReflectionUtils.getTypeArguments(baseClass, childClass);
	}
	
	/**
	 * Returns true when the given string is a number.
	 * @param s string to be verified
	 * @return {@code true} when the string contains a number.
	 */
	public static boolean isNumber(String s) {
	    return isNumber(s, 10);
	}

	/**
	 * Returns true when the given string is a number with a given radix.
	 * @param s string to be verified
	 * @param radix the radix to check
	 * @return {@code true} when the string contains a number.
	 */
	public static boolean isNumber(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	/**
	 * Converts the object to a string.
	 * @param o object to be converted
	 * @return null if object is null, string value of object otherwise
	 */
	public static String getString(Object o) {
		if (o == null) return null;
		
		if (o instanceof String) {
			return (String)o;
		}	
		return o.toString();
	}

	/**
	 * Converts the object to an int.
	 * The method will throw an exception when the object is not null and cannot be parsed.
	 * @param o object to be converted
	 * @return 0 if object is null, int value of object otherwise
	 */
	public static int getInt(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).intValue();
		}
		
		return Integer.parseInt(o.toString());
	}

	/**
	 * Converts the object to an int.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, int value of object otherwise
	 */
	public static int getInt(Object o, int defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof Number) {
			return ((Number)o).intValue();
		}
		
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	/**
	 * Converts the object to a long.
	 * The method will throw an exception when the object is not null and cannot be parsed.
	 * @param o object to be converted
	 * @return 0 if object is null, long value of object otherwise
	 */
	public static long getLong(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).longValue();
		}
		
		return Long.parseLong(o.toString());
	}

	/**
	 * Converts the object to a long.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, long value of object otherwise
	 */
	public static long getLong(Object o, long defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof Number) {
			return ((Number)o).longValue();
		}
		
		try {
			return Long.parseLong(o.toString());
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	/**
	 * Converts the object to a float.
	 * The method will throw an exception when the object is not null and cannot be parsed.
	 * @param o object to be converted
	 * @return 0 if object is null, float value of object otherwise
	 */
	public static float getFloat(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).floatValue();
		}
		
		return Float.parseFloat(o.toString());
	}

	/**
	 * Converts the object to a float.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, float value of object otherwise
	 */
	public static float getFloat(Object o, float defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof Number) {
			return ((Number)o).floatValue();
		}
		
		try {
			return Float.parseFloat(o.toString());
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	/**
	 * Converts the object to a BigDecimal.
	 * @param o object to be converted
	 * @return {@link BigDecimal#ZERO} if o is null or cannot be parsed, BigDecimal value of object otherwise
	 * @since 1.3.2
	 */
	public static BigDecimal getBigDecimal(Object o) {
		return getBigDecimal(o, BigDecimal.ZERO);
	}
	
	/**
	 * Converts the object to a BigDecimal.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, BigDecimal value of object otherwise
	 * @since 1.3.2
	 */
	public static BigDecimal getBigDecimal(Object o, BigDecimal defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof BigDecimal) {
			return (BigDecimal)o;
		}
		
		if (o instanceof Number) {
			return new BigDecimal(((Number)o).doubleValue());
		}
		
		try {
			return BigDecimal.valueOf(Double.parseDouble(o.toString()));
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}


	/**
	 * Converts the object to a double.
	 * The method will throw an exception when the object is not null and cannot be parsed.
	 * @param o object to be converted
	 * @return 0 if object is null, double value of object otherwise
	 */
	public static double getDouble(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).longValue();
		}
		
		return Double.parseDouble(o.toString());
	}

	/**
	 * Converts the object to a double.
	 * @param o object to be converted
	 * @param defaultValue the default value to return
	 * @return default if object is null or cannot be parsed, double value of object otherwise
	 */
	public static double getDouble(Object o, double defaultValue) {
		if (o == null) return defaultValue;
		
		if (o instanceof Number) {
			return ((Number)o).doubleValue();
		}
		
		try {
			return Double.parseDouble(o.toString());
		} catch (Exception e) {
			// ignore
		}
		return defaultValue;
	}

	/**
	 * Converts the object to a boolean.
	 * Values recognized a s<code>true</code> are 1, true, on, yes, y (case-insensitive).
	 * @param o object to be converted
	 * @return false if object is null, boolean value of object otherwise
	 */
	public static boolean getBoolean(Object o) {
		if (o == null) return false;
		
		if (o instanceof Boolean) {
			return ((Boolean)o).booleanValue();
		}
		
		String s = o.toString().toLowerCase().trim();
		if (s.equals("1")) return true;
		if (s.equals("true")) return true;
		if (s.equals("on")) return true;
		if (s.equals("yes")) return true;
		if (s.equals("y")) return true;
		
		return false;
	}
	
	/**
	 * Returns the date using the first {@link DateFormat} that produces a result.
	 * @param o object to be transformed
	 * @param formats formats to be used
	 * @return the date if it could be parsed or <code>null</code>
	 */
	public static RsDate getRsDate(Object o, DateFormat formats[]) {
		return getRsDate(o, formats, null);
	}
	
	/**
	 * Returns the date using the first {@link DateFormat} that produces a result.
	 * @param o object to be transformed
	 * @param formats formats to be used
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @return the date if it could be parsed, the default value otherwise
	 */
	public static RsDate getRsDate(Object o, DateFormat formats[], RsDate defaultValue) {
		if (o == null) return defaultValue;
		if (o instanceof RsDate) return (RsDate)o;
		RsDate rc = null; 
		for (DateFormat format : formats) {
			rc = getRsDate(o, format, null, false);
			if (rc != null) break;
		}
		if (rc == null) rc = defaultValue;
		return rc;
	}
	
	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @return {@link RsDate} value of object, <code>null</code> otherwise
	 */
	public static RsDate getRsDate(Object o) {
		return getRsDate(o, DateFormat.getDateTimeInstance(), null, true);
	}

	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @return {@link RsDate} value of object, <code>null</code> otherwise
	 */
	public static RsDate getRsDate(Object o, DateFormat format) {
		return getRsDate(o, format, null, true);
	}
	
	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @return {@link RsDate} value of object, the default value otherwise
	 */
	public static RsDate getRsDate(Object o, DateFormat format, RsDate defaultValue) {
		return getRsDate(o, format, defaultValue, true);
	}
	
	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @param logError whether parsing error shall be logged
	 * @return {@link RsDate} value of object, the default value otherwise
	 */
	public static RsDate getRsDate(Object o, DateFormat format, RsDate defaultValue, boolean logError) {
		if (o == null) return defaultValue;
		if (o instanceof RsDate) return (RsDate)o;
		try {
			String s = o.toString().trim();
			if (s.length() == 0) return defaultValue;
			return new RsDate(format.parse(s));
		} catch (ParseException e) {
			if (logError)log.error("Cannot parse date: "+o.toString(), e);
		}
		return defaultValue;
	}
	
	/**
	 * Returns the date using the first {@link DateFormat} that produces a result.
	 * @param o object to be transformed
	 * @param formats formats to be used
	 * @return the date if it could be parsed, <code>null</code> otherwise
	 */
	public static Date getDate(Object o, DateFormat formats[]) {
		return getDate(o, formats, null);
	}
	
	/**
	 * Returns the date using the first {@link DateFormat} that produces a result.
	 * @param o object to be transformed
	 * @param formats formats to be used
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @return the date if it could be parsed, the default value otherwise
	 */
	public static Date getDate(Object o, DateFormat formats[], Date defaultValue) {
		if (o == null) return defaultValue;
		if (o instanceof Date) return (Date)o;
		Date rc = null; 
		for (DateFormat format : formats) {
			rc = getDate(o, format, null, false);
			if (rc != null) break;
		}
		if (rc == null) rc = defaultValue;
		return rc;
	}
	
	/**
	 * Converts the object to a {@link Date}.
	 * @param o object to be converted
	 * @return {@link Date} value of object, <code>null</code> otherwise
	 */
	public static Date getDate(Object o) {
		return getDate(o, DateFormat.getDateTimeInstance(), null, true);
	}

	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @return {@link Date} value of object, <code>null</code> otherwise
	 */
	public static Date getDate(Object o, DateFormat format) {
		return getDate(o, format, null, true);
	}
	
	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @return {@link Date} value of object, the default value otherwise
	 */
	public static Date getDate(Object o, DateFormat format, Date defaultValue) {
		return getDate(o, format, defaultValue, true);
	}
	
	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param defaultValue value to be returned when object cannot be transformed.
	 * @param logError whether parsing error shall be logged
	 * @return {@link Date} value of object, the default value otherwise
	 */
	public static Date getDate(Object o, DateFormat format, Date defaultValue, boolean logError) {
		if (o == null) return defaultValue;
		if (o instanceof Date) return (Date)o;
		try {
			String s = o.toString().trim();
			if (s.length() == 0) return defaultValue;
			return format.parse(s);
		} catch (ParseException e) {
			if (logError) log.error("Cannot parse date: "+o.toString(), e);
		}
		return defaultValue;
	}
	
	/**
	 * Returns true when the runtime is Java 6.
	 * @return true if Java 6, false otherwise.
	 */
	public static boolean isJava6() {
		if (javaVersion == null) {
			javaVersion = System.getProperty("java.specification.version");
			if (javaVersion == null) {
				log.error("Cannot determine Java version.");
				javaVersion = "unknown";
			}
		}
		return javaVersion.equals("1.6");
	}
	
	/**
	 * Returns the locale with the id.
	 * @param id id of locale as returned by toString()
	 * @return the locale or the default Locale if not found.
	 */
	public static Locale getLocale(String id) {
		return getLocale(id, Locale.getDefault());
	}
	
	/**
	 * Returns the locale with the id.
	 * @param id id of locale as returned by toString()
	 * @param defaultLocale default to return when locale cannot be found
	 * @return the locale or the default if not found.
	 */
	public static Locale getLocale(String id, Locale defaultLocale) {
		Locale rc = defaultLocale;
		for (Locale l : Locale.getAvailableLocales()) {
			if (l.toString().equals(id)) {
				rc = l;
				break;
			}
		}
		return rc;
	}

	/**
	 * Serializes the given value into BASE64.
	 * @param value value to be serialized
	 * @return the serialized string
	 * @throws IOException when serialization fails
	 */
	public static String serializeBase64(Object value) throws IOException {
		return new String(Base64.encodeBase64(serialize(value)), java.nio.charset.StandardCharsets.UTF_8);
	}
	
	/**
	 * Serializes the given value.
	 * @param value value to be serialized
	 * @return the serialized string
	 * @throws IOException when serialization fails
	 */
	public static byte[] serialize(Object value) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ObjectOutputStream s = new ObjectOutputStream(stream);
		s.writeObject(value);
		s.close();
		return stream.toByteArray();
	}
	
	/**
	 * Unserializes the given value.
	 * @param value the serialized string (BASE64 encoded)
	 * @return the object (or null)
	 * @throws ClassNotFoundException when class to be unserialized cannot be found
	 * @throws IOException when unserialization fails
	 */
	public static Object unserialize(String value) throws ClassNotFoundException, IOException {
		return unserialize(Base64.decodeBase64(value));
	}

	/**
	 * Unserializes the given value.
	 * @param bytes the bytes of serialized object
	 * @return the object (or null)
	 * @throws ClassNotFoundException when class to be unserialized cannot be found
	 * @throws IOException when unserialization fails
	 */
	public static Object unserialize(byte bytes[]) throws ClassNotFoundException, IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream s = new ObjectInputStream(in);
		return s.readObject();
	}

	/**
	 * Creates a unified <code>toString()</code> output.
	 * <p>Properties must be represented with two values: name of property and its value, e.g. the call</p>
	 * <pre>
	 *    toString("ClassName", "property1", "value1", "property2", "value2")
	 * </pre>
	 * <p>will produce <code>ClassName[property1=value1;property2=value2]</code>.</p> 
	 * @param className the class name to be used
	 * @param properties the properties where each property is represented with two values: name of property and its value.
	 * @return a unified toString() output, .
	 */
	public static String toString(String className, Object... properties) {
		StringBuilder rc = new StringBuilder();
		rc.append(className);
		rc.append("[");
		for (int i=0; i<properties.length; i+=2) {
			if (i != 0) rc.append(';');
			rc.append(properties[i]);
			rc.append("=");
			rc.append(properties[i+1]);
		}
		rc.append("]");
		return rc.toString();
	}
	
	/**
	 * Returns true when given property is transient.
	 * A transient property has either a missing getter or setter or its getter is
	 * marked with {@link Transient} annotation.
	 * @param desc property descriptor
	 * @return true when property is transient and must not be persisted
	 */
	public static boolean isTransient(PropertyDescriptor desc) {
		Method rm = desc.getReadMethod();
		Method wm = desc.getWriteMethod();
		if ((rm == null) || (wm == null)) return true;
		return rm.isAnnotationPresent(Transient.class);
	}

	/**
	 * Returns true when given property is not allowed to be copied with {@link rs.baselib.bean.IBean#copyTo(Object)}.
	 * A {@link NoCopy} property has either a missing getter or setter or its getter is
	 * marked with {@link NoCopy} annotation.
	 * @param desc property descriptor
	 * @return true when property is transient and must not be persisted
	 */
	public static boolean isNoCopy(PropertyDescriptor desc) {
		Method rm = desc.getReadMethod();
		Method wm = desc.getWriteMethod();
		if ((rm == null) || (wm == null)) return true;
		return rm.isAnnotationPresent(NoCopy.class);
	}

	/**
	 * A wrapper for {@link Thread#sleep(long)} to avoid try/catch blocks.
	 * The method throws a {@link RuntimeException} in case of exceptions.
	 * @param milliseconds milliseconds to sleep
	 */
	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Replacement for "instanceof" operator when it cannot be guaranteed that
	 * the class is available in classpath at runtime.
	 * @param o the object to be checked
	 * @param className the complete class name
	 * @return when the object is of that class
	 * @deprecated see {@link ReflectionUtils#isInstanceOf(Object, String)}
	 */
	@Deprecated
	public static boolean isInstanceOf(Object o, String className) {
		return ReflectionUtils.isInstanceOf(o, className);
	}
	
	/**
	 * Replacement for {@link Class#isAssignableFrom(Class)} when it cannot be guaranteed that
	 * the class is available in classpath at runtime.
	 * @param inspectedClass the class to be checked
	 * @param className the complete class name that should be implemented or a superclass of the inspected class
	 * @return when the inspected class implements or derived from the class with given name
	 * @deprecated see {@link ReflectionUtils#isInstanceOf(Class, String)}
	 */
	@Deprecated
	public static boolean isInstanceOf(Class<?> inspectedClass, String className) {
		return ReflectionUtils.isInstanceOf(inspectedClass, className);
	}
}
