/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exception for no SPDX identifier found
 * 
 * @author Gary O'Neall
 *
 */
@SuppressWarnings("unused")
public class SpdxIdNotFoundException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;
	

	/**
	 * General SPDX ID not found exception
	 */
	public SpdxIdNotFoundException() {
	}

	/**
	 * @param arg0 message
	 */
	public SpdxIdNotFoundException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0 cause exception
	 */
	public SpdxIdNotFoundException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0 message
	 * @param arg1 cause
	 */
	public SpdxIdNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SpdxIdNotFoundException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
