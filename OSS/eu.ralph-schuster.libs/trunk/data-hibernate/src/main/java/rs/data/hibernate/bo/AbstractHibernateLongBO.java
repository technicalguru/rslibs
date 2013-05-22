/**
 * 
 */
package rs.data.hibernate.bo;

import rs.data.impl.dto.GeneralDTO;

/**
 * Hibernate BO for Long based keys.
 * @author ralph
 *
 */
public abstract class AbstractHibernateLongBO<T extends GeneralDTO<Long>> extends AbstractHibernateBO<Long,T> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -7115538745412431772L;

	/**
	 * Constructor.
	 */
	public AbstractHibernateLongBO() {
	}

	/**
	 * Constructor.
	 * @param transferObject
	 */
	public AbstractHibernateLongBO(T transferObject) {
		super(transferObject);
	}

}
