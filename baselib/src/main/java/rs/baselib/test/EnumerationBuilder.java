package rs.baselib.test;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Selects values from an enumeration (randomly).
 * @author ralph
 *
 */
public class EnumerationBuilder<T extends Enum<T>> implements Builder<T> {

	private T[] values;
	
	/**
	 * Constructor.
	 */
	public EnumerationBuilder(Class<T> clazz) {
		this.values = clazz.getEnumConstants();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T build() {
		return values[RandomUtils.nextInt(values.length)];
	}

	
}
