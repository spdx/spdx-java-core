/**
 * Copyright (c) 2019 Source Auditor Inc.
 * <p>
 * SPDX-License-Identifier: Apache-2.0
 * <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * <p>
 *       http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
