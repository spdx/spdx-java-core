/**
 * SPDX-FileCopyrightText: Copyright (c) 2015 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

import org.spdx.core.InvalidSPDXAnalysisException;

/**
 * Exception caused by an invalid license expression
 * @author Gary O'Neall
 *
 */
public class LicenseParserException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	public LicenseParserException(String msg) {
		super(msg);
	}

	public LicenseParserException(String msg, Throwable inner) {
		super(msg, inner);
	}
}
