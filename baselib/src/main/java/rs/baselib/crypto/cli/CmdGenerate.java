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

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.ParentCommand;

/**
 * Command generate.
 * @author ralph
 *
 */
@Command(name = "generate", aliases = { "gen", "g" } , description = "Generate keys and passwords", subcommands = {
		HelpCommand.class, 
		CmdGeneratePassword.class,
		CmdGenerateKeyPair.class,
		CmdGenerateSecretKey.class,
})
public class CmdGenerate {

	@ParentCommand
	private KeyGenCli cmdMain;
	
}
