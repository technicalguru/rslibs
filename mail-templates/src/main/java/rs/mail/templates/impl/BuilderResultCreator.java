package rs.mail.templates.impl;

import rs.mail.templates.BuilderResult;

/**
 * A simple {@link MessageCreator} that just passes on the {@link BuilderResult}.
 * 
 * @author ralph
 *
 */
public class BuilderResultCreator implements MessageCreator<BuilderResult> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BuilderResult create(BuilderResult result) throws Exception {
		return result;
	}

	
}
