package rs.restclient.core.api.request;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * A default implementation of the Variant specification in case an implementation
 * does not provide its own version of it.
 */
public class Variant {
    private Locale language;
    private String mediaType;
    private String encoding;

    /**
     * Create a new instance of Variant.
     *
     * @param mediaType the media type of the variant - may be {@code null}.
     * @param language the language of the variant (two-letter ISO-639 code); may be {@code null}.
     * @param encoding the content encoding of the variant - may be {@code null}.
     * @throws java.lang.IllegalArgumentException if all the parameters are {@code null}.
     * @since 2.0
     */
    public Variant(final String mediaType, final String language, final String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : Locale.of(language);
        this.mediaType = mediaType;
    }

    /**
     * Create a new instance of Variant.
     *
     * @param mediaType the media type of the variant - may be {@code null}.
     * @param language the language of the variant (two-letter ISO-639 code); may be {@code null}.
     * @param country uppercase two-letter ISO-3166 language code of the variant; may be {@code null} provided
     * {@code language} is {@code null} too.
     * @param encoding the content encoding of the variant - may be {@code null}.
     * @throws java.lang.IllegalArgumentException if all the parameters are {@code null}.
     * @since 2.0
     */
    public Variant(final String mediaType, final String language, final String country, final String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : Locale.of(language, country);
        this.mediaType = mediaType;
    }

    /**
     * Create a new instance of Variant.
     *
     * @param mediaType the media type of the variant - may be {@code null}.
     * @param language the language of the variant (two-letter ISO-639 code); may be {@code null}.
     * @param country uppercase two-letter ISO-3166 language code of the variant; may be {@code null} provided
     * {@code language} is {@code null} too.
     * @param languageVariant vendor and browser specific language code of the variant (see also {@link Locale} class
     * description); may be {@code null} provided {@code language} and {@code country} are {@code null} too.
     * @param encoding the content encoding of the variant - may be {@code null}.
     * @throws java.lang.IllegalArgumentException if all the parameters are {@code null}.
     * @since 2.0
     */
    public Variant(final String mediaType, final String language, final String country, final String languageVariant, final String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = (language == null) ? null : Locale.of(language, country, languageVariant);
        this.mediaType = mediaType;
    }

    /**
     * Create a new instance of Variant.
     *
     * @param mediaType the media type of the variant - may be {@code null}.
     * @param language the language of the variant - may be {@code null}.
     * @param encoding the content encoding of the variant - may be {@code null}.
     * @throws java.lang.IllegalArgumentException if all the parameters are {@code null}.
     */
    public Variant(final String mediaType, final Locale language, final String encoding) {
        if (mediaType == null && language == null && encoding == null) {
            throw new IllegalArgumentException("mediaType, language, encoding all null");
        }
        this.encoding = encoding;
        this.language = language;
        this.mediaType = mediaType;
    }

    /**
     * Get the language of the variant.
     *
     * @return the language or {@code null} if none set.
     */
    public Locale getLanguage() {
        return language;
    }

    /**
     * Get the string representation of the variant language, or {@code null} if no language has been set.
     *
     * @return the string representing variant language or {@code null} if none set.
     * @since 2.0
     */
    public String getLanguageString() {
        return (language == null) ? null : language.toString();
    }

    /**
     * Get the media type of the variant.
     *
     * @return the media type or {@code null} if none set.
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Get the encoding of the variant.
     *
     * @return the encoding or {@code null} if none set.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Create a {@link VariantListBuilder} initialized with a set of supported media types.
     *
     * @param mediaTypes the available mediaTypes. If specific char-sets are supported they should be included as parameters
     * of the respective media type.
     * @return the initialized builder.
     * @throws java.lang.IllegalArgumentException if mediaTypes is null or contains no elements.
     */
    public static VariantListBuilder mediaTypes(final String... mediaTypes) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.mediaTypes(mediaTypes);
        return b;
    }

    /**
     * Create a {@link VariantListBuilder} initialized with a set of supported languages.
     *
     * @param languages the available languages.
     * @return the initialized builder.
     * @throws java.lang.IllegalArgumentException if languages is null or contains no elements.
     */
    public static VariantListBuilder languages(final Locale... languages) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.languages(languages);
        return b;
    }

    /**
     * Create a {@link VariantListBuilder} initialized with a set of supported encodings.
     *
     * @param encodings the available encodings.
     * @return the initialized builder.
     * @throws java.lang.IllegalArgumentException if encodings is null or contains no elements.
     */
    public static VariantListBuilder encodings(final String... encodings) {
        VariantListBuilder b = VariantListBuilder.newInstance();
        b.encodings(encodings);
        return b;
    }

    /**
     * Generate hash code from variant properties.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.language, this.mediaType, this.encoding);
    }

    /**
     * Compares obj to this variant to see if they are the same considering all property values.
     *
     * @param obj the object to compare to.
     * @return true if the two variants are the same, false otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Variant other = (Variant) obj;
        if (!Objects.equals(this.language, other.language)) {
            return false;
        }
        if (!Objects.equals(this.mediaType, other.mediaType)) {
            return false;
        }
        return Objects.equals(this.encoding, other.encoding);
    }

    @Override
    public String toString() {
        StringWriter w = new StringWriter();
        w.append("Variant[mediaType=");
        w.append(mediaType == null ? "null" : mediaType.toString());
        w.append(", language=");
        w.append(language == null ? "null" : language.toString());
        w.append(", encoding=");
        w.append(encoding == null ? "null" : encoding);
        w.append("]");
        return w.toString();
    }

    /**
     * A builder for a list of representation variants.
     */
    public static class VariantListBuilder {

       	private List<Locale>    languages  = new ArrayList<>();
       	private List<String>    encodings  = new ArrayList<>();
       	private List<String>    mediaTypes = new ArrayList<>();
    	
       	private List<Variant>   added      = new ArrayList<>();
       	
        /**
         * Protected constructor, use the static {@code newInstance} method to obtain an instance.
         */
        protected VariantListBuilder() {
        }

        /**
         * Create a new builder instance.
         *
         * @return a new builder instance.
         */
        public static VariantListBuilder newInstance() {
            return new VariantListBuilder();
        }

        /**
         * Add the current combination of metadata to the list of supported variants (provided the current combination of
         * metadata is not empty) and build a list of representation variants from the current state of the builder. After this
         * method is called the builder is reset to an empty state.
         *
         * @return a list of representation variants.
         */
        public List<Variant> build() {
        	add();
        	List<Variant> rc = new ArrayList<>(added);
        	added.clear();
        	return rc;
        }

        /**
         * Add the current combination of metadata to the list of supported variants, after this method is called the current
         * combination of metadata is emptied.
         * <p>
         * If more than one value is supplied for one or more of the variant properties then a variant will be generated for
         * each possible combination. E.g. in the following {@code list} would have five (4 + 1) members:
         * </p>
         *
         * <pre>
         * List&lt;Variant&gt; list = VariantListBuilder.newInstance()
         *         .languages(Locale.ENGLISH, Locale.FRENCH).encodings("zip", "identity").add()
         *         .languages(Locale.GERMAN).mediaTypes(MediaType.TEXT_PLAIN_TYPE).add()
         *         .build()
         * </pre>
         * <p>
         * Note that it is not necessary to call the {@code add()} method immediately before the build method is called. E.g.
         * the resulting list produced in the example above would be identical to the list produced by the following code:
         * </p>
         *
         * <pre>
         * List&lt;Variant&gt; list = VariantListBuilder.newInstance()
         *         .languages(Locale.ENGLISH, Locale.FRENCH).encodings("zip", "identity").add()
         *         .languages(Locale.GERMAN).mediaTypes(MediaType.TEXT_PLAIN_TYPE)
         *         .build()
         * </pre>
         *
         * @return the updated builder.
         * @throws IllegalStateException if there is not at least one mediaType, language or encoding set for the current
         * variant.
         */
        public VariantListBuilder add() {
        	for (Locale l : languages) {
        		for (String s : encodings) {
        			for (String t : mediaTypes) {
        				added.add(new Variant(t, l, s));
        			}
        		}
        	}
        	languages.clear();
        	encodings.clear();
        	mediaTypes.clear();
        	return this;
        }

        /**
         * Set the language(s) for this variant.
         *
         * @param languages the available languages.
         * @return the updated builder.
         */
        public VariantListBuilder languages(Locale... languages) {
        	for (Locale l : languages) this.languages.add(l);
        	return this;
        }

        /**
         * Set the encoding(s) for this variant.
         *
         * @param encodings the available encodings.
         * @return the updated builder.
         */
        public VariantListBuilder encodings(String... encodings) {
        	for (String s : encodings) this.encodings.add(s);
        	return this;
        }

        /**
         * Set the media type(s) for this variant.
         *
         * @param mediaTypes the available mediaTypes. If specific charsets are supported they should be included as parameters
         * of the respective media type.
         * @return the updated builder.
         */
        public VariantListBuilder mediaTypes(String... mediaTypes) {
        	for (String t : mediaTypes) this.mediaTypes.add(t);
        	return this;
        }
    }

}
