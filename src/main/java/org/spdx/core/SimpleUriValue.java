/**
 * Copyright (c) 2019 Source Auditor Inc.
 * <p>
 * SPDX-License-Identifier: Apache-2.0
 * <p>
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * <p>
 *       http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.spdx.core;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spdx.storage.IModelStore;

/**
 * Simple class to just store a URI value
 * <p>
 * The method toModelObject will convert / inflate the value back to
 * either an Enum (if the URI matches), an ExternalSpdxElement if it matches
 * the pattern of an external SPDX element 
 * or returns itself otherwise
 * 
 * @author Gary O'Neall
 */
public class SimpleUriValue implements IndividualUriValue {
	
	static final Logger logger = LoggerFactory.getLogger(SimpleUriValue.class);
	
	private final String uri;

	/**
	 * returns hash based on URI of the IndividualUriValue
	 * @param individualUri IndividualUriValue to obtain a hash from
	 * @return hash based on URI of the IndividualUriValue
	 */
	public static int getIndividualUriValueHash(IndividualUriValue individualUri) {
		return 11 ^ individualUri.getIndividualURI().hashCode();
	}
	
	/**
	 * Compares an object to an individual URI and returns true if the URI values are equal
	 * @param individualUri IndividualUriValue to compare
	 * @param comp Object to compare
	 * @return true if the individualUri has the same URI as comp and comp is of type IndividualUriValue
	 */
	public static boolean isIndividualUriValueEquals(IndividualUriValue individualUri, Object comp) {
		if (!(comp instanceof IndividualUriValue)) {
			return false;
		}
		return Objects.equals(individualUri.getIndividualURI(), ((IndividualUriValue)comp).getIndividualURI());
	}

	/**
	 * @param fromIndividualValue individual value to copy the URI from
	 */
	public SimpleUriValue(IndividualUriValue fromIndividualValue) {
		this(fromIndividualValue.getIndividualURI());
	}
	
	/**
	 * @param uri URI for the value
	 */
	public SimpleUriValue(String uri) {
		Objects.requireNonNull(uri, "URI can not be null");
		this.uri = uri;
	}

	/* (non-Javadoc)
	 * @see org.spdx.library.model.compat.v2.compat.v2.IndividualValue#getIndividualURI()
	 */
	@Override
	public String getIndividualURI() {
		return uri;
	}
	
	/**
	 * inflate the value back to either an Enum (if the URI matches),  an Individual (if the URI matches),
	 * the modelObject (if the store contains the object matching the URI), or an ExternalObject if not in
	 * the store
	 * @param store store to use for the inflated object
	 * @param copyManager if non-null, implicitly copy any referenced properties from other model stores
	 * @param specVersion version of the SPDX spec the object complies with
	 * @param type optional type hint - used for individuals where the type may be ambiguous
	 * @return Enum, ExternalSpdxElement or itself depending on the pattern
	 * @throws InvalidSPDXAnalysisException on any store or parsing error
	 */
	public Object toModelObject(IModelStore store, IModelCopyManager copyManager,
			String specVersion, @Nullable Class<?> type) throws InvalidSPDXAnalysisException {
		Object retval = ModelRegistry.getModelRegistry().uriToEnum(uri, specVersion);
		if (Objects.nonNull(retval)) {
			return retval;
		}
		retval = ModelRegistry.getModelRegistry().uriToIndividual(uri, specVersion, type);
		if (Objects.nonNull(retval)) {
			return retval;
		}
		Optional<TypedValue> typedValue = store.getTypedValue(uri);
		if (typedValue.isPresent()) {
			return ModelRegistry.getModelRegistry().inflateModelObject(store, uri, typedValue.get().type, 
					copyManager, specVersion, false, null); // note that idPrefix would not be used since any any of the inflated model objects
		} else {
			retval = ModelRegistry.getModelRegistry().getExternalElement(store, uri, copyManager, type, specVersion);
			if (Objects.isNull(retval)) {
				logger.warn("{} does not match an enum, individual, or external pattern", this.getIndividualURI());
				retval = this;
			}
		}
		return retval;
	}
	
	@SuppressWarnings("EqualsDoesntCheckParameterClass")
    @Override
	public boolean equals(Object comp) {
		return isIndividualUriValueEquals(this, comp);
	}

	@Override
	public int hashCode() {
		return getIndividualUriValueHash(this);
	}
}
