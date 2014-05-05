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
import java.security.Key;
import java.security.PrivateKey;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.time.DateUtils;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.util.CommonUtils;

/**
 * A universal generator for creating RSA licenses.
 * @author ralph
 *
 */
public class LicGen implements Runnable {

	private static LicGen licgen;
	
	private String product;
	private long expirationTime;
	private String owner;
	private String licenseKey;
	
	/**
	 * Constructor.
	 */
	public LicGen(String product, long expirationTime, String owner) {
		this.product = product;
		this.expirationTime = expirationTime;
		this.owner = owner;
	}

	
	/**
	 * Returns the {@link #licenseKey}.
	 * @return the licenseKey
	 */
	public String getLicenseKey() {
		return licenseKey;
	}


	public void run() {
		try {
			System.out.print("Generating \""+product+"\" license (");
			if (expirationTime > 0) {
				System.out.print("expires "+CommonUtils.DATE_FORMATTER().format(new Date(expirationTime)));
			} else {
				System.out.print("unlimited");
			}
			System.out.println("):");
						
			File f = new File(product + ".private");
			PrivateKey privateKey = EncryptionUtils.loadPrivateKey(f, "RSA");
			
			rs.baselib.licensing.LicenseGenerator licgen = new rs.baselib.licensing.LicenseGenerator();
			ILicenseContext context = new DefaultLicenseContext();
			context.set(ILicense.PRODUCT_KEY, product);
			context.set(ILicense.EXPIRATION_DATE_KEY, expirationTime);
			context.set(ILicense.OWNER_KEY, owner);
			context.set(Key.class, privateKey);
			licenseKey = licgen.createLicenseKey(context);
			System.out.println(licenseKey);
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
		String product = null;
		long expirationTime = 0L;
		String owner = null;
		
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			product = cmd.getOptionValue("p");
			owner = cmd.getOptionValue("o");
			expirationTime = parseTime(cmd.getOptionValue("e"));
			
			if (CommonUtils.isEmpty(product, true) || CommonUtils.isEmpty(owner, true)) {
				printHelp(options);
				System.exit(-1);
			}
			
		} catch (ParseException e) {
			printHelp(options);
			System.exit(-1);
		}

		try {
			licgen = new LicGen(product, expirationTime, owner);
			licgen.run();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	/**
	 * Prints the CLI help.
	 * @param options CLI options.
	 */
	protected static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "KeyGen", options );
	}

	/**
	 * Creates the CLI options.
	 * @return CLI options
	 */
	public static Options getOptions() {
		Options rc = new Options();

		Option option = new Option("e", "expiry", true, "time of expiry (dd.MM.yyyy or days)");
		option.setOptionalArg(false);
		rc.addOption(option);

		option = new Option("p", "product", true, "name of product (\"rsbudget\", ...");
		option.setOptionalArg(false);
		rc.addOption(option);

		option = new Option("o", "owner", true, "license owner");
		option.setOptionalArg(false);
		rc.addOption(option);
		return rc;
	}

	protected static long parseTime(String s) {
		if (s == null) return 0L;
		try {
			return CommonUtils.DATE_FORMATTER().parse(s).getTime();
		} catch (java.text.ParseException e) {
			
		}
		
		int days = Integer.parseInt(s);
		return System.currentTimeMillis()+days*DateUtils.MILLIS_PER_DAY;
	}

	/**
	 * Returns the {@link #licgen}.
	 * @return the licgen
	 */
	public static LicGen getLicgen() {
		return licgen;
	}

	/**
	 * Returns the {@link #product}.
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * Returns the {@link #expirationTime}.
	 * @return the expirationTime
	 */
	public long getExpirationTime() {
		return expirationTime;
	}

	/**
	 * Returns the {@link #owner}.
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	
	
}
