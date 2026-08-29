package rs.restclient.core.api.request;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.multimap.UnmodifiableMultiValuedMap;

/**
 * To reuse in various classes for the same reason.
 * @author ralph
 *
 */
public class QueryParamsSpec {

    protected MultiValuedMap<String, Object> params;

	/**
	 * Copy the spec
	 * @param spec the spec to copy
	 */
	public QueryParamsSpec(QueryParamsSpec spec) {
		this(spec.getParams());
	}

	/**
	 * Creates an empty spec.
	 */
	public QueryParamsSpec() {
		this(new ArrayListValuedHashMap<>());
	}

	/**
	 * Constructor with predefined headers and cookies.
	 * @param params params to be used
	 */
	public QueryParamsSpec(MultiValuedMap<String, Object> params) {
		this.params = new ArrayListValuedHashMap<>(params);
	}
	
	/**
	 * Returns the params (cannot be modified).
	 * @return the params
	 */
	public MultiValuedMap<String, Object> getParams() {
		return UnmodifiableMultiValuedMap.unmodifiableMultiValuedMap(params);
	}

	public void add(String name, Object ...values) {
		for (Object value : values) {
			if (value != null) params.put(name, value);
		}
	}
	
	public void add(MultiValuedMap<String, Object> params) {
		this.params.putAll(params);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "QueryParamsSpec [params=" + params + "]";
	}
	


}
