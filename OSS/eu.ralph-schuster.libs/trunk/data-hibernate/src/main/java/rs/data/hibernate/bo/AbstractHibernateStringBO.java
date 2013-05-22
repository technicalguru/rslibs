/**
 * 
 */
package rs.data.hibernate.bo;

import rs.data.impl.dto.GeneralDTO;

/**
 * Hibernate BO for String based keys.
 * @author ralph
 *
 */
public abstract class AbstractHibernateStringBO<T extends GeneralDTO<String>> extends AbstractHibernateBO<String,T> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4550720926743247410L;

	/**
	 * Constructor.
	 */
	public AbstractHibernateStringBO() {
	}

	/**
	 * Constructor.
	 * @param transferObject
	 */
	public AbstractHibernateStringBO(T transferObject) {
		super(transferObject);
	}

}
