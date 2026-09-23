package rs.baselib.test;

/**
 * Helper builder to produce the same value all the time.
 * @author ralph
 *
 */
public class StaticBuilder<T> extends AbstractBuilder<T> {

	private T value;
	
	/**
	 * Constructor.
	 * @param value
	 */
	public StaticBuilder(T value) {
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T _build() {
		return value;
	}

}
