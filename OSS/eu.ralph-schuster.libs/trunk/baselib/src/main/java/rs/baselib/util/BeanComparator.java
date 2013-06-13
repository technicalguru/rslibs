/**
 * 
 */
package rs.baselib.util;

import java.util.Comparator;

/**
 * A comparator based on a list of bean properties.
 * The comparison will be null-safe and exception-safe.
 * 
 * @author ralph
 *
 */
public class BeanComparator<T> implements Comparator<T> {

	/** The comparator being used */
	private Comparator<Object> comparator;
	/** The value providers */
	private ValueProvider valueProviders[];

	/**
	 * Constructor.
	 * @param beanProperties the properties in order of priority.
	 */
	public BeanComparator(String... beanProperties) {
		this(null, BeanValueProvider.getValueProviders(beanProperties));
	}

	/**
	 * Constructor.
	 * @param comparator the comparator to be used
	 * @param beanProperties the properties in order of priority.
	 */
	public BeanComparator(Comparator<Object> comparator, String... beanProperties) {
		this(comparator, BeanValueProvider.getValueProviders(beanProperties));
	}

	/**
	 * Constructor.
	 * @param valueProviders the value providers for each of the properties to be compared
	 */
	public BeanComparator(ValueProvider... valueProviders) {
		this(null, valueProviders);
	}

	/**
	 * Constructor.
	 * @param comparator the comparator to be used
	 * @param valueProviders the value providers for each of the properties to be compared
	 */
	public BeanComparator(Comparator<Object> comparator, ValueProvider... valueProviders) {
		this.comparator = comparator;
		this.valueProviders = valueProviders;
	}

	/**
	 * Returns the comparator used to compare the values.
	 * @return the comparator
	 */
	public Comparator<Object> getComparator() {
		return comparator != null ? comparator : DefaultComparator.INSTANCE;
	}

	/**
	 * Returns the value providers of this comparator.
	 * @return providers that will be used
	 */
	public ValueProvider[] getValueProviders() {
		return valueProviders;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(T o1, T o2) {
		for (ValueProvider provider : getValueProviders()) {
			Object v1 = o1;
			Object v2 = o2;
			if (provider != null) {
				v1 = provider.getValue(o1);
				v2 = provider.getValue(o2);
			}
			int cmp = getComparator().compare(v1, v2);
			if (cmp != 0) return cmp;
		}
		return 0;
	}


}
