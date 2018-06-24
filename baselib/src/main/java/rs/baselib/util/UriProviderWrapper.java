/*
 * This file is part of RS Library (Base Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
	 * @param provider the actual URI provider
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
