/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Invalid property name or value for an SPDX item
 * 
 * @author Gary O'Neall
 *
 */
@SuppressWarnings("unused")
public class InvalidSpdxPropertyException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;
	
	public InvalidSpdxPropertyException() {
		super();
	}

	public InvalidSpdxPropertyException(String arg0) {
		super(arg0);
	}

	public InvalidSpdxPropertyException(Throwable arg0) {
		super(arg0);
	}

	public InvalidSpdxPropertyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidSpdxPropertyException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
