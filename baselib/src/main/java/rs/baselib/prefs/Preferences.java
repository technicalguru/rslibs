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
package rs.baselib.prefs;

import java.util.concurrent.locks.Lock;

/**
 * Default implementation of preferences.
 * @author ralph
 *
 */
public class Preferences extends AbstractPreferences {

	/**
	 * Constructor.
	 */
	public Preferences(AbstractPreferences parent, String name) {
		super(parent, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractPreferences createNode(AbstractPreferences parent, String name) {
		return new Preferences(parent, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractPreferencesService getPreferencesService() {
		return (PreferencesService)PreferencesService.INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Lock createReadLock() {
		return PreferencesService.INSTANCE.getReadLock(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Lock createWriteLock() {
		return PreferencesService.INSTANCE.getWriteLock(this);
	}

	
}
