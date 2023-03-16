/**
 * Created on Oct 23, 2022. This file is part of the RsBudget REST server.
 */
package rs.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * JSON utils for mapping back and forth
 * @author ralph
 *
 */
public class JsonUtils {

	private static JsonFactory  jsonFactory;
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
			jsonMapper = new ObjectMapper(getJsonFactory());
			jsonMapper.registerModule(new JavaTimeModule());
			jsonMapper.setSerializationInclusion(Include.NON_NULL);
		}
		return jsonMapper;
	}

	public static JsonFactory getJsonFactory() {
		if (jsonFactory == null) {
			jsonFactory = new JsonFactory();
		}
		return jsonFactory;
	}
	
	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 */
	public static JsonParser getParser(File file) throws IOException {
		return getJsonFactory().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.lang.String)
	 */
	public static JsonParser getParser(String content) throws IOException {
		return getJsonFactory().createParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.net.URL)
	 */
	public static JsonParser getParser(URL url) throws IOException {
		return getJsonFactory().createParser(url);
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.InputStream)
	 */
	public static JsonParser getParser(InputStream in) throws IOException {
		return getJsonFactory().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param r - the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.Reader)
	 */
	public static JsonParser getParser(Reader r) throws IOException {
		return getJsonFactory().createParser(r);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(byte[])
	 */
	public static JsonParser getParser(byte[] data) throws IOException {
		return getJsonFactory().createParser(data);
	}

	/**
	 * Creates a {@link TypeReference} reference for a list of given class.
	 * @param <T> the type of the items
	 * @param clazz the class of the items
	 * @return the {@link TypeReference} for a list of these items
	 */
	public static <T> TypeReference<ArrayList<T>> getListTypeRef(Class<T> clazz) {
		return new TypeReference<ArrayList<T>>() {};
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a set of given class.
	 * @param <T> the type of the items
	 * @param clazz the class of the items
	 * @return the {@link TypeReference} for a set of these items
	 */
	public static <T> TypeReference<Set<T>> getSetTypeRef(Class<T> clazz) {
		return new TypeReference<Set<T>>() {};
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a map of given key and value classes.
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @param keyClass class of keys
	 * @param valueClass class of values
	 * @return the {@link TypeReference} for a map of these types
	 */
	public static <K,V> TypeReference<Map<K,V>> getMapTypeRef(Class<K> keyClass, Class<V> valueClass) {
		return new TypeReference<Map<K,V>>() {};
	}

}
