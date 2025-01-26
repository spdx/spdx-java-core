/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

/**
 * Common constants used in the SPDX core library
 *
 * @author Gary O'Neall
 */
public class SpdxCoreConstants {

	/**
	 * Enumeration representing the major versions of the SPDX specification
	 */
	@SuppressWarnings("unused")
    public enum SpdxMajorVersion {
		/**
		 * SPDX version 1.x
		 */
		VERSION_1("SPDX-1."),

		/**
		 * SPDX version 2.x
		 */
		VERSION_2("SPDX-2."),

		/**
		 * SPDX version 3.x
		 */
		VERSION_3("3.");

		private final String prefix;

		/**
		 * Returns the latest major version of the SPDX specification.
		 *
		 * @return the latest major version
		 */
		public static SpdxMajorVersion latestVersion() {
			return VERSION_3;
		}
		
		SpdxMajorVersion(String prefix) {
			this.prefix = prefix;
		}

		/**
		 * @return the string prefix used in all version strings
		 */
		String prefix() {
			return prefix;
		}
	}

	/**
	 * The URL for the SPDX Listed Licenses.
 	 */
	public static final String LISTED_LICENSE_URL = "https://spdx.org/licenses/";

	/**
	 * The namespace prefix for the SPDX Listed Licenses.
	 * <p>
	 * Note: This uses "http" rather than "https" since RDF depends on the exact string,
	 * and we were not able to update the namespace variable to match the URL's.
	 */
	public static final String LISTED_LICENSE_NAMESPACE_PREFIX = "http://spdx.org/licenses/";

}
