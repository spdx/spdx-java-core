/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Invalid type for an SPDX property
 * 
 * @author Gary O'Neall
 *
 */
public class SpdxInvalidTypeException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	public SpdxInvalidTypeException() {
	}

	public SpdxInvalidTypeException(String arg0) {
		super(arg0);
	}

	public SpdxInvalidTypeException(Throwable arg0) {
		super(arg0);
	}

	public SpdxInvalidTypeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SpdxInvalidTypeException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
