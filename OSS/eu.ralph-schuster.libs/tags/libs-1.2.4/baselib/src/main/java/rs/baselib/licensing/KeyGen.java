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
package rs.baselib.licensing;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.util.CommonUtils;

/**
 * A RSA Key Generator.
 * @author ralph
 *
 */
public class KeyGen implements Runnable {

	private String algorithm;
	private int length;
	private boolean overwrite;
	private String file;
	private KeyPair keyPair;

	public KeyGen(String algorithm, int length, String file, boolean overwrite) {
		if (algorithm == null) algorithm = "RSA";
		if (length == 0) length = 2048;
		this.algorithm = algorithm;
		this.length = length;
		this.file = file;
		this.overwrite = overwrite;
	}

	/**
	 * Returns the {@link #algorithm}.
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * Returns the {@link #length}.
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the {@link #file}.
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Returns the {@link #keyPair}.
	 * @return the keyPair
	 */
	public KeyPair getKeyPair() {
		return keyPair;
	}

	/**
	 * Returns the {@link #overwrite}.
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * Generates and saved the key.
	 */
	public void run() {
		try {
			System.out.println("Creating "+getAlgorithm()+" key with "+getLength()+" bit:");
			keyPair = EncryptionUtils.generateKey(getAlgorithm(), getLength());
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			
			File f = new File(getFile()+".public");
			if (f.exists() && !isOverwrite()) {
				System.out.println("   "+f.getAbsolutePath()+" already exists");
				System.exit(-1);
			}
			EncryptionUtils.save(f, publicKey);
			System.out.println("    Public Key: "+f.getAbsolutePath());
			
			f = new File(getFile()+".private");
			if (f.exists() && !isOverwrite()) {
				System.out.println("   "+f.getAbsolutePath()+" already exists");
				System.exit(-1);
			}
			EncryptionUtils.save(f, privateKey);
			System.out.println("   Private Key: "+f.getAbsolutePath());
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	/**
	 * CLI.
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = getOptions();
		String algorithm = null;
		String file = null;
		int length = 0;
		boolean overwrite = false;
		
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("a")) {
				algorithm = cmd.getOptionValue("a");
			}

			if (cmd.hasOption("l")) {
				length = Integer.parseInt(cmd.getOptionValue("l"));
			}

			file = cmd.getOptionValue("n");
			if (CommonUtils.isEmpty(file, true)) {
				printHelp(options);
				System.exit(-1);
			}
			
			overwrite = cmd.hasOption("f");
		} catch (ParseException e) {
			printHelp(options);
			System.exit(-1);
		}

		try {
			KeyGen keygen = new KeyGen(algorithm, length, file, overwrite);
			keygen.run();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	/**
	 * Prints the CLI help text.
	 * @param options options for CLI
	 */
	protected static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "KeyGen", options );
	}

	/**
	 * Creates the CLI options.
	 * @return the CLI options
	 */
	protected static Options getOptions() {
		Options rc = new Options();

		Option option = new Option("a", "algorithm", true, "The algorithm to create the key (default: RSA)");
		option.setOptionalArg(true);
		rc.addOption(option);

		option = new Option("n", "name", true, "name of key");
		option.setOptionalArg(false);
		rc.addOption(option);

		option = new Option("l", "length", true, "the length of the key (default: 2048)");
		option.setOptionalArg(true);
		rc.addOption(option);

		option = new Option("f", "force", false, "overwrite any existing file (default: false)");
		option.setOptionalArg(false);
		rc.addOption(option);

		return rc;
	}
}
