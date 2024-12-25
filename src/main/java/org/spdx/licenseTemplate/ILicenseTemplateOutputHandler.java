/**
 * Copyright (c) 2013 Source Auditor Inc.
 * <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * <p>
 *       http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
*/
package org.spdx.licenseTemplate;

/**
 * Handles output for parsed license templates
 * <p>
 * The methods are called during parsing
 * to handle the parsed rules and text.
 * 
 * @author Gary O'Neall
 */
public interface ILicenseTemplateOutputHandler {

	/**
	 * Text for processing
	 * @param text text to be processed
	 */
	void text(String text);

	/**
	 * Variable rule found within the template
	 * @param rule license template rule
	 */
	void variableRule(LicenseTemplateRule rule);

	/**
	 * Begin optional rule found
	 * @param rule optional rule
	 */
	void beginOptional(LicenseTemplateRule rule);

	/**
	 * End optional rule found
	 * @param rule end optional rule
	 */
	void endOptional(LicenseTemplateRule rule);
	
	/**
	 * Signals all text has been added and parsing can be completed.
	 * @throws LicenseParserException if the license template could not be parsed
	 */
	void completeParsing() throws LicenseParserException;

}
