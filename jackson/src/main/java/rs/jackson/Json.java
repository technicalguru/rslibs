/**
 * Created on Oct 23, 2022. This file is part of the RsBudget REST server.
 */
package rs.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

//import com.fasterxml.jackson.annotation.JsonInclude;
//import tools.jackson.core.JsonFactory;
//import tools.jackson.core.JsonFactoryBuilder;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.json.JsonFactoryBuilder;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * New JSON utils for mapping back and forth.
 * <p>Create with:
 * <pre>
 *   Json.builder()
 *      .with(myJsonMapper)
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

	private JsonFactory jsonFactory;
	private JsonMapper  jsonMapper;

	/**
	 * Constructor with given JsonMapper.
	 * @param jsonMapper JsonMapper to be used
	 */
	private Json(JsonMapper jsonMapper) {
		this.jsonMapper  = jsonMapper;
		this.jsonFactory = jsonMapper.tokenStreamFactory();
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
	public JsonMapper getJsonMapper() {
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
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromJson(String json, Class<T> type) {
		return fromJson(json, null, type);
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
		return fromJson(json, null, type);
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
		return fromJson(json, null, type);
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
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromJson(File file, Class<T> type) {
		return fromJson(file, null, type);
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
		return fromJson(file, null, type);
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
		return fromJson(file, null, type);
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
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromJson(InputStream stream, Class<T> type) {
		return fromJson(stream, null, type);
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
		return fromJson(stream, null, type);
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
		return fromJson(stream, null, type);
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
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromJson(Reader reader, Class<T> type) {
		return fromJson(reader, null, type);
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
		return fromJson(reader, null, type);
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
		return fromJson(reader, null, type);
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
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!- can be null or empty)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T convertFrom(JsonNode root, String path, Class<T> type) {
		try {
			Optional<JsonNode> child = JacksonUtils.traverse(root, path);
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
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!- can be null or empty)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T convertFrom(JsonNode root, String path, JavaType type) {
		try {
			Optional<JsonNode> child = JacksonUtils.traverse(root, path);
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
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!! - can be null or empty)
	 * @param type Type Reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T convertFrom(JsonNode root, String path, TypeReference<T> type) {
		try {
			Optional<JsonNode> child = JacksonUtils.traverse(root, path);
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
		return getJsonMapper().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.JsonFactory#createParser(java.lang.String)
	 */
	public JsonParser getParser(String content) throws IOException {
		return getJsonMapper().createParser(content);
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.JsonFactory#createParser(java.io.InputStream)
	 */
	public JsonParser getParser(InputStream in) throws IOException {
		return getJsonMapper().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param reader the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.JsonFactory#createParser(java.io.Reader)
	 */
	public JsonParser getParser(Reader reader) throws IOException {
		return getJsonMapper().createParser(reader);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.JsonFactory#createParser(byte[])
	 */
	public JsonParser getParser(byte[] data) throws IOException {
		return getJsonMapper().createParser(data);
	}

	/**
	 * Builder class for {@link Json} objects.
	 * @author ralph
	 *
	 */
	public static class Builder {
		
		private JsonFactory        jsonFactory;
		private JsonFactoryBuilder jsonFactoryBuilder;
		private JsonMapper         jsonMapper;
		private JsonMapper.Builder jsonMapperBuilder;
		
		/**
		 * Private constructor. Use {@link Json#builder()}
		 */
		private Builder() {
			this.jsonFactory = null;
			this.jsonMapper  = null;
		}
		
		/**
		 * Use the given {@link JsonFactory}.
		 * <p>Will ignore any configured {@link JsonFactoryBuilder}.
		 * @param jsonFactory the JsonFactory to be used
		 * @return the builder for method chaining
		 */
		public Builder with(JsonFactory  jsonFactory) {
			this.jsonFactory = jsonFactory;
			return this;
		}
		
		/**
		 * Returns the configured {@link JsonFactory} object for building.
		 * @return the {@link JsonFactory} or null if not specified
		 */
		public JsonFactory jsonFactory() {
			return this.jsonFactory;
		}
		
		/**
		 * Use the given {@link JsonFactoryBuilder}.
		 * <p>Will be ignored when {@link #with(JsonFactory)}, {@link #with(JsonMapper)} or  {@link #with(JsonMapper.Builder)} is used.
		 * @param jsonFactoryBuilder the JsonFactoryBuilder to be used
		 * @return the builder for method chaining
		 */
		public Builder with(JsonFactoryBuilder jsonFactoryBuilder) {
			this.jsonFactoryBuilder = jsonFactoryBuilder;
			return this;
		}
		
		/**
		 * Returns the configured {@link JsonFactoryBuilder} object for building.
		 * @return the {@link JsonFactoryBuilder} or null if not specified
		 */
		public JsonFactoryBuilder jsonFactoryBuilder() {
			return this.jsonFactoryBuilder;
		}
		
		/**
		 * Use the given {@link JsonMapper}.
		 * <p>Will ignore any configured {@link JsonMapper.Builder}, {@link JsonFactory} or {@link JsonFactoryBuilder}.
		 * @param jsonMapper the {@link JsonMapper} to be used
		 * @return the builder for method chaining
		 */
		public Builder with(JsonMapper jsonMapper) {
			this.jsonMapper = jsonMapper;
			return this;
		}
		
		/**
		 * Returns the configured {@link JsonMapper} object for building.
		 * @return the {@link JsonMapper} or null if not specified
		 */
		public JsonMapper jsonMapper() {
			return this.jsonMapper;
		}
		
		/**
		 * Use the given {@link JsonMapper.Builder}.
		 * <p>Will be ignored when {@link #with(JsonMapper)} is used.
		 * @param jsonMapper the {@link JsonMapper} to be used
		 * @return the builder for method chaining
		 */
		public Builder with(JsonMapper.Builder jsonMapperBuilder) {
			this.jsonMapperBuilder = jsonMapperBuilder;
			return this;
		}
		
		/**
		 * Returns the configured {@link JsonMapper.Builder} object for building.
		 * @return the {@link JsonMapper.Builder} or null if not specified
		 */
		public JsonMapper.Builder jsonMapperBuilder() {
			return this.jsonMapperBuilder;
		}
		
		/**
		 * Returns the configured JsonMapper object (or creates it using the {@link #getJsonMapperBuilder()} method).
		 * @return the JsonMapper to be used
		 */
		private JsonMapper getJsonMapper() {
			if (jsonMapper == null) return getJsonMapperBuilder().build();
			return jsonMapper;
		}

		/**
		 * Returns the configured {@link JsonMapper.Builder} (or creates it using the {@link Json#defaultJsonMapperBuilder(JsonFactory)} method).
		 * @return the JsonMapper.Builder to be used
		 */
		private JsonMapper.Builder getJsonMapperBuilder() {
			if (jsonMapperBuilder == null) return defaultJsonMapperBuilder(getJsonFactory());
			return jsonMapperBuilder;
		}
		
		/**
		 * Returns the configured {@link JsonFactory} (or creates it using the {@link #getJsonFactoryBuilder()} method).
		 * @return the JsonFactory to be used
		 */
		private JsonFactory getJsonFactory() {
			if (jsonFactory == null) return getJsonFactoryBuilder().build();
			return jsonFactory;
		}
		
		/**
		 * Returns the configured {@link JsonFactoryBuilder} (or creates it using the {@link Json#defaultJsonFactoryBuilder()} method).
		 * @return the JsonFactory to be used
		 */
		private JsonFactoryBuilder getJsonFactoryBuilder() {
			if (jsonFactoryBuilder == null) return defaultJsonFactoryBuilder();
			return jsonFactoryBuilder;
		}

		/**
		 * Builds the new {@link JsonMapper} with configured values.
		 * <p>Re-entrant, will always create a new one based on configuration.
		 * @return the Json utility object
		 */
		public Json build() {
			return new Json(getJsonMapper());
		}
	}

	/**
	 * Creates a {@link Builder} object.
	 * <p>A Builder object shall be used only once to build.
	 * @return the new builder object
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Creates a default {@link JsonMapper.Builder} object.
	 * <p>The builder is configured to ignore unknown properties when deserializing and with {@link JsonInclude.Value#ALL_NON_NULL} 
	 * property inclusion.
	 * @param jsonFactory the {@link JsonFactory} to be used
	 * @return the Builder
	 */
	public static JsonMapper.Builder defaultJsonMapperBuilder(JsonFactory jsonFactory) {
		return JsonMapper.builder(jsonFactory)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL));
	}
	
	/**
	 * Creates a default {@link JsonFactoryBuilder} object.
	 * <p>No additional configuration is made.
	 * @return the Builder
	 */
	public static JsonFactoryBuilder defaultJsonFactoryBuilder() {
		return new JsonFactoryBuilder();
	}
	
}
