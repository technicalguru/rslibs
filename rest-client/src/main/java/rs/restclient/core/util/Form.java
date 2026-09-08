package rs.restclient.core.util;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

/**
 * @author ralph
 *
 */
public class Form {


	private final MultiValuedMap<String, String> parameters;

    /**
     * Create a new form data instance.
     * <p>
     * The underlying form parameter store is configured to preserve the insertion order of the parameters. I.e. parameters
     * can be iterated in the same order as they were inserted into the {@code Form}.
     * </p>
     */
    public Form() {
        this(new ArrayListValuedHashMap<String, String>());
    }

    /**
     * Create a new form data instance with a single parameter entry.
     * <p>
     * The underlying form parameter store is configured to preserve the insertion order of the parameters. I.e. parameters
     * can be iterated in the same order as they were inserted into the {@code Form}.
     * </p>
     *
     * @param parameterName form parameter name.
     * @param parameterValue form parameter value.
     */
    public Form(final String parameterName, final String parameterValue) {
        this();

        parameters.put(parameterName, parameterValue);
    }

    /**
     * Create a new form data instance and register a custom underlying parameter store.
     * <p>
     * This method is useful in situations when a custom parameter store is needed in order to change the default parameter
     * iteration order, improve performance or facilitate other custom requirements placed on the parameter store.
     * </p>
     *
     * @param store form data store used by the created form instance.
     */
    public Form(final MultiValuedMap<String, String> store) {
        this.parameters = store;
    }

    /**
     * Adds a new value to the specified form parameter.
     *
     * @param name name of the parameter.
     * @param value new parameter value to be added.
     * @return updated {@code Form} instance.
     */
    public Form param(final String name, final String value) {
        parameters.put(name, value);

        return this;
    }

    /**
     * Returns multivalued map representation of the form.
     *
     * @return form represented as multivalued map.
     * @see MultivaluedMap
     */
    public MultiValuedMap<String, String> asMap() {
        return parameters;
    }

    /**
     * Compare for equality
     * 
     * @param object the object to compare to.
     * @return {@code true}, if the object is a {@code Form} with the same form parameters, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (getClass() != object.getClass()) return false;
        Form form = (Form) object;
        return parameters.equals(form.parameters);
    }

    /**
     * Returns the hashCode of the underlying form parameters.
     * 
     * @return a hash code value for the underlying form parameters.
     */
    @Override
    public int hashCode() {
        return parameters.hashCode();
    }
}
