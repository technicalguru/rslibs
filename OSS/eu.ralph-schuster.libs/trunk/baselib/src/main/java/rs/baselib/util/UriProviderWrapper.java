/**
 * 
 */
package rs.baselib.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import rs.baselib.util.IUriProvider;

/**
 * Wraps an {@link IUriProvider} into an {@link IUrlTransformer}.
 * 
 * @author ralph
 *
 */
public class UriProviderWrapper implements IUrlTransformer {

	private IUriProvider provider;
	
	/**
	 * Constructor.
	 */
	public UriProviderWrapper(IUriProvider provider) {
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL toURL(String url) throws MalformedURLException {
		URI uri = provider.getUri();
		if (uri != null) return uri.toURL();
		return null;
	}

}
