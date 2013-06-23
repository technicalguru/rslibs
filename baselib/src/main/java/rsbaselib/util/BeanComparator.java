/**
 * 
 */
package rsbaselib.util;

import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A comparator based on a bean property.
 * @author ralph
 *
 */
public class BeanComparator<T> implements Comparator<T> {

	private static Logger log = LoggerFactory.getLogger(BeanComparator.class);
	
	/** The bean property to compare */
	private String beanProperty;
	
	/**
	 * Constructor.
	 */
	public BeanComparator(String beanProperty) {
		this.beanProperty = beanProperty;
	}

	/**
	 * Returns the bean property of this comparator.
	 * @return
	 */
	public String getBeanProperty() {
		return beanProperty;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T o1, T o2) {
		Object v1 = null;
		Object v2 = null;
		if (o1 != null) try {
			v1 = PropertyUtils.getProperty(o1, getBeanProperty());
		} catch (Throwable t) {
			log.error("Cannot get property \""+getBeanProperty()+"\" of bean: "+o1);
		}
		if (o2 != null) try {
			v2 = PropertyUtils.getProperty(o2, getBeanProperty());
		} catch (Throwable t) {
			log.error("Cannot get property \""+getBeanProperty()+"\" of bean: "+o2);
		}
		return DefaultComparator.INSTANCE.compare(v1, v2);
	}

	
}
