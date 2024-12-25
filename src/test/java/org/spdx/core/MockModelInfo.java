/**
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) 2024 Source Auditor Inc.
 */
package org.spdx.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.spdx.storage.IModelStore;

/**
 * @author Gary O'Neall
 *
 */
public class MockModelInfo implements ISpdxModelInfo {
	
	static final Map<String, Enum<?>> URI_TO_ENUM_MAP = new HashMap<>();
	static final Map<String, Object> URI_TO_INDIVIDUAL_MAP = new HashMap<>();
	static final List<String> SPEC_VERSIONS = Arrays.asList(new String[] {"3.0.0", "SPDX-2.3"});
	static final Map<String, Class<?>> TYPE_TO_CLASS_MAP = new HashMap<>();
	
	static {
		TYPE_TO_CLASS_MAP.put(MockModelType.TYPE, MockModelType.class);
		URI_TO_ENUM_MAP.put(MockEnum.ENUM1.getIndividualURI(), MockEnum.ENUM1);
		URI_TO_ENUM_MAP.put(MockEnum.ENUM2.getIndividualURI(), MockEnum.ENUM2);
		URI_TO_INDIVIDUAL_MAP.put(MockIndividual.INDIVIDUAL_URI, new MockIndividual());
	}

	@Override
	public Map<String, Enum<?>> getUriToEnumMap() {
		return URI_TO_ENUM_MAP;
	}

	@Override
	public List<String> getSpecVersions() {
		return SPEC_VERSIONS;
	}

	@Override
	public CoreModelObject createExternalElement(IModelStore store, String uri,
			IModelCopyManager copyManager, Class<?> type, String specVersion)
			throws InvalidSPDXAnalysisException {
		// Mock - not implemented
		return null;
	}

	@Override
	public Object uriToIndividual(String uri, @Nullable Class<?> type) {
		return URI_TO_INDIVIDUAL_MAP.get(uri);
	}

	@Override
	public CoreModelObject createModelObject(IModelStore modelStore,
			String objectUri, String type, IModelCopyManager copyManager,
			String specVersion, boolean create, String idPrefix)
			throws InvalidSPDXAnalysisException {
		if (type.equals(MockModelType.TYPE)) {
			return new MockModelType(modelStore, objectUri, copyManager, create, specVersion);
		} else {
			return null;
		}
	}

	@Override
	public Map<String, Class<?>> getTypeToClassMap() {
		return TYPE_TO_CLASS_MAP;
	}

	@Override
	public boolean canBeExternal(Class<?> clazz) {
		return false;
	}

}
