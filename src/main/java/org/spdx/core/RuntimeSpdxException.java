/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Runtime Exception wrapper for SPDX exceptions (cause field)
 * 
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public class RuntimeSpdxException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message exception message
	 */
	@SuppressWarnings("unused")
    public RuntimeSpdxException(String message) {
		super(message);
	}

	/**
	 * @param cause SPDX analysis cause
	 */
	public RuntimeSpdxException(InvalidSPDXAnalysisException cause) {
		super(cause);
	}

	/**
	 * @param message exception message
	 * @param cause SPDX analysis cause
	 */
	public RuntimeSpdxException(String message, InvalidSPDXAnalysisException cause) {
		super(message, cause);
	}

	/**
	 * @param message exception message
	 * @param cause SPDX analysis cause
	 * @param enableSuppression to suppress message
	 * @param writableStackTrace stack trace
	 */
	public RuntimeSpdxException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
