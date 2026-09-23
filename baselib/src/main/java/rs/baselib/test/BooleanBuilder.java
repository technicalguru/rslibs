package rs.baselib.test;

/**
 * Randomly creates true or false or specific value (cannot create NULL).
 * @author ralph
 *
 */
public class BooleanBuilder extends AbstractBuilder<Boolean> {

	private Boolean value;
	
	/**
	 * Build only with specific value.
	 * @param value - the boolean to produce
	 * @return this builder for concatenation
	 */
	public BooleanBuilder with(boolean value) {
		this.value = value;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean _build() {
		if (value != null) return value;
		return BuilderUtils.RNG.nextBoolean();
	}
}
