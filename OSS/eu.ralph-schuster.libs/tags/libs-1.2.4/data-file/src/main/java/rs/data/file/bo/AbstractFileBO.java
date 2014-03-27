/*
 * This file is part of RS Library (Data File Library).
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
package rs.data.file.bo;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Transient;

import rs.data.impl.bo.AbstractMapBO;
import rs.data.impl.dto.MapDTO;

/**
 * A Business Object with a File underneath.
 * @author ralph
 *
 */
public abstract class AbstractFileBO<K extends Serializable> extends AbstractMapBO<K> {

	/** Serial UID */
	private static final long serialVersionUID = 1L;
	/** The file that we are connected with */
	private File file;
	
	/**
	 * Constructor.
	 */
	public AbstractFileBO() {
		this(new MapDTO<K>());
	}

	/**
	 * Constructor.
	 */
	public AbstractFileBO(MapDTO<K> dto) {
		super(dto);
	}

	/**
	 * Returns the {@link #file}.
	 * @return the file
	 */
	@Transient
	public File getFile() {
		return file;
	}

	/**
	 * Sets the {@link #file}.
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

}
