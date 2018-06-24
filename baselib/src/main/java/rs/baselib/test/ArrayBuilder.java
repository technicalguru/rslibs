package rs.baselib.test;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Creates random number of values from a builder.
 * @author ralph
 *
 */
public class ArrayBuilder<T extends Object> implements Builder<T[]> {

	private Builder<T> builder;
	private int min;
	private int max;
	
	/**
	 * Constructor.
	 * @param min - minimum number of objects to create
	 * @param max - maximum number of objects to create
	 * @param builder - builder to be used
	 */
	public ArrayBuilder(int min, int max, Builder<T> builder) {
		this.builder = builder;
		this.min = min;
		this.max = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] build() {
		int n = RandomUtils.nextInt(max-min)+min;
		@SuppressWarnings("unchecked")
		T rc[] = (T[]) new Object[n];
		for (int i=0; i<n; i++) rc[i] = builder.build();
		return rc;
	}

	
}