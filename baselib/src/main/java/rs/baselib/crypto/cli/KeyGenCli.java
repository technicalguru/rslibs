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

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;

/**
 * CLI execution for KeyGen.
 * @author ralph
 *
 */
@Command(version = "1.0", name = "KeyGenCli", subcommands = {
		HelpCommand.class,
		CmdList.class,
		CmdGenerate.class,
	})
public class KeyGenCli {

	private CommandLine cli;

	@Option(names = {"-v", "--version"}, versionHelp = true, description = "display version info")
	boolean versionInfoRequested;

	@Option(names = {"-h", "--help"}, usageHelp = true, description = "display this help message")
	boolean usageHelpRequested;
	
	/**
	 * Returns the cli.
	 * @return the cli
	 */
	public CommandLine getCli() {
		return cli;
	}

	/**
	 * Sets the cli.
	 * @param cli the cli to set
	 */
	public void setCli(CommandLine cli) {
		this.cli = cli;
	}
	
	
}
