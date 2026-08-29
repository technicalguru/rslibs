package rs.restclient.core.api.request;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Unmodifiable;

/**
 * Creates unmodifiable version of a {@link QueryParamsSpec}.
 * 
 * @author ralph
 *
 */
public final class UnmodifiableQueryParamsSpec extends QueryParamsSpec implements Unmodifiable {

	/**
	 * Returns an unmodifiable version of the query params.
	 * @param paramsSpec the query params 
	 * @return the unmodifiable version
	 */
	public static QueryParamsSpec unmodifiableQueryParamsSpec(QueryParamsSpec paramsSpec) {
		return new UnmodifiableQueryParamsSpec(paramsSpec);
	}
	
	/**
	 * Constructor.
	 * @param paramsSpec the query params 
	 */
	private UnmodifiableQueryParamsSpec(QueryParamsSpec paramsSpec) {
		super(paramsSpec);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String name, Object... values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(MultiValuedMap<String, Object> headers) {
		throw new UnsupportedOperationException();
	}

}
