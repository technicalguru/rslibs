package rs.mail.templates;

/**
 * General resolver interface.
 * 
 * @author ralph
 *
 */
public interface Resolver<X> {

	/**
	 * Provides the object of the given name to be used in the given context.
	 * @param name - name of object
	 * @param context - context for usage
	 * @return the object instance suitable for the context or {@code null} when no such object can be found
	 * @throws ResolverException when resolving fails (e.g. loading the object)
	 */
	public X resolve(String name, TemplateContext context) throws ResolverException;

}
