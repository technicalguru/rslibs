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
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import rs.baselib.util.CommonUtils;

/**
 * JSON utils for mapping back and forth
 * @author ralph
 *
 */
public class JsonUtils {

	private static JsonFactory  jsonFactory;
	private static ObjectMapper jsonMapper;
	
	/**
	 * Convert any object to its JSON representation.
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param json JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(String json, String path, Class<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(json), path, type);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param json JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(String json, String path, JavaType type) {
		try {
			return convertFrom(getJsonMapper().readTree(json), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON \""+json+"\"", t);
		}
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param jaon JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(String json, String path, TypeReference<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(json), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(File file, String path, Class<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(file), path, type);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(File file, String path, JavaType type) {
		try {
			return convertFrom(getJsonMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON file \""+file+"\"", t);
		}
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(File file, String path, TypeReference<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream JSON input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(InputStream stream, String path, Class<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON stream", t);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream JSON stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(InputStream stream, String path, JavaType type) {
		try {
			return convertFrom(getJsonMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON stream.", t);
		}
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param stream JSON input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(InputStream stream, String path, TypeReference<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(stream), path, type);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(Reader reader, String path, Class<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader", t);
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(Reader reader, String path, JavaType type) {
		try {
			return convertFrom(getJsonMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
		}
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
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
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromJson(Reader reader, String path, TypeReference<T> type) {
		try {
			return convertFrom(getJsonMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON reader.", t);
		}
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T convertFrom(JsonNode root, String path, Class<T> type) {
		try {
			Optional<JsonNode> child = traverse(root, path);
			if (child.isPresent()) {
				return getJsonMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON node.", t);
		}
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T convertFrom(JsonNode root, String path, JavaType type) {
		try {
			Optional<JsonNode> child = traverse(root, path);
			if (child.isPresent()) {
				return getJsonMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON node.", t);
		}
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Type Reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T convertFrom(JsonNode root, String path, TypeReference<T> type) {
		try {
			Optional<JsonNode> child = traverse(root, path);
			if (child.isPresent()) {
				return getJsonMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from JSON node.", t);
		}
	}

	/**
	 * Returns a configured JsonMapper object.
	 * @return the JsonMapper
	 */
	public static ObjectMapper getJsonMapper() {
		if (jsonMapper == null) {
			jsonMapper = JsonMapper.builder(getJsonFactory())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.addModule(new JavaTimeModule())
				.defaultPropertyInclusion(JsonInclude.Value.ALL_NON_NULL)
				.build();
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
		return getJsonFactory().createParser(url.openStream());
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

	/**
	 * Traverse to the node given in path.
	 * @param node the node to traverse from
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @return the node found
	 */
	public static Optional<JsonNode> traverse(JsonNode node, String path) {
		if (!CommonUtils.isEmpty(path)) {
			String paths[] = path.split("\\.");
			if (paths.length > 0) {
				for (String p : paths) {
					if (!CommonUtils.isEmpty(p)) {
						node = node.findPath(p.trim());
						if (node.isMissingNode()) return Optional.empty();
					}
				}
			}
		}
		
		return Optional.of(node);
	}

}
