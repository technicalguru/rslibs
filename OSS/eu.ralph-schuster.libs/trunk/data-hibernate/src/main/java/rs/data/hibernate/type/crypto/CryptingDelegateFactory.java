/**
 * 
 */
package rs.data.hibernate.type.crypto;

import java.io.IOException;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.configuration.ConfigurationUtils;
import rs.baselib.crypto.EncryptionUtils;
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
public class CryptingDelegateFactory implements ICryptingDelegateFactory {

	private static Logger log = LoggerFactory.getLogger(CryptingDelegateFactory.class);
	
	public static final CryptingDelegateFactory INSTANCE = new CryptingDelegateFactory();
	
	private ICryptingDelegate cryptingDelegate = null;
	private KeyPair keyPair;
	private String algorithm;
	private AlgorithmParameterSpec paramSpec;
	private XMLConfiguration config;
	private Map<String, IPasswordCallback> passwordCallbacks;
	
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
	private CryptingDelegateFactory() {
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
			throw new RuntimeException("Cannot load keys", e);
		}
	};
	
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
		if (className == null) className = "rsbudget.data.encrypt.DefaultCryptingDelegate";
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
			SubnodeConfiguration rc = config.configurationAt("passwordCallback("+index+")");
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
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getSalt() {
		String s = new String(getPassword("salt"));
		return EncryptionUtils.decodeBase64(s);
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
	 * Returns the paramSpec.
	 * @return the paramSpec
	 */
	public AlgorithmParameterSpec getParamSpec() {
		return paramSpec;
	}

	/**
	 * Sets the paramSpec.
	 * @param paramSpec the paramSpec to set
	 */
	public void setParamSpec(AlgorithmParameterSpec paramSpec) {
		this.paramSpec = paramSpec;
	}
	
	
}
