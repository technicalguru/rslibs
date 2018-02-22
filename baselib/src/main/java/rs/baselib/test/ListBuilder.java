package rs.baselib.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Selects values from an enumeration (randomly)
 * @author ralph
 *
 */
public class ListBuilder<T> implements Builder<List<T>> {

	private Builder<T> builder;
	private int min;
	private int max;
	
	/**
	 * Constructor.
	 * @param min - minimum number of objects to create
	 * @param max - maximum number of objects to create
	 * @param builder - builder to be used
	 */
	public ListBuilder(int min, int max, Builder<T> builder) {
		this.builder = builder;
		this.min = min;
		this.max = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> build() {
		List<T> rc = new ArrayList<>();
		int n = RandomUtils.nextInt(max-min)+min;
		for (int i=0; i<n; i++) rc.add(builder.build());
		return rc;
	}

	
}
