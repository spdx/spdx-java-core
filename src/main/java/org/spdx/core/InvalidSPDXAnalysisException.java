/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exception for invalid SPDX Documents
 * 
 * @author Gary O'Neall
 *
 */
public class InvalidSPDXAnalysisException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidSPDXAnalysisException() {
	}

	public InvalidSPDXAnalysisException(String arg0) {
		super(arg0);
	}

	public InvalidSPDXAnalysisException(Throwable arg0) {
		super(arg0);
	}
	
	public InvalidSPDXAnalysisException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidSPDXAnalysisException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
