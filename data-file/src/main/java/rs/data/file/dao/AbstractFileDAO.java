/**
 * 
 */
package rs.data.file.dao;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.file.bo.AbstractFileBO;
import rs.data.file.util.DefaultFilenameStrategy;
import rs.data.file.util.IFilenameStrategy;
import rs.data.impl.dao.AbstractGeneralDAO;
import rs.data.util.IDaoIterator;

/**
 * Abstract implementation of file-based DAOs.
 * @author ralph
 *
 */
public abstract class AbstractFileDAO<K extends Serializable, B extends AbstractFileBO<K>, C extends IGeneralBO<K>> extends AbstractGeneralDAO<K, B, C> {

	/** The key for the data directory as stored in DAO Master */
	public static final String PROPERTY_DATA_DIR = "dataDir";
	/** The key for the suffix as stored in DAO Master */
	public static final String PROPERTY_DATA_SUFFIX = "dataSuffix";
	/** The key for the prefix as stored in DAO Master */
	public static final String PROPERTY_DATA_PREFIX = "dataPrefix";
	
	/** The filename strategy */
	private IFilenameStrategy<K> filenameStrategy;
	
	/**
	 * Constructor.
	 */
	public AbstractFileDAO() {
		filenameStrategy = createFilenameStrategy();
	}

	/**
	 * Sets the {@link #filenameStrategy}.
	 * The method instantiates a {@link DefaultFilenameStrategy} with properties from the DAO Master.
	 * @see #PROPERTY_DATA_DIR
	 * @see #PROPERTY_DATA_PREFIX
	 * @see #PROPERTY_DATA_SUFFIX
	 */
	public IFilenameStrategy<K> createFilenameStrategy() {
		String dataDir = null;
		String prefix  = null;
		String suffix  = null;
		IDaoMaster daoMaster = getDaoMaster();
		if (daoMaster != null) {
			dataDir = daoMaster.getProperty(PROPERTY_DATA_DIR);
			prefix  = daoMaster.getProperty(PROPERTY_DATA_PREFIX);
			suffix  = daoMaster.getProperty(PROPERTY_DATA_SUFFIX);
		}
		if (dataDir == null) dataDir = IFilenameStrategy.DEFAULT_DATA_DIR;
		if (suffix  == null) suffix  = IFilenameStrategy.DEFAULT_DATA_SUFFIX;
		return new DefaultFilenameStrategy<K>(new File(dataDir, getBoInterfaceClass().getSimpleName()), prefix, suffix);
	}

	/**
	 * Returns all files for this object.
	 * @return all files
	 */
	protected Collection<File> getAll() {
		return getFilenameStrategy().getFiles();
	}
	
	/**
	 * Returns all files that belong to the default criteria.
	 * @return default object files
	 */
	protected Collection<File> getDefaultAll() {
		return getAll();
	}
	
	/**
	 * Returns the {@link #filenameStrategy}.
	 * @return the filenameStrategy
	 */
	public IFilenameStrategy<K> getFilenameStrategy() {
		return filenameStrategy;
	}

	/**
	 * Returns the file that shall have the object with given key.
	 * @param key key of business object
	 * @return the file for this object
	 */
	protected File getFile(K key) {
		// Forward to filename strategy
		return getFilenameStrategy().getFile(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getObjectCount() {
		return getAll().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDefaultObjectCount() {
		return getDefaultAll().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public C findById(K id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateAll(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _create(B object) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(B object) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _delete(B object) {
		// TODO Auto-generated method stub
		
	}
	
	
}
