/**
 * 
 */
package rs.data.hibernate.dao;

import rs.data.api.bo.GeneralBO;
import rs.data.impl.bo.AbstractBO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for Long based BOs.
 * @author ralph
 *
 */
public class AbstractHibernateLongDAO<T extends GeneralDTO<Long>, B extends  AbstractBO<Long,T>, C extends GeneralBO<Long>> extends AbstractHibernateDAO<Long, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractHibernateLongDAO() {
	}

}
