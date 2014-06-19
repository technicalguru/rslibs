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
package rs.baselib.util;


/**
 * A definition of log levels for internal uses.
 * @author ralph
 *
 */
public enum LogLevel {

	/** SLF4J Trace level. See {@link Logger#trace(String)}. */
	TRACE,
	/** SLF4J Debug level. See {@link Logger#debug(String)}. */
	DEBUG,
	/** SLF4J Info level. See {@link Logger#info(String)}. */
	INFO,
	/** SLF4J Warning level. See {@link Logger#warn(String)}. */
	WARN,
	/** SLF4J Error level. See {@link Logger#error(String)}. */
	ERROR
}
