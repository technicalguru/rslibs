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
import com.fasterxml.jackson.core.JsonFactoryBuilder;
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
 * New JSON utils for mapping back and forth.
 * <p>Create with:
 * <pre>
 *   Json.builder()
 *      .with(myJasonMapper)
 *      .build();
 * </pre>
 * You can also pass {@link JsonFactory}, {@link JsonFactoryBuilder} and {@link JsonMapper.Builder}
 * to configure the {@link Json} class more fine-granularily.
 * 
 * <p>Please notice that the builder classes will not be used when an object was already configured.
 * <pre>
 *   Json.builder()
 *      .with(myJsonFactory)
 *      .with(myJsonFactoryBuilder) // Ignored!
 *      .build();
 * </pre>
 * This will not use the configured {@link JsonFactoryBuilder} instance as a {@link JsonFactory} is 
 * already available.
 * 
 * @author ralph
 *
 */
public class Json {

	/** The default {@link Json} instance */
	public static Json JSON = builder().build();
	
	private JsonFactory  jsonFactory;
	private ObjectMapper jsonMapper;
	
	private Json(ObjectMapper jsonMapper) {
		this.jsonMapper  = jsonMapper;
		this.jsonFactory = jsonMapper.getFactory();
	}
	
	/**
	 * Returns the underlying JsonFactory.
	 * @return the JsonFactory
	 */
	public JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	/**
	 * Returns the underlying JsonMapper.
	 * @return the JsonMapper
	 */
	public ObjectMapper getJsonMapper() {
		return jsonMapper;
	}

	/**
	 * Convert any object to its JSON representation.
	 * @param o - the object to convert
	 * @return the JSON string
	 */
	public String toJson(Object o) {
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
	public <T> T fromJson(String json, Class<T> clazz) {
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
	public <T> T fromJson(String json, String path, Class<T> type) {
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
	public <T> T fromJson(String json, JavaType type) {
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
	public <T> T fromJson(String json, String path, JavaType type) {
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
	public <T> T fromJson(String json, TypeReference<T> type) {
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
	public <T> T fromJson(String json, String path, TypeReference<T> type) {
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
	public <T> T fromJson(File file, Class<T> clazz) {
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
	public <T> T fromJson(File file, String path, Class<T> type) {
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
	public <T> T fromJson(File file, JavaType type) {
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
	public <T> T fromJson(File file, String path, JavaType type) {
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
	public <T> T fromJson(File file, TypeReference<T> type) {
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
	public <T> T fromJson(File file, String path, TypeReference<T> type) {
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
	public <T> T fromJson(InputStream stream, Class<T> clazz) {
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
	public <T> T fromJson(InputStream stream, String path, Class<T> type) {
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
	public <T> T fromJson(InputStream stream, JavaType type) {
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
	public <T> T fromJson(InputStream stream, String path, JavaType type) {
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
	public <T> T fromJson(InputStream stream, TypeReference<T> type) {
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
	public <T> T fromJson(InputStream stream, String path, TypeReference<T> type) {
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
	public <T> T fromJson(Reader reader, Class<T> clazz) {
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
	public <T> T fromJson(Reader reader, String path, Class<T> type) {
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
	public <T> T fromJson(Reader reader, JavaType type) {
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
	public <T> T fromJson(Reader reader, String path, JavaType type) {
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
	public <T> T fromJson(Reader reader, TypeReference<T> type) {
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
	public <T> T fromJson(Reader reader, String path, TypeReference<T> type) {
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
	public <T> T convertFrom(JsonNode root, String path, Class<T> type) {
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
	public <T> T convertFrom(JsonNode root, String path, JavaType type) {
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
	public <T> T convertFrom(JsonNode root, String path, TypeReference<T> type) {
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
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 */
	public JsonParser getParser(File file) throws IOException {
		return getJsonFactory().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.lang.String)
	 */
	public JsonParser getParser(String content) throws IOException {
		return getJsonFactory().createParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.net.URL)
	 */
	public JsonParser getParser(URL url) throws IOException {
		return getJsonFactory().createParser(url.openStream());
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.InputStream)
	 */
	public JsonParser getParser(InputStream in) throws IOException {
		return getJsonFactory().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param r - the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(java.io.Reader)
	 */
	public JsonParser getParser(Reader r) throws IOException {
		return getJsonFactory().createParser(r);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.core.JsonFactory#createParser(byte[])
	 */
	public JsonParser getParser(byte[] data) throws IOException {
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

	/**
	 * Builder class for {@link Json} objects.
	 * @author ralph
	 *
	 */
	public static class Builder {
		
		private JsonFactory        jsonFactory;
		private JsonFactoryBuilder jsonFactoryBuilder;
		private ObjectMapper       jsonMapper;
		private com.fasterxml.jackson.databind.json.JsonMapper.Builder jsonMapperBuilder;
		
		private Builder() {
			this.jsonFactory = null;
			this.jsonMapper  = null;
		}
		
		public Builder with(JsonFactory  jsonFactory) {
			this.jsonFactory = jsonFactory;
			return this;
		}
		
		public Builder with(JsonFactoryBuilder jsonFactoryBuilder) {
			this.jsonFactoryBuilder = jsonFactoryBuilder;
			return this;
		}
		
		public Builder with(ObjectMapper jsonMapper) {
			this.jsonMapper = jsonMapper;
			return this;
		}
		
		public Builder with(com.fasterxml.jackson.databind.json.JsonMapper.Builder jsonMapperBuilder) {
			this.jsonMapperBuilder = jsonMapperBuilder;
			return this;
		}
		
		/**
		 * Returns the configured JsonMapper object (or creates it).
		 * @return the JsonMapper
		 */
		private ObjectMapper getJsonMapper() {
			if (jsonMapper == null) {
				jsonMapper = getJsonMapperBuilder().build();
			}
			return jsonMapper;
		}

		private com.fasterxml.jackson.databind.json.JsonMapper.Builder getJsonMapperBuilder() {
			if (jsonMapperBuilder == null) {
				jsonMapperBuilder = defaultJsonMapperBuilder(getJsonFactory());
			}
			return jsonMapperBuilder;
		}
		
		private JsonFactory getJsonFactory() {
			if (jsonFactory == null) {
				jsonFactory = getJsonFactoryBuilder().build();
			}
			return jsonFactory;
		}
		
		private JsonFactoryBuilder getJsonFactoryBuilder() {
			if (jsonFactoryBuilder == null) {
				jsonFactoryBuilder = defaultJsonFactoryBuilder();
			}
			return jsonFactoryBuilder;
		}

		public Json build() {
			return new Json(getJsonMapper());
		}

	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static com.fasterxml.jackson.databind.json.JsonMapper.Builder defaultJsonMapperBuilder(JsonFactory jsonFactory) {
		return JsonMapper.builder(jsonFactory)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.addModule(new JavaTimeModule())
			.defaultPropertyInclusion(JsonInclude.Value.ALL_NON_NULL);
	}
	
	public static JsonFactoryBuilder defaultJsonFactoryBuilder() {
		return new JsonFactoryBuilder();
	}
	
}
