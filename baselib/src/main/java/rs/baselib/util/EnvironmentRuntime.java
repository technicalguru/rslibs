/**
 * 
 */
package rs.baselib.util;

import org.slf4j.LoggerFactory;

/**
 * Keeps track of a runtime environment.
 * @author ralph
 *
 */
public class EnvironmentRuntime {

	private static Environment runtime = detectRuntime();

	/**
	 * Returns the {@link #runtime}.
	 * @return the runtime
	 */
	public static Environment getRuntime() {
		return runtime;
	}

	/**
	 * Sets the {@link #runtime}.
	 * @param newRuntime the runtime to set
	 */
	public static void setRuntime(Environment newRuntime) {
		runtime = newRuntime;
	}

	/**
	 * Detect runtime from environment variable RUNTIME_ENVIRONMENT
	 * or from system property runtime.environment.
	 * @return the runtime environment detected
	 */
	public static Environment detectRuntime() {
		String envName = null;
		try {
			envName = System.getenv("RUNTIME_ENVIRONMENT");
		} catch (SecurityException e) {
			// Ignore, we don't have permission to see this
		}
		if (envName == null) {
			try {
				envName = System.getProperty("runtime.environment");
			} catch (SecurityException e) {
				// Ignore, we don't have permission to see this
			}
		}
		if (envName == null) {
			envName = Environment.TEST.name();
		}
		try {
			return Environment.valueOf(envName);
		} catch (SecurityException e) {
			// Invalid value
			LoggerFactory.getLogger(EnvironmentRuntime.class).error("No such environment type: "+envName+" (Using "+Environment.TEST+" now)");
		}
		return Environment.TEST;
	}
}
