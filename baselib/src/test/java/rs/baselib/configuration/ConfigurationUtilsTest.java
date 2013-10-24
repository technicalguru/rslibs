/**
 * 
 */
package rs.baselib.configuration;

import static org.junit.Assert.*;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpClass() throws Exception {
		config = new XMLConfiguration(FileFinder.find(ConfigurationUtilsTest.class, "test-config.xml"));
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(Configuration, boolean)}.
	 */
	@Test
	public void testLoadConfigurationBoolean() {
		ConfigTestObject o = (ConfigTestObject)ConfigurationUtils.load(config, false);
		assertNotNull("Object was not loaded", o);
		assertNull("Object was configured", o.getStringValue());
		assertEquals("Object was configured", 0, o.getIntValue());
		
		o = (ConfigTestObject)ConfigurationUtils.load(config, true);
		assertNotNull("Object was not loaded", o);
		assertEquals("Object was not configured", "a value", o.getStringValue());
		assertEquals("Object was not configured", 5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(String, Configuration, boolean)}.
	 */
	@Test
	public void testLoadStringConfigurationBoolean() {
		ConfigTestObject o = (ConfigTestObject)ConfigurationUtils.load(ConfigTestObject.class.getName(), config, false);
		assertNotNull("Object was not loaded", o);
		assertNull("Object was configured", o.getStringValue());
		assertEquals("Object was configured", 0, o.getIntValue());
		
		o = (ConfigTestObject)ConfigurationUtils.load(ConfigTestObject.class.getName(), config, true);
		assertNotNull("Object was not loaded", o);
		assertEquals("Object was not configured", "a value", o.getStringValue());
		assertEquals("Object was not configured", 5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#load(Class, Configuration, boolean)}.
	 */
	@Test
	public void testLoadClassConfigurationBoolean() {
		ConfigTestObject o = ConfigurationUtils.load(ConfigTestObject.class, config, false);
		assertNotNull("Object was not loaded", o);
		assertNull("Object was configured", o.getStringValue());
		assertEquals("Object was configured", 0, o.getIntValue());
		
		o = ConfigurationUtils.load(ConfigTestObject.class, config, true);
		assertNotNull("Object was not loaded", o);
		assertEquals("Object was not configured", "a value", o.getStringValue());
		assertEquals("Object was not configured", 5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#configure(IConfigurable, Configuration)}.
	 */
	@Test
	public void testConfigure() throws ConfigurationException {
		ConfigTestObject o = new ConfigTestObject();
		ConfigurationUtils.configure(o, config);
		assertEquals("Object was not configured", "a value", o.getStringValue());
		assertEquals("Object was not configured", 5, o.getIntValue());
	}

	/**
	 * Test method for {@link ConfigurationUtils#getParam(SubnodeConfiguration, String)}.
	 */
	@Test
	public void testGetParam() {
		SubnodeConfiguration cfg = ((XMLConfiguration)config).configurationAt("paramTest(0)");
		assertEquals("aParameter1 is not correct", "value1", ConfigurationUtils.getParam(cfg, "aParameter1"));
		assertEquals("aParameter2 is not correct", "value2", ConfigurationUtils.getParam(cfg, "aParameter2"));
	}

}
