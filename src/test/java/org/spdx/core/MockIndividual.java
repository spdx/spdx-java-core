/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Mock individual for testing
 *
 * @author Gary O'Neall
 */
public class MockIndividual implements IndividualUriValue {
	
	static final String INDIVIDUAL_URI = "https://individual/uri/value";

	@Override
	public String getIndividualURI() {
		return INDIVIDUAL_URI;
	}

}
