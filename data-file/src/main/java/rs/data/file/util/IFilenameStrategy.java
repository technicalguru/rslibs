/**
 * 
 */
package rs.data.file.util;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

/**
 * Interface that defines a filename strategy for deriving filenames from keys.
 * @param <K> type of ID this strategy converts to file names
 * @author ralph
 *
 */
public interface IFilenameStrategy<K extends Serializable> {

	/** The default data directory */
	public static final String DEFAULT_DATA_DIR = "data";	
	/** The default suffix */
	public static final String DEFAULT_DATA_SUFFIX = ".data";

	/**
	 * Compute the filename from the key.
	 * @param key key
	 * @return filename for key
	 */
	public File getFile(K key);
	
	/**
	 * Returns all existing files that the strategy knows about.
	 * @return all existing files
	 */
	public Collection<File> getFiles();
}
