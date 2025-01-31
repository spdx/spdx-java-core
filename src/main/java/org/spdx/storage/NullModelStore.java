/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.storage;

import java.util.*;
import java.util.stream.Stream;

import org.spdx.core.InvalidSPDXAnalysisException;
import org.spdx.core.TypedValue;

/**
 * Null model store to be used with constants and individuals
 * 
 * @author Gary O'Neall
 *
 */
public class NullModelStore implements IModelStore {

	private static final String NULL_MODEL_MSG = "Null model store - can only be used with constants and individuals";

	@Override
	public void close() throws Exception {
		// Nothing to close
	}

	@Override
	public boolean exists(String objectUri) {
		return false;
	}

	@Override
	public void create(TypedValue typedValue)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public List<PropertyDescriptor> getPropertyValueDescriptors(
			String objectUri) throws InvalidSPDXAnalysisException {
		return new ArrayList<>();
	}

	@Override
	public void setValue(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public Optional<Object> getValue(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		return Optional.empty();
	}

	@Override
	public String getNextId(IdType idType) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public void removeProperty(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public Stream<TypedValue> getAllItems(String nameSpace, String typeFilter)
			throws InvalidSPDXAnalysisException {
		return Stream.empty();
	}

	@Override
	public IModelStoreLock enterCriticalSection(boolean readLockRequested)
			throws InvalidSPDXAnalysisException {
		return () -> {
            // no need to do anything
        };
	}

	@Override
	public void leaveCriticalSection(IModelStoreLock lock) {
		lock.unlock();
	}

	@Override
	public boolean removeValueFromCollection(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public int collectionSize(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public boolean collectionContains(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public void clearValueCollection(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public boolean addValueToCollection(String objectUri,
			PropertyDescriptor propertyDescriptor, Object value)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public Iterator<Object> listValues(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
        return Collections.emptyIterator();
	}

	@Override
	public boolean isCollectionMembersAssignableTo(String objectUri,
			PropertyDescriptor propertyDescriptor, Class<?> clazz)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public boolean isPropertyValueAssignableTo(String objectUri,
			PropertyDescriptor propertyDescriptor, Class<?> clazz,
			String specVersion) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public boolean isCollectionProperty(String objectUri,
			PropertyDescriptor propertyDescriptor)
			throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public IdType getIdType(String objectUri) {
		return IdType.Unkown;
	}

	@Override
	public Optional<String> getCaseSensitiveId(String nameSpace,
			String caseInsensitiveId) {
		return Optional.empty();
	}

	@Override
	public Optional<TypedValue> getTypedValue(String objectUri)
			throws InvalidSPDXAnalysisException {
		return Optional.empty();
	}

	@Override
	public void delete(String objectUri) throws InvalidSPDXAnalysisException {
		throw new InvalidSPDXAnalysisException(NULL_MODEL_MSG);
	}

	@Override
	public boolean isAnon(String objectUri) {
		return false;
	}

}
