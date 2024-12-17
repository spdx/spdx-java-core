/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spdx.core.CoreModelObject.CoreModelObjectBuilder;
import org.spdx.storage.MockModelStore;
import org.spdx.storage.PropertyDescriptor;

/**
 * @author gary
 *
 */
public class TestCoreModelObject {
	
	static final String OBJECT_URI = "https://myspdx.docs/objecturi#part1";
	static final String OBJECT_PROPERTY_NAME = "objectPropName";
	static final String PROPERTY_NAMESPACE = "https://spdx-mock/namespace";
	static final String STRING_PROPERTY_NAME = "stringPropName";
	static final String BOOLEAN_PROPERTY_NAME = "booleanPropName";
	static final String INTEGER_PROPERTY_NAME = "integerPropName";
	static final String ENUM_PROPERTY_NAME = "enumPropName";
	static final String INDIVIDUAL_PROPERTY_NAME = "individualPropName";
	static final String COLLECTION_PROPERTY_NAME = "collectionPropName";
	static final PropertyDescriptor OBJECT_PROPERTY_DESCRIPTOR = new PropertyDescriptor(OBJECT_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor STRING_PROPERTY_DESCRIPTOR = new PropertyDescriptor(STRING_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor BOOLEAN_PROPERTY_DESCRIPTOR = new PropertyDescriptor(BOOLEAN_PROPERTY_NAME, PROPERTY_NAMESPACE);
	static final PropertyDescriptor INTEGER_PROPERTY_DESCRIPTOR = new PropertyDescriptor(INTEGER_PROPERTY_NAME, PROPERTY_NAMESPACE);
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
	 * Test method for {@link org.spdx.core.CoreModelObject#CoreModelObject(org.spdx.storage.IModelStore, java.lang.String, org.spdx.core.IModelCopyManager, boolean, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testCoreModelObjectIModelStoreStringIModelCopyManagerBooleanString() throws InvalidSPDXAnalysisException {
		// already created in the setup
		assertEquals("3.0.0", modelType.getSpecVersion());
		assertEquals(modelStore, modelType.getModelStore());
		assertEquals(OBJECT_URI, modelType.getObjectUri());
		assertEquals(copyManager, modelType.getCopyManager());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#CoreModelObject(org.spdx.core.CoreModelObject.CoreModelObjectBuilder, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testCoreModelObjectCoreModelObjectBuilderString() throws InvalidSPDXAnalysisException {
		String objectUri = "urn:different.uri";
		CoreModelObjectBuilder builder = new CoreModelObjectBuilder(modelStore, objectUri, copyManager)
				.setStrict(false);
		CoreModelObject result = new MockModelType(builder, "3.0.0");
		assertEquals("3.0.0", result.getSpecVersion());
		assertEquals(modelStore, result.getModelStore());
		assertEquals(objectUri, result.getObjectUri());
		assertEquals(copyManager, result.getCopyManager());
		assertEquals(false, result.isStrict());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#verify(java.lang.String)}.
	 */
	@Test
	public void testVerifyString() {
		List<String> result = modelType.verify("3.0.0");
		assertTrue(MockModelType.TEST_VERIFY.size() == result.size() &&
				MockModelType.TEST_VERIFY.containsAll(result) &&
				result.containsAll(MockModelType.TEST_VERIFY));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#verifyCollection(java.util.Collection, java.lang.String, java.util.Set, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testVerifyCollection() throws InvalidSPDXAnalysisException {
		Collection<CoreModelObject> collection = Arrays.asList(new CoreModelObject[] {
				new MockModelType(modelStore, OBJECT_URI, copyManager, true, "3.0.0"),
				new MockModelType(modelStore, "http://uri2", copyManager, true, "3.0.0"),});
		List<String> result = modelType.verifyCollection(collection, "warning", new HashSet<>(), "3.0.0");
		assertEquals(MockModelType.TEST_VERIFY.size() * 2, result.size());
		for (String s:result) {
			assertTrue(s.startsWith("warning"));
		}
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#setStrict(boolean)}.
	 */
	@Test
	public void testSetStrict() {
		assertTrue(modelType.isStrict());
		modelType.setStrict(false);
		assertFalse(modelType.isStrict());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#getPropertyValueDescriptors()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testGetPropertyValueDescriptors() throws InvalidSPDXAnalysisException {
		
		// String
		String strValue = "This is a string";
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, STRING_PROPERTY_DESCRIPTOR, strValue, copyManager, null);
		// boolean
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, BOOLEAN_PROPERTY_DESCRIPTOR, false, copyManager, null);
		// enumeration
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, ENUM_PROPERTY_DESCRIPTOR, MockEnum.ENUM1, copyManager, null);
		// individual
		MockIndividual individual = new MockIndividual();
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, INDIVIDUAL_PROPERTY_DESCRIPTOR, individual, copyManager, null);
		// collection
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, COLLECTION_PROPERTY_DESCRIPTOR, c1, copyManager, null);
		List<PropertyDescriptor> result = modelType.getPropertyValueDescriptors();
		List<PropertyDescriptor> expected = Arrays.asList(new PropertyDescriptor[] {
				STRING_PROPERTY_DESCRIPTOR, BOOLEAN_PROPERTY_DESCRIPTOR, 
				ENUM_PROPERTY_DESCRIPTOR, INDIVIDUAL_PROPERTY_DESCRIPTOR,
				COLLECTION_PROPERTY_DESCRIPTOR
		});
		assertTrue(result.size() == expected.size() && result.containsAll(expected) && expected.containsAll(result));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#getObjectPropertyValue(org.spdx.storage.PropertyDescriptor)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testGetObjectPropertyValue() throws InvalidSPDXAnalysisException {
		String valueObjectUri = "https://value/object/uri";
		MockModelType value = new MockModelType(modelStore, valueObjectUri, copyManager, true, "3.0.0");
		ModelObjectHelper.setPropertyValue(modelStore, OBJECT_URI, OBJECT_PROPERTY_DESCRIPTOR, value, copyManager, null);
		Optional<Object> result = modelType.getObjectPropertyValue(OBJECT_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof MockModelType);
		MockModelType resultModelType = (MockModelType)(result.get());
		assertEquals(valueObjectUri, resultModelType.getObjectUri());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#setPropertyValue(org.spdx.storage.PropertyDescriptor, java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testSetPropertyValue() throws InvalidSPDXAnalysisException {
		// String
		String strValue = "This is a string";
		modelType.setPropertyValue(STRING_PROPERTY_DESCRIPTOR, strValue);
		Optional<String> result = modelType.getStringPropertyValue(STRING_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertEquals(strValue, result.get());
		// boolean
		modelType.setPropertyValue(BOOLEAN_PROPERTY_DESCRIPTOR, false);
		Optional<Boolean> bResult = modelType.getBooleanPropertyValue(BOOLEAN_PROPERTY_DESCRIPTOR);
		assertTrue(bResult.isPresent());
		assertEquals(false, bResult.get());
		// enumeration
		modelType.setPropertyValue(ENUM_PROPERTY_DESCRIPTOR, MockEnum.ENUM1);
		Optional<? extends Enum<?>> eResult = modelType.getEnumPropertyValue(ENUM_PROPERTY_DESCRIPTOR);
		assertTrue(eResult.isPresent());
		assertEquals(MockEnum.ENUM1, eResult.get());
		// individual
		MockIndividual individual = new MockIndividual();
		modelType.setPropertyValue(INDIVIDUAL_PROPERTY_DESCRIPTOR, individual);
		Optional<Object> oResult = modelType.getObjectPropertyValue(INDIVIDUAL_PROPERTY_DESCRIPTOR);
		assertTrue(oResult.isPresent());
		assertTrue(oResult.get() instanceof MockIndividual);
		// collection
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		Collection<String> c2 = Arrays.asList(new String[] {"s3"});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		Collection<String> cResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(c1.size() == cResult.size() && c1.containsAll(cResult) && cResult.containsAll(c1));
		
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c2);
		cResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(c2.size() == cResult.size() && c2.containsAll(cResult) && cResult.containsAll(c2));
		
		modelType.setPropertyValue(INTEGER_PROPERTY_DESCRIPTOR, 15);
		assertEquals(Integer.valueOf(15), modelType.getIntegerPropertyValue(INTEGER_PROPERTY_DESCRIPTOR).get());
		// Object property is tested in getPropertyValue tests
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#removeProperty(org.spdx.storage.PropertyDescriptor)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRemoveProperty() throws InvalidSPDXAnalysisException {
		String strValue = "This is a string";
		modelType.setPropertyValue(STRING_PROPERTY_DESCRIPTOR, strValue);
		Optional<Object> result = modelType.getObjectPropertyValue(STRING_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertEquals(strValue, result.get());
		modelType.removeProperty(STRING_PROPERTY_DESCRIPTOR);
		result = modelType.getObjectPropertyValue(STRING_PROPERTY_DESCRIPTOR);
		assertFalse(result.isPresent());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#clearValueCollection(org.spdx.storage.PropertyDescriptor)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testClearValueCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		Optional<Object> result = modelType.getObjectPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(result.isPresent());
		assertTrue(result.get() instanceof Collection);
		@SuppressWarnings("unchecked")
		Collection<String> colResult = (Collection<String>)(result.get());
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		modelType.clearValueCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		colResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(colResult.isEmpty());
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#addPropertyValueToCollection(org.spdx.storage.PropertyDescriptor, java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAddPropertyValueToCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		Collection<String> colResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		modelType.addPropertyValueToCollection(COLLECTION_PROPERTY_DESCRIPTOR, "S3");
		colResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(c1.size() + 1 == colResult.size() && colResult.contains("S3") && colResult.containsAll(c1));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#removePropertyValueFromCollection(org.spdx.storage.PropertyDescriptor, java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRemovePropertyValueFromCollection() throws InvalidSPDXAnalysisException {
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		Collection<String> colResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(c1.size() == colResult.size() && c1.containsAll(colResult) && colResult.containsAll(c1));
		modelType.removePropertyValueFromCollection(COLLECTION_PROPERTY_DESCRIPTOR, "s1");
		colResult = modelType.getStringCollection(COLLECTION_PROPERTY_DESCRIPTOR);
		assertTrue(colResult.size() == 1 && colResult.contains("s2"));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#getObjectPropertyValueSet(org.spdx.storage.PropertyDescriptor, java.lang.Class)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testGetObjectPropertyValueSet() throws InvalidSPDXAnalysisException {
		String m1Uri = "https://uri1";
		String m2Uri = "https://uri2";
		MockModelType m1 = new MockModelType(modelStore, m1Uri, copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, m2Uri, copyManager, true, "3.0.0");
		Collection<MockModelType> c1 = Arrays.asList(new MockModelType[] {m1, m2});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		ModelSet<?> result = modelType.getObjectPropertyValueSet(COLLECTION_PROPERTY_DESCRIPTOR, MockModelType.class);
		assertEquals(2, result.size());
		assertTrue(result.containsAll(c1));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#getObjectPropertyValueCollection(org.spdx.storage.PropertyDescriptor, java.lang.Class)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testGetObjectPropertyValueCollection() throws InvalidSPDXAnalysisException {
		String m1Uri = "https://uri1";
		String m2Uri = "https://uri2";
		MockModelType m1 = new MockModelType(modelStore, m1Uri, copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, m2Uri, copyManager, true, "3.0.0");
		Collection<MockModelType> c1 = Arrays.asList(new MockModelType[] {m1, m2});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		Collection<?> result = modelType.getObjectPropertyValueCollection(COLLECTION_PROPERTY_DESCRIPTOR, MockModelType.class);
		assertEquals(2, result.size());
		assertTrue(result.containsAll(c1));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#equivalent(org.spdx.core.CoreModelObject, boolean)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testEquivalentCoreModelObjectBoolean() throws InvalidSPDXAnalysisException {
		// String
		String strValue = "This is a string";
		modelType.setPropertyValue(STRING_PROPERTY_DESCRIPTOR, strValue);
		// boolean
		modelType.setPropertyValue(BOOLEAN_PROPERTY_DESCRIPTOR, false);
		// enumeration
		modelType.setPropertyValue(ENUM_PROPERTY_DESCRIPTOR, MockEnum.ENUM1);
		// individual
		MockIndividual individual = new MockIndividual();
		modelType.setPropertyValue(INDIVIDUAL_PROPERTY_DESCRIPTOR, individual);
		// collection
		Collection<String> c1 = Arrays.asList(new String[] {"s1", "s2"});
		modelType.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		// Integer
		modelType.setPropertyValue(INTEGER_PROPERTY_DESCRIPTOR, 15);
		// Object property
		String propModelTypeUri = "urn:propertyuri";
		MockModelType propModelType = new MockModelType(modelStore, propModelTypeUri, copyManager, true, "3.0.0");
		String subPropertyStrValue = "Sub property string";
		propModelType.setPropertyValue(STRING_PROPERTY_DESCRIPTOR, subPropertyStrValue);
		modelType.setPropertyValue(OBJECT_PROPERTY_DESCRIPTOR, propModelType);
		
		// Second one to compare
		String compareUri = "https://second/model/object";
		MockModelType compare = new MockModelType(modelStore, compareUri, copyManager, true, "3.0.0");
		compare.setPropertyValue(STRING_PROPERTY_DESCRIPTOR, strValue);
		compare.setPropertyValue(BOOLEAN_PROPERTY_DESCRIPTOR, false);
		compare.setPropertyValue(ENUM_PROPERTY_DESCRIPTOR, MockEnum.ENUM1);
		compare.setPropertyValue(INDIVIDUAL_PROPERTY_DESCRIPTOR, individual);
		compare.setPropertyValue(COLLECTION_PROPERTY_DESCRIPTOR, c1);
		compare.setPropertyValue(INTEGER_PROPERTY_DESCRIPTOR, 15);
		compare.setPropertyValue(OBJECT_PROPERTY_DESCRIPTOR, propModelType);
		
		assertTrue(modelType.equivalent(modelType));
		assertTrue(modelType.equivalent(compare));
		assertTrue(compare.equivalent(modelType));
		
		// change one of the properties
		modelType.setPropertyValue(INTEGER_PROPERTY_DESCRIPTOR, 12312);
		assertFalse(modelType.equivalent(compare));
		assertFalse(compare.equivalent(modelType));
	}

	/**
	 * Test method for {@link org.spdx.core.CoreModelObject#toTypedValue()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testToTypedValue() throws InvalidSPDXAnalysisException {
		TypedValue result = modelType.toTypedValue();
		assertEquals(OBJECT_URI, result.getObjectUri());
		assertEquals(MockModelType.TYPE, result.getType());
		assertEquals("3.0.0", result.getSpecVersion());
	}

}
