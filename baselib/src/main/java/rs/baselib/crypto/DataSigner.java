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
package rs.baselib.crypto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.apache.commons.codec.binary.Base64;

/**
 * The class for signing and verifying signatures.
 * @author ralph
 *
 */
public class DataSigner {

	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	/**
	 * Constructor.
	 * @throws DecryptionException - when a problem occurs
	 */
	public DataSigner() throws DecryptionException {
		this(null, null);
	}

	/**
	 * Constructor from key pair.
	 * @param keyPair the key pair to be used
	 * @throws DecryptionException - when a problem occurs
	 */
	public DataSigner(KeyPair keyPair) throws DecryptionException {
		this(keyPair.getPrivate(), keyPair.getPublic());
	}

	/**
	 * Constructor from key pair.
	 * @param privateKey private key
	 * @param publicKey public key
	 * @throws DecryptionException - when a problem occurs
	 */
	public DataSigner(PrivateKey privateKey, PublicKey publicKey) throws DecryptionException {
		setPublicKey(publicKey);
		setPrivateKey(privateKey);
	}

	/**
	 * Constructor from private key.
	 * @param privateKey the private key to be used
	 * @throws DecryptionException - when a problem occurs
	 */
	public DataSigner(PrivateKey privateKey) throws DecryptionException {
        this(privateKey, null);
	}

	/**
	 * Constructor from public key.
	 * @param publicKey the public key to be used
	 * @throws DecryptionException - when a problem occurs
	 */
	public DataSigner(PublicKey publicKey) throws DecryptionException {
        this(null, publicKey);
	}

    /**
	 * Returns the privateKey.
	 * @return the privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * Sets the privateKey.
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * Sets the privateKey.
	 * @param privateKey the privateKey to set
	 * @throws SigningException - when a problem occurs
	 */
	public void setPrivateKey(String privateKey) throws SigningException {
		try {
			setPrivateKey(EncryptionUtils.decodeBase64PrivateKey(privateKey));
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}

	/**
	 * Returns the publicKey.
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

	/**
	 * Sets the publicKey.
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * Sets the publicKey.
	 * @param publicKey the publicKey to set
	 * @throws SigningException - when a problem occurs
	 */
	public void setPublicKey(String publicKey) throws SigningException {
		try {
			setPublicKey(EncryptionUtils.decodeBase64PublicKey(publicKey));
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}

	/**
     * Creates a signature for the given stream.
     * @param reader stream to be signed
     * @return signature of the provided stream
	 * @throws SigningException - when a problem occurs
     */
	public String sign(Reader reader) throws SigningException {
		return Base64.encodeBase64String(getByteSignature(reader)).trim();
	}
	
	/**
     * Creates a signature for the given stream.
     * @param reader stream to be signed
     * @return signature of the provided stream
	 * @throws SigningException - when a problem occurs
     */
	public byte[] getByteSignature(Reader reader) throws SigningException {
		try {
			Signature dsa = Signature.getInstance("SHA1withDSA");

			/* Initializing the object with a private key */
			dsa.initSign(getPrivateKey());
			
			update(dsa, reader);
			
			return dsa.sign();
		} catch (SigningException e) {
			throw e;
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}
	
	/**
     * Creates a signature for the given stream.
     * @param in stream to be signed
     * @return signature of the provided stream
	 * @throws SigningException - when a problem occurs
     */
	public String sign(InputStream in) throws SigningException {
		return Base64.encodeBase64String(getByteSignature(in)).trim();
	}

	/**
     * Creates a signature for the given stream.
     * @param in stream to be signed
     * @return signature of the provided stream
	 * @throws SigningException - when a problem occurs
     */
	public byte[] getByteSignature(InputStream in) throws SigningException {
		try {
			Signature dsa = Signature.getInstance("SHA1withDSA");

			/* Initializing the object with a private key */
			dsa.initSign(getPrivateKey());
			
			update(dsa, in);
			
			return dsa.sign();
		} catch (SigningException e) {
			throw e;
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}

	/**
	 * Updates the DSA with the bytes from this stream.
	 * @param signature signature
	 * @param in input stream
	 * @throws SigningException when the stream cannot be signed
	 * @throws SigningException - when a problem occurs
	 */
	public void update(Signature signature, InputStream in) throws SigningException {
		try {
			// Read and update the stream
			int rc = -1;
			byte[] buf = new byte[1024];
			while ((rc = in.read(buf)) >= 0) {
				if (rc != 0) signature.update(buf, 0, rc);
			}
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}
	
	/**
	 * Updates the DSA with the bytes from this stream.
	 * @param signature signature
	 * @param reader reader for the input stream
	 * @throws SigningException when the signing process fails
	 */
	protected void update(Signature signature, Reader reader) throws SigningException {
		try {
			// Read and update the stream
			int rc = -1;
			char buf[] = new char[1024];
			while ((rc = reader.read(buf)) >= 0) {
				if (rc != 0) {
					String s = new String(buf, 0, rc);
					signature.update(s.getBytes("UTF8"));
				}
			}
		} catch (Throwable t) {
			throw new SigningException(t);
		}
	}
	
	/**
     * Creates a signature for the given string.
     * @param str string to be signed
     * @return signature of the provided String
	 * @throws SigningException when the stream cannot be signed
     */
	public String sign(String str) throws SigningException {
		return sign(new StringReader(str));
    }

	/**
     * Creates a signature for the given string.
     * @param str string to be signed
     * @return signature of the provided String
	 * @throws SigningException when the stream cannot be signed
     */
	public byte[] getByteSignature(String str) throws SigningException {
		return getByteSignature(new StringReader(str));
    }

	/**
     * Creates a signature for the given bytes.
     * @param bytes bytes to be signed
     * @return signature of the provided bytes
	 * @throws SigningException when the bytes cannot be signed
     */
	public String sign(byte bytes[]) throws SigningException {
		return sign(new ByteArrayInputStream(bytes));
    }

	/**
     * Creates a signature for the given bytes.
     * @param bytes bytes to be signed
     * @return signature of the provided bytes
	 * @throws SigningException when the bytes cannot be signed
     */
	public byte[] getByteSignature(byte bytes[]) throws SigningException {
		return getByteSignature(new ByteArrayInputStream(bytes));
    }

	/**
	 * Verifies the signature on the given stream.
	 * @param signature signature
	 * @param in stream to be verified
	 * @return true if the signature confirms stream integrity
	 * @throws SigningException when the signature cannot be verified
	 */
	public boolean verify(String signature, InputStream in) throws SigningException {
		return verify(EncryptionUtils.decodeBase64(signature), in);
    }

	/**
	 * Verifies the signature on the given stream.
	 * @param signature signature
	 * @param in stream to be verified
	 * @return true if the signature confirms stream integrity
	 * @throws SigningException when the signature cannot be verified
	 */
	public boolean verify(byte signature[], InputStream in) throws SigningException {
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA");
            /* Initializing the object with the public key */
            dsa.initVerify(getPublicKey());
            
            /* Update and verify the data */
			update(dsa, in);
			
            return dsa.verify(signature);
		} catch (SigningException e) {
			throw e;
        } catch (Throwable t) {
        	throw new SigningException("Cannot verify signature: "+t.getMessage(), t);
        }
        
    }

	/**
	 * Verifies the signature on the given stream.
	 * @param signature signature
	 * @param in stream to be verified
	 * @return true if the signature confirms stream integrity
	 * @throws SigningException when the signature cannot be verified
	 */
	public boolean verify(String signature, Reader in) throws SigningException {
        return verify(EncryptionUtils.decodeBase64(signature), in);
    }
    
	/**
	 * Verifies the signature on the given stream.
	 * @param signature signature
	 * @param in stream to be verified
	 * @return true if the signature confirms stream integrity
	 * @throws SigningException when the signature cannot be verified
	 */
	public boolean verify(byte signature[], Reader in) throws SigningException {
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA");
            /* Initializing the object with the public key */
            dsa.initVerify(getPublicKey());

            /* Update and verify the data */
			update(dsa, in);
			
            return dsa.verify(signature);
		} catch (SigningException e) {
			throw e;
        } catch (Throwable t) {
        	throw new SigningException("Cannot verify signature: "+t.getMessage(), t);
        }
        
    }
    
	/**
	 * Verifies the signature on the given string.
	 * @param signature signature
	 * @param str string to be verified
	 * @return true if the signature confirms string integrity
	 * @throws SigningException when the signature cannot be verified
	 */
    public boolean verify(String signature, String str) throws SigningException {
    	return verify(signature, new StringReader(str));
    }
    
	/**
	 * Verifies the signature on the given bytes.
	 * @param signature signature
	 * @param data bytes to be verified
	 * @return true if the signature confirms data integrity
	 * @throws SigningException when the signature cannot be verified
	 */
    public boolean verify(byte signature[], byte data[]) throws SigningException {
    	return verify(signature, new ByteArrayInputStream(data));
    }
    
    
}
