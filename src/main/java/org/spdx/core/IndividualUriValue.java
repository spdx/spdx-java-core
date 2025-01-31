/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Implementation classes of the <code>IndividualUriValue</code> stores
 * a single URI value
 * <p>
 * These classes must NOT implement any properties themselves.
 * Any such properties will be lost during storage and retrieval.
 * 
 * @author Gary O'Neall
 */
public interface IndividualUriValue {
	
	/**
	 * @return a unique identifier for this value.  Typically the namespace + the long name
	 */
    String getIndividualURI();
}
