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
package rs.baselib.crypto.cli;

import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.crypto.KeyGen;

/**
 * Subcommand "generate secret-key".
 * @author ralph
 *
 */
@Command(name = "secret-key", aliases = { "key", "sk", "k" } , description = "Generate a PBE secret key", subcommands = { HelpCommand.class })
public class CmdGenerateSecretKey implements Callable<Integer> {

	@ParentCommand
	private CmdGenerate cmdGenerate;
	
	@Option(names = { "--iterations", "-i" }, description = "number of iterations for PBE key", defaultValue = "19")
	private int iterations;
	
	@Option(names = { "--passphrase", "-p" }, description = "passphrase for secret", required = true)
	private String passphrase;
	
	@Option(names = { "--num", "-n" }, description = "number of passwords to generate", defaultValue = "1")
	private int num;
	
	@Override
	public Integer call() throws Exception {
		for (int i=0; i<num; i++) {
			System.out.println("#"+(i+1)+": "+EncryptionUtils.encodeBase64(KeyGen.generateSecretKey(iterations, passphrase, null).getEncoded()));
		}
		return 0;
	}	
}
