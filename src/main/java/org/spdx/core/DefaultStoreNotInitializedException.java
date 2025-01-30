/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

/**
 * Exception where the default store is used before it has been initialized
 * 
 * @author Gary O'Neall
 *
 */
@SuppressWarnings("unused")
public class DefaultStoreNotInitializedException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

	public DefaultStoreNotInitializedException() {
	}
	
	public DefaultStoreNotInitializedException(String arg0) {
		super(arg0);
	}

	public DefaultStoreNotInitializedException(Throwable arg0) {
		super(arg0);
	}

	public DefaultStoreNotInitializedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
	
	public DefaultStoreNotInitializedException(String arg0, Throwable arg1, boolean arg2,
											   boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
