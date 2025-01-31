/**
 * SPDX-FileCopyrightText: Copyright (c) 2023 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.spdx.storage.IModelStore;
import org.spdx.storage.PropertyDescriptor;
import org.spdx.storage.IModelStore.IModelStoreLock;

/**
 * A set of static methods to help with common ModelObject functions
 * 
 * @author Gary O'Neall
 *
 */
public class ModelObjectHelper {
	
	private ModelObjectHelper() {
		// this is only a static helper class
	}
	
	/**
	 * Get an object value for a property
	 * @param modelStore Model store for the object
	 * @param objectUri the Object URI or anonymous ID
	 * @param propertyDescriptor property descriptor for the property
	 * @param copyManager if non null, any ModelObject property value not stored in the modelStore under the stDocumentUri will be copied to make it available
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @param idPrefix prefix to be used when generating new SPDX IDs
	 * @return value associated with a property
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static Optional<Object> getObjectPropertyValue(IModelStore modelStore, String objectUri,
			PropertyDescriptor propertyDescriptor, IModelCopyManager copyManager,
			String specVersion, @Nullable Class<?> type, String idPrefix) throws InvalidSPDXAnalysisException {
		IModelStoreLock lock = modelStore.enterCriticalSection(false);
		// NOTE: we use a write lock since the ModelStorageClassConverter may end up creating objects in the store
		try {
			if (!modelStore.exists(objectUri)) {
				return Optional.empty();
			} else if (modelStore.isCollectionProperty(objectUri, propertyDescriptor)) {
                return Optional.of(new ModelCollection<>(modelStore, objectUri, propertyDescriptor, copyManager,
                        null, specVersion, idPrefix));
			} else {
				return optionalStoredObjectToModelObject(modelStore.getValue(objectUri,
						propertyDescriptor), modelStore, copyManager, specVersion, type, idPrefix);
			}
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Set a property value for a property descriptor, creating the property if necessary
	 * @param modelStore Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor Descriptor for the property associated with this object
	 * @param value Value to associate with the property
	 * @param copyManager if non null, any ModelObject property value not stored in the modelStore under the stDocumentUri will be copied to make it available
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void setPropertyValue(IModelStore modelStore, String objectUri, 
			PropertyDescriptor propertyDescriptor, @Nullable Object value, 
			IModelCopyManager copyManager, String idPrefix) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(modelStore, "Model Store can not be null");
		Objects.requireNonNull(objectUri, "Object Uri or anonymous ID can not be null");
		Objects.requireNonNull(propertyDescriptor, "Property descriptor can not be null");
		if (value == null) {
			// we just remove the value
			removeProperty(modelStore, objectUri, propertyDescriptor);
		} else if (value instanceof Collection) {
			replacePropertyValueCollection(modelStore, objectUri, propertyDescriptor, (Collection<?>)value, 
					copyManager, idPrefix);
		} else {
			modelStore.setValue(objectUri, propertyDescriptor, 
					modelObjectToStoredObject(value, modelStore, copyManager, idPrefix));
		}
	}
	
	/**
	 * Removes a property and its value from the model store if it exists
	 * @param modelStore Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor Descriptor for the property associated with this object to be removed
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void removeProperty(IModelStore modelStore, String objectUri, PropertyDescriptor propertyDescriptor) throws InvalidSPDXAnalysisException {
		modelStore.removeProperty(objectUri, propertyDescriptor);
	}
	
	/**
	 * Clears a collection of values associated with a property creating the property if it does not exist
	 * @param modelStore Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor Descriptor for the property
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void clearValueCollection(IModelStore modelStore, String objectUri, PropertyDescriptor propertyDescriptor) throws InvalidSPDXAnalysisException {
		modelStore.clearValueCollection(objectUri, propertyDescriptor);
	}
	
	/**
	 * Add a value to a collection of values associated with a property. If a value
	 * is a ModelObject and does not belong to the document, it will be copied into
	 * the object store
	 * 
	 * @param modelStore  Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor  Descriptor for the property
	 * @param value to add
	 * @param copyManager copyManager for any copying of properties
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void addValueToCollection(IModelStore modelStore, String objectUri, 
			PropertyDescriptor propertyDescriptor, Object value, IModelCopyManager copyManager,
			String idPrefix) throws InvalidSPDXAnalysisException {
		Objects.requireNonNull(value, "Value can not be null");
		modelStore.addValueToCollection(objectUri, propertyDescriptor, 
				modelObjectToStoredObject(value, modelStore, copyManager, idPrefix));
	}
	
	/**
	 * Replace the entire value collection for a property.  If a value is a ModelObject and does not
	 * belong to the document, it will be copied into the object store
	 * @param modelStore Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor Descriptor for the property
	 * @param values collection of new properties
	 * @param copyManager if non-null, any ModelObject property value not stored in the modelStore under the stDocumentUri will be copied to make it available
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void replacePropertyValueCollection(IModelStore modelStore, String objectUri, 
			PropertyDescriptor propertyDescriptor, Collection<?> values, IModelCopyManager copyManager,
			String idPrefix) throws InvalidSPDXAnalysisException {
		clearValueCollection(modelStore, objectUri, propertyDescriptor);
		for (Object value:values) {
			addValueToCollection(modelStore, objectUri, propertyDescriptor, value, copyManager, idPrefix);
		}
	}
	
	/**
	 * Remove a property value from a collection
	 * @param modelStore Model store for the properties
	 * @param objectUri URI or anonymous ID for the object
	 * @param propertyDescriptor descriptor for the property
	 * @param value Value to be removed
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static void removePropertyValueFromCollection(IModelStore modelStore, String objectUri, 
			PropertyDescriptor propertyDescriptor, Object value) throws InvalidSPDXAnalysisException {
		modelStore.removeValueFromCollection(objectUri, propertyDescriptor, 
				modelObjectToStoredObject(value, modelStore, null, null));
	}
	
	/**
	 * Converts any typed value or IndividualValue objects to a ModelObject,
	 * returning an existing ModelObject if it exists or creates a new ModelObject
	 * 
	 * @param value Value which may be a TypedValue
	 * @param modelStore  ModelStore to use in fetching or creating
	 * @param copyManager   if not null, copy any referenced ID's outside of this
	 *                      document/model store
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @param idPrefix Prefix to be used if any new object URI's are generated
	 * @return the object itself unless it is a TypedValue, in which case a
	 *         ModelObject is returned
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static Optional<Object> optionalStoredObjectToModelObject(Optional<Object> value,
                                                                     IModelStore modelStore, IModelCopyManager copyManager, String specVersion,
                                                                     @Nullable Class<?> type, String idPrefix) throws InvalidSPDXAnalysisException {
		if (value.isPresent() && value.get() instanceof IndividualUriValue) {
			return Optional.ofNullable(new SimpleUriValue((IndividualUriValue)value.get()).toModelObject(modelStore, copyManager, 
					specVersion, type));
		} else if (value.isPresent() && value.get() instanceof TypedValue) {
			TypedValue tv = (TypedValue)value.get();
			return Optional.of(ModelRegistry.getModelRegistry().inflateModelObject(modelStore, 
					tv.getObjectUri(), tv.getType(), copyManager, specVersion, false, idPrefix));
		} else {
			return value;
		}
	}
	
	/**
	 * Converts a model object to the object to be stored
	 * @param value Value which may be a TypedValue
	 * @param modelStore ModelStore to use in fetching or creating
	 * @param copyManager if not null, copy any referenced ID's outside of this document/model store
	 * @param idPrefix Prefix to be used if any new object URI's are generated
	 * @return Model Object appropriate type
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static Object modelObjectToStoredObject(Object value, IModelStore modelStore, 
			IModelCopyManager copyManager, String idPrefix) throws InvalidSPDXAnalysisException {
		if (value instanceof IndividualUriValue) {
			// Convert to a simple URI value to save storage
			return new SimpleUriValue((IndividualUriValue)value);
		} else if (value instanceof CoreModelObject) {
			CoreModelObject mValue = (CoreModelObject)value;
			if (!mValue.getModelStore().equals(modelStore)) {
				if (Objects.nonNull(copyManager)) {
					return copyManager.copy(modelStore, mValue.getModelStore(), mValue.getObjectUri(), 
							mValue.getSpecVersion(), idPrefix);
				} else {
					throw new SpdxObjectNotInStoreException("Can not set a property value to a Model Object stored in a different model store");
				}
			} else {
				return mValue.toTypedValue();
			}
		} else if (value instanceof Integer || value instanceof String ||
				value instanceof Boolean || value instanceof Double) {
			return value;
		} else if (Objects.isNull(value)) {
			throw new SpdxInvalidTypeException("Property value is null");
		} else {
			throw new SpdxInvalidTypeException("Property value type not supported: "+value.getClass().getName());
		}
	}
	
	/**
	 * Converts any typed value or individual value objects to a ModelObject,
	 * returning an existing ModelObject if it exists or creates a new ModelObject
	 * 
	 * @param value       Value which may be a TypedValue
	 * @param modelStore  ModelStore to use in fetching or creating
	 * @param copyManager if not null, copy any referenced ID's outside of this
	 *                    document/model store
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @param idPrefix Prefix to be used if any new object URI's are generated
	 * @return the object itself unless it is a TypedValue, in which case a
	 *         ModelObject is returned
	 * @throws InvalidSPDXAnalysisException on any SPDX related error
	 */
	public static Object storedObjectToModelObject(Object value, IModelStore modelStore,
			IModelCopyManager copyManager, String specVersion, @Nullable Class<?> type,
			String idPrefix) throws InvalidSPDXAnalysisException {
		if (value instanceof IndividualUriValue) {	// Note: this must be before the check for TypedValue
			SimpleUriValue suv = new SimpleUriValue((IndividualUriValue)value);
			return suv.toModelObject(modelStore, copyManager, specVersion, type);
		} else if (value instanceof TypedValue) {
			TypedValue tv = (TypedValue)value;
			return ModelRegistry.getModelRegistry().inflateModelObject(modelStore, tv.getObjectUri(),
					tv.getType(), copyManager, specVersion, true, idPrefix);
		} else {
			return value;
		}
	}
	
	/**
	 * Convert the class to the appropriate stored class
	 * @param clazz Model class
	 * @return class compatible with stored classes
	 */
	public static Class<?> modelClassToStoredClass(Class<?> clazz) {
		if (CoreModelObject.class.isAssignableFrom(clazz)) {
			return TypedValue.class;
		} else if (implementsIndividualUriValue(clazz)) {
			return SimpleUriValue.class;
		} else {
			return clazz;
		}
	}
	
	/**
	 * @param clazz class to check
	 * @return true if the clazz implements the IndividualUriValue interface
	 */
	public static boolean implementsIndividualUriValue(Class<?> clazz) {
		for (Class<?> intefaceClass:clazz.getInterfaces()) {
			if (intefaceClass.equals(IndividualUriValue.class)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verifies all elements in a collection
	 * @param specVersion version of the SPDX specification to verify against
	 * @param collection collection to be verifies
	 * @param verifiedIds verifiedIds list of all Id's which have already been verified - prevents infinite recursion
	 * @param warningPrefix String to prefix any warning messages
	 */
	public static List<String> verifyCollection(Collection<? extends CoreModelObject> collection, String warningPrefix, Set<String> verifiedIds, String specVersion) {
		List<String> retval = new ArrayList<>();
		for (CoreModelObject mo:collection) {
			for (String warning:mo.verify(verifiedIds, specVersion)) {
				if (Objects.nonNull(warningPrefix)) {
					retval.add(warningPrefix + warning);
				} else {
					retval.add(warning);
				}
			}
		}
		return retval;
	}
}
