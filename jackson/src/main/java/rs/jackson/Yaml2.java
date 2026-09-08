package rs.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonInclude.Value;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactoryBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

/**
 * New YAML utils for mapping back and forth using Jackson v2.
 * <p>Create with:
 * <pre>
 *   Yaml2.builder()
 *      .with(myYamlMapper)
 *      .build();
 * </pre>
 * You can also pass {@link YAMLFactory}, {@link YAMLFactoryBuilder} and {@link YAMLMapper.Builder}
 * to configure the {@link Yaml2} class more fine-granularily.
 * 
 * <p>Please notice that the builder classes will not be used when an object was already configured.
 * <pre>
 *   Yaml2.builder()
 *      .with(myYamlFactory)
 *      .with(myYamlFactoryBuilder) // Ignored!
 *      .build();
 * </pre>
 * This will not use the configured {@link YAMLFactoryBuilder} instance as a {@link YAMLFactory} is 
 * already available.
 * 
 * @author ralph
 *
 */
public class Yaml2 {
	
	/** The default {@link Yaml2} instance */
	public static Yaml2 YAML = builder().build();
		
	private YAMLFactory yamlFactory;
	private YAMLMapper  yamlMapper;
	
	/**
	 * Constructor with given YAMLMapper.
	 * @param yamlMapper YAMLMapper to be used
	 */
	private Yaml2(YAMLMapper yamlMapper) {
		this.yamlMapper  = yamlMapper;
		this.yamlFactory = yamlMapper.getFactory();
	}
	
	/**
	 * Returns the underlying YAMLFactory.
	 * @return the YAMLFactory
	 */
	public YAMLFactory getYamlFactory() {
		return yamlFactory;
	}

	/**
	 * Returns the underlying YAMLMapper.
	 * @return the YAMLMapper
	 */
	public YAMLMapper getYamlMapper() {
		return yamlMapper;
	}

	/**
	 * Convert any object to its YAML representation.
	 * @param o - the object to convert
	 * @return the YAML string
	 */
	public String toYaml(Object o) {
		try {
			return getYamlMapper().writeValueAsString(o);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert to YAML", t);
		}
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromYaml(String yaml, Class<T> type) {
		return fromYaml(yaml, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(String yaml, String path, Class<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(yaml), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Java type
	 * @return the object
	 */
	public <T> T fromYaml(String yaml, JavaType type) {
		return fromYaml(yaml, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(String yaml, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(yaml), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Type reference
	 * @return the object
	 */
	public <T> T fromYaml(String yaml, TypeReference<T> type) {
		return fromYaml(yaml, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param jaon YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(String yaml, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(yaml), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromYaml(File file, Class<T> type) {
		return fromYaml(file, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(File file, String path, Class<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Java type
	 * @return the object
	 */
	public <T> T fromYaml(File file, JavaType type) {
		return fromYaml(file, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(File file, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Type reference
	 * @return the object
	 */
	public <T> T fromYaml(File file, TypeReference<T> type) {
		return fromYaml(file, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(File file, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromYaml(InputStream stream, Class<T> type) {
		return fromYaml(stream, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(InputStream stream, String path, Class<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Java type
	 * @return the object
	 */
	public <T> T fromYaml(InputStream stream, JavaType type) {
		return fromYaml(stream, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(InputStream stream, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type reference
	 * @return the object
	 */
	public <T> T fromYaml(InputStream stream, TypeReference<T> type) {
		return fromYaml(stream, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(InputStream stream, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type Class
	 * @return the object
	 */
	public <T> T fromYaml(Reader reader, Class<T> type) {
		return fromYaml(reader, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type Class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(Reader reader, String path, Class<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Java type
	 * @return the object
	 */
	public <T> T fromYaml(Reader reader, JavaType type) {
		return fromYaml(reader, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(Reader reader, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type reference
	 * @return the object
	 */
	public <T> T fromYaml(Reader reader, TypeReference<T> type) {
		return fromYaml(reader, null, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public <T> T fromYaml(Reader reader, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
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
				return getYamlMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML node.", t);
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
				return getYamlMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML node.", t);
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
				return getYamlMapper().convertValue(child.get(), type);
			}
			return null;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML node.", t);
		}
	}

	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 */
	public YAMLParser getParser(File file) throws IOException {
		return (YAMLParser)getYamlMapper().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.YAMLFactory#createParser(java.lang.String)
	 */
	public YAMLParser getParser(String content) throws IOException {
		return (YAMLParser)getYamlMapper().createParser(content);
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.YAMLFactory#createParser(java.io.InputStream)
	 */
	public YAMLParser getParser(InputStream in) throws IOException {
		return (YAMLParser)getYamlMapper().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param reader the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.YAMLFactory#createParser(java.io.Reader)
	 */
	public YAMLParser getParser(Reader reader) throws IOException {
		return (YAMLParser)getYamlMapper().createParser(reader);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see tools.jackson.core.YAMLFactory#createParser(byte[])
	 */
	public YAMLParser getParser(byte[] data) throws IOException {
		return (YAMLParser)getYamlMapper().createParser(data);
	}

	/**
	 * Parses multiple documents in a YAML file.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param file - the YAML file to read from
	 * @param  type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public <T> List<T> parseMultiple(File file, Class<T> type) throws IOException {
		return parseMultiple(getParser(file),  type);
	}

	/**
	 * Parses multiple documents from a reader.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param reader - the reader to read from
	 * @param  type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public <T> List<T> parseMultiple(Reader reader, Class<T> type) throws IOException {
		return parseMultiple(getParser(reader),  type);
		
	}

	/**
	 * Parses multiple documents from an input stream.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param inputStream - the inputStream to read from
	 * @param  type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public <T> List<T> parseMultiple(InputStream inputStream, Class<T> type) throws IOException {
		return parseMultiple(getParser(inputStream),  type);
		
	}

	/**
	 * Reads multiple documents from a parser.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param parser - the YAML parser initialized with content
	 * @param  type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public <T> List<T> parseMultiple(YAMLParser parser, Class<T> type) throws IOException {
		YAMLMapper mapper = getYamlMapper();
		return mapper.readValues(parser, new TypeReference<T>() {}).readAll();
	}
	
	/**
	 * Builder class for {@link Yaml2} objects.
	 * @author ralph
	 *
	 */
	public static class Builder {
		
		private YAMLFactory        yamlFactory;
		private YAMLFactoryBuilder yamlFactoryBuilder;
		private YAMLMapper         yamlMapper;
		private YAMLMapper.Builder yamlMapperBuilder;
		
		/**
		 * Private constructor. Use {@link Yaml2#builder()}
		 */
		private Builder() {
			this.yamlFactory = null;
			this.yamlMapper  = null;
		}
		
		/**
		 * Use the given {@link YAMLFactory}.
		 * <p>Will ignore any configured {@link YAMLFactoryBuilder}.
		 * @param yamlFactory the YAMLFactory to be used
		 * @return the builder for method chaining
		 */
		public Builder with(YAMLFactory  yamlFactory) {
			this.yamlFactory = yamlFactory;
			return this;
		}
		
		/**
		 * Returns the configured {@link YAMLFactory} object for building.
		 * @return the {@link YAMLFactory} or null if not specified
		 */
		public YAMLFactory yamlFactory() {
			return this.yamlFactory;
		}
		
		/**
		 * Use the given {@link YAMLFactoryBuilder}.
		 * <p>Will be ignored when {@link #with(YAMLFactory)}, {@link #with(YAMLMapper)} or  {@link #with(YAMLMapper.Builder)} is used.
		 * @param yamlFactoryBuilder the YAMLFactoryBuilder to be used
		 * @return the builder for method chaining
		 */
		public Builder with(YAMLFactoryBuilder yamlFactoryBuilder) {
			this.yamlFactoryBuilder = yamlFactoryBuilder;
			return this;
		}
		
		/**
		 * Returns the configured {@link YAMLFactoryBuilder} object for building.
		 * @return the {@link YAMLFactoryBuilder} or null if not specified
		 */
		public YAMLFactoryBuilder yamlFactoryBuilder() {
			return this.yamlFactoryBuilder;
		}
		
		/**
		 * Use the given {@link YAMLMapper}.
		 * <p>Will ignore any configured {@link YAMLMapper.Builder}, {@link YAMLFactory} or {@link YAMLFactoryBuilder}.
		 * @param yamlMapper the {@link YAMLMapper} to be used
		 * @return the builder for method chaining
		 */
		public Builder with(YAMLMapper yamlMapper) {
			this.yamlMapper = yamlMapper;
			return this;
		}
		
		/**
		 * Returns the configured {@link YAMLMapper} object for building.
		 * @return the {@link YAMLMapper} or null if not specified
		 */
		public YAMLMapper yamlMapper() {
			return this.yamlMapper;
		}
		
		/**
		 * Use the given {@link YAMLMapper.Builder}.
		 * <p>Will be ignored when {@link #with(YAMLMapper)} is used.
		 * @param yamlMapper the {@link YAMLMapper} to be used
		 * @return the builder for method chaining
		 */
		public Builder with(YAMLMapper.Builder yamlMapperBuilder) {
			this.yamlMapperBuilder = yamlMapperBuilder;
			return this;
		}
		
		/**
		 * Returns the configured {@link YAMLMapper.Builder} object for building.
		 * @return the {@link YAMLMapper.Builder} or null if not specified
		 */
		public YAMLMapper.Builder yamlMapperBuilder() {
			return this.yamlMapperBuilder;
		}
		
		/**
		 * Returns the configured YAMLMapper object (or creates it using the {@link #getYAMLMapperBuilder()} method).
		 * @return the YAMLMapper to be used
		 */
		private YAMLMapper getYAMLMapper() {
			if (yamlMapper == null) return getYAMLMapperBuilder().build();
			return yamlMapper;
		}

		/**
		 * Returns the configured {@link YAMLMapper.Builder} (or creates it using the {@link Yaml2#defaultYAMLMapperBuilder(YAMLFactory)} method).
		 * @return the YAMLMapper.Builder to be used
		 */
		private YAMLMapper.Builder getYAMLMapperBuilder() {
			if (yamlMapperBuilder == null) return defaultYAMLMapperBuilder(getYAMLFactory());
			return yamlMapperBuilder;
		}
		
		/**
		 * Returns the configured {@link YAMLFactory} (or creates it using the {@link #getYAMLFactoryBuilder()} method).
		 * @return the YAMLFactory to be used
		 */
		private YAMLFactory getYAMLFactory() {
			if (yamlFactory == null) return getYAMLFactoryBuilder().build();
			return yamlFactory;
		}
		
		/**
		 * Returns the configured {@link YAMLFactoryBuilder} (or creates it using the {@link Yaml2#defaultYAMLFactoryBuilder()} method).
		 * @return the YAMLFactory to be used
		 */
		private YAMLFactoryBuilder getYAMLFactoryBuilder() {
			if (yamlFactoryBuilder == null) return defaultYAMLFactoryBuilder();
			return yamlFactoryBuilder;
		}

		/**
		 * Builds the new {@link YAMLMapper} with configured values.
		 * <p>Re-entrant, will always create a new one based on configuration.
		 * @return the Yaml utility object
		 */
		public Yaml2 build() {
			return new Yaml2(getYAMLMapper());
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
	 * Creates a default {@link YAMLMapper.Builder} object.
	 * <p>The builder is configured to ignore unknown properties when deserializing, using JavaTime objects and with {@link YamlInclude.Value#ALL_NON_NULL} 
	 * property inclusion.
	 * @param yamlFactory the {@link YAMLFactory} to be used
	 * @return the Builder
	 */
	public static YAMLMapper.Builder defaultYAMLMapperBuilder(YAMLFactory yamlFactory) {
		return YAMLMapper.builder(yamlFactory)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.defaultPropertyInclusion(Value.construct(Include.NON_NULL, Include.NON_NULL));
	}
	
	/**
	 * Creates a default {@link YAMLFactoryBuilder} object.
	 * <p>No additional configuration is made.
	 * @return the Builder
	 */
	public static YAMLFactoryBuilder defaultYAMLFactoryBuilder() {
		return YAMLFactory.builder();
	}

}
