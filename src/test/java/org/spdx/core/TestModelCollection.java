/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.spdx.storage.MockModelStore;
import org.spdx.storage.PropertyDescriptor;

/**
 * @author Gary O'Neall
 *
 */
public class TestModelCollection {
	
	static final String OBJECT_URI = "https://myspdx.docs/objecturi#part1";
	static final String MODEL_COLLECTION_URI = "https://myspdx.docs/collection";
	static final String PROPERTY_NAMESPACE = "https://spdx-mock/namespace";
	static final String COLLECTION_PROPERTY_NAME = "collectionPropName";
	static final PropertyDescriptor COLLECTION_PROPERTY_DESCRIPTOR = new PropertyDescriptor(COLLECTION_PROPERTY_NAME, PROPERTY_NAMESPACE);

	MockModelStore modelStore;
	MockCopyManager copyManager;
	MockModelType modelType;
	ModelCollection<MockModelType> modelCollection;
	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Before
	public void setUp() throws Exception {
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
		ModelRegistry.getModelRegistry().registerModel(new MockModelInfo());
		modelType = new MockModelType(modelStore, OBJECT_URI, copyManager, true, "3.0.0"); // creates the mock model in the store
		modelStore.create(new TypedValue(MODEL_COLLECTION_URI, MockModelType.TYPE, "3.0.0"));
		modelCollection = new ModelCollection(modelStore, MODEL_COLLECTION_URI, COLLECTION_PROPERTY_DESCRIPTOR,
				copyManager, MockModelType.class, "3.0.0", null);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#ModelCollection(org.spdx.storage.IModelStore, java.lang.String, org.spdx.storage.PropertyDescriptor, org.spdx.core.IModelCopyManager, java.lang.Class, java.lang.String)}.
	 */
	@Test
	public void testModelCollection() {
		assertEquals(MODEL_COLLECTION_URI, modelCollection.getObjectUri());
		assertEquals(modelStore, modelCollection.getModelStore());
		assertEquals(COLLECTION_PROPERTY_DESCRIPTOR, modelCollection.getPropertyDescriptor());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#size()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testSize() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		assertEquals(3, modelCollection.size());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#isEmpty()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testIsEmpty() throws InvalidSPDXAnalysisException {
		assertTrue(modelCollection.isEmpty());
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		assertFalse(modelCollection.isEmpty());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#contains(java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testContains() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		assertTrue(modelCollection.contains(m1));
		assertFalse(modelCollection.contains(m3));
		assertTrue(modelCollection.contains(m2));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#toImmutableList()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testToImmutableList() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#iterator()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testIterator() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		Iterator<Object> iter = modelCollection.iterator();
		int count = 0;
		while (iter.hasNext()) {
			Object o = iter.next();
			assertTrue(expected.contains(o));
			count++;
		}
		assertEquals(expected.size(), count);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#toArray()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testToArray() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		MockModelType[] expected = new MockModelType[] {m1, m2, m3};
		Object[] result = modelCollection.toArray();
		assertTrue(Arrays.deepEquals(expected, result));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#toArray(AT[])}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testToArrayATArray() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		MockModelType[] expected = new MockModelType[] {m1, m2, m3};
		MockModelType[] result = modelCollection.toArray(new MockModelType[3]);
		assertTrue(Arrays.deepEquals(expected, result));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#add(java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAdd() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2});
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
		modelCollection.add(m3);
		expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#remove(java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRemove() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
		modelCollection.remove(m2);
		expected = Arrays.asList(new MockModelType[] {m1, m3});
		result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#containsAll(java.util.Collection)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testContainsAll() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		assertTrue(modelCollection.containsAll(expected));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#addAll(java.util.Collection)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAddAll() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		List<MockModelType> added = Arrays.asList(new MockModelType[] {m2, m3});
		modelCollection.addAll(added);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		assertTrue(modelCollection.containsAll(expected));
		assertEquals(expected.size(), modelCollection.size());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#removeAll(java.util.Collection)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRemoveAll() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
		modelCollection.removeAll(expected);
		assertTrue(modelCollection.isEmpty());
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#retainAll(java.util.Collection)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testRetainAll() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2});
		modelCollection.retainAll(expected);
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelCollection#clear()}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testClear() throws InvalidSPDXAnalysisException {
		MockModelType m1 = new MockModelType(modelStore, "https://uri/m1", copyManager, true, "3.0.0");
		MockModelType m2 = new MockModelType(modelStore, "https://uri/m2", copyManager, true, "3.0.0");
		MockModelType m3 = new MockModelType(modelStore, "https://uri/m3", copyManager, true, "3.0.0");
		modelCollection.add(m1);
		modelCollection.add(m2);
		modelCollection.add(m3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {m1, m2, m3});
		List<Object> result = modelCollection.toImmutableList();
		assertEquals(expected.size(), result.size());
		assertTrue(expected.containsAll(result) && result.containsAll(expected));
		modelCollection.clear();
		assertTrue(modelCollection.isEmpty());
	}
}
