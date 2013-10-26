package rsbaselib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import rs.baselib.io.FileFinder;

/**
 * @author ralph
 *
 */
public class GenerateCountryList {

	/**
	 * Constructor.
	 */
	@Test
	public void generateCountryList() throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(FileFinder.open("countries.csv"), Charset.forName("UTF-8")));
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = r.readLine()) != null) {
			String arr[] = line.split(";");
			lines.add("\t"+capitalizeCountry(arr[2])+"(Continent."+getContinent(arr[1])+", \""+sanitizeCapital(arr[3])+"\","+
			          "\""+arr[4].trim()+"\", \""+arr[5].trim()+"\", \""+arr[9]+"\", \""+arr[7]+"\")");
		}
		Collections.sort(lines);
		for (String s : lines) {
			System.out.println(s+",");
		}
	}

	private String capitalizeCountry(String country) {
		country = country.trim().toUpperCase().replaceAll("['\\(\\)\\s,\\.\\-]+", "_").replaceAll("_$", "");
		return country;
	}
	
	private String getContinent(String s) {
		s = s.trim();
		if (s.equalsIgnoreCase("Europe")) return "EUROPE";
		if (s.equalsIgnoreCase("North America")) return "NORTH_AMERICA";
		if (s.equalsIgnoreCase("South America")) return "SOUTH_AMERICA";
		if (s.equalsIgnoreCase("Africa")) return "AFRICA";
		if (s.equalsIgnoreCase("Asia")) return "ASIA";
		if (s.equalsIgnoreCase("Australia")) return "AUSTRALIA";
		if (s.equalsIgnoreCase("Antarctica")) return "ANTARCTICA";
		return "";
	}
	
	private String sanitizeCapital(String s) {
		s = unicode(s.trim());
		return s;
	}
	
	private String unicode(String s) {
		StringBuilder buf = new StringBuilder();
		for (char c : s.toCharArray()) {
			buf.append(unicode(c));
		}
		return buf.toString();
	}
	
	private String unicode(char c) {
		if (c <= '\u007F') {
			return ""+c;
		}
		return("\\u"+Integer.toHexString(c | 0x10000).substring(1));
	}
}
