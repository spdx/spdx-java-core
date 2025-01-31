/**
 * SPDX-FileCopyrightText: Copyright (c) 2024 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.core;

/**
 * Value which is a stored typed item
 */
public class TypedValue {
	
	String objectUri;
	String type;
	String specVersion;
	
	/**
	 * Constructs a TypedValue with the specified object URI, type, and spec version.
	 * 
	 * @param objectUri URI or anon ID for the object
	 * @param type a string representation of the type of the object
	 * @param specVersion version of the spec
	 * @throws SpdxInvalidIdException if the ID is not valid
	 * @throws SpdxInvalidTypeException if the type is not valid
	 * @throws ModelRegistryException if there is an error in the model registry
	 */
	public TypedValue(String objectUri, String type, String specVersion) throws SpdxInvalidIdException, SpdxInvalidTypeException, ModelRegistryException {
		if (objectUri == null) {
			throw new SpdxInvalidIdException("Null value Id");
		}
		if (type == null) {
			throw new SpdxInvalidTypeException("Null type");
		}
		if (specVersion == null) {
			throw new SpdxInvalidTypeException("Null specVersion");
		}
		if (ModelRegistry.getModelRegistry().typeToClass(type, specVersion) == null) {
			throw new SpdxInvalidTypeException("Unknown type "+type+" for spec Version "+specVersion);
		}
		this.objectUri = objectUri;
		this.type = type;
		this.specVersion = specVersion;
	}

	/**
	 * @return the objectUri
	 */
	public String getObjectUri() {
		return objectUri;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the specVersion
	 */
	public String getSpecVersion() {
		return specVersion;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof TypedValue)) {
			return false;
		}
		TypedValue tv = (TypedValue)o;
		return tv.getObjectUri().equals(this.objectUri) && tv.getType().equals(this.type);
	}
	
	@Override
	public int hashCode() {
		return 181 ^ this.objectUri.hashCode() ^ this.type.hashCode();
	}
	
	@Override
	public String toString() {
		return this.objectUri + ":" + this.type + "(" + this.specVersion + ")";
	}
}