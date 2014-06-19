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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds release information about packages, artifacts and versions.
 * @author ralph
 *
 */
public class ReleaseInformation {

	/** The Maven Group ID key (groupId) */
	public static final String KEY_GROUP_ID = "groupId";
	/** The Maven Artifact ID key (artifactId) */
	public static final String KEY_ARTIFACT_ID = "artifactId";
	/** The Maven version key (version) */
	public static final String KEY_VERSION = "version";
	/** The Maven name key (name) */
	public static final String KEY_NAME = "name";
	/** The Subversion repository key (repository) */
	public static final String KEY_SVN_REPOSITORY = "repository";
	/** The Subversion path key (path) */
	public static final String KEY_SVN_PATH = "path";
	/** The Subversion revision key (revision) */
	public static final String KEY_SVN_REVISION = "revision";
	/** The Subversion mixed revision key (mixedRevisions) */
	public static final String KEY_SVN_MIXED_REVISIONS = "mixedRevisions";
	/** The Subversion committed revision key (committedRevision) */
	public static final String KEY_SVN_COMMITTED_REVISION = "committedRevision";
	/** The Subversion committed date key (committedDate), e.g. 2012-12-19 11:17:24 +0100 */
	public static final String KEY_SVN_COMMITTED_DATE = "committedDate"; 
	/** The Subversion status key (status) */
	public static final String KEY_SVN_STATUS = "status";
	/** The Subversion special status key (specialStatus) */
	public static final String KEY_SVN_SPECIAL_STATUS = "specialStatus";
	/** The build date, e.g. 2012-12-19 11:17:24 +0100 */
	public static final String KEY_BUILD_DATE = "buildDate"; 

	private String groupId;
	private String artifactId;
	private String version;
	private String name;
	private String svnRepository;
	private String svnPath;
	private long svnRevision;
	private boolean svnMixedRevisions;
	private long svnCommittedRevision;
	private RsDate svnCommittedDate; 
	private String svnStatus;
	private String svnSpecialStatus;
	private RsDate buildDate; 
	
	/**
	 * Constructor.
	 */
	public ReleaseInformation() {
	}

	/**
	 * Returns the groupId.
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Sets the groupId.
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * Returns the artifactId.
	 * @return the artifactId
	 */
	public String getArtifactId() {
		return artifactId;
	}

	/**
	 * Sets the artifactId.
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * Returns the version.
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the svnRepository.
	 * @return the svnRepository
	 */
	public String getSvnRepository() {
		return svnRepository;
	}

	/**
	 * Sets the svnRepository.
	 * @param svnRepository the svnRepository to set
	 */
	public void setSvnRepository(String svnRepository) {
		this.svnRepository = svnRepository;
	}

	/**
	 * Returns the svnPath.
	 * @return the svnPath
	 */
	public String getSvnPath() {
		return svnPath;
	}

	/**
	 * Sets the svnPath.
	 * @param svnPath the svnPath to set
	 */
	public void setSvnPath(String svnPath) {
		this.svnPath = svnPath;
	}

	/**
	 * Returns the svnRevision.
	 * @return the svnRevision
	 */
	public long getSvnRevision() {
		return svnRevision;
	}

	/**
	 * Sets the svnRevision.
	 * @param svnRevision the svnRevision to set
	 */
	public void setSvnRevision(long svnRevision) {
		this.svnRevision = svnRevision;
	}

	/**
	 * Returns the svnMixedRevisions.
	 * @return the svnMixedRevisions
	 */
	public boolean isSvnMixedRevisions() {
		return svnMixedRevisions;
	}

	/**
	 * Sets the svnMixedRevisions.
	 * @param svnMixedRevisions the svnMixedRevisions to set
	 */
	public void setSvnMixedRevisions(boolean svnMixedRevisions) {
		this.svnMixedRevisions = svnMixedRevisions;
	}

	/**
	 * Returns the svnCommittedRevision.
	 * @return the svnCommittedRevision
	 */
	public long getSvnCommittedRevision() {
		return svnCommittedRevision;
	}

	/**
	 * Sets the svnCommittedRevision.
	 * @param svnCommittedRevision the svnCommittedRevision to set
	 */
	public void setSvnCommittedRevision(long svnCommittedRevision) {
		this.svnCommittedRevision = svnCommittedRevision;
	}

	/**
	 * Returns the svnCommittedDate.
	 * @return the svnCommittedDate
	 */
	public RsDate getSvnCommittedDate() {
		return svnCommittedDate;
	}

	/**
	 * Sets the svnCommittedDate.
	 * @param svnCommittedDate the svnCommittedDate to set
	 */
	public void setSvnCommittedDate(RsDate svnCommittedDate) {
		this.svnCommittedDate = svnCommittedDate;
	}

	/**
	 * Sets the svnCommittedDate.
	 * @param svnCommittedDate the svnCommittedDate to set
	 */
	public void setSvnCommittedDate(Date svnCommittedDate) {
		setSvnCommittedDate(svnCommittedDate != null ? new RsDate(svnCommittedDate) : null);
	}

	/**
	 * Returns the svnStatus.
	 * @return the svnStatus
	 */
	public String getSvnStatus() {
		return svnStatus;
	}

	/**
	 * Sets the svnStatus.
	 * @param svnStatus the svnStatus to set
	 */
	public void setSvnStatus(String svnStatus) {
		this.svnStatus = svnStatus;
	}

	/**
	 * Returns the svnSpecialStatus.
	 * @return the svnSpecialStatus
	 */
	public String getSvnSpecialStatus() {
		return svnSpecialStatus;
	}

	/**
	 * Sets the svnSpecialStatus.
	 * @param svnSpecialStatus the svnSpecialStatus to set
	 */
	public void setSvnSpecialStatus(String svnSpecialStatus) {
		this.svnSpecialStatus = svnSpecialStatus;
	}

	/**
	 * Returns the buildDate.
	 * @return the buildDate
	 */
	public RsDate getBuildDate() {
		return buildDate;
	}

	/**
	 * Sets the buildDate.
	 * @param buildDate the buildDate to set
	 */
	public void setBuildDate(RsDate buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * Sets the buildDate.
	 * @param buildDate the buildDate to set
	 */
	public void setBuildDate(Date buildDate) {
		setBuildDate(buildDate != null ? new RsDate(buildDate) : null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((artifactId == null) ? 0 : artifactId.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ReleaseInformation)) {
			return false;
		}
		ReleaseInformation other = (ReleaseInformation) obj;
		if (artifactId == null) {
			if (other.artifactId != null) {
				return false;
			}
		} else if (!artifactId.equals(other.artifactId)) {
			return false;
		}
		if (groupId == null) {
			if (other.groupId != null) {
				return false;
			}
		} else if (!groupId.equals(other.groupId)) {
			return false;
		}
		if (version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!version.equals(other.version)) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the information with given key.
	 * @param key key string
	 * @param value value string (will be converted)
	 */
	public void set(String key, String value) throws ParseException {
		if ((value != null) && value.startsWith("${")) value = null;
		if (value == null) return;
		DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		
		if (isKey(key, KEY_GROUP_ID))                    setGroupId(value);
		else if (isKey(key, KEY_ARTIFACT_ID))            setArtifactId(value);
		else if (isKey(key, KEY_VERSION))                setVersion(value);
		else if (isKey(key, KEY_NAME))                   setName(value);
		else if (isKey(key, KEY_SVN_REPOSITORY))         setSvnRepository(value);
		else if (isKey(key, KEY_SVN_PATH))               setSvnPath(value);
		else if (isKey(key, KEY_SVN_REVISION))           setSvnRevision(Long.parseLong(value));
		else if (isKey(key, KEY_SVN_MIXED_REVISIONS))    setSvnMixedRevisions(value.equalsIgnoreCase("true"));
		else if (isKey(key, KEY_SVN_COMMITTED_REVISION)) setSvnCommittedRevision(Long.parseLong(value));
		else if (isKey(key, KEY_SVN_COMMITTED_DATE))     setSvnCommittedDate(DATE_FORMATTER.parse(value));
		else if (isKey(key, KEY_SVN_STATUS))             setSvnStatus(value);
		else if (isKey(key, KEY_SVN_SPECIAL_STATUS))     setSvnSpecialStatus(value);
		else if (isKey(key, KEY_BUILD_DATE))             setBuildDate(DATE_FORMATTER.parse(value));
	}
	
	/**
	 * Returns true when the given key ends with or is equal the given suffix.
	 * @param key key to check
	 * @param suffix suffix to match
	 * @return true when the key ends with the suffix
	 */
	private static boolean isKey(String key, String suffix) {
		return key.endsWith("."+suffix) || key.equals(suffix);
	}
	
	/**
	 * Checks whether this info is valid.
	 * @return true when the key information is present.
	 */
	public boolean isValid() {
		return (getGroupId() != null) && (getArtifactId() != null) && (getVersion() != null);
	}
	
	/**
	 * Returns whether this is a Snapshot version.
	 * <p>The method returns <code>true</code> when the version is either <code>null</code> or
	 * the version ends with "-SNAPSHOT".</p>
	 * @return <code>true</code> when this is a snapshot version
	 * @since 1.2.4
	 */
	public boolean isSnapshot() {
		String version = getVersion();
		return (version == null) || version.endsWith("-SNAPSHOT"); 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName()+"["+getGroupId()+":"+getArtifactId()+"] - V"+getVersion();
	}
	
	
}
