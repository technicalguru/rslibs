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

/**
 * Implements a combined strategy to hash passwords.
 * <p>The object will use a {@link #getDefaultPasswordHasher() default} {@link ExtendedPasswordHasher} in order
 * to create hashes but will use all defined hashers to test a given password hash.
 * @author ralph
 * @see ExtendedPasswordHasher
 * @since 1.2.9
 *
 */
public class CombinedPasswordHasher implements PasswordHasher {

	/**
	 * A UNIX strategy to hash and check passwords (with {@link Md5PasswordHasher} as default).
	 * <p>This instance used (in order of check):</p>
	 * <ul>
	 * <li>{@link Md5PasswordHasher}</li>
	 * <li>{@link BlowfishPasswordHasher}</li>
	 * <li>{@link PhpPasswordHasher}</li>
	 * <li>{@link DummyPasswordHasher}</li>
	 * </ul>
	 */
	public static final PasswordHasher UNIX_STRATEGY_MD5 = 
			new CombinedPasswordHasher(Md5PasswordHasher.INSTANCE, BlowfishPasswordHasher.INSTANCE, PhpPasswordHasher.INSTANCE, DummyPasswordHasher.INSTANCE);
	
	/**
	 * A UNIX strategy to hash and check passwords (with {@link BlowfishPasswordHasher} as default).
	 * <p>This instance used (in order of check):</p>
	 * <ul>
	 * <li>{@link BlowfishPasswordHasher}</li>
	 * <li>{@link Md5PasswordHasher}</li>
	 * <li>{@link PhpPasswordHasher}</li>
	 * <li>{@link DummyPasswordHasher}</li>
	 * </ul>
	 */
	public static final PasswordHasher UNIX_STRATEGY_BLOWFISH = 
			new CombinedPasswordHasher(BlowfishPasswordHasher.INSTANCE, Md5PasswordHasher.INSTANCE, PhpPasswordHasher.INSTANCE, DummyPasswordHasher.INSTANCE);
	
	/**
	 * A UNIX strategy to hash and check passwords (with {@link PhpPasswordHasher} as default).
	 * <p>This instance used (in order of check):</p>
	 * <ul>
	 * <li>{@link PhpPasswordHasher}</li>
	 * <li>{@link Md5PasswordHasher}</li>
	 * <li>{@link BlowfishPasswordHasher}</li>
	 * <li>{@link DummyPasswordHasher}</li>
	 * </ul>
	 */
	public static final PasswordHasher UNIX_STRATEGY_PHP = 
			new CombinedPasswordHasher(PhpPasswordHasher.INSTANCE, Md5PasswordHasher.INSTANCE, BlowfishPasswordHasher.INSTANCE, DummyPasswordHasher.INSTANCE);
	
	
	/** all hashers */
	private ExtendedPasswordHasher passwordHashers[] = null;
	
	/**
	 * Constructor.
	 * @param passwordHashers the list of hash algorithms to be used. the first algorithm will be used for hashing a password (default hasher).
	 */
	public CombinedPasswordHasher(ExtendedPasswordHasher... passwordHashers) {
		if ((passwordHashers == null) || (passwordHashers.length == 0)) throw new IllegalArgumentException("No hashers given");
		this.passwordHashers = passwordHashers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPasswordHash(String plainPassword) {
		return getDefaultPasswordHasher().getPasswordHash(plainPassword);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean testPassword(String plainPassword, String passwordHash) {
		for (ExtendedPasswordHasher hasher : passwordHashers) {
			if (hasher.isHash(passwordHash)) {
				return hasher.testPassword(plainPassword, passwordHash);
			}
		}
		return false;
	}

	/**
	 * Returns the password hashers used.
	 * @return the password hashers
	 */
	public ExtendedPasswordHasher[] getPasswordHashers() {
		return passwordHashers;
	}

	/**
	 * Returns the default password hasher, that is the hasher used for hashing passwords.
	 * @return the default password hasher
	 */
	public ExtendedPasswordHasher getDefaultPasswordHasher() {
		return passwordHashers[0];
	}

	
}
