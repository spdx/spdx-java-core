/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exception when an SPDX ID or object was not found in a model store
 * 
 * @author Gary O'Neall
 *
 */
@SuppressWarnings("unused")
public class SpdxObjectNotInStoreException extends InvalidSPDXAnalysisException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpdxObjectNotInStoreException() {
		super();
	}

	public SpdxObjectNotInStoreException(String arg0) {
		super(arg0);
	}

	public SpdxObjectNotInStoreException(Throwable arg0) {
		super(arg0);
	}

	public SpdxObjectNotInStoreException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SpdxObjectNotInStoreException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
