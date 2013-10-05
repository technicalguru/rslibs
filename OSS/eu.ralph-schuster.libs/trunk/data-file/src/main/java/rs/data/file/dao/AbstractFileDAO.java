/**
 * 
 */
package rs.data.file.dao;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

import rs.baselib.configuration.ConfigurationUtils;
import rs.data.api.IDaoFactory;
import rs.data.api.IDaoMaster;
import rs.data.api.bo.IGeneralBO;
import rs.data.file.bo.AbstractFileBO;
import rs.data.file.storage.AbstractStorageStrategy;
import rs.data.file.storage.IStorageStrategy;
import rs.data.file.storage.XmlStorageStrategy;
import rs.data.file.util.DefaultFilenameStrategy;
import rs.data.file.util.IFilenameStrategy;
import rs.data.file.util.IKeyGenerator;
import rs.data.file.util.LongKeyGenerator;
import rs.data.impl.dao.AbstractGeneralDAO;
import rs.data.util.CID;
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

	/** The Key Generator */
	private IKeyGenerator<K> keyGenerator;
	
	/** The filename strategy */
	private IFilenameStrategy<K> filenameStrategy;

	/** The storage strategy */
	private IStorageStrategy<File> storageStrategy;

	/**
	 * Constructor.
	 */
	public AbstractFileDAO() {
		setKeyGenerator(createKeyGenerator());
		setFilenameStrategy(createFilenameStrategy());
		setStorageStrategy(createStorageStrategy());
	}

	/**
	 * Creates a default key generation strategy (for numbers only!).
	 * Descendants shall override this method or configure their DAO through XML.
	 * @return the default strategy in case of numbers or <code>null</code>
	 * @see #configure(Configuration)
	 */
	@SuppressWarnings("unchecked")
	protected IKeyGenerator<K> createKeyGenerator() {
		if (Long.class.isAssignableFrom(getKeyClass())) {
			return (IKeyGenerator<K>)new LongKeyGenerator();
		}
		return null;
	}
	
	/**
	 * Sets the {@link #filenameStrategy}.
	 * The method instantiates a {@link DefaultFilenameStrategy} with properties from the DAO Master.
	 * @see #PROPERTY_DATA_DIR
	 * @see #PROPERTY_DATA_PREFIX
	 * @see #PROPERTY_DATA_SUFFIX
	 */
	protected IFilenameStrategy<K> createFilenameStrategy() {
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
	 * Creates a storage strategy, here a XML strategy.
	 * Descendants shall override this method if they do not wish to store their data in XML files
	 * or use the configuration method.
	 * @return the storage strategy.
	 * @see #configure(Configuration)
	 */
	protected IStorageStrategy<File> createStorageStrategy() {
		return new XmlStorageStrategy(getFactory());
	}
	
	/**
	 * {@inheritDoc}
	 * <p>This method also updates the storage strategy.</p>
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void setFactory(IDaoFactory factory) {
		super.setFactory(factory);
		IStorageStrategy<File> storageStrategy = getStorageStrategy();
		if (storageStrategy instanceof AbstractStorageStrategy) {
			((AbstractStorageStrategy)storageStrategy).setDaoFactory(factory);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void configure(Configuration cfg) throws ConfigurationException {
		super.configure(cfg);
		try {
			Configuration subConfig = ((HierarchicalConfiguration)cfg).configurationAt("keyGenerator(0)");
			setKeyGenerator((IKeyGenerator<K>)ConfigurationUtils.load(subConfig, true));
		} catch (Exception e) {
			// Ignore
		}
		
		try {
			Configuration subConfig = ((HierarchicalConfiguration)cfg).configurationAt("filenameStrategy(0)");
			setFilenameStrategy((IFilenameStrategy<K>)ConfigurationUtils.load(subConfig, true));
		} catch (Exception e) {
			// Ignore
		}
		
		try {
			Configuration subConfig = ((HierarchicalConfiguration)cfg).configurationAt("storageStrategy(0)");
			setStorageStrategy((IStorageStrategy<File>)ConfigurationUtils.load(subConfig, true));
		} catch (Exception e) {
			// Ignore
		}
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
	 * Returns the keyGenerator.
	 * @return the keyGenerator
	 */
	public IKeyGenerator<K> getKeyGenerator() {
		return keyGenerator;
	}

	/**
	 * Sets the keyGenerator.
	 * @param keyGenerator the keyGenerator to set
	 */
	public void setKeyGenerator(IKeyGenerator<K> keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	/**
	 * Returns the {@link #filenameStrategy}.
	 * @return the filenameStrategy
	 */
	public IFilenameStrategy<K> getFilenameStrategy() {
		return filenameStrategy;
	}

	/**
	 * Sets the filenameStrategy.
	 * @param filenameStrategy the filenameStrategy to set
	 */
	public void setFilenameStrategy(IFilenameStrategy<K> filenameStrategy) {
		this.filenameStrategy = filenameStrategy;
	}

	/**
	 * Returns the {@link #storageStrategy}.
	 * @return the storageStrategy
	 */
	public IStorageStrategy<File> getStorageStrategy() {
		return storageStrategy;
	}

	/**
	 * Sets the {@link #storageStrategy}.
	 * @param storageStrategy the storageStrategy to set
	 */
	public void setStorageStrategy(IStorageStrategy<File> storageStrategy) {
		this.storageStrategy = storageStrategy;
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
	public C findBy(K id) {
		C rc = getCached(new CID(getBoImplementationClass(), id));
		if (rc == null) {
			File file = getFilenameStrategy().getFile(id);
			if (!file.canRead()) return null;
			rc = _load(file);
		}
		return (C)rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(C object) {
		try {
			getStorageStrategy().refresh(object, getFilenameStrategy().getFile(object.getId()));
		} catch (Exception e) {
			throw new RuntimeException("Cannot load file", e);
		}
	}

	/**
	 * Loads the business object from the given file.
	 * @param file file to load from
	 * @return the business object
	 */
	protected C _load(File file) {
		C rc = newInstance();
		try {
			getStorageStrategy().load(rc, file);
		} catch (Exception e) {
			throw new RuntimeException("Cannot load file", e);
		}
		addCached(rc);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findAll(int firstResult, int maxResults) {
		return load(getAll(), firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<C> findDefaultAll(int firstResult, int maxResults) {
		return load(getDefaultAll(), firstResult, maxResults);
	}

	/**
	 * Load the files given and return the objects.
	 * @param files files to be loaded
	 * @param firstResult first result index (-1 for not set)
	 * @param maxResults maximum number of results (-1 for not limited)
	 * @return loaded objects
	 */
	protected List<C> load(Collection<File> files, int firstResult, int maxResults) {
		List<C> rc = new ArrayList<C>();
		int i = 0;
		for (File file : files) {
			boolean doLoad = true;
			if (firstResult > i) doLoad = false;
			if (rc.size() == maxResults) doLoad = false;
			if (doLoad) {
				rc.add((C)_load(file));
			}
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateAll(int firstResult, int maxResults) {
		return iterate(getAll(), firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<C> iterateDefaultAll(int firstResult, int maxResults) {
		return iterate(getDefaultAll(), firstResult, maxResults);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		return delete(findAll());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		return delete(findDefaultAll());
	}

	/**
	 * Deletes the objects in the given collection.
	 * @param objects objects to be deleted
	 * @return number of objects deleted
	 */
	@SuppressWarnings("unchecked")
	protected int delete(List<C> objects) {
		int cnt = 0;
		for (C object : objects) {
			delete(object);
			cnt++;
		}
		return cnt;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void _create(C object) {
		K id = getNewId();
		((B)object).setId(id);
		try {
			File file = getFilenameStrategy().getFile(id);
			getStorageStrategy().save(object, file);
		} catch (IOException e) {
			throw new RuntimeException("Cannot create object", e);
		}
	}

	/**
	 * Returns a new Id.
	 * @return the generated new ID
	 */
	protected K getNewId() {
		K rc = null;
		IKeyGenerator<K> generator = getKeyGenerator();
		IFilenameStrategy<K> nameStrategy = getFilenameStrategy();
		while (rc == null) {
			rc = generator.getNewId();
			File f = nameStrategy.getFile(rc);
			if (f.exists()) {
				rc = null;
			}
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(C object) {
		try {
			File file = getFilenameStrategy().getFile(object.getId());
			getStorageStrategy().save(object, file);
		} catch (IOException e) {
			throw new RuntimeException("Cannot save object", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _delete(C object) {
		File file = getFile(object.getId());
		file.delete();
	}

	/**
	 * Creates the appropriate iterator for the given collection and index restrictions.
	 * @param files files to be delivered
	 * @param firstResult first index to be returned
	 * @param maxResults max number of results
	 * @return iterator
	 */
	protected IDaoIterator<C> iterate(Collection<File> files, int firstResult, int maxResults) {
		return new FileDaoIterator(files, firstResult, maxResults);
	}
	
	/**
	 * DAO Iterator for file based objects.
	 * @author ralph
	 *
	 */
	public class FileDaoIterator implements IDaoIterator<C> {

		/** Iterator */
		private Iterator<File> files;
		/** Maximum number of files to be returned */
		private int maxResults;
		/** Number of files already returned */
		private int delivered;
		
		/**
		 * Constructor.
		 * @param files files to iterate over
		 * @param firstResults first index to be returned
		 * @param maxResult max number of results
		 */
		public FileDaoIterator(Collection<File> files, int firstResult, int maxResults) {
			this.files = files.iterator();
			this.maxResults = maxResults;
			
			// Skip results
			while ((firstResult > 0) && this.files.hasNext()) {
				this.files.next();
				firstResult--;
			}
			
			this.delivered = 0;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return ((maxResults < 0) || (delivered < maxResults)) && files.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public C next() {
			if (!hasNext()) throw new IllegalArgumentException("No more objects!");
			return _load(files.next());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new IllegalArgumentException("Not supported");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void close() {
			// Nothing to do
		}
		
	}
}
