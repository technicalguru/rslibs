package rs.baselib.test;

/**
 * Selects values from an enumeration (randomly).
 * @author ralph
 *
 */
public class EnumerationBuilder<T extends Enum<T>> implements Builder<T> {

	private T[] values;
	private T   last;
	
	/**
	 * Constructor.
	 * @param clazz the enumeration class
	 */
	public EnumerationBuilder(Class<T> clazz) {
		this.values = clazz.getEnumConstants();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T build() {
		last = values[BuilderUtils.RNG.nextInt(0, values.length)];
		return last;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T last() {
		return last;
	}


}
