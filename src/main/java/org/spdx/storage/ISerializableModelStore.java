/**
 * SPDX-FileCopyrightText: Copyright (c) 2019 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 */
package org.spdx.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nullable;

import org.spdx.core.CoreModelObject;
import org.spdx.core.InvalidSPDXAnalysisException;

/**
 * A model store that can be serialized and de-serialized to and from a <code>Stream</code>
 *
 * @author Gary O'Neall
 */
@SuppressWarnings("unused")
public interface ISerializableModelStore extends IModelStore {

	/**
	 * Serialize all the items stored in the model store
	 * <p>
	 * The specific format for serialization depends on the document store.
	 * 
	 * @param stream output stream to serialize to
	 * @throws InvalidSPDXAnalysisException on any SPDX error
	 * @throws IOException on IO error
	 */
    void serialize(OutputStream stream)  throws InvalidSPDXAnalysisException, IOException;
	
	/**
	 * Serialize the items stored in the model store
	 * <p>
	 * The specific format for serialization depends on the document store.
	 *
	 * @param stream output stream to serialize to
	 * @param objectToSerialize if an SpdxDocument, serialize all elements represented by that document, otherwise
	 * serialize just the object.  If null, serialize all items in the store.
	 * @throws InvalidSPDXAnalysisException on any SPDX error
	 * @throws IOException on IO error
	 */
    void serialize(OutputStream stream, @Nullable CoreModelObject objectToSerialize)  throws InvalidSPDXAnalysisException, IOException;
	
	/**
	 * Deserialize / read an SPDX document from a stream
	 *
	 * @param stream input stream to deserialize from
	 * @param overwrite if true, allow any existing documents with the same documentUri to be overwritten
	 * @return a model object representing the deserialized SPDX data - commonly an SPDX Document, but depends on version and what was serialized
	 * @throws InvalidSPDXAnalysisException on any SPDX error
	 * @throws IOException on IO error
	 */
    CoreModelObject deSerialize(InputStream stream, boolean overwrite) throws InvalidSPDXAnalysisException, IOException;
}
