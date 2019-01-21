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
package rs.data.hibernate.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import rs.baselib.util.CommonUtils;

/**
 * Implementations for ease of use.
 * @author ralph
 * @since 1.3.2
 *
 */
public class HbmUtils {

	/**
	 * Adds the given sorting to the criteria.
	 * @param criteria - criteria to be extended
	 * @param sortBy - the order clauses, e.g. {@code column1 ASC, column2, column3 DESC}
	 * @since 1.3.2
	 */
	public static void addSortClauses(Criteria criteria, String sortBy) {
		Order orders[] = getOrderClauses(sortBy);
		if (orders != null) {
			for (Order o : orders) criteria.addOrder(o);
		}
	}
	
	/**
	 * Translates a comma separated order string ({@code column ASC, column, column DESC}) into order clauses.
	 * @param sortBy - the order clauses, e.g. {@code column1 ASC, column2, column3 DESC}
	 * @return the Order clauses
	 * @since 1.3.2
	 */
	public static Order[] getOrderClauses(String sortBy) {
		if (!CommonUtils.isEmpty(sortBy, true)) {
			String columns[] = sortBy.split(",");
			Order rc[] = new Order[columns.length];
			for (int i=0; i<columns.length; i++) {
				String parts[] = columns[i].trim().split(" ", 2);
				String name  = parts[0].trim();
				String order = parts.length>1 ? parts[1].toLowerCase().trim() : "asc";
				if ("asc".equals(order)) rc[i] = Order.asc(name);
				else rc[i] = Order.desc(name);
			}
			return rc;
		}
		return null;
	}
	

}
