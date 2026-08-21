package rs.mail.templates.impl;

import rs.mail.templates.BuilderResult;

/**
 * A message creator translates the result from the template building process into
 * an actual message.
 * 
 * @author ralph
 *
 */
public interface MessageCreator<T> {

	/**
	 * Create the message from the builder result.
	 * 
	 * @param result result of the building process
	 * @return the message
	 * @throws Exception when the creation fails
	 */
	public T create(BuilderResult result) throws Exception;
}
