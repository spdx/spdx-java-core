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
