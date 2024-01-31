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
package rs.baselib.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rs.baselib.io.FileFinder;

/**
 * Tests the {@link ConfigurationUtils} class.
 * @author ralph
 *
 */
public class ConfigurationUtilsTest {

	private static Configuration config;
	
	/**
	 * Loads the configuration.
	 * @throws java.lang.Exception when configuration cannot be found or read
	 */
	@BeforeAll
	public static void setUpClass() throws Exception {
		config = ConfigurationUtils.getXmlConfiguration(FileFinder.find(ConfigurationUtilsTest.class, "test-config.xml"));
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(Configuration, boolean)}.
	 */
	@Test
	public void testLoadConfigurationBoolean() {
		ConfigTestObject o = (ConfigTestObject)ConfigurationUtils.load(config, false);
		assertNotNull(o);
		assertNull(o.getStringValue());
		assertEquals(0, o.getIntValue());
		
		o = (ConfigTestObject)ConfigurationUtils.load(config, true);
		assertNotNull(o);
		assertEquals("a value", o.getStringValue());
		assertEquals(5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(String, Configuration, boolean)}.
	 */
	@Test
	public void testLoadStringConfigurationBoolean() {
		ConfigTestObject o = (ConfigTestObject)ConfigurationUtils.load(ConfigTestObject.class.getName(), config, false);
		assertNotNull(o);
		assertNull(o.getStringValue());
		assertEquals(0, o.getIntValue());
		
		o = (ConfigTestObject)ConfigurationUtils.load(ConfigTestObject.class.getName(), config, true);
		assertNotNull(o);
		assertEquals("a value", o.getStringValue());
		assertEquals(5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(Class, Configuration, boolean)}.
	 */
	@Test
	public void testLoadClassConfigurationBoolean() {
		ConfigTestObject o = ConfigurationUtils.load(ConfigTestObject.class, config, false);
		assertNotNull(o);
		assertNull(o.getStringValue());
		assertEquals(0, o.getIntValue());
		
		o = ConfigurationUtils.load(ConfigTestObject.class, config, true);
		assertNotNull(o);
		assertEquals("a value", o.getStringValue());
		assertEquals(5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#configure(IConfigurable, Configuration)}.
	 * 
	 * @throws ConfigurationException - when the configuration parsing fails
	 */
	@Test
	public void testConfigure() throws ConfigurationException {
		ConfigTestObject o = new ConfigTestObject();
		ConfigurationUtils.configure(o, config);
		assertEquals("a value", o.getStringValue());
		assertEquals(5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#getParam(SubnodeConfiguration, String)}.
	 */
	@Test
	public void testGetParam() {
		XMLConfiguration xmlConfig = (XMLConfiguration)config;
		HierarchicalConfiguration<ImmutableNode> cfg = xmlConfig.configurationAt("paramTest(0)");
		assertEquals("value1", ConfigurationUtils.getParam(cfg, "aParameter1"));
		assertEquals("value2", ConfigurationUtils.getParam(cfg, "aParameter2"));
	}

}
