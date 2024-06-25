/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nullable;

import org.spdx.storage.IModelStore;

/**
 * @author Gary O'Neall
 * 
 * Singleton class which contains a registry of SPDX model versions
 * 
 * Each model version implements a model interface <code>ISpdxModelInfo</code> which 
 * supports inflating an SPDX type specific to that version
 * s
 */
public class ModelRegistry {

	private static final String SPEC_VERSION_NULL_MSG = "Spec version must not be null";
	private static final String URI_NULL_MSG = "URI must not be null";
	private static final String STORE_NULL_MSG = "Store must nut be null";
	private static final String TYPE_NULL_MSG = "Type must not be null";
	private static final String DOES_NOT_EXIST_MSG = " does not exits";
	
	private static final ModelRegistry _instance = new ModelRegistry();
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();

	private Map<String, ISpdxModelInfo> registeredModels = new HashMap<>();
	
	/**
	 * Private constructor - singleton class
	 */
	private ModelRegistry() {
		// Nothing really todo here
	}
	
	public static ModelRegistry getModelRegistry() {
		return _instance;
	}
	
	public void registerModel(ISpdxModelInfo modelInfo) {
		lock.writeLock().lock();
		try {
			for (String specVersion:modelInfo.getSpecVersions()) {
				registeredModels.put(specVersion, modelInfo);
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * @param specVersion
	 * @return
	 */
	public boolean containsSpecVersion(String specVersion) {
		lock.readLock().lock();
		try {
			return registeredModels.containsKey(specVersion);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Converts a URI to enum
	 * @param uri URI for the Enum individual
	 * @param specVersion Version of the spec the enum belongs to
	 * @return the Enum represented by the individualURI if it exists within the spec model
	 * @throws ModelRegistryException if the spec version does not exist
	 */
	public @Nullable Enum<?> uriToEnum(String uri, String specVersion) throws ModelRegistryException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(uri, URI_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			return registeredModels.get(specVersion).getUriToEnumMap().get(uri);
		} finally {
			lock.readLock().unlock();
		}
	}
	

	/**
	 * @param store store to use for the inflated object
	 * @param uri URI of the external element
	 * @param copyManager if non-null, implicitly copy any referenced properties from other model stores
	 * @param documentUri URI for the SPDX document to store the external element reference - used for compatibility with SPDX 2.X model stores
	 * @param externalMap Map of URI's of elements referenced but not present in the store
	 * @param specVersion version of the SPDX spec the object complies with
	 * @return a java object representing an SPDX element external to model store, collection or document
	 * @throws InvalidSPDXAnalysisException 
	 */
	public Object getExternalElement(IModelStore store, String uri,
			@Nullable IModelCopyManager copyManager,
			String specVersion) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(uri, URI_NULL_MSG);
		Objects.requireNonNull(store, STORE_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			return registeredModels.get(specVersion).createExternalElement(store, uri, copyManager, 
					specVersion);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * @param individualUri URI for the individual
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @return Individual represented by the URI
	 * @throws ModelRegistryException 
	 */
	public Object uriToIndividual(String individualUri, String specVersion, @Nullable Class<?> type) throws ModelRegistryException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(individualUri, "individualURI must not be null");
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			return registeredModels.get(specVersion).uriToIndividual(individualUri, type);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * If the object exists in the model store, it will be "inflated" back to the Java object.
	 * If the object does not exist AND the create parameter is true, a new object will be created and
	 * its inflated form will be returned
	 * @param modelStore store to use for the inflated object
	 * @param objectUri URI of the external element
	 * @param documentUri URI for the SPDX document to store the external element reference - used for compatibility with SPDX 2.X model stores
	 * @param type Type of the object to create
	 * @param copyManager if non-null, implicitly copy any referenced properties from other model stores
	 * @param externalMap map of URI's to ExternalMaps for any external elements
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param create if true, create the model object ONLY if it does not already exist
	 * @return model object of type type
	 * @throws InvalidSPDXAnalysisException 
	 */
	public CoreModelObject inflateModelObject(IModelStore modelStore, String objectUri, 
			String type, IModelCopyManager copyManager,
			String specVersion, boolean create) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(objectUri, URI_NULL_MSG);
		Objects.requireNonNull(modelStore, STORE_NULL_MSG);
		Objects.requireNonNull(type, TYPE_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			return registeredModels.get(specVersion).createModelObject(modelStore, objectUri, 
					type, copyManager, specVersion, create);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * @param type String representation of the SPDX type
	 * @param specVersion version of the SPDX spec
	 * @return the class representing the SPDX type, null if it does not exist in the model
	 * @throws ModelRegistryException if the spec version isn't found
	 */
	public @Nullable Class<?> typeToClass(String type, String specVersion) throws ModelRegistryException {
		Objects.requireNonNull(type, TYPE_NULL_MSG);
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!registeredModels.containsKey(specVersion)) {
				throw new ModelRegistryException("No implementation found for SPDX spec version "+specVersion);
			}
			return registeredModels.get(specVersion).getTypeToClassMap().get(type);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Removes all the registered models - should only be used in testing
	 */
	public void clearAll() {
		lock.writeLock().lock();
		try {
			registeredModels.clear();
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * @return a list of all supported versions
	 */
	public List<String> getSupportedVersions() {
		return Collections.unmodifiableList(new ArrayList<>(registeredModels.keySet()));
	}
}
