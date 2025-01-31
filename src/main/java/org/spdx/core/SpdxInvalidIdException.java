/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Invalid SPDX identifier
 * 
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public class SpdxInvalidIdException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	/**
	 * Invalid ID
	 */
	public SpdxInvalidIdException() {
		super();
	}

	/**
	 * @param message message
	 */
	public SpdxInvalidIdException(String message) {
		super(message);
	}

	/**
	 * @param cause Cause
	 */
	public SpdxInvalidIdException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message Message
	 * @param cause Cause
	 */
	public SpdxInvalidIdException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message message
	 * @param cause cause
	 * @param enableSuppression if true, enable suppression
	 * @param writableStackTrace stack trace
	 */
	public SpdxInvalidIdException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
