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

import java.security.KeyPair;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.crypto.KeyGen;

/**
 * Subcommand "generate key-pair".
 * @author ralph
 *
 */
@Command(name = "key-pair", aliases = { "keypair", "pair", "kp" } , description = "Generate a key pair", subcommands = { HelpCommand.class })
public class CmdGenerateKeyPair implements Callable<Integer> {

	@ParentCommand
	private CmdGenerate cmdGenerate;

	@Option(names = { "--num", "-n" }, description = "number of passwords to generate", defaultValue = "1")
	private int num;
	
	@Option(names = { "--algorithm", "-a" }, description = "algorithm for new key pair", defaultValue = KeyGen.DEFAULT_KEY_ALGORITHM)
	private String algorithm;
	
	@Option(names = { "--key-size", "-s", "--length", "-l", "--size" }, description = "key size of new key pair")
	private int keySize;
	
	@Override
	public Integer call() throws Exception {
		for (int i=0; i<num; i++) {
			KeyPair pair = KeyGen.generateKeyPair(algorithm, keySize);
			System.out.println("#"+(i+1)+": private key: "+EncryptionUtils.encodeBase64(pair.getPrivate()));
			System.out.println("#"+(i+1)+": public key:  "+EncryptionUtils.encodeBase64(pair.getPublic()));
			System.out.println("#"+(i+1)+": thumbprint:  "+EncryptionUtils.getThumbprint(pair.getPublic()));
			System.out.println();
		}
		return 0;
	}	
}
