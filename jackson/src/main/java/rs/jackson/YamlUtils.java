/**
 * 
 */
package rs.jackson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * YAML utils for mapping back and forth
 *
 * @author ralph
 *
 */
public class YamlUtils {

	private static YAMLFactory  yamlFactory;
	private static ObjectMapper yamlMapper;
	
	/**
	 * Convert any object to its YAML representation.
	 * @param o - the object to convert
	 * @return the YAML string
	 */
	public static String toYaml(Object o) {
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
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(String yaml, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(yaml, clazz);
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
	public static <T> T fromYaml(String yaml, JavaType type) {
		try {
			return getYamlMapper().readValue(yaml, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(String yaml, String path, Class<T> clazz) {
		try {
			return convertFrom(getYamlMapper().readTree(yaml), path, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}

	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(String yaml, String path, JavaType type) {
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
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(String yaml, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(yaml, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(String yaml, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(yaml), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML \""+yaml+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(File file, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(file, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(File file, String path, Class<T> clazz) {
		try {
			return convertFrom(getYamlMapper().readTree(file), path, clazz);
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
	public static <T> T fromYaml(File file, JavaType type) {
		try {
			return getYamlMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(File file, String path, JavaType type) {
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
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(File file, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(file, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
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
	public static <T> T fromYaml(File file, String path, TypeReference<T> type) {
		try {
			return convertFrom(getYamlMapper().readTree(file), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML file \""+file+"\"", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(stream, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(InputStream stream, String path, Class<T> clazz) {
		try {
			return convertFrom(getYamlMapper().readTree(stream), path, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, JavaType type) {
		try {
			return getYamlMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(InputStream stream, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(stream), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(InputStream stream, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(stream, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML stream.", t);
		}
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
	public static <T> T fromYaml(InputStream stream, String path, TypeReference<T> type) {
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
	 * @param clazz Type Class
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, Class<T> clazz) {
		try {
			return getYamlMapper().readValue(reader, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param clazz Type class
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(Reader reader, String path, Class<T> clazz) {
		try {
			return convertFrom(getYamlMapper().readTree(reader), path, clazz);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Java type
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, JavaType type) {
		try {
			return getYamlMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T fromYaml(Reader reader, String path, JavaType type) {
		try {
			return convertFrom(getYamlMapper().readTree(reader), path, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type reference
	 * @return the object
	 */
	public static <T> T fromYaml(Reader reader, TypeReference<T> type) {
		try {
			return getYamlMapper().readValue(reader, type);
		} catch (Throwable t) {
			throw new RuntimeException("Cannot convert from YAML reader.", t);
		}
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
	public static <T> T fromYaml(Reader reader, String path, TypeReference<T> type) {
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
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 */
	public static <T> T convertFrom(JsonNode root, String path, Class<T> type) {
		try {
			Optional<JsonNode> child = JsonUtils.traverse(root, path);
			if (child.isPresent()) {
				return getYamlMapper().convertValue(child.get(), type);
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
			Optional<JsonNode> child = JsonUtils.traverse(root, path);
			if (child.isPresent()) {
				return getYamlMapper().convertValue(child.get(), type);
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
			Optional<JsonNode> child = JsonUtils.traverse(root, path);
			if (child.isPresent()) {
				return getYamlMapper().convertValue(child.get(), type);
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
	public static ObjectMapper getYamlMapper() {
		if (yamlMapper == null) {
			yamlMapper = YAMLMapper.builder(getYamlFactory())
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.addModule(new JavaTimeModule())
					.defaultPropertyInclusion(JsonInclude.Value.ALL_NON_NULL)
					.build();
		}
		return yamlMapper;
	}

	public static YAMLFactory getYamlFactory() {
		if (yamlFactory == null) {
			yamlFactory = new YAMLFactory();
		}
		return yamlFactory;
	}
	
	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 */
	public static YAMLParser getParser(File file) throws IOException {
		return getYamlFactory().createParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.lang.String)
	 */
	public static YAMLParser getParser(String content) throws IOException {
		return getYamlFactory().createParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.net.URL)
	 */
	public static YAMLParser getParser(URL url) throws IOException {
		return getYamlFactory().createParser(url.openStream());
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.InputStream)
	 */
	public static YAMLParser getParser(InputStream in) throws IOException {
		return getYamlFactory().createParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param r - the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.Reader)
	 */
	public static YAMLParser getParser(Reader r) throws IOException {
		return getYamlFactory().createParser(r);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(byte[])
	 */
	public static YAMLParser getParser(byte[] data) throws IOException {
		return getYamlFactory().createParser(data);
	}

	/**
	 * Parses multiple documents in a YAML file.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param file - the YAML file to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(File file, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(file), clazz);
		
	}

	/**
	 * Parses multiple documents from a reader.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param reader - the reader to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(Reader reader, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(reader), clazz);
		
	}

	/**
	 * Parses multiple documents from an input stream.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param inputStream - the inputStream to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(InputStream inputStream, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(inputStream), clazz);
		
	}

	/**
	 * Parses multiple documents froma URL.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param url - the URL to read from
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(URL url, Class<T> clazz) throws IOException {
		return parseMultiple(getParser(url), clazz);
	}

	/**
	 * Reads multiple documents from a parser.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param parser - the YAML parser initialized with content
	 * @param clazz - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 */
	public static <T> List<T> parseMultiple(YAMLParser parser, Class<T> clazz) throws IOException {
		ObjectMapper mapper = getYamlMapper();
		return mapper.readValues(parser, new TypeReference<T>() {}).readAll();
	}
}
