/**
 * SPDX-FileCopyrightText: Copyright (c) 2020 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exception when an SPDX element is in use (e.g. exception thrown when attempting to delete)
 * 
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public class SpdxIdInUseException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	public SpdxIdInUseException() {
		super();
	}

	public SpdxIdInUseException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public SpdxIdInUseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SpdxIdInUseException(String arg0) {
		super(arg0);
	}

	public SpdxIdInUseException(Throwable arg0) {
		super(arg0);
	}

}
