package rs.mail.templates.resolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rs.mail.templates.ResolverException;
import rs.mail.templates.TemplateContext;
import rs.mail.templates.cache.Cache;
import rs.mail.templates.cache.CacheFactory;
import rs.mail.templates.cache.CacheFactory.CacheBuilder;
import rs.mail.templates.cache.CacheStrategy;
import rs.mail.templates.impl.ResolverId;

/**
 * The default resolver searches a specific directory (non-recursive)
 * versions of a file name. The order of lookups will be provided through 
 * method ...
 * 
 * @param <X> the type of object to be resolved
 * 
 * @author ralph
 *
 */
public abstract class AbstractFileResolver<X> extends AbstractResolver<X> {

	private File directory;
	private Charset charset;
	
	/**
	 * Constructor (which uses a LRU cache).
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param objectClass - the type of object to be resolved
	 * @throws IOException - when the directory is not accessible
	 */
	public AbstractFileResolver(File directory, Class<X> objectClass) throws IOException {
		this(directory, true, objectClass);
	}
	
	/**
	 * Constructor to enable or disable the default LRU cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param enableCache whether to enable the Cache
	 * @param objectClass - the type of object to be resolved
	 * @throws IOException - when the directory is not accessible
	 */
	public AbstractFileResolver(File directory, boolean enableCache, Class<X> objectClass) throws IOException {
		this(directory, enableCache? CacheFactory.newBuilder(ResolverId.class, objectClass).with(CacheStrategy.LRU).build() : null);
	}
	
	/**
	 * Constructor to use a specific cache strategy.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheStrategy the cahce strategy to be used
	 * @param objectClass - the type of object to be resolved
	 * @throws IOException - when the directory is not accessible
	 */
	public AbstractFileResolver(File directory, CacheStrategy cacheStrategy, Class<X> objectClass) throws IOException {
		this(directory, CacheFactory.newBuilder(ResolverId.class, objectClass).with(cacheStrategy));
	}
	
	/**
	 * Constructor to use a custom {@link CacheBuilder}.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cacheBuilder the cache builder
	 * @throws IOException - when the directory is not accessible
	 */
	public AbstractFileResolver(File directory, CacheBuilder<ResolverId,X> cacheBuilder) throws IOException {
		this(directory, cacheBuilder.build());
	}
	
	/**
	 * Constructor to use a custom cache.
	 * 
	 * @param directory - the directory to search templates (non-recursive)
	 * @param cache the cache to be used (can be null)
	 * @throws IOException - when the directory is not accessible
	 */
	public AbstractFileResolver(File directory, Cache<ResolverId, X> cache) throws IOException {
		super(cache);
		this.directory = directory;
		this.charset   = StandardCharsets.UTF_8;
		if (!directory.exists())      throw new FileNotFoundException("Not found: "+directory.getCanonicalPath());
		if (!directory.isDirectory()) throw new IOException("Not a directory: "+directory.getCanonicalPath());
		if (!directory.canRead())     throw new IOException("Cannot read: "+directory.getCanonicalPath());
	}

	/**
	 * Returns the directory.
	 * @return the directory
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * Returns the charset to be used.
	 * @return charset to be used
	 */
	public Charset getCharset() {
		return getCharset(false);
	}
	
	/**
	 * Sets the charset.
	 * @param charset the charset to set
	 */
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * Returns the charset to be used or - if required - a default one
	 * @param resolveWithDefault whether a default charset shall be returned when no charset was set before
	 * @return a charset
	 */
	protected Charset getCharset(boolean resolveWithDefault) {
		if (charset == null) {
			if (resolveWithDefault) return Charset.defaultCharset();
		}
		return charset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected X resolve(ResolverId id, String name, TemplateContext context) throws ResolverException {
		X rc = null;
		
		// Find in cache
		boolean cacheHit = false;
		Cache<ResolverId,X> cache = getCache();
		if (cache != null) {
			rc = cache.get(id);
		}
		if (rc == null) {
			rc = create(id, name, context);
		} else {
			cacheHit = true;
		}
		
		if ((cache != null) && (rc != null) && !cacheHit) {
			cache.put(id, rc);
		}
		return rc;
	}

	/**
	 * Creates the resolved object based on the resolver id, the name and the context.
	 * <p>It is up to descendants to resolve files and how to incorporate in the resolved
	 *    object. This class provides some useful methods for that purpose.</p>
	 *    
	 * @param id - ID of resolved object
	 * @param name - base name of resolved object
	 * @param context - the template context for further information
	 * @return the created object
	 * @throws ResolverException when resolving fails
	 * @see #getPriorityPaths(String, TemplateContext, String)
	 * @see #getFileVariants(TemplateContext)
	 * @see #findFile(List)
	 */
	protected abstract X create(ResolverId id, String name, TemplateContext context) throws ResolverException;
	
	/**
	 * Returns the files to be checked in order of priority.
	 * <p>This implementation uses {@link #getFileVariants(TemplateContext)}, but subclasses my decide
	 *    to overrride this method for additional files to be returned.</p>
	 *    
	 * @param name - the base name of the file
	 * @param context - the context
	 * @param suffix - the suffix of the file
	 * @return the files to be checked
	 * @see #getFileVariants(TemplateContext)
	 */
	protected List<File> getPriorityPaths(String name, TemplateContext context, String suffix) {
		String variants[]  = getFileVariants(context); 
		List<File> files   = new ArrayList<>();
		for (int i=0; i<variants.length; i++) {
			files.add(new File(directory, name+"."+variants[i]+suffix));
		}
		files.add(new File(directory, name+suffix));
		return files;
	}
	
	/**
	 * Returns the variants of the file in order of precendence.
	 * <p>Default implementation uses the locale in the context and e.g. returns ["de-DE", "de" ]</p>
	 * @param context - the context holding information about the message to be built
	 * @return the list of variants to be checked for files
	 */
	protected String[] getFileVariants(TemplateContext context) {
		Locale locale = context.getLocale();
		return new String[] {
				locale.toLanguageTag(),
				locale.getLanguage()
		};
	}
	
	/**
	 * Check files in order of priorities to find the first readable.
	 * @param priorities - list of files in order of priority
	 * @return - the file that can be used or {@code null} if none is available
	 */
	protected File findFile(List<File> priorities) {
		for (File file : priorities) {
			if (file.exists() && file.isFile() && file.canRead()) {
				return file;
			}
		}
		return null;
	}
}
