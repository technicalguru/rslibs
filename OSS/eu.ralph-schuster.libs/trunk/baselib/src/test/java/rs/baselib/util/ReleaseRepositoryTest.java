/**
 * 
 */
package rs.baselib.util;

import java.util.Collection;

import org.junit.Test;

import rs.baselib.util.ReleaseInformation;
import rs.baselib.util.ReleaseRepository;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the release information.
 * @author ralph
 *
 */
public class ReleaseRepositoryTest {

	/**
	 * Test method for {@link rs.baselib.util.ReleaseRepository#getVersionInfos(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetVersionInfos() {
		Collection<ReleaseInformation> infos = ReleaseRepository.INSTANCE.getVersionInfos(ReleaseRepository.BASELIB_GROUP_ID, ReleaseRepository.BASELIB_ARTIFACT_ID);
		assertNotNull("No versions found", infos);
		if (infos.size() == 0) infos = ReleaseRepository.INSTANCE.getVersionInfos(ReleaseRepository.BASELIB_GROUP_ID, ReleaseRepository.BUNDLE_ARTIFACT_ID);
		assertTrue("No release information for baselib", infos.size() > 0);
		for (ReleaseInformation info : infos) {
			assertTrue("Wrong SVN Repository", info.getSvnRepository().indexOf("h2031995.stratoserver.net") > 0 || info.getSvnPath().contains("eu.ralph-schuster.baselib"));
		}
	}

}
