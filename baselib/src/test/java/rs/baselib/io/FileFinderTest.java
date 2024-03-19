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
package rs.baselib.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;

import rs.baselib.configuration.ConfigurationUtilsTest;
import rs.baselib.util.CommonUtils;
/**
 * Test for {@link FileFinder}.
 * @author ralph
 *
 */
public class FileFinderTest {

	/**
	 * Test method for {@link FileFinder#find(String)}.
	 */
	@Test
	public void testFindFileString() {
		URL url = FileFinder.find("finder-file1.txt");
		assertNotNull(url);
		url = FileFinder.find("finder-file2.txt");
		assertNotNull(url);
		url = FileFinder.find("finder-file3.txt");
		assertNotNull(url);
		url = FileFinder.find("finder-file4.txt");
		assertNotNull(url);
	}

	/**
	 * Test method for {@link FileFinder#findDir(String)}.
	 */
	@Test
	public void testFindDirString() {
		URL url = FileFinder.findDir("crypto");
		assertNotNull(url);
	}
	
	/**
	 * Test method for {@link FileFinder#find(Class, String)}.
	 */
	@Test
	public void testFindFileClassOfQString() {
		URL url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file1.txt");
		assertNull(url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file2.txt");
		assertNotNull(url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file3.txt");
		assertNotNull(url);
		url = FileFinder.find(ConfigurationUtilsTest.class, "finder-file4.txt");
		assertNotNull(url);
	}

	/**
	 * Test method for {@link FileFinder#find(Class, String)}.
	 */
	@Test
	public void testFindDirClassOfQString() {
		URL url = FileFinder.findDir(ConfigurationUtilsTest.class, "crypto");
		assertNotNull(url);
	}
	
	
	/**
	 * Test method for {@link FileFinder#open(String)}.
	 * 
	 * @throws IOException - when the test data cannot be loaded
	 */
	@Test
	public void testOpenString() throws IOException {
		InputStream in = null;
		try {
			in = FileFinder.open("finder-file1.txt");
			String s = CommonUtils.loadContent(in);
			assertEquals("finder-file1", s.trim());
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Test method for {@link FileFinder#open(Class, String)}.
	 * 
	 * @throws IOException - when the test data cannot be loaded
	 */
	@Test
	public void testOpenClassOfQString() throws IOException {
		InputStream in = null;
		try {
			in = FileFinder.open(ConfigurationUtilsTest.class, "finder-file2.txt");
			String s = CommonUtils.loadContent(in);
			assertEquals("finder-file2", s.trim());
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Test method for {@link FileFinder#load(String)}.
	 * 
	 * @throws IOException - when the test data cannot be loaded
	 */
	@Test
	public void testLoadString() throws IOException {
		String s = FileFinder.load("finder-file3.txt");
		assertEquals("finder-file3", s.trim());
	}

	/**
	 * Test method for {@link FileFinder#load(Class, String)}.
	 * 
	 * @throws IOException - when the test data cannot be loaded
	 */
	@Test
	public void testLoadClassOfQString() throws IOException {
		String s = FileFinder.load(ConfigurationUtilsTest.class, "finder-file4.txt");
		assertEquals("finder-file4", s.trim());
	}

}
