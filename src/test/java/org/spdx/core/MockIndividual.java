/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

/**
 * @author Gary O'Neall
 *
 */
public class MockIndividual implements IndividualUriValue {
	
	static final String INDIVIDUAL_URI = "https://individual/uri/value";

	@Override
	public String getIndividualURI() {
		return INDIVIDUAL_URI;
	}

}
