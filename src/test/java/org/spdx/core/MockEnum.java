/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Mock enum for testing
 *
 * @author Gary O'Neall
 */
public enum MockEnum implements IndividualUriValue {
	
	ENUM1("https://mock/enum1"),
	ENUM2("https://mock/enum2");
	
	private String uri;
	
	private MockEnum(String enumUri) {
		this.uri = enumUri;
	}
	@Override
	public String getIndividualURI() {
		return this.uri;
	}

}
