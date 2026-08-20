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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON utils for mapping back and forth.
 * @deprecated Please use {@link Json} or {@link Json#JSON}. The preferred way is to create your own
 *             instance of {@link Json} in your project as a static variable.
 *             
 * @author ralph
 *
 */
@Deprecated
public class JsonUtils {

	/** The {@link Json} instance used by this class */
	private static Json JSON = Json.builder().build();

	/**
	 * Convert any object to its JSON representation.
	 * @param o - the object to convert
	 * @return the JSON string
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static String toJson(Object o) {
		return JSON.toJson(o);
	}
	
	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param json JSON string
	 * @param clazz Type Class
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, Class<T> clazz) {
		return JSON.fromJson(json, clazz);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param json JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, String path, Class<T> type) {
		return JSON.fromJson(json, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param json JSON string
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, JavaType type) {
		return JSON.fromJson(json, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param json JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, String path, JavaType type) {
		return JSON.fromJson(json, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param json JSON string
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, TypeReference<T> type) {
		return JSON.fromJson(json, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param jaon JSON string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(String json, String path, TypeReference<T> type) {
		return JSON.fromJson(json, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param file JSON file
	 * @param clazz Type Class
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, Class<T> clazz) {
		return JSON.fromJson(file, clazz);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, String path, Class<T> clazz) {
		return JSON.fromJson(file, path, clazz);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param file JSON file
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, JavaType type) {
		return JSON.fromJson(file, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, String path, JavaType type) {
		return JSON.fromJson(file, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param file JSON file
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, TypeReference<T> type) {
		return JSON.fromJson(file, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param file JSON file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(File file, String path, TypeReference<T> type) {
		return JSON.fromJson(file, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param clazz Type Class
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, Class<T> clazz) {
		return JSON.fromJson(stream, clazz);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream JSON input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, String path, Class<T> clazz) {
		return JSON.fromJson(stream, path, clazz);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, JavaType type) {
		return JSON.fromJson(stream, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream JSON stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, String path, JavaType type) {
		return JSON.fromJson(stream, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param stream JSON input stream
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, TypeReference<T> type) {
		return JSON.fromJson(stream, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param stream JSON input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(InputStream stream, String path, TypeReference<T> type) {
		return JSON.fromJson(stream, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param clazz Type Class
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, Class<T> clazz) {
		return JSON.fromJson(reader, clazz);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, String path, Class<T> clazz) {
		return JSON.fromJson(reader, path, clazz);
	}

	/**
	 * Convert from JSON to Object.
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, JavaType type) {
		return JSON.fromJson(reader, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, String path, JavaType type) {
		return JSON.fromJson(reader, path, type);
	}

	/**
	 * Convert from JSON to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param reader JSON reader
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, TypeReference<T> type) {
		return JSON.fromJson(reader, type);
	}
	
	/**
	 * Parses the JSON, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param reader JSON reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T fromJson(Reader reader, String path, TypeReference<T> type) {
		return JSON.fromJson(reader, path, type);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param clazz Java Class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, Class<T> clazz) {
		return JSON.convertFrom(root, path, clazz);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, JavaType type) {
		return JSON.convertFrom(root, path, type);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Type Reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, TypeReference<T> type) {
		return JSON.convertFrom(root, path, type);
	}

	/**
	 * Returns a configured JsonMapper object.
	 * @return the JsonMapper
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static ObjectMapper getJsonMapper() {
		return JSON.getJsonMapper();
	}

	/**
	 * Returns the JSON factory or creates a new one if it doesn't exist yet
	 * @return JsonFactory
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonFactory getJsonFactory() {
		return JSON.getJsonFactory();
	}
	
	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonParser getParser(File file) throws IOException {
		return JSON.getParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.lang.String)
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonParser getParser(String content) throws IOException {
		return JSON.getParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.net.URL)
	 * @deprecated use {@link Json#getParser(InputStream)} instead
	 */
	@Deprecated
	public static JsonParser getParser(URL url) throws IOException {
		return JSON.getParser(url.openStream());
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.InputStream)
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonParser getParser(InputStream in) throws IOException {
		return JSON.getParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param reader the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.Reader)
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonParser getParser(Reader reader) throws IOException {
		return JSON.getParser(reader);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(byte[])
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static JsonParser getParser(byte[] data) throws IOException {
		return JSON.getParser(data);
	}

	/**
	 * Creates a {@link TypeReference} reference for a list of given class.
	 * @param <T> the type of the items
	 * @param clazz the class of the items
	 * @return the {@link TypeReference} for a list of these items
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> TypeReference<ArrayList<T>> getListTypeRef(Class<T> clazz) {
		return JacksonUtils.getListTypeRef(clazz);
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a set of given class.
	 * @param <T> the type of the items
	 * @param clazz the class of the items
	 * @return the {@link TypeReference} for a set of these items
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <T> TypeReference<Set<T>> getSetTypeRef(Class<T> clazz) {
		return JacksonUtils.getSetTypeRef(clazz);
	}
	
	/**
	 * Creates a {@link TypeReference} reference for a map of given key and value classes.
	 * @param <K> type of keys
	 * @param <V> type of values
	 * @param keyClass class of keys
	 * @param valueClass class of values
	 * @return the {@link TypeReference} for a map of these types
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static <K,V> TypeReference<Map<K,V>> getMapTypeRef(Class<K> keyClass, Class<V> valueClass) {
		return JacksonUtils.getMapTypeRef(keyClass, valueClass);
	}

	/**
	 * Traverse to the node given in path.
	 * @param node the node to traverse from
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @return the node found
	 * @deprecated use {@link Json} class instead
	 */
	@Deprecated
	public static Optional<JsonNode> traverse(JsonNode node, String path) {
		return JacksonUtils.traverse(node, path);
	}

}
