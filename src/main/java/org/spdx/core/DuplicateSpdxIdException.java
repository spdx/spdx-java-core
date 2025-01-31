/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exception for duplicate SPDX ID creations
 * 
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public class DuplicateSpdxIdException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	public DuplicateSpdxIdException() {
	}

	public DuplicateSpdxIdException(String arg0) {
		super(arg0);
	}

	public DuplicateSpdxIdException(Throwable arg0) {
		super(arg0);
	}

	public DuplicateSpdxIdException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateSpdxIdException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
