/**
 * 
 */
package rs.baselib.licensing;

import java.io.Serializable;

/**
 * The interface a license has to fulfill.
 * @author ralph
 *
 */
public interface ILicense extends Serializable {

	/**
	 * Initialize the license using the given context.
	 * @param context context containing parameters.
	 */
	public void init(ILicenseContext context);
	
	/**
	 * Verify the license using the given context
	 * @param context the context containing verification parameters
	 */
	public void verify(ILicenseContext context);
	
}
