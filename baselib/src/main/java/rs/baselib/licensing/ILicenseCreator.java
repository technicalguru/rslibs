/**
 * 
 */
package rs.baselib.licensing;


/**
 * Internal interface for implementations of license generators.
 * @author ralph
 *
 */
public interface ILicenseCreator {

	/**
	 * Generates a key from the given license.
	 * @param context the licensing context.
	 * @param license the license to be created.
	 * @return the license key
	 */
	public String createLicenseKey(ILicenseContext context, ILicense license);

}
