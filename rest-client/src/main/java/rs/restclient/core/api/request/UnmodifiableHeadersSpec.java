package rs.restclient.core.api.request;

import java.net.HttpCookie;
import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Unmodifiable;

/**
 * Creates unmodifiable version of a {@link HeadersSpec}.
 * 
 * @author ralph
 *
 */
public final class UnmodifiableHeadersSpec extends HeadersSpec implements Unmodifiable {

	/**
	 * Returns an unmodifiable version of the headers.
	 * @param headersSpec the headers 
	 * @return the unmodifiable version
	 */
	public static HeadersSpec unmodifiableHeadersSpec(HeadersSpec headersSpec) {
		return new UnmodifiableHeadersSpec(headersSpec);
	}
	
	/**
	 * Constructor.
	 * @param headersSpec
	 */
	private UnmodifiableHeadersSpec(HeadersSpec headersSpec) {
		super(headersSpec);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCookie(String name, String cookie) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCookie(String cookie) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCookies(Collection<HttpCookie> cookies) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCookie(HttpCookie cookie) {
		throw new UnsupportedOperationException();
	}
	
	
}
