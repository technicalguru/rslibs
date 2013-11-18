/**
 * 
 */
package rs.baselib.util;

import java.net.MalformedURLException;
import java.net.URL;

import rs.baselib.util.IUrlProvider;

/**
 * Wraps an {@link IUrlProvider} into an {@link IUrlTransformer}.
 * 
 * @author ralph
 *
 */
public class UrlProviderWrapper implements IUrlTransformer {

	private IUrlProvider provider;
	
	/**
	 * Constructor.
	 */
	public UrlProviderWrapper(IUrlProvider provider) {
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL toURL(String url) throws MalformedURLException {
		return provider.getUrl();
	}

}
