package rs.restclient.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import org.springframework.http.MediaType;


/**
 * Encapsulates message entity including the associated variant information.
 *
 * @param <T> entity type.
 * @author Marek Potociar
 */
public class Entity<T> {
    private static final Annotation[] EMPTY_ANNOTATIONS = new Annotation[0];

    private final T entity;
    private final Variant variant;
    private final Annotation[] annotations;

    /**
     * Create an entity using a supplied content media type.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @param mediaType entity content type.
     * @return entity instance.
     */
    public static <T> Entity<T> entity(final T entity, final MediaType mediaType) {
        return new Entity<T>(entity, mediaType);
    }

    /**
     * Create an entity using a supplied content media type.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @param mediaType entity content type.
     * @param annotations entity annotations.
     * @return entity instance.
     */
    public static <T> Entity<T> entity(final T entity, final MediaType mediaType, final Annotation[] annotations) {
        return new Entity<T>(entity, mediaType, annotations);
    }

    /**
     * Create an entity using a supplied content media type.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @param mediaType entity content type.
     * @return entity instance.
     * @throws IllegalArgumentException if the supplied string cannot be parsed or is {@code null}.
     */
    public static <T> Entity<T> entity(final T entity, final String mediaType) {
        return new Entity<T>(entity, MediaType.valueOf(mediaType));
    }

    /**
     * Create an entity using a supplied content media type.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @param variant entity {@link Variant variant} information.
     * @return entity instance.
     */
    public static <T> Entity<T> entity(final T entity, final Variant variant) {
        return new Entity<T>(entity, variant);
    }

    /**
     * Create an entity using a supplied content media type.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @param variant entity {@link Variant variant} information.
     * @param annotations entity annotations.
     * @return entity instance.
     */
    public static <T> Entity<T> entity(final T entity, final Variant variant, final Annotation[] annotations) {
        return new Entity<T>(entity, variant, annotations);
    }

    /**
     * Create a {@value jakarta.ws.rs.core.MediaType#TEXT_PLAIN} entity.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @return {@value jakarta.ws.rs.core.MediaType#TEXT_PLAIN} entity instance.
     */
    public static <T> Entity<T> text(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_PLAIN);
    }

    /**
     * Create an {@value jakarta.ws.rs.core.MediaType#APPLICATION_XML} entity.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @return {@value jakarta.ws.rs.core.MediaType#APPLICATION_XML} entity instance.
     */
    public static <T> Entity<T> xml(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_XML);
    }

    /**
     * Create an {@value jakarta.ws.rs.core.MediaType#APPLICATION_JSON} entity.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @return {@value jakarta.ws.rs.core.MediaType#APPLICATION_JSON} entity instance.
     */
    public static <T> Entity<T> json(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_JSON);
    }

    /**
     * Create a {@value jakarta.ws.rs.core.MediaType#TEXT_HTML} entity.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @return {@value jakarta.ws.rs.core.MediaType#TEXT_HTML} entity instance.
     */
    public static <T> Entity<T> html(final T entity) {
        return new Entity<T>(entity, MediaType.TEXT_HTML);
    }

    /**
     * Create an {@value jakarta.ws.rs.core.MediaType#APPLICATION_XHTML_XML} entity.
     *
     * @param <T> entity Java type.
     * @param entity entity data.
     * @return {@value jakarta.ws.rs.core.MediaType#APPLICATION_XHTML_XML} entity instance.
     */
    public static <T> Entity<T> xhtml(final T entity) {
        return new Entity<T>(entity, MediaType.APPLICATION_XHTML_XML);
    }

    private Entity(final T entity, final MediaType mediaType) {
        this(entity, new Variant(mediaType, (Locale) null, null), null);
    }

    private Entity(final T entity, final Variant variant) {
        this(entity, variant, null);
    }

    private Entity(final T entity, final MediaType mediaType, final Annotation[] annotations) {
        this(entity, new Variant(mediaType, (Locale) null, null), annotations);
    }

    private Entity(final T entity, final Variant variant, final Annotation[] annotations) {
        this.entity = entity;
        this.variant = variant;

        this.annotations = (annotations == null) ? EMPTY_ANNOTATIONS : annotations;
    }

    /**
     * Get entity {@link Variant variant} information.
     *
     * @return entity variant information.
     */
    public Variant getVariant() {
        return variant;
    }

    /**
     * Get entity media type.
     *
     * @return entity media type.
     */
    public MediaType getMediaType() {
        return variant.getMediaType();
    }

    /**
     * Get entity encoding.
     *
     * @return entity encoding.
     */
    public String getEncoding() {
        return variant.getEncoding();
    }

    /**
     * Get entity language.
     *
     * @return entity language.
     */
    public Locale getLanguage() {
        return variant.getLanguage();
    }

    /**
     * Get entity data.
     *
     * @return entity data.
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Get the entity annotations.
     *
     * @return entity annotations if set, an empty annotation array if no entity annotations have been specified.
     */
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entity)) {
            return false;
        }

        Entity<?> entity1 = (Entity<?>) o;

        if (!Objects.equals(annotations, entity1.annotations)) {
            return false;
        }
        if (!Objects.equals(entity, entity1.entity)) {
            return false;
        }
        if (!Objects.equals(variant, entity1.variant)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotations, entity, variant);
    }

    @Override
    public String toString() {
        return "Entity{" + "entity=" + entity + ", variant=" + variant
                + ", annotations=" + Arrays.toString(annotations) + '}';
    }

}
