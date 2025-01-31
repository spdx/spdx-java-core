/**
 * SPDX-FileCopyrightText: Copyright (c) 2013 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.licenseTemplate;

/**
 * License template output handler for generating a text version of a license
 * from a license template
 * <p>
 * Used when parsing a license template.
 * 
 * @author Gary O'Neall
 */
public class TextTemplateOutputHandler implements ILicenseTemplateOutputHandler {

	StringBuilder textString = new StringBuilder();

	/* (non-Javadoc)
	 * @see org.spdx.licenseTemplate.ILicenseTemplateOutputHandler#text(java.lang.String)
	 */
	@Override
	public void text(String text) {
		textString.append(text);
	}

	/* (non-Javadoc)
	 * @see org.spdx.licenseTemplate.ILicenseTemplateOutputHandler#variableRule(org.spdx.licenseTemplate.LicenseTemplateRule)
	 */
	@Override
	public void variableRule(LicenseTemplateRule rule) {
		textString.append(rule.getOriginal());
	}

	/* (non-Javadoc)
	 * @see org.spdx.licenseTemplate.ILicenseTemplateOutputHandler#beginOptional(org.spdx.licenseTemplate.LicenseTemplateRule)
	 */
	@Override
	public void beginOptional(LicenseTemplateRule rule) {
		// ignore
	}

	/* (non-Javadoc)
	 * @see org.spdx.licenseTemplate.ILicenseTemplateOutputHandler#endOptional(org.spdx.licenseTemplate.LicenseTemplateRule)
	 */
	@Override
	public void endOptional(LicenseTemplateRule rule) {
		// ignore
	}

	public String getText() {
		return this.textString.toString();
	}

	@Override
	public void completeParsing() {
		// // Nothing needs to be done - everything is processed inline
		
	}
}
