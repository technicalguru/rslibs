/**
 * 
 */
package rs.data.file;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rs.data.file.impl.TestDaoFactory;

/**
 * @author ralph
 *
 */
public class FileDaoTest {

	private static TestDaoFactory factory;
	
	@BeforeClass
	protected void beforeClass() {
		factory = new TestDaoFactory();
	}
	

	@AfterClass
	protected void afterClass() {
		
	}
	
	
}
