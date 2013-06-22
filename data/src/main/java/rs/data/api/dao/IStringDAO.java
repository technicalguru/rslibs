/**
 * 
 */
package rs.data.api.dao;

import rs.data.api.bo.IStringBO;

/**
 * Data Access Object interface for {@link String} key-based DTOs and BOs.
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface IStringDAO<B extends IStringBO> extends IGeneralDAO<String, B> {

}
