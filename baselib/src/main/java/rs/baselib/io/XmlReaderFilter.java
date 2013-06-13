/**
 * 
 */
package rs.baselib.io;

import java.io.Reader;

/**
 * Filters invalid XML characters from a stream.
 * @author ralph
 *
 */
public class XmlReaderFilter extends AbstractReaderFilter {

	/**
	 * Constructor.
	 * @param in
	 */
	public XmlReaderFilter(Reader in) {
		super(in);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValidChar(char c) {
		return (c == 0x9) ||  (c == 0xA) ||  (c == 0xD) || 
				((c >= 0x20) && (c <= 0xD7FF)) || 
				((c >= 0xE000) && (c <= 0xFFFD)) || 
				((c >= 0x10000) && (c <= 0x10FFFF));
	}

}
