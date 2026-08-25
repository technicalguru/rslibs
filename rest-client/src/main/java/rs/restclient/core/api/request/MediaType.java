/**
 * 
 */
package rs.restclient.core.api.request;

/**
 * Strings for the media type
 */
public interface MediaType {

	/**
	 * Media type for "&#42;/&#42;", including all media ranges.
	 */
	public static final String ALL = "*/*";

	/**
	 * Media type for {@code application/atom+xml}.
	 */
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";

	/**
	 * Media type for {@code application/cbor}.
	 * @since 5.2
	 */
	public static final String APPLICATION_CBOR = "application/cbor";

	/**
	 * Media type for {@code application/x-www-form-urlencoded}.
	 */
	public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

	/**
	 * Media type for {@code application/graphql-response+json}.
	 * @since 6.0.3
	 * @see <a href="https://github.com/graphql/graphql-over-http">GraphQL over HTTP spec</a>
	 */
	public static final String APPLICATION_GRAPHQL_RESPONSE = "application/graphql-response+json";


	/**
	 * Media type for {@code application/json}.
	 */
	public static final String APPLICATION_JSON = "application/json";

	/**
	 * Media type for {@code application/octet-stream}.
	 */
	public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

	/**
	 * Media type for {@code application/pdf}.
	 * @since 4.3
	 */
	public static final String APPLICATION_PDF = "application/pdf";

	/**
	 * Media type for {@code application/problem+json}.
	 * @since 5.0
	 * @see <a href="https://www.iana.org/assignments/media-types/application/problem+json">
	 *     Problem Details for HTTP APIs, 6.1. application/problem+json</a>
	 */
	public static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

	/**
	 * Media type for {@code application/problem+xml}.
	 * @since 5.0
	 * @see <a href="https://www.iana.org/assignments/media-types/application/problem+xml">
	 *     Problem Details for HTTP APIs, 6.2. application/problem+xml</a>
	 */
	public static final String APPLICATION_PROBLEM_XML = "application/problem+xml";

	/**
	 * Media type for {@code application/x-protobuf}.
	 * @since 6.0
	 */
	public static final String APPLICATION_PROTOBUF = "application/x-protobuf";

	/**
	 * Media type for {@code application/rss+xml}.
	 * @since 4.3.6
	 */
	public static final String APPLICATION_RSS_XML = "application/rss+xml";

	/**
	 * Media type for {@code application/x-ndjson}.
	 * @since 5.3
	 */
	public static final String APPLICATION_NDJSON = "application/x-ndjson";

	/**
	 * Media type for {@code application/xhtml+xml}.
	 */
	public static final String APPLICATION_XHTML_XML = "application/xhtml+xml";

	/**
	 * Media type for {@code application/xml}.
	 */
	public static final String APPLICATION_XML = "application/xml";

	/**
	 * Media type for {@code application/yaml}.
	 * @since 6.2
	 */
	public static final String APPLICATION_YAML = "application/yaml";

	/**
	 * Media type for {@code image/gif}.
	 */
	public static final String IMAGE_GIF = "image/gif";

	/**
	 * Media type for {@code image/jpeg}.
	 */
	public static final String IMAGE_JPEG = "image/jpeg";

	/**
	 * Media type for {@code image/png}.
	 */
	public static final String IMAGE_PNG = "image/png";

	/**
	 * Media type for {@code multipart/form-data}.
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/**
	 * Media type for {@code multipart/mixed}.
	 * @since 5.2
	 */
	public static final String MULTIPART_MIXED = "multipart/mixed";

	/**
	 * Media type for {@code multipart/related}.
	 * @since 5.2.5
	 */
	public static final String MULTIPART_RELATED = "multipart/related";

	/**
	 * Media type for {@code text/event-stream}.
	 * @since 4.3.6
	 * @see <a href="https://html.spec.whatwg.org/multipage/server-sent-events.html">Server-Sent Events</a>
	 */
	public static final String TEXT_EVENT_STREAM = "text/event-stream";

	/**
	 * Media type for {@code text/html}.
	 */
	public static final String TEXT_HTML = "text/html";

	/**
	 * Media type for {@code text/markdown}.
	 * @since 4.3
	 */
	public static final String TEXT_MARKDOWN = "text/markdown";

	/**
	 * Media type for {@code text/plain}.
	 */
	public static final String TEXT_PLAIN = "text/plain";

	/**
	 * Media type for {@code text/xml}.
	 */
	public static final String TEXT_XML = "text/xml";

}
