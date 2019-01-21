/**
 * Helper methods for licensing products.
 * <p>The classes generate and verify license strings using a public/private key pair which
 * clients must provide to these classes.</p>
 * <p>A license consist of a product ID which clients interprete themselves, validity date
 * and a license holder string. Latter is again subject to clients to define.</p>
 * <p>A license is generated using the private key which must be kept secret and shall never
 * be distributed to license holders or with applications. The {@link rs.baselib.licensing.LicenseGenerator}
 * is responsible for creating such licenses. The {@link rs.baselib.licensing.LicenseManager}
 * verifies the license using the public key of your key pair. This public key can be safely
 * distributed with applications to license holders.</p>
 * <p>This package does not provide any other infrastructure than this simple classes. Licenses
 * in general look like 8-character blocks, separated by dash characters. The number of blocks
 * is determined by the size of the license holder string. The general rule is that
 * 5 character (to be precise: bytes!) form an 8-character block in the license. The first
 * block is reserved for meta-data (product ID and validity date).</p> 
 * <p>This package is experimental yet. Usage is described on 
 * <a href="http://techblog.ralph-schuster.eu/rs-library/baselib/class-usage/#licensing">Class Usage Page</a></p>
 * @author ralph
 *
 */
package rs.baselib.licensing;