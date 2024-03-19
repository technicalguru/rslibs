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

/**
 * Subcommand "generate password".
 * @author ralph
 *
 */
@Command(name = "password", aliases = { "passwd", "pwd", "p" } , description = "Generate a password", subcommands = { HelpCommand.class })
public class CmdGeneratePassword implements Callable<Integer> {

	@ParentCommand
	private CmdGenerate cmdGenerate;
	
	@Option(names = { "--allowed-chars", "--chars", "-c" }, description = "allowed characters in password", defaultValue = EncryptionUtils.PASSWORD_CHARS)
	private String allowedChars;
	
	@Option(names = { "--length", "-l" }, description = "length of password", defaultValue = "12")
	private int length;
	
	@Option(names = { "--num", "-n" }, description = "number of passwords to generate", defaultValue = "1")
	private int num;
	
	@Override
	public Integer call() throws Exception {
		for (int i=0; i<num; i++) {
			System.out.println("#"+(i+1)+": "+EncryptionUtils.generatePassword(allowedChars, 0, length));
		}
		return 0;
	}	
}
