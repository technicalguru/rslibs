/**
 * 
 */
package rsbaselib.util;

import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A comparator based on a list of bean properties.
 * The comparison will be null-safe and exception-safe.
 * 
 * @author ralph
 *
 */
public class BeanComparator<T> implements Comparator<T> {

	private static Logger log = LoggerFactory.getLogger(BeanComparator.class);

	/** The bean property to compare */
	private String beanProperties[];

	/**
	 * Constructor.
	 * @param beanProperties the properties in order of priority.
	 */
	public BeanComparator(String... beanProperties) {
		this.beanProperties = beanProperties;
	}

	/**
	 * Returns the bean property of this comparator.
	 * @return properties that will be compared
	 */
	public String[] getBeanProperties() {
		return beanProperties;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T o1, T o2) {
		for (String property : getBeanProperties()) {
			Object v1 = null;
			Object v2 = null;
			if (o1 != null) try {
				v1 = PropertyUtils.getProperty(o1, property);
			} catch (Throwable t) {
				log.error("Cannot get property \""+property+"\" of bean: "+o1);
			}
			if (o2 != null) try {
				v2 = PropertyUtils.getProperty(o2, property);
			} catch (Throwable t) {
				log.error("Cannot get property \""+property+"\" of bean: "+o2);
			}
			int cmp = DefaultComparator.INSTANCE.compare(v1, v2);
			if (cmp != 0) return cmp;
		}
		return 0;
	}


}
