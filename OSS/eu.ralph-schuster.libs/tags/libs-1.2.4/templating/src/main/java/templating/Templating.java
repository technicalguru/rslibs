/*
 * This file is part of RS Library (Templating Library).
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
package templating;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rs.baselib.util.CommonUtils;

/**
 * Implements templating methods similar to typo3
 * Simple tag markers must be upper-case. The must appear in
 * templates embraced by three hash signs, e.g. ###MARKER###
 * @author RalphSchuster
 *
 */
public class Templating {

	/**
	 * Replaces a simple marker by its value.
	 * Simple tag markers must be upper-case. The must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param marker - marker name, e.g. MARKER
	 * @param value - value to be replaced
	 * @return the template string with replacements of all markers
	 */
	public static String replace(String template, String marker, Object value) {
		if (value == null) value = "";
		String v = value.toString();
		v = v.replaceAll("\\(", "\\\\(");
		v = v.replaceAll("\\)", "\\\\)");
		v = v.replaceAll("\\$", "\\\\\\$");
		return template.replaceAll("###"+marker.toUpperCase()+"###", v);
	}
	
	/**
	 * Replaces multiple simple tag markers.
	 * Simple tag markers must be upper-case. The must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param markers - markers map
	 * @return - replaced template text
	 */
	public static String replace(String template, Map<String,Object> markers) {
		if (template == null) return null;
		
		// Iterate while there are changes
		// Necessary due to unpredictable iterating order
		while (true) {
			String pre = template;
			Iterator<String> i = markers.keySet().iterator();
			while (i.hasNext()) {
				String marker = (String)i.next();
				Object value  = markers.get(marker);
				if (value == null) value = "";
				// value needs to be adjusted
				String v = value.toString();
				v = v.replaceAll("\\(", "\\\\(");
				v = v.replaceAll("\\)", "\\\\)");
				v = v.replaceAll("\\$", "\\\\\\$");
				template = template.replaceAll("###"+marker.toUpperCase()+"###", v);
			}
			if (pre.equals(template)) break;
		}
		return template;
	}
	
	/**
	 * Returns a sub template.
	 * Sub templates are enclosed by XML comment tags as known from TYPO3:
	 * &lt;!-- ###MARKER### begin --&gt; my sub template text &lt;!-- ###MARKER### end --&gt;
	 * Sub templates of same name are not allowed 
	 * @param template - parent template
	 * @param name - name of sub template, e.g. MARKER
	 * @return sub template, or null if not found
	 */
	public static String getSubTemplate(String template, String name) {
		String startTag = "<!--\\s*###"+name+"###\\s+begin\\s*-->\\s*\\n?";
		String endTag   = "<!--\\s*###"+name+"###\\s+end\\s*-->";
		String pattern = startTag+"(.*)"+endTag;
		Pattern p = Pattern.compile(".*"+pattern+".*", Pattern.DOTALL);
		Matcher m = p.matcher(template);
		if (m.matches()) return m.group(1);
		return null;
	}
	
	/**
	 * Returns a template from a file.
	 * @param filename - filename
	 * @return template's content
	 * @throws IOException when an error occurred
	 */
	public static String getTemplate(String filename) throws IOException {
		return getTemplate(filename);
	}
	
	/**
	 * Returns a template from a file.
	 * @param f - file object to load from
	 * @return template's content
	 * @throws IOException when an error occurred
	 */
	public static String getTemplate(File f) throws IOException {
		return CommonUtils.loadContent(f);
	}
}
