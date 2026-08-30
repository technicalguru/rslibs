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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.collections4.MultiValuedMap;

/**
 * URI utilities provide ways to encode parts o f an URI.
 * @author ralph
 *
 */
public class UriUtils {

    /**
     * Encodes a path according to RFC 3986 (/) will be preserved.
     * @param path the path to encode
     * @return the encoded path
     */
    public static String encodePath(String path) {
        if (path == null) return null;
        try {
            URI uri = new URI(null, null, path, null);
            return uri.getRawPath();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not encode path: " + path, e);
        }
    }
    
    /**
     * Enocdes the auth info in a URI
     * @param s the auth information (user[:password])
     * @return the encoded info
     */
	public static String encodeAuthInfo(String s) {
		if (s == null) return null;
		try {
			URI uri = new URI(null, s, "dummy.com", -1, null, null, null);
	        return uri.getRawUserInfo();
	    } catch (URISyntaxException e) {
    		throw new RuntimeException("Cannot encode authorization: "+s);
    	}	    
	}

	/**
	 * Encode the query parameters.
	 * @param queryParams query parameters
	 * @return the encoded parameters
	 */
	public static String encodeQuery(MultiValuedMap<String, String> queryParams) {
		if (queryParams == null || queryParams.isEmpty()) {
			return null;
		}

		StringBuilder rc = new StringBuilder();
		for (String name : queryParams.keySet()) {
			for (String value : queryParams.get(name)) {
				if (rc.length() > 0) {
					rc.append('&');
				}
				rc.append(encodeQueryParamPart(name));
				if (!CommonUtils.isEmpty(value)) rc.append('=').append(encodeQueryParamPart(value));
			}
		}
		return rc.toString();
	}

	/**
	 * Decodes a query parameter (name or value).
	 * @param s the query parameter part to decode
	 * @return the decoded parameter part
	 */
	public static String decodeQueryParamPart(String s) {
		return URLDecoder.decode(s, StandardCharsets.UTF_8);
	}
	
	/**
	 * Encodes a query parameter (name or value).
	 * @param s the query parameter part to encode
	 * @return the encoded parameter part
	 */
	public static String encodeQueryParamPart(String s) {
		if (s == null) return null;
		return URLEncoder.encode(s, StandardCharsets.UTF_8);
	}

	/**
	 * Encodes a fragment in a URI
	 * @param s the fragment (no leading hash)
	 * @return the encoded fragment
	 */
    public static String encodeFragment(String s) {
    	if (s == null) return null;
    	try {
	    	URI uri = new URI(null, null, null, null, s);
	        return uri.getRawFragment();
	    } catch (URISyntaxException e) {
    		throw new RuntimeException("Cannot encode fragment");
    	}
    }

}
