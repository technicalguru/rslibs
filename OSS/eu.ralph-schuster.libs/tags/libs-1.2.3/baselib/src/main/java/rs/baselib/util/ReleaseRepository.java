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
package rs.baselib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.JarDescriptor;
import rs.baselib.lang.ResourceList;

/**
 * Holds information about packages and modules.
 * @author ralph
 *
 */
public class ReleaseRepository {

	/** The instance of the repository */
	public static ReleaseRepository INSTANCE = new ReleaseRepository();

	public static final String BASELIB_GROUP_ID = "eu.ralph-schuster";
	public static final String BASELIB_ARTIFACT_ID = "baselib";
	public static final String BUNDLE_ARTIFACT_ID = "lib-bundle";

	private static Logger log = LoggerFactory.getLogger(ReleaseRepository.class);

	private Properties properties = new Properties();
	private Map<String, Map<String, Map<String, ReleaseInformation>>> releaseInformation = new HashMap<String, Map<String,Map<String,ReleaseInformation>>>();

	/**
	 * Constructor.
	 */
	public ReleaseRepository() {
		try {
			Collection<JarDescriptor> descs = ResourceList.getJars();
			for (JarDescriptor desc : descs) addJar(desc);
			Collection<File> dirs = ResourceList.getDirectories();
			for (File dir : dirs) addProperties(new File(dir, "build.properties"));
		} catch (IOException e) {
			log.error("Cannot load baselib version information: ", e);
		}
	}

	/**
	 * Adds the manifest information.
	 * @param manifest
	 */
	private void addJar(JarDescriptor desc) throws IOException {
		Manifest manifest = desc.getManifest();
		boolean added = false;
		if (manifest != null) {
			Attributes attr = manifest.getAttributes("eu.ralph-schuster.baselib");
			if (attr != null) {
				String file = attr.getValue("Package-File");
				if (file != null) added = addProperties(new URL(desc.getUrlPrefix()+file));
			}
		}
		
		if (!added) try {
			// Try to add build.properties
			addProperties(new URL(desc.getUrlPrefix()+"build.properties"));
		} catch (IOException e) {
			// Ignore: no such info
		}
	}

	/**
	 * Add properties from given File and parse
	 * release information with given prefix.
	 * @param file file load from
	 * @return true when the information could be added
	 * @throws IOException when the stream cannot be read. 
	 */
	public boolean addProperties(File file) throws IOException {
		boolean rc = false;
		if (file.exists() && file.canRead()) {
			InputStream in = new FileInputStream(file);
			rc = addProperties(in);
			in.close();
		}
		return rc;
	}
	
	/**
	 * Add properties from given URL and parse
	 * release information with given prefix.
	 * @param url url to load from
	 * @return true when the information could be added
	 * @throws IOException when the stream cannot be read. 
	 */
	public boolean addProperties(URL url) throws IOException {
		if (url == null) return false;
		InputStream in = url.openStream();
		boolean rc = addProperties(in);
		in.close();
		return rc;
	}

	/**
	 * Add properties from given stream and parse
	 * release information with given prefix.
	 * @param in stream
	 * @return true when the information could be added
	 * @throws IOException when the stream cannot be read. 
	 */
	public boolean addProperties(InputStream in) throws IOException {
		Properties newProps = new Properties();
		newProps.load(in);
		ReleaseInformation rc = addReleaseInformation(newProps);
		return rc != null;
	}

	/**
	 * Parse release information in repository using the given properties.
	 * @param props the properties to be parsed
	 * @return the information found or null
	 */
	public ReleaseInformation addReleaseInformation(Properties props) {
		ReleaseInformation rc = new ReleaseInformation();
		for (Object o : props.keySet()) {
			String key = (String)o;
			try {
				rc.set(key, props.getProperty(key));
				properties.setProperty(key, props.getProperty(key));
			} catch (ParseException e) {
				if (log != null) {
					log.error("Cannot read \""+key+"\": "+props.get(key), e);
				} else {
					System.out.println("Cannot read \""+key+"\": "+props.get(key));
					e.printStackTrace(System.out);
				}
			}
		}
		if (addReleaseInformation(rc)) return rc;
		return null;
	}

	/**
	 * Add the given information to the repository.
	 * @param info info to be added.
	 * @return true when the information could be added
	 */
	public boolean addReleaseInformation(ReleaseInformation info) {
		if (!info.isValid()) return false;
		
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(info.getGroupId());
		if (groupInfo == null) {
			groupInfo = new HashMap<String, Map<String,ReleaseInformation>>();
			releaseInformation.put(info.getGroupId(), groupInfo);
		}

		Map<String, ReleaseInformation> artifactInfo = groupInfo.get(info.getArtifactId());
		if (artifactInfo == null) {
			artifactInfo = new HashMap<String, ReleaseInformation>();
			groupInfo.put(info.getArtifactId(), artifactInfo);
		}

		artifactInfo.put(info.getVersion(), info);
		return true;
	}

	/**
	 * Returns all group IDs.
	 * @return collection with group IDs
	 */
	public Collection<String> getGroups() {
		return releaseInformation.keySet();
	}

	/**
	 * Returns the information for the given artifact.
	 * @param groupId group ID
	 * @param artifactId artifact ID
	 * @param version version
	 * @return release information or NULL
	 */
	public ReleaseInformation getReleaseInformation(String groupId, String artifactId, String version) {
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(groupId);
		if (groupInfo == null) return null;
		Map<String, ReleaseInformation> artifactInfo = groupInfo.get(artifactId);
		if (artifactInfo == null) return null;
		return artifactInfo.get(version);
	}

	/**
	 * Returns all artifacts.
	 * @return collection with group IDs
	 */
	public Collection<ReleaseInformation> getAllInfos() {
		Set<ReleaseInformation> rc = new HashSet<ReleaseInformation>();
		for (String groupId : getGroups()) {
			for (String artifactId : getArtifacts(groupId)) {
				for (String version : getVersions(groupId, artifactId)) {
					rc.add(getReleaseInformation(groupId, artifactId, version));
				}
			}
		}
		return rc;
	}

	/**
	 * Returns all artifact versions.
	 * @return collection with release information
	 */
	public Collection<ReleaseInformation> getArtifactInfos(String groupId) {
		Set<ReleaseInformation> rc = new HashSet<ReleaseInformation>();
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(groupId);
		if (groupInfo == null) return rc;
		for (String artifactId : groupInfo.keySet()) {
			rc.addAll(getVersionInfos(groupId, artifactId));
		}
		return rc;
	}

	/**
	 * Returns all artifact versions.
	 * @return collection with release information
	 */
	public Collection<ReleaseInformation> getVersionInfos(String groupId, String artifactId) {
		Set<ReleaseInformation> rc = new HashSet<ReleaseInformation>();
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(groupId);
		if (groupInfo == null) return rc;
		Map<String, ReleaseInformation> artifactInfo = groupInfo.get(artifactId);
		if (artifactInfo == null) return rc;
		rc.addAll(artifactInfo.values());
		return rc;
	}

	/**
	 * Returns all artifact IDs of a group.
	 * @param groupId group ID to return
	 * @return collection with artifact IDs
	 */
	public Collection<String> getArtifacts(String groupId) {
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(groupId);
		if (groupInfo == null) return Collections.emptySet();
		return groupInfo.keySet();
	}

	/**
	 * Returns all versions of an artifact.
	 * @param groupId group ID to return
	 * @param artifactId artifact ID to return
	 * @return collection with versions
	 */
	public Collection<String> getVersions(String groupId, String artifactId) {
		Map<String, Map<String, ReleaseInformation>> groupInfo = releaseInformation.get(groupId);
		if (groupInfo == null) return Collections.emptySet();
		Map<String, ReleaseInformation> artifactInfo = groupInfo.get(artifactId);
		if (artifactInfo == null) return Collections.emptySet();
		return artifactInfo.keySet();
	}
}
