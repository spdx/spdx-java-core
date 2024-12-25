package org.spdx.core;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.spdx.storage.IModelStore;
import org.spdx.storage.MockModelStore;

/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */

/**
 * @author Gary O'Neall
 *
 */
public class TestModelRegistry {
	
	static final String OBJECT_URI = "https://myspdx.docs/objecturi#part1";
	
	MockModelStore modelStore;
	MockCopyManager copyManager;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() {
		modelStore = new MockModelStore();
		copyManager = new MockCopyManager();
	}

	/**
	 * Test method for {@link org.spdx.core.ModelRegistry#containsSpecVersion(java.lang.String)}.
	 * @throws InvalidSPDXAnalysisException 
	 */
	@Test
	public void testAll() throws InvalidSPDXAnalysisException {
		ModelRegistry.getModelRegistry().clearAll();
		assertFalse(ModelRegistry.getModelRegistry().containsSpecVersion("3.0.0"));
		Map<String, Enum<?>> uriToEnum = new HashMap<>();
		uriToEnum.put(MockEnum.ENUM1.getIndividualURI(), MockEnum.ENUM1);
		Map<String, Object> uriToIndividual = new HashMap<>();
		MockIndividual individual = new MockIndividual();
		uriToIndividual.put(individual.getIndividualURI(), individual);
		Map<String, Class<?>> classMap = new HashMap<>();
		classMap.put(MockModelType.TYPE, MockModelType.class);
		ModelRegistry.getModelRegistry().registerModel(new ISpdxModelInfo() {

			@Override
			public Map<String, Enum<?>> getUriToEnumMap() {
				return uriToEnum;
			}

			@Override
			public List<String> getSpecVersions() {
				return Arrays.asList(new String[] {"3.0.0"});
			}

			@Override
			public CoreModelObject createExternalElement(IModelStore store,
					String uri, IModelCopyManager copyManager, Class<?> type,
					String specVersion) throws InvalidSPDXAnalysisException {
				return new MockModelType(store, uri, copyManager, true, specVersion);
			}

			@Override
			public CoreModelObject createModelObject(IModelStore modelStore,
					String objectUri, String type,
					IModelCopyManager copyManager, String specVersion,
					boolean create, String idPrefix) throws InvalidSPDXAnalysisException {
				return new MockModelType(modelStore, objectUri, copyManager, create, specVersion);
			}

			@Override
			public Map<String, Class<?>> getTypeToClassMap() {
				return classMap;
			}

			@Override
			public Object uriToIndividual(String uri, @Nullable Class<?> type) {
				return uriToIndividual.get(uri);
			}

			@Override
			public boolean canBeExternal(Class<?> clazz) {
				return false;
			}
			
		});
		assertTrue(ModelRegistry.getModelRegistry().containsSpecVersion("3.0.0"));
		CoreModelObject result = ModelRegistry.getModelRegistry().inflateModelObject(modelStore, OBJECT_URI, MockModelType.TYPE,
				copyManager, "3.0.0", true, null);
		assertEquals(OBJECT_URI, result.getObjectUri());
		Object oResult = ModelRegistry.getModelRegistry().getExternalElement(modelStore, OBJECT_URI, copyManager, null, "3.0.0");
		assertTrue(oResult instanceof MockModelType);
		assertEquals(OBJECT_URI, ((MockModelType)oResult).getObjectUri());
		Class<?> cResult = ModelRegistry.getModelRegistry().typeToClass(MockModelType.TYPE, "3.0.0");
		assertEquals(cResult, MockModelType.class);
		Enum<?> eResult = ModelRegistry.getModelRegistry().uriToEnum(MockEnum.ENUM1.getIndividualURI(), "3.0.0");
		assertEquals(MockEnum.ENUM1, eResult);
		Object iResult = ModelRegistry.getModelRegistry().uriToIndividual(individual.getIndividualURI(), "3.0.0", null);
		assertEquals(individual.getIndividualURI(), ((MockIndividual)iResult).getIndividualURI());
	}

}
