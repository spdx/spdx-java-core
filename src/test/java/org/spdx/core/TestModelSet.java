/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.spdx.storage.MockModelStore;
import org.spdx.storage.PropertyDescriptor;

/**
 * @author Gary O'Neall
 *
 */
public class TestModelSet {
	
	static final String OBJECT_URI = "https://myspdx.docs/objecturi#part1";
	static final String MODEL_COLLECTION_URI = "https://myspdx.docs/collection";
	static final String PROPERTY_NAMESPACE = "https://spdx-mock/namespace";
	static final String COLLECTION_PROPERTY_NAME = "collectionPropName";
	static final PropertyDescriptor COLLECTION_PROPERTY_DESCRIPTOR = new PropertyDescriptor(COLLECTION_PROPERTY_NAME, PROPERTY_NAMESPACE);

	MockModelStore modelStore;
	MockCopyManager copyManager;
	MockModelType modelType;
	ModelSet<MockModelType> modelSet;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Before
	public void setUp() throws Exception {
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
		ModelRegistry.getModelRegistry().registerModel(new MockModelInfo());
		modelType = new MockModelType(modelStore, OBJECT_URI, copyManager, true, "3.0.0"); // creates the mock model in the store
		modelStore.create(new TypedValue(MODEL_COLLECTION_URI, MockModelType.TYPE, "3.0.0"));
		modelSet = new ModelSet(modelStore, MODEL_COLLECTION_URI, COLLECTION_PROPERTY_DESCRIPTOR,
				copyManager, MockModelType.class, "3.0.0", null);
	}

	/**
	 * Test method for {@link org.spdx.core.ModelSet#add(java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAdd() throws InvalidSPDXAnalysisException {
		assertTrue(modelSet.isEmpty());
		MockModelType mt1 = new MockModelType(modelStore, "http://uri1", copyManager, true, "3.0.0");
		MockModelType mt2 = new MockModelType(modelStore, "http://uri2", copyManager, true, "3.0.0");
		MockModelType mt3 = new MockModelType(modelStore, "http://uri3", copyManager, true, "3.0.0");
		modelSet.add(mt1);
		assertEquals(1, modelSet.size());
		assertTrue(modelSet.contains(mt1));
		modelSet.add(mt2);
		modelSet.add(mt3);
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {mt1, mt2, mt3});
		assertTrue(modelSet.size() == expected.size() && modelSet.containsAll(expected) && expected.containsAll(modelSet));
		
		// make sure we can't add the same type twice
		modelSet.add(mt2);
		assertTrue(modelSet.size() == expected.size() && modelSet.containsAll(expected) && expected.containsAll(modelSet));
	}

	/**
	 * Test method for {@link org.spdx.core.ModelSet#addAll(java.util.Collection)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAddAll() throws InvalidSPDXAnalysisException {
		assertTrue(modelSet.isEmpty());
		MockModelType mt1 = new MockModelType(modelStore, "http://uri1", copyManager, true, "3.0.0");
		MockModelType mt2 = new MockModelType(modelStore, "http://uri2", copyManager, true, "3.0.0");
		MockModelType mt3 = new MockModelType(modelStore, "http://uri3", copyManager, true, "3.0.0");
		List<MockModelType> expected = Arrays.asList(new MockModelType[] {mt1, mt2, mt3});
		modelSet.addAll(expected);
		assertTrue(modelSet.size() == expected.size() && modelSet.containsAll(expected) && expected.containsAll(modelSet));
		
		// make sure we can't add the same type twice
		modelSet.addAll(expected);
		assertTrue(modelSet.size() == expected.size() && modelSet.containsAll(expected) && expected.containsAll(modelSet));
	}

}
