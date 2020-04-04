/*
 * This file is part of RS Library (Base Library).
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
package rs.baselib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

/**
 * Tests the release information.
 * @author ralph
 *
 */
public class ReleaseRepositoryTest {

	/**
	 * Test method for {@link ReleaseRepository#getVersionInfos(java.lang.String, java.lang.String)}.
	 * <p>Currently deactivated due to GIT</p>
	 */
	// @Test
	public void testGetVersionInfos() {
		Collection<ReleaseInformation> infos = ReleaseRepository.INSTANCE.getVersionInfos(ReleaseRepository.BASELIB_GROUP_ID, ReleaseRepository.BASELIB_ARTIFACT_ID);
		assertNotNull("No versions found", infos);
		if (infos.size() == 0) infos = ReleaseRepository.INSTANCE.getVersionInfos(ReleaseRepository.BASELIB_GROUP_ID, ReleaseRepository.BUNDLE_ARTIFACT_ID);
		assertTrue("No release information for baselib", infos.size() > 0);
//		for (ReleaseInformation info : infos) {
//			assertTrue("Wrong SVN Repository", info.isSnapshot() || info.getSvnRepository().indexOf("svn.ralph-schuster.eu") > 0 || info.getSvnPath().contains("eu.ralph-schuster.baselib"));
//		}
	}

	/**
	 * Tests whether a MANIFEST info was loaded.
	 */
	@Test
	public void testManifestInfo() {
		Collection<ReleaseInformation> infos = ReleaseRepository.INSTANCE.getVersionInfos("javax.persistence", "Sun Java System Application Server");
		assertNotNull("No versions found", infos);
		assertEquals("No version found", 1, infos.size());
	}
}
