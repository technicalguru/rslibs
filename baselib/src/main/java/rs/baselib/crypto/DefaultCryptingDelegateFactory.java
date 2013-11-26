/**
 * 
 */
package rs.baselib.crypto;

import java.io.IOException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.ConfigurationUtils;
import rs.baselib.io.FileFinder;
import rs.baselib.lang.LangUtils;
import rs.baselib.security.IPasswordCallback;

/**
 * Creates a crypting delegator.
 * 
 * This class finds its configuration via classpath. You can give java argument -Dencryption.config=path
 * which will then load the config from this path. The default location is encryption-config.xml.
 * @author ralph
 *
 */
public class DefaultCryptingDelegateFactory implements ICryptingDelegateFactory {

	private static Logger log = LoggerFactory.getLogger(DefaultCryptingDelegateFactory.class);

	public static final DefaultCryptingDelegateFactory INSTANCE = new DefaultCryptingDelegateFactory();

	private ICryptingDelegate cryptingDelegate = null;
	private KeyPair keyPair = null;
	private String algorithm;
	private AlgorithmParameterSpec paramSpec;
	private XMLConfiguration config;
	private Map<String, IPasswordCallback> passwordCallbacks;
	private boolean specLoaded = false;

	/**
	 * Returns the crypting delegate factory.
	 * @return the factory
	 */
	public static ICryptingDelegateFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * Constructor.
	 */
	private DefaultCryptingDelegateFactory() {
		init();
	}

	/**
	 * Initializes this factory.
	 */
	protected void init() {
		try {
			// Some init
			passwordCallbacks = new HashMap<String, IPasswordCallback>();

			// Load the configuration
			String configLocation = System.getProperty("encryption.config");
			if (configLocation == null) configLocation = "encryption-config.xml";
			URL configURL = FileFinder.find(configLocation);
			log.info("Encryption configuration defined as: "+configLocation);
			log.info("Encryption configuration found at: "+configURL);
			config = new XMLConfiguration(configURL);
			
			// Attention! Do not load here due to bootstrap issues
			// Apply lazy load through loadSpec() method
		} catch (Exception e) {
			throw new RuntimeException("Cannot load keys", e);
		}
	};

	/**
	 * Loads the spec params (lazily).
	 */
	protected synchronized void loadSpec() {
		if (!specLoaded) {
			if (algorithm == null) algorithm = loadAlgorithm(config);
			if (paramSpec == null) paramSpec = loadParamSpec(config);
			specLoaded = true;
		}
	}

	/**
	 * Returns the configuration.
	 * @return the configuration
	 */
	protected XMLConfiguration getConfiguration() {
		return config;
	}

	/**
	 * Returns the name of the delegate class.
	 * @return the name of delegate class
	 */
	protected String getDelegateClassName() {
		String className = config.getString("cryptingDelegate(0)[@class]");
		if (className == null) className = DefaultCryptingDelegate.class.getName();
		return className;

	}

	/**
	 * Returns a callback for the given type.
	 * @param type type of password callback
	 * @return the password callback
	 */
	protected synchronized IPasswordCallback getPasswordCallback(String type) {
		SubnodeConfiguration config = getPasswordCallbackConfig(type);
		String className = config.getString("[@class]");
		if (className != null) {
			IPasswordCallback rc = passwordCallbacks.get(className);
			if (rc == null) {
				rc = (IPasswordCallback)ConfigurationUtils.load(config, true);
				passwordCallbacks.put(className, rc);
			}
			return rc;
		}
		return null;
	}

	/**
	 * Returns the configuration for the password callback.
	 * @param type type of callback
	 * @return classname
	 */
	protected SubnodeConfiguration getPasswordCallbackConfig(String type) {
		int index = 0;
		while (true) {
			SubnodeConfiguration rc = null;
			try {
				rc = config.configurationAt("passwordCallback("+index+")");
			} catch (IllegalArgumentException e) {
				break;
			}
			if (rc == null) break;
			String t = rc.getString("[@type]");
			if (type.equals(t)) return rc;
			index++;
		}
		return null;
	}

	/**
	 * Asks the respective callback to deliver a password.
	 * @param type type of callback
	 * @return password or null if no callback exists.
	 */
	protected char[] getPassword(String type) {
		char rc[] = null;
		IPasswordCallback callback = getPasswordCallback(type);
		if (callback == null) rc = null;
		else rc = callback.getPassword();
		return rc;
	}

	/**
	 * Return the public key salt.
	 * @return key salt
	 */
	protected byte[] getKeySalt() {
		return getSalt("key");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getSalt() {
		return getKeySalt();
	}

	/**
	 * Asks the respective callback to deliver a salt.
	 * @param type type of callback
	 * @return salt or null if no callback exists.
	 */
	protected byte[] getSalt(String type) {
		byte rc[] = null;
		IPasswordCallback callback = getPasswordCallback(type);
		if (callback == null) rc = null;
		else rc = callback.getSalt();
		return rc;
	}

	/**
	 * Returns the configuration for the keystore.
	 * @return classname
	 */
	protected SubnodeConfiguration getKeyStoreConfig() {
		try {
			return config.configurationAt("keystore(0)");
		} catch (IllegalArgumentException t) {
			// No such config
		}
		return null;
	}

	/**
	 * Returns the key store.
	 * @return the key store
	 */
	protected KeyStore getKeyStore() throws IOException {
		String path = getKeyStorePath();
		if (path != null) {
			log.info("Keystore defined as: "+path);
			URL url = FileFinder.find(path);
			log.info("Keystore found at: "+url);
			return EncryptionUtils.getKeyStore(getKeyStoreType(), url.openStream(), getKeyStorePassword());
		}
		return null;
	}

	/**
	 * Returns the key store path.
	 * @return key store path
	 */
	protected String getKeyStoreType() {
		String rc = ConfigurationUtils.getParam(getKeyStoreConfig(), "type");
		if (rc == null) rc = KeyStore.getDefaultType();
		return rc;
	}

	/**
	 * Returns the key store path.
	 * @return key store path
	 */
	protected String getKeyStorePath() {
		return ConfigurationUtils.getParam(getKeyStoreConfig(), "location");
	}

	/**
	 * Return the key store password
	 * @return key store password
	 */
	protected char[] getKeyStorePassword() {
		return getPassword("keystore");
	}

	/**
	 * Returns the public key alias in key store.
	 * @return key alias
	 */
	protected String getKeyAlias() {
		return ConfigurationUtils.getParam(getKeyStoreConfig(), "key.alias");
	}

	/**
	 * Return the public key password
	 * @return key password
	 */
	protected char[] getKeyPassword() {
		return getPassword("key");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char[] getPassphrase() {
		return getKeyPassword();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public ICryptingDelegate getCryptingDelegate() {
		if (cryptingDelegate == null) createCryptingDelegate();
		return cryptingDelegate;
	}

	/**
	 * Creates and initializes the crypting delegate.
	 */
	@SuppressWarnings({ "unchecked" })
	protected synchronized void createCryptingDelegate() {
		if (cryptingDelegate != null) return;
		try {
			Class<? extends ICryptingDelegate> clazz = (Class<? extends ICryptingDelegate>)LangUtils.forName(getDelegateClassName());
			cryptingDelegate = clazz.newInstance();
			cryptingDelegate.init(this);
		} catch (Exception e) {
			throw new RuntimeException("Cannot create crypting delegate", e);
		}
	}

	/**
	 * Returns the keyPair.
	 * @return the keyPair
	 */
	public KeyPair getKeyPair() {
		if (keyPair == null) {
			try {
				// Load the key pair from the key store
				KeyStore ks = getKeyStore();
				String keyAlias = getKeyAlias();
				char keyPassword[] = getKeyPassword();
				if ((keyAlias != null) && (keyPassword != null)) {
					PrivateKey privKey = (PrivateKey)ks.getKey(getKeyAlias(), getKeyPassword());
					PublicKey pubKey = ks.getCertificate(getKeyAlias()).getPublicKey();
					setKeyPair(new KeyPair(pubKey, privKey));
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot create key pair:", e);
			}
		}
		return keyPair;
	}

	/**
	 * Sets the keyPair.
	 * @param keyPair the keyPair to set
	 */
	public void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
	}

	/**
	 * Returns the algorithm.
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		if (!specLoaded)  loadSpec();
		return algorithm;
	}

	/**
	 * Sets the algorithm.
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * Returns the algorithm definition from the config
	 * @param config the config
	 * @return the algorithm 
	 */
	protected String loadAlgorithm(Configuration config) {
		try {
			return config.getString("algorithm("+0+")");
		} catch (IllegalArgumentException e) {
			// No such definition
		}
		return null;
	}

	/**
	 * Returns the paramSpec.
	 * @return the paramSpec
	 */
	public AlgorithmParameterSpec getParamSpec() {
		if (!specLoaded)  loadSpec();
		return paramSpec;
	}

	/**
	 * Sets the paramSpec.
	 * @param paramSpec the paramSpec to set
	 */
	public void setParamSpec(AlgorithmParameterSpec paramSpec) {
		this.paramSpec = paramSpec;
	}

	/**
	 * Currently only default PBE spec.
	 * @param config configuration to load from
	 * @return the param spec
	 */
	protected AlgorithmParameterSpec loadParamSpec(Configuration config) {
		return EncryptionUtils.generateParamSpec(getSalt(), EncryptionUtils.DEFAULT_ITERATIONS);
	}
}
