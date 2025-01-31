/**
 * SPDX-FileCopyrightText: Copyright (c) 2016 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

/**
 * Exceptions related to invalid license templates
 * @author Gary O'Neall
 *
 */
@SuppressWarnings("unused")
public class InvalidLicenseTemplateException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidLicenseTemplateException() {
		super();
	}

	public InvalidLicenseTemplateException(String message) {
		super(message);
	}

	public InvalidLicenseTemplateException(Throwable cause) {
		super(cause);
	}

	public InvalidLicenseTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

}
