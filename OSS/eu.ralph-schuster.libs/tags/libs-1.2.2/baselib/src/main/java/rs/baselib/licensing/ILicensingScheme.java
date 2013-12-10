/**
 * 
 */
package rs.baselib.licensing;

/**
 * Describes a licensing scheme.
 * @author ralph
 *
 */
public interface ILicensingScheme {

	/**
	 * Returns an instance of the responsible license creator implementation.
	 * @return the license creator
	 */
	public ILicenseCreator getLicenseCreator();
	
	/**
	 * Returns an instance of the responsible license verifier implementation.
	 * @return the license verifier
	 */
	public ILicenseVerifier getLicenseVerifier();
	
}
