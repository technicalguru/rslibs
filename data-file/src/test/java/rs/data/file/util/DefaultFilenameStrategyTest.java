/*
 * This file is part of RS Library (Data File Library).
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
package rs.data.file.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the default filename strategy.
 * @author ralph
 *
 */
public class DefaultFilenameStrategyTest {

	private DefaultFilenameStrategy<Long> strategy;
	
	private Set<File> testFiles = new HashSet<File>();
	
	/**
	 * Creates the strategy with default parameters and 10 test files.
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		strategy = new DefaultFilenameStrategy<Long>();
		// Create 10 files
		for (long l=1; l<11; l++) {
			File f = strategy.getFile(l);
			f.createNewFile();
			testFiles.add(f);
		}
	}

	/**
	 * Removes test files and data directory.
	 */
	@After
	public void tearDown() {
		for (File f : testFiles) {
			f.delete();
		}
		new File(System.getProperty("user.dir"), IFilenameStrategy.DEFAULT_DATA_DIR).delete();		
	}
	
	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#getParentDir()}.
	 */
	@Test
	public void testGetParentDir() {
		assertEquals("default data dir not set correctly", IFilenameStrategy.DEFAULT_DATA_DIR, strategy.getParentDir().getName());
		assertEquals("Data dir not set correctly", IFilenameStrategy.DEFAULT_DATA_DIR, strategy.getFile(1L).getParentFile().getName());
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#setParentDir(java.io.File)}.
	 */
	@Test
	public void testSetParentDir() {
		strategy.setParentDir(new File(System.getProperty("user.dir"), "dataDir"));
		assertEquals("Data dir not set correctly", "dataDir", strategy.getParentDir().getName());
		assertEquals("Data dir not set correctly", "dataDir", strategy.getFile(1L).getParentFile().getName());
		new File(System.getProperty("user.dir"), "dataDir").delete();
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#setPrefix(java.lang.String)}.
	 */
	@Test
	public void testSetPrefix() {
		strategy.setPrefix("aPrefix");
		assertEquals("Prefix not set correctly", "aPrefix", strategy.getPrefix());
		assertTrue("Prefix not set correctly", strategy.getFile(1L).getName().startsWith("aPrefix"));
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#setSuffix(java.lang.String)}.
	 */
	@Test
	public void testSetSuffix() {
		strategy.setSuffix("aSuffix");
		assertEquals("Suffix not set correctly", "aSuffix", strategy.getSuffix());
		assertTrue("Suffix not set correctly", strategy.getFile(1L).getName().endsWith("aSuffix"));
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#matches(java.io.File)}.
	 */
	@Test
	public void testMatches() {
		for (String s : new String[]{ "1", "01", "001", "2", "20"}) {
		File f = new File(strategy.getParentDir(), strategy.getPrefix()+s+strategy.getSuffix()); 
		assertTrue("Valid filename cannot be recognozed", strategy.matches(f));
		}
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#matchesFilename(java.lang.String)}.
	 */
	@Test
	public void testMatchesFilename() {
		for (String s : new String[]{ "1", "01", "001", "2", "20"}) {
			String name = strategy.getPrefix()+s+strategy.getSuffix(); 
			assertTrue("Valid filename cannot be recognozed", strategy.matchesFilename(name));
		}
	}

	/**
	 * Test method for {@link rs.data.file.util.DefaultFilenameStrategy#getFiles()}.
	 */
	@Test
	public void testGetFiles() {
		Collection<File> files = strategy.getFiles();
		for (long l=1; l<11; l++) {
			File f = strategy.getFile(l);
			assertTrue("Test file cannot be "+f.getName()+" found", files.contains(f));
		}
	}

}
