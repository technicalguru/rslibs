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

import java.security.Provider;
import java.security.Provider.Service;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

/**
 * Sub command "list"
 * 
 * @author ralph
 *
 */
@Command(name = "list", aliases = { "l" }, description = "List security providers", subcommands = { HelpCommand.class })
public class CmdList implements Callable<Integer> {

	@ParentCommand
	private KeyGenCli cmdMain;
	
	@Option(names = "-n", description = "Name of provider type to be listed (e.g. KeyGenerator, SecretKeyFactory)")
	private String name;
	
	@Override
	public Integer call() throws Exception {
		if (name != null) {
			System.out.println("Available algorithms for "+name+":");
			System.out.println();
			for (Provider provider : Security.getProviders()) {
				for (Service service : provider.getServices()) {
					if (name.equalsIgnoreCase(service.getType())) System.out.println("   - "+service.getAlgorithm());
				}
			}
		} else {
			System.out.println("List of provider types:");
			System.out.println();
			List<String> types = new ArrayList<>();
			for (Provider provider : Security.getProviders()) {
				for (Service service : provider.getServices()) {
					if (!types.contains(service.getType())) types.add(service.getType());
				}
			}
			Collections.sort(types);
			for (String t : types) {
				System.out.println("   - "+t);
			}
		}
		System.out.println();
		return 0;
	}	
}
