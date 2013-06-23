/**
 * 
 */
package rs.data.api.dao;

import rs.data.api.bo.StringBO;

/**
 * Data Access Object interface for {@link String} key-based DTOs and BOs.
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface StringDAO<B extends StringBO> extends GeneralDAO<String, B> {

}
