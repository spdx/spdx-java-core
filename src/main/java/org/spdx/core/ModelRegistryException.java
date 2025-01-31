/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Exceptions related to a model registry
 *
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public class ModelRegistryException extends InvalidSPDXAnalysisException {

	private static final long serialVersionUID = 1L;

    public ModelRegistryException() {
		super();
	}

	public ModelRegistryException(String arg0) {
		super(arg0);
	}

	public ModelRegistryException(Throwable arg0) {
		super(arg0);
	}

	public ModelRegistryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ModelRegistryException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
