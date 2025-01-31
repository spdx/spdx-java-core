/**
 * SPDX-FileCopyrightText: Copyright (c) 2013 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

/**
 * Exception for license template rules
 * @author Gary O'Neall
 *
 */
public class LicenseTemplateRuleException extends Exception {

	private static final long serialVersionUID = 1L;

	public LicenseTemplateRuleException(String msg) {
		super(msg);
	}
	
	public LicenseTemplateRuleException(String msg, Throwable inner) {
		super(msg, inner);
	}
}
