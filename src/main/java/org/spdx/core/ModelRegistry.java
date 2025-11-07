/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 * Singleton class which contains a registry of SPDX model versions
 * <p>
 * Each model version implements a model interface <code>ISpdxModelInfo</code> which 
 * supports inflating an SPDX type specific to that version
 * 
 * @author Gary O'Neall
 */
public class ModelRegistry {

	private static final String SPEC_VERSION_NULL_MSG = "Spec version must not be null";
	private static final String URI_NULL_MSG = "URI must not be null";
	private static final String STORE_NULL_MSG = "Store must not be null";
	private static final String TYPE_NULL_MSG = "Type must not be null";
	private static final String DOES_NOT_EXIST_MSG = " does not exist";
	
	private static final ModelRegistry _instance = new ModelRegistry();
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();

	private final Map<String, ISpdxModelInfo> registeredModels = new HashMap<>();
	private final Map<String, Class<? extends CoreModelObject>> extensions = new HashMap<>();
	
	/**
	 * Private constructor - singleton class
	 */
	private ModelRegistry() {
		// Nothing really to be done here
	}

	/**
	 * Retrieve the singleton instance of the ModelRegistry
	 *
	 * @return the singleton instance of ModelRegistry
	 */
	public static ModelRegistry getModelRegistry() {
		return _instance;
	}

	/**
	 * Register a model in the registry
	 * <p>
	 * Each specification version supported by the model will be added to the registry.
	 *
	 * @param modelInfo The model information to register
	 */
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
	 * Check if the specified SPDX specification version is supported by the registry
	 *
	 * @param specVersion The version of the SPDX specification to check.
	 * @return {@code true} if the specified specVersion is supported, {@code false} otherwise.
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
	 *
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
	 * @param type type hint to create the appropriate external element type
	 * @param specVersion version of the SPDX spec the object complies with
	 * @return a java object representing an SPDX element external to model store, collection or document
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public Object getExternalElement(IModelStore store, String uri,
			@Nullable IModelCopyManager copyManager,
			Class<?> type, String specVersion) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(uri, URI_NULL_MSG);
		Objects.requireNonNull(store, STORE_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			return registeredModels.get(specVersion).createExternalElement(store, uri, copyManager, 
					type, specVersion);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * @param individualUri URI for the individual
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @return Individual represented by the URI
	 * @throws ModelRegistryException if the registry does not support the specVersion
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
	 * @param type Type of the object to create
	 * @param copyManager if non-null, implicitly copy any referenced properties from other model stores
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param create if true, create the model object ONLY if it does not already exist
	 * @param idPrefix optional prefix used for any new object URI's created in support of this model object
	 * @return model object of type type
	 * @throws InvalidSPDXAnalysisException on any SPDX related exception
	 */
	public CoreModelObject inflateModelObject(IModelStore modelStore, String objectUri, 
			String type, IModelCopyManager copyManager,
			String specVersion, boolean create, String idPrefix) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		Objects.requireNonNull(objectUri, URI_NULL_MSG);
		Objects.requireNonNull(modelStore, STORE_NULL_MSG);
		Objects.requireNonNull(type, TYPE_NULL_MSG);
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			if (extensions.containsKey(type)) {
				return inflateExtension(modelStore, objectUri, type, copyManager, specVersion, create, idPrefix);
			} else {
				return registeredModels.get(specVersion).createModelObject(modelStore, objectUri,
						type, copyManager, specVersion, create, idPrefix);
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	private CoreModelObject inflateExtension(IModelStore modelStore, String objectUri, String type,
											 IModelCopyManager copyManager, String specVersion,
											 boolean create, String idPrefix) throws InvalidSPDXAnalysisException {
		try {
			Constructor<?> con = extensions.get(type).getDeclaredConstructor(IModelStore.class, String.class,
					IModelCopyManager.class, boolean.class, String.class, String.class);
			return (CoreModelObject)con.newInstance(modelStore, objectUri, copyManager, create, specVersion, idPrefix);
		} catch (NoSuchMethodException e) {
			throw new InvalidSPDXAnalysisException("Could not create the extension type: "+type);
		} catch (SecurityException e) {
			throw new InvalidSPDXAnalysisException("Unexpected security exception for extension type: "+type, e);
		} catch (InstantiationException e) {
			throw new InvalidSPDXAnalysisException("Unexpected instantiation exception for extension type: "+type, e);
		} catch (IllegalAccessException e) {
			throw new InvalidSPDXAnalysisException("Unexpected illegal access exception for extension type: "+type, e);
		} catch (IllegalArgumentException e) {
			throw new InvalidSPDXAnalysisException("Unexpected illegal argument exception for extension type: "+type, e);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof InvalidSPDXAnalysisException) {
				throw (InvalidSPDXAnalysisException)e.getTargetException();
			} else {
				throw new InvalidSPDXAnalysisException("Unexpected invocation target exception for extension type: "+type, e);
			}
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
			return registeredModels.get(specVersion).getTypeToClassMap().getOrDefault(type, extensions.get(type));
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
			extensions.clear();;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Retrieve a list of all supported SPDX specification versions
	 *
	 * @return An unmodifiable list of all supported specification versions.
	 */
	public List<String> getSupportedVersions() {
		return Collections.unmodifiableList(new ArrayList<>(registeredModels.keySet()));
	}

	/**
	 * Determine if the specified class can be represented as external to the model store for the
	 * given SPDX specification version
	 *
	 * @param clazz The model class to check.
	 * @param specVersion The version of the SPDX specification.
	 * @return {@code true} if the class can be represented as external to the store, {@code false}
	 *         otherwise.
	 * @throws ModelRegistryException If the registry does not support the specified specVersion.
	 */
	public boolean canBeExternal(Class<?> clazz, String specVersion) throws ModelRegistryException {
		Objects.requireNonNull(specVersion, SPEC_VERSION_NULL_MSG);
		if (Objects.isNull(clazz)) {
			return false;
		}
		lock.readLock().lock();
		try {
			if (!containsSpecVersion(specVersion)) {
				throw new ModelRegistryException(specVersion + DOES_NOT_EXIST_MSG);
			}
			if (extensions.containsValue(clazz)) {
				return false;
			}
			return registeredModels.get(specVersion).canBeExternal(clazz);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Registers an extension class that can be used to extend an SPDX model
	 * @param type type to be used
	 * @param clazz class which must be a subclass of ModelObject
	 * @return the class which was added to the registry
	 * @throws ModelRegistryException on missing model registry for the provided specVersion
	 */
	public Class<?> registerExtensionType(String type, Class<? extends CoreModelObject> clazz) throws ModelRegistryException {
		Objects.requireNonNull(clazz, "Class can not be null to register extension type");
		Objects.requireNonNull(type, "Type can not be null to register extension type");
		lock.writeLock().lock();
		try {
			return extensions.put(type, clazz);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
