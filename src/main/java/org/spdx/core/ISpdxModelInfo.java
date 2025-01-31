/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.spdx.storage.IModelStore;

/**
 * Interface for SPDX model information
 * 
 * @author Gary O'Neall
 */
public interface ISpdxModelInfo {
	
	/**
	 * @return a map of URIs to Enums which represents individual vocabularies in the SPDX model
	 */
    Map<String, Enum<?>> getUriToEnumMap();

	/**
	 * @return the spec versions this model supports
	 */
    List<String> getSpecVersions();

	/**
	 * @param store store to use for the inflated object
	 * @param uri URI of the external element
	 * @param copyManager if non-null, create the external Doc ref if it is not a property of the SPDX Document
	 * @param specVersion version of the SPDX specification used by the external element
	 * @return model object of type type
	 */
    CoreModelObject createExternalElement(IModelStore store, String uri,
                                          @Nullable IModelCopyManager copyManager, Class<?> type, String specVersion) throws InvalidSPDXAnalysisException;

	/**
	 * @param uri URI for the individual
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @return a matching individual for a given URI or null if no individual exists
	 */
	@Nullable Object uriToIndividual(String uri, Class<?> type);

	/**
	 * @param modelStore store to use for the inflated object
	 * @param objectUri URI of the external element
	 * @param type Type of the object to create
	 * @param copyManager if non-null, implicitly copy any referenced properties from other model stores
	 * @param specVersion version of the SPDX specification used by the model object
	 * @param create if true, create the model object ONLY if it does not already exist
	 * @param idPrefix optional prefix used for any new object URI's created in support of this model object
	 * @return fully inflated model object of type type
	 */
    CoreModelObject createModelObject(IModelStore modelStore,
                                      String objectUri, String type, IModelCopyManager copyManager,
                                      String specVersion, boolean create, @Nullable String idPrefix) throws InvalidSPDXAnalysisException;

	/**
	 * @return a map of string representation of types to classes which implement those types
	 */
    Map<String, Class<?>> getTypeToClassMap();

	/**
	 * @param clazz model class
	 * @return true if clazz can be represented as external to the store
	 */
    boolean canBeExternal(Class<?> clazz);

}
