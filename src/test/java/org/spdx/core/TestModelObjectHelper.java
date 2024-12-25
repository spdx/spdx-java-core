/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spdx.storage.MockModelStore;
import org.spdx.storage.PropertyDescriptor;

/**
 * @author Gary O'Neall
 *
 */
public class TestModelObjectHelper {
	
	static final String OBJECT_URI = "https://myspdx.docs/objecturi#part1";
	static final String OBJECT_PROPERTY_NAME = "objectPropName";
	static final String PROPERTY_NAMESPACE = "https://spdx-mock/namespace";
	static final String STRING_PROPERTY_NAME = "stringPropName";
	static final String BOOLEAN_PROPERTY_NAME = "booleanPropName";
	static final String ENUM_PROPERTY_NAME = "enumPropName";
	static final String INDIVIDUAL_PROPERTY_NAME = "individualPropName";
	static final String COLLECTION_PROPERTY_NAME = "collectionPropName";
	static final PropertyDescriptor OBJECT_PROPERTY_DESCRIPTOR = new PropertyDescriptor(OBJECT_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor STRING_PROPERTY_DESCRIPTOR = new PropertyDescriptor(STRING_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor BOOLEAN_PROPERTY_DESCRIPTOR = new PropertyDescriptor(BOOLEAN_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor ENUM_PROPERTY_DESCRIPTOR = new PropertyDescriptor(ENUM_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor INDIVIDUAL_PROPERTY_DESCRIPTOR = new PropertyDescriptor(INDIVIDUAL_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor COLLECTION_PROPERTY_DESCRIPTOR = new PropertyDescriptor(COLLECTION_PROPERTY_NAME, PROPERTY_NAMESPACE);
	
	MockModelStore modelStore;
	MockCopyManager copyManager;
	MockModelType modelType;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
		ModelRegistry.getModelRegistry().registerModel(new MockModelInfo());
		modelType = new MockModelType(modelStore, OBJECT_URI, copyManager, true, "3.0.0"); // creates the mock model in the store
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Nothing to tear down (yet)
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#getObjectPropertyValue(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, org.spdx.core.IModelCopyManager, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testGetObjectPropertyValue() throws InvalidSPDXAnalysisException {
		String valueObjectUri = "https://value/object/uri";
		MockModelType value = new MockModelType(modelStore, valueObjectUri, copyManager, true, "3.0.0");
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, OBJECT_PROPERTY_DESCRIPTOR, value, copyManager, null);
		Optional<Object> result = ModelObjectHelper.getObjectPropertyValue(modelStore, OBJECT_URI, OBJECT_PROPERTY_DESCRIPTOR, copyManager, "3.0.0", null, null);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MockModelType);
		MockModelType resultModelType = (MockModelType)(result.get());
		assertEquals(valueObjectUri, resultModelType.getObjectUri());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#setPropertyValue(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, java.lang.Object, org.spdx.core.IModelCopyManager)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetPropertyValue() throws InvalidSPDXAnalysisException {
		
		// String
		String strValue = "This is a string";
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, STRING_PROPERTY_DESCRIPTOR, strValue, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, STRING_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertEquals(strValue, result.get());
		// boolean
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, BOOLEAN_PROPERTY_DESCRIPTOR, false, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, BOOLEAN_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertEquals(false, result.get());
		// enumeration
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, ENUM_PROPERTY_DESCRIPTOR, MockEnum.ENUM1, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, ENUM_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof SimpleUriValue);
		SimpleUriValue simpleResult = (SimpleUriValue)(result.get());
		assertEquals(MockEnum.ENUM1.getIndividualURI(), simpleResult.getIndividualURI());
		// individual
		MockIndividual individual = new MockIndividual();
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, INDIVIDUAL_PROPERTY_DESCRIPTOR, individual, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, INDIVIDUAL_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof SimpleUriValue);
		simpleResult = (SimpleUriValue)(result.get());
		assertEquals(MockIndividual.INDIVIDUAL_URI, simpleResult.getIndividualURI());
		// collection
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		Collection<String> c2 = Arrays.asList(new String[] {"s3"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c2, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		colResult = (Collection<String>)(result.get());
		assertTrue(c2.size() == colResult.size() && c2.containsAll(colResult) && colResult.containsAll(c2));
		// Object property is tested in tegObjectPropertyValue tests
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#removeProperty(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRemoveProperty() throws InvalidSPDXAnalysisException {
		String strValue = "This is a string";
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, STRING_PROPERTY_DESCRIPTOR, strValue, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, STRING_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertEquals(strValue, result.get());
		ModelObjectHelper.removeProperty(modelStore, OBJECT_URI, STRING_PROPERTY_DESCRIPTOR);
		result = modelStore.getValue(OBJECT_URI, STRING_PROPERTY_DESCRIPTOR);
		assertFalse(result.isPresent());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#clearValueCollection(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testClearValueCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		ModelObjectHelper.clearValueCollection(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		colResult = (Collection<String>)(result.get());
		assertTrue(colResult.isEmpty());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#addValueToCollection(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, java.lang.Object, org.spdx.core.IModelCopyManager)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAddValueToCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		ModelObjectHelper.addValueToCollection(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, "S3", copyManager, null);
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() + 1 == colResult.size() && colResult.contains("S3") && colResult.containsAll(c1));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#replacePropertyValueCollection(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, java.util.Collection, org.spdx.core.IModelCopyManager)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testReplacePropertyValueCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		Collection<String> c2 = Arrays.asList(new String[] {"s3"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		
		ModelObjectHelper.replacePropertyValueCollection(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c2, copyManager, null);
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		colResult = (Collection<String>)(result.get());
		assertTrue(c2.size() == colResult.size() && c2.containsAll(colResult) && colResult.containsAll(c2));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#removePropertyValueFromCollection(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testRemovePropertyValueFromCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		Optional<Object> result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		ModelObjectHelper.removePropertyValueFromCollection(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, "s1");
		result = modelStore.getValue(OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		colResult = (Collection<String>)(result.get());
		assertTrue(colResult.size() == 1 && colResult.contains("s2"));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#optionalStoredObjectToModelObject(java.util.Optional, org.spdx.storage.IModelStore, org.spdx.core.IModelCopyManager, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testOptionalStoredObjectToModelObject() throws InvalidSPDXAnalysisException {
		// TypedValue
		Optional<Object> tv = Optional.of(new TypedValue(OBJECT_URI, MockModelType.TYPE, "3.0.0"));
		Optional<Object> result = ModelObjectHelper.optionalStoredObjectToModelObject(tv, modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MockModelType);
		assertEquals(OBJECT_URI, ((MockModelType)result.get()).getObjectUri());
		// Enum
		result = ModelObjectHelper.optionalStoredObjectToModelObject(Optional.of(MockEnum.ENUM1), modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result.isPresent());
		assertEquals(MockEnum.ENUM1, result.get());
		// Individual URI
		result = ModelObjectHelper.optionalStoredObjectToModelObject(Optional.of(new MockIndividual()), modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MockIndividual);
		assertEquals(MockIndividual.INDIVIDUAL_URI, ((MockIndividual)result.get()).getIndividualURI());
		// String
		String s = "string";
		result = ModelObjectHelper.optionalStoredObjectToModelObject(Optional.of(s), modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result.isPresent());
		assertEquals(s, result.get());
		// empty
		result = ModelObjectHelper.optionalStoredObjectToModelObject(Optional.empty(), modelStore, copyManager, "3.0.0", null, null);
		assertFalse(result.isPresent());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#modelObjectToStoredObject(java.lang.Object, org.spdx.storage.IModelStore, org.spdx.core.IModelCopyManager)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testModelObjectToStoredObject() throws InvalidSPDXAnalysisException {
		String valueObjectUri = "https://value/object/uri";
		MockModelType mockType = new MockModelType(modelStore, valueObjectUri, copyManager, true, "3.0.0");
		Object result = ModelObjectHelper.modelObjectToStoredObject(mockType, modelStore, copyManager, null);
		assertTrue(result instanceof TypedValue);
		TypedValue tvResult = (TypedValue)result;
		assertEquals(valueObjectUri, tvResult.getObjectUri());
		assertEquals(MockModelType.TYPE, tvResult.getType());
		assertEquals("3.0.0", tvResult.getSpecVersion());
		// Enum
		result = ModelObjectHelper.modelObjectToStoredObject(MockEnum.ENUM1, modelStore, copyManager, null);
		assertTrue(result instanceof IndividualUriValue);
		assertEquals(MockEnum.ENUM1.getIndividualURI(), ((IndividualUriValue)result).getIndividualURI());
		// Individual
		result = ModelObjectHelper.modelObjectToStoredObject(new MockIndividual(), modelStore, copyManager, null);
		assertTrue(result instanceof IndividualUriValue);
		assertEquals(MockIndividual.INDIVIDUAL_URI, ((IndividualUriValue)result).getIndividualURI());
		// String
		String s = "this is a String";
		result = ModelObjectHelper.modelObjectToStoredObject(s, modelStore, copyManager, null);
		assertEquals(s, result);
		// boolean
		result = ModelObjectHelper.modelObjectToStoredObject(false, modelStore, copyManager, null);
		assertEquals(false, result);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#storedObjectToModelObject(java.lang.Object, org.spdx.storage.IModelStore, org.spdx.core.IModelCopyManager, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testStoredObjectToModelObject() throws InvalidSPDXAnalysisException {
		// TypedValue
		String valueObjectUri = "https://value/object/uri";
		Object result = ModelObjectHelper.storedObjectToModelObject(new TypedValue(valueObjectUri, MockModelType.TYPE, "3.0.0"), modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result instanceof MockModelType);
		assertEquals(valueObjectUri, ((MockModelType)result).getObjectUri());
		// Enum
		result = ModelObjectHelper.storedObjectToModelObject(MockEnum.ENUM1, modelStore, copyManager, "3.0.0", null, null);
		assertEquals(MockEnum.ENUM1, result);
		// Individual URI
		result = ModelObjectHelper.storedObjectToModelObject(new MockIndividual(), modelStore, copyManager, "3.0.0", null, null);
		assertTrue(result instanceof MockIndividual);
		assertEquals(MockIndividual.INDIVIDUAL_URI, ((MockIndividual)result).getIndividualURI());
		// String
		String s = "string";
		result = ModelObjectHelper.storedObjectToModelObject(s, modelStore, copyManager, "3.0.0", null, null);
		assertEquals(s, result);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#modelClassToStoredClass(java.lang.Class)}.
	 */
	@Test
	public void testModelClassToStoredClass() {
		// model objects
		Class<?> result = ModelObjectHelper.modelClassToStoredClass(MockModelType.class);
		assertEquals(TypedValue.class, result);
		// Enum
		result = ModelObjectHelper.modelClassToStoredClass(MockEnum.class);
		assertEquals(SimpleUriValue.class, result);
		// Individual
		result = ModelObjectHelper.modelClassToStoredClass(MockIndividual.class);
		assertEquals(SimpleUriValue.class, result);
		// String
		result = ModelObjectHelper.modelClassToStoredClass(String.class);
		assertEquals(String.class, result);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#implementsIndividualUriValue(java.lang.Class)}.
	 */
	@Test
	public void testImplementsIndividualUriValue() {
		// Model objects
		assertFalse(ModelObjectHelper.implementsIndividualUriValue(MockModelType.class));
		assertFalse(ModelObjectHelper.implementsIndividualUriValue(String.class));
		assertTrue(ModelObjectHelper.implementsIndividualUriValue(MockEnum.class));
		assertTrue(ModelObjectHelper.implementsIndividualUriValue(MockIndividual.class));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelObjectHelper#verifyCollection(java.util.Collection, java.lang.String, java.util.Set, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testVerifyCollection() throws InvalidSPDXAnalysisException {
		Collection<CoreModelObject> collection = Arrays.asList(new CoreModelObject[] {
				new MockModelType(modelStore, OBJECT_URI, copyManager, true, "3.0.0"),
				new MockModelType(modelStore, "http://uri2", copyManager, true, "3.0.0"),});
		ModelObjectHelper.verifyCollection(collection, "warning", new HashSet<>(), "3.0.0");
	}

}
