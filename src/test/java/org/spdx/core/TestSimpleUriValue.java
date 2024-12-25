/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.spdx.storage.MockModelStore;

/**
 * @author Gary O'Neall
 *
 */
public class TestSimpleUriValue {
	
	static final String URI = "http://individual/uri";
	static final String URI2 = "http://individual/uri2";
	
	MockModelStore modelStore;
	MockCopyManager copyManager;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
		ModelRegistry.getModelRegistry().registerModel(new MockModelInfo());
	}

	/**
	 * Test method for {@link org.spdx.core.SimpleUriValue#isIndividualUriValueEquals(org.spdx.core.IndividualUriValue, java.lang.Object)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testIsIndividualUriValueEquals() {
		SimpleUriValue suv = new SimpleUriValue(URI);
		SimpleUriValue same = new SimpleUriValue(URI);
		SimpleUriValue different = new SimpleUriValue(URI2);
		assertTrue(SimpleUriValue.isIndividualUriValueEquals(suv, same));
		assertFalse(SimpleUriValue.isIndividualUriValueEquals(suv, different));
	}

	/**
	 * Test method for {@link org.spdx.core.SimpleUriValue#SimpleUriValue(org.spdx.core.IndividualUriValue)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testSimpleUriValueIndividualUriValue() {
		SimpleUriValue suv = new SimpleUriValue(URI);
		SimpleUriValue same = new SimpleUriValue(suv);
		assertEquals(suv, same);
		assertEquals(MockEnum.ENUM1.getIndividualURI(), new SimpleUriValue(MockEnum.ENUM1).getIndividualURI());
	}

	/**
	 * Test method for {@link org.spdx.core.SimpleUriValue#SimpleUriValue(java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testSimpleUriValueString() {
		SimpleUriValue suv = new SimpleUriValue(URI);
		assertEquals(URI, suv.getIndividualURI());
	}

	/**
	 * Test method for {@link org.spdx.core.SimpleUriValue#toModelObject(org.spdx.storage.IModelStore, org.spdx.core.IModelCopyManager, java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testToModelObject() throws InvalidSPDXAnalysisException {
		// enum
		SimpleUriValue enumSimple = new SimpleUriValue(MockEnum.ENUM1);
		Object result = enumSimple.toModelObject(modelStore, copyManager, "3.0.0", null);
		assertTrue(result instanceof MockEnum);
		assertEquals(MockEnum.ENUM1, result);
		// individual
		MockIndividual individual = new MockIndividual();
		SimpleUriValue indSimple = new SimpleUriValue(individual);
		result = indSimple.toModelObject(modelStore, copyManager, "3.0.0", null);
		assertTrue(result instanceof MockIndividual);
		// neither
	}

}
