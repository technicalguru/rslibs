/**
 * Created on Oct 23, 2022. This file is part of the RsBudget REST server.
 */
package rs.jackson;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * JSON utils for mapping back and forth
 * @author ralph
 *
 */
public class JsonUtils {

	private static ObjectMapper jsonMapper;
	
	/**
	 * Convert any object to its Json representation.
	 * @param o - the object to convert
	 * @return the JSON string
	 */
	public static String toJson(Object o) {
		try {
			return getJsonMapper().writeValueAsString(o);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert to JSON", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param json JSON string
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return getJsonMapper().readValue(json, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param json JSON string
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromJson(String json, JavaType type) {
		try {
			return getJsonMapper().readValue(json, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param json JSON string
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromJson(String json, TypeReference<T> type) {
		try {
			return getJsonMapper().readValue(json, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON \""+json+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param file JSON file
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromJson(File file, Class<T> clazz) {
		try {
			return getJsonMapper().readValue(file, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param file JSON file
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromJson(File file, JavaType type) {
		try {
			return getJsonMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param file JSON file
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromJson(File file, TypeReference<T> type) {
		try {
			return getJsonMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON file \""+file+"\"", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromJson(InputStream stream, Class<T> clazz) {
		try {
			return getJsonMapper().readValue(stream, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON stream.", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromJson(InputStream stream, JavaType type) {
		try {
			return getJsonMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON stream.", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromJson(InputStream stream, TypeReference<T> type) {
		try {
			return getJsonMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON stream.", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromJson(Reader reader, Class<T> clazz) {
		try {
			return getJsonMapper().readValue(reader, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromJson(Reader reader, JavaType type) {
		try {
			return getJsonMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
		}
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromJson(Reader reader, TypeReference<T> type) {
		try {
			return getJsonMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
		}
	}
	
	/**
	 * Returns a configured JsonMapper object.
	 * @return the JsonMapper
	 */
	public static ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = new ObjectMapper();
			jsonMapper.registerModule(new JavaTimeModule());
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
		}
		return jsonMapper;
	}

}
