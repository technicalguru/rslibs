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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

/**
 * YAML utils for mapping back and forth.
 * @deprecated Please use {@link Yaml} and {@link JacksonUtils#YAML}. The preferred way is to create your own
 *             instance of {@link Yaml} in your project as a static variable.
 *             
 * @author ralph
 *
 */
@Deprecated
public class YamlUtils {

	/** The default {@link Yaml} instance */
	private static Yaml YAML = Yaml.builder().build();

	/**
	 * Convert any object to its YAML representation.
	 * @param o - the object to convert
	 * @return the YAML string
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static String toYaml(Object o) {
		return YAML.toYaml(o);
	}
	
	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Type Class
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, Class<T> type) {
		return YAML.fromYaml(yaml, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, String path, Class<T> type) {
		return YAML.fromYaml(yaml, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, JavaType type) {
		return YAML.fromYaml(yaml, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, String path, JavaType type) {
		return YAML.fromYaml(yaml, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param yaml YAML string
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, TypeReference<T> type) {
		return YAML.fromYaml(yaml, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param yaml YAML string
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(String yaml, String path, TypeReference<T> type) {
		return YAML.fromYaml(yaml, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Type Class
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, Class<T> type) {
		return YAML.fromYaml(file, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, String path, Class<T> type) {
		return YAML.fromYaml(file, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, JavaType type) {
		return YAML.fromYaml(file, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, String path, JavaType type) {
		return YAML.fromYaml(file, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param file YAML file
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, TypeReference<T> type) {
		return YAML.fromYaml(file, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param file YAML file
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(File file, String path, TypeReference<T> type) {
		return YAML.fromYaml(file, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type Class
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, Class<T> type) {
		return YAML.fromYaml(stream, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, String path, Class<T> type) {
		return YAML.fromYaml(stream, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, JavaType type) {
		return YAML.fromYaml(stream, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, String path, JavaType type) {
		return YAML.fromYaml(stream, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param stream YAML input stream
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, TypeReference<T> type) {
		return YAML.fromYaml(stream, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param stream YAML input stream
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(InputStream stream, String path, TypeReference<T> type) {
		return YAML.fromYaml(stream, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type Class
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, Class<T> type) {
		return YAML.fromYaml(reader, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type class
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, String path, Class<T> type) {
		return YAML.fromYaml(reader, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Java type
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, JavaType type) {
		return YAML.fromYaml(reader, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Java type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, String path, JavaType type) {
		return YAML.fromYaml(reader, path, type);
	}

	/**
	 * Convert from YAML to Object.
	 * @param <T> Class type
	 * @param reader YAML reader
	 * @param type Type reference
	 * @return the object
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, TypeReference<T> type) {
		return YAML.fromYaml(reader, type);
	}
	
	/**
	 * Parses the YAML, navigates to given path and returns object as given type.
	 * <p>Use e.g with: <code>new TypeReference&lt;ArrayList&lt;String&gt;&gt;() {}</code></p>
	 * @param <T> class type
	 * @param reader YAML reader
	 * @param path the path (simple dot notation, nothing else!!!)
	 * @param type Type reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T fromYaml(Reader reader, String path, TypeReference<T> type) {
		return YAML.fromYaml(reader, path, type);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, Class<T> type) {
		return YAML.convertFrom(root, path, type);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Java Type
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, JavaType type) {
		return YAML.convertFrom(root, path, type);
	}

	/**
	 * Convert from a specific sub-path in the {@link JsonNode}.
	 * @param <T> class type
	 * @param root node to start from when traversing
	 * @param path the path (simple dot notation, e.g. "path1.path2" - nothing else!!!)
	 * @param type Type Reference
	 * @return the object at the specified path or null if it doesn't exist
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> T convertFrom(JsonNode root, String path, TypeReference<T> type) {
		return YAML.convertFrom(root, path, type);
	}

	/**
	 * Returns a configured JsonMapper object.
	 * @return the JsonMapper
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static ObjectMapper getYamlMapper() {
		return YAML.getYamlMapper();
	}

	public static YAMLFactory getYamlFactory() {
		return YAML.getYamlFactory();
	}
	
	/**
	 * Returns a parser for the given file.
	 * @param file - the file to be parsed
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static YAMLParser getParser(File file) throws IOException {
		return YAML.getParser(file);
	}

	/**
	 * Returns a parser for the given string.
	 * @param content - the content
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.lang.String)
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static YAMLParser getParser(String content) throws IOException {
		return YAML.getParser(content);
	}

	/**
	 * Returns a parser for the given URL resource.
	 * @param url - the URL
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.net.URL)
	 * @deprecated use {@link Yaml#getParser(InputStream)} instead
	 */
	@Deprecated
	public static YAMLParser getParser(URL url) throws IOException {
		return YAML.getParser(url.openStream());
	}

	/**
	 * Returns a parser for the given input stream.
	 * @param in - the input stream
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.InputStream)
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static YAMLParser getParser(InputStream in) throws IOException {
		return YAML.getParser(in);
	}

	/**
	 * Returns a parser for the given reader.
	 * @param reader the reader
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(java.io.Reader)
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static YAMLParser getParser(Reader reader) throws IOException {
		return YAML.getParser(reader);
	}

	/**
	 * Returns a parser for the given bytes.
	 * @param data - the data in bytes
	 * @return the parser
	 * @throws IOException - when the input cannot be read
	 * @see com.fasterxml.jackson.dataformat.yaml.YAMLFactory#createParser(byte[])
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static YAMLParser getParser(byte[] data) throws IOException {
		return YAML.getParser(data);
	}

	/**
	 * Parses multiple documents in a YAML file.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param file - the YAML file to read from
	 * @param type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> List<T> parseMultiple(File file, Class<T> type) throws IOException {
		return YAML.parseMultiple(file, type);
	}

	/**
	 * Parses multiple documents from a reader.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param reader - the reader to read from
	 * @param type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> List<T> parseMultiple(Reader reader, Class<T> type) throws IOException {
		return YAML.parseMultiple(reader, type);
	}

	/**
	 * Parses multiple documents from an input stream.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param stream - the inputStream to read from
	 * @param type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> List<T> parseMultiple(InputStream stream, Class<T> type) throws IOException {
		return YAML.parseMultiple(stream, type);
	}

	/**
	 * Parses multiple documents froma URL.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param url - the URL to read from
	 * @param type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> List<T> parseMultiple(URL url, Class<T> type) throws IOException {
		return YAML.parseMultiple(url.openStream(), type);
	}

	/**
	 * Reads multiple documents from a parser.
	 * <p>It is expected that all documents are of the same class.</p>
	 * @param <T> the class expected to be parsed
	 * @param parser - the YAML parser initialized with content
	 * @param type - the expected class of the documents
	 * @return the list of documents parsed
	 * @throws IOException - when the input cannot be read
	 * @deprecated use {@link Yaml} class instead
	 */
	@Deprecated
	public static <T> List<T> parseMultiple(YAMLParser parser, Class<T> type) throws IOException {
		return YAML.parseMultiple(parser, type);
	}
}
