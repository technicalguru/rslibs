/*
 * This file is part of RS Library (Data Hibernate Library).
 *
 *  RS Library is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  RS Library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with RS Library.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package rs.data.hibernate.dao;

import rs.data.api.bo.IGeneralBO;
import rs.data.hibernate.bo.AbstractHibernateBO;
import rs.data.impl.dto.GeneralDTO;

/**
 * Abstract implementation for Long based BOs.
 * @author ralph
 *
 */
public class AbstractHibernateLongDAO<T extends GeneralDTO<Long>, B extends  AbstractHibernateBO<Long,T>, C extends IGeneralBO<Long>> extends AbstractHibernateDAO<Long, T, B, C> {

	/**
	 * Constructor.
	 */
	public AbstractHibernateLongDAO() {
	}

}
