/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;


import javax.annotation.Nullable;
import org.spdx.storage.IModelStore;

/**
 * Implementation classes of this interface helps facilitate copying objects from one model to another.
 * <p>
 * In addition to the copy functions (methods), these objects keeps track of 
 * what was copied where so that the same object is not copied twice.
 * <p>
 * These objects can be passed into the constructor for ModelObjects to allow the objects to be copied.
 * 
 * @author Gary O'Neall
 */
public interface IModelCopyManager {
	
	   /**
		 * Copy an item from one Model Object Store to another using the source ID for the target unless it is anonymous
		 * @param toStore Model Store to copy to
		 * @param fromStore Model Store containing the source item
		 * @param sourceUri URI for the Source object
		 * @param toSpecVersion version of the spec the to value should comply with
		 * @param toNamespace optional namespace of the to property
		 * @return Object URI for the copied object
		 * @throws InvalidSPDXAnalysisException on any SPDX related error
		 */
		TypedValue copy(IModelStore toStore, IModelStore fromStore,
				String sourceUri, String toSpecVersion, @Nullable String toNamespace) throws InvalidSPDXAnalysisException;

		/**
		 * Copy an item from one Model Object Store to another
		 * @param toStore Model Store to copy to
		 * @param toObjectUri URI for the destination object
		 * @param fromStore Model Store containing the source item
		 * @param fromObjectUri Object URI for the source item
		 * @param toSpecVersion version of the spec the to value should comply with
		 * @param toNamespace optional namespace of the to property
		 * @throws InvalidSPDXAnalysisException on any SPDX related error
		 */
		void copy(IModelStore toStore, String toObjectUri, IModelStore fromStore, String fromObjectUri,
				String toSpecVersion, @Nullable String toNamespace) throws InvalidSPDXAnalysisException;

		/**
		 * @param fromStore Store copied from
		 * @param fromObjectUri Object URI in the from tsotre
		 * @param toStore store copied to
		 * @return the objectId which has already been copied, or null if it has not been copied
		 */
		String getCopiedObjectUri(IModelStore fromStore, String fromObjectUri,
				IModelStore toStore);

		/**
		 * Record a copied ID between model stores
		 * @param fromStore Store copied from
		 * @param fromObjectUri URI for the from Object
		 * @param toObjectUri URI for the to Object
		 * @return any copied to ID for the same stores, URI's, nameSpace and fromID
		 */
		String putCopiedId(IModelStore fromStore, String fromObjectUri, IModelStore toStore,
				String toObjectUri);
}
