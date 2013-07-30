/**
 * 
 */
package rs.baselib.lang;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * Get the underlying class for a type, or null if the type is a variable type.
	 * @param type the type
	 * @return the underlying class
	 */
	public static Class<?> getClass(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType) type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			if (componentClass != null ) {
				return Array.newInstance(componentClass, 0).getClass();
			}
			else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic base class.
	 *
	 * @param baseClass the base class
	 * @param childClass the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	public static <T> List<Class<?>> getTypeArguments(Class<T> baseClass, Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = childClass;
		
		// start walking up the inheritance hierarchy until we hit baseClass
		while (! getClass(type).equals(baseClass)) {
			if (type instanceof Class) {
				// there is no useful information for us in raw types, so just keep going.
				type = ((Class<?>) type).getGenericSuperclass();
			} else {
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Class<?> rawType = (Class<?>) parameterizedType.getRawType();

				Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
				TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++) {
					resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!rawType.equals(baseClass)) {
					type = rawType.getGenericSuperclass();
				}
			}
		}

		// finally, for each actual type argument provided to baseClass, determine (if possible)
		// the raw class for that type argument.
		Type[] actualTypeArguments;
		if (type instanceof Class) {
			actualTypeArguments = ((Class<?>) type).getTypeParameters();
		} else {
			actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
		}
		List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
		// resolve types by chasing down type variables.
		for (Type baseType: actualTypeArguments) {
			while (resolvedTypes.containsKey(baseType)) {
				baseType = resolvedTypes.get(baseType);
			}
			typeArgumentsAsClasses.add(getClass(baseType));
		}
		return typeArgumentsAsClasses;
	}
	
	/**
	 * Returns all elements of a Java package.
	 * @param packageName name of java package
	 * @return list of files that are in this package
	 * @throws IOException when an error occurs
	 */
	public static File[] getPackageContent(String packageName) throws IOException {
	    ArrayList<File> list = new ArrayList<File>();
	    Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
	                            .getResources(packageName);
	    while (urls.hasMoreElements()) {
	        URL url = urls.nextElement();
	        File dir = new File(url.getFile());
	        for (File f : dir.listFiles()) {
	            list.add(f);
	        }
	    }
	    return list.toArray(new File[]{});
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
	 * Converts the object to a long.
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
	 * Converts the object to a double.
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
	 * Converts the object to a boolean.
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
	 * @return the date if it could be parsed
	 */
	public RsDate getRsDate(Object o, DateFormat formats[]) {
		if (o == null) return null;
		if (o instanceof RsDate) return (RsDate)o;
		RsDate rc = null; 
		for (DateFormat format : formats) {
			rc = getRsDate(o, format, false);
			if (rc != null) break;
		}
		return rc;
	}
	
	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @return null if object is null, {@link RsDate} value of object otherwise
	 */
	public static RsDate getRsDate(Object o, DateFormat format) {
		return getRsDate(o, format, true);
	}
	
	/**
	 * Converts the object to a {@link RsDate}.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param logError whether parsing error shall be logged
	 * @return null if object is null, {@link RsDate} value of object otherwise
	 */
	private static RsDate getRsDate(Object o, DateFormat format, boolean logError) {
		if (o == null) return null;
		if (o instanceof RsDate) return (RsDate)o;
		try {
			String s = o.toString().trim();
			if (s.length() == 0) return null;
			return new RsDate(format.parse(s));
		} catch (ParseException e) {
			if (logError)log.error("Cannot parse date: "+o.toString(), e);
		}
		return null;
	}
	
	/**
	 * Returns the date using the first {@link DateFormat} that produces a result.
	 * @param o object to be transformed
	 * @param formats formats to be used
	 * @return the date if it could be parsed
	 */
	public Date getDate(Object o, DateFormat formats[]) {
		if (o == null) return null;
		if (o instanceof Date) return (Date)o;
		Date rc = null; 
		for (DateFormat format : formats) {
			rc = getDate(o, format, false);
			if (rc != null) break;
		}
		return rc;
	}
	
	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @return null if object is null, {@link Date} value of object otherwise
	 */
	public static Date getDate(Object o, DateFormat format) {
		return getDate(o, format, true);
	}
	
	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @param format format to be applied
	 * @param logError whether parsing error shall be logged
	 * @return null if object is null, {@link Date} value of object otherwise
	 */
	public static Date getDate(Object o, DateFormat format, boolean logError) {
		if (o == null) return null;
		if (o instanceof Date) return (Date)o;
		try {
			String s = o.toString().trim();
			if (s.length() == 0) return null;
			return format.parse(s);
		} catch (ParseException e) {
			if (logError) log.error("Cannot parse date: "+o.toString(), e);
		}
		return null;
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

}
