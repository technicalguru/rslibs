package rs.baselib.test;

import java.util.UUID;

/**
 * Builder for UUIDs.
 * @author ralph
 *
 */
public class UuidBuilder extends AbstractBuilder<UUID> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UUID _build() {
		return UUID.randomUUID();
	}


}
