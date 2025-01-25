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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.spdx.storage.IModelStore;

/**
 * Singleton class to hold a default model store used when no model store is provided
 * <p>
 * WARNING: The model store and copy manager are in memory and will continue to grow as they are utilized.  There is NO garbage collection.
 * 
 * @author Gary O'Neall
 *
 */
public class DefaultModelStore {

	static IModelStore defaultStore = null;
	static String defaultDocumentUri = "http://www.spdx.org/documents/default_doc_uri_for_SPDX_tools";
	static IModelCopyManager defaultCopyManager = null;
	static final String NOT_INITIALIZED_MSG = "Default model store has not been initialized";
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private DefaultModelStore() {
		// prevent instantiating class
	}

	/**
	 * Checks if the default model store is initialized
	 * @return true if the model store is initialized
	 */
	public static boolean isInitialized() {
		lock.readLock().lock();
		try {
			return Objects.nonNull(defaultStore);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/**
	 * @return the default model store
	 * @throws DefaultStoreNotInitialized if the <code>initialize(...)</code> was not called prior
	 */
	public static IModelStore getDefaultModelStore() throws DefaultStoreNotInitialized {
		lock.readLock().lock();
		try {
			if (Objects.isNull(defaultStore)) {
				throw new DefaultStoreNotInitialized(NOT_INITIALIZED_MSG);
			}
			return defaultStore;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/**
	 * @return the default SPDX 2.X document URi
	 * @throws DefaultStoreNotInitialized if the <code>initialize(...)</code> was not called prior
	 */
	public static String getDefaultDocumentUri() throws DefaultStoreNotInitialized {
		lock.readLock().lock();
		try {
			if (Objects.isNull(defaultDocumentUri)) {
				throw new DefaultStoreNotInitialized(NOT_INITIALIZED_MSG);
			}
			return defaultDocumentUri;
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/**
	 * Initialize the default model store.  This must be done prior to any use
	 * @param newModelStore new default model store
	 * @param newDefaultDocumentUri new SPDX 2.X document URI
	 * @param newDefaultCopyManager new default copy manager
	 */
	public static void initialize(IModelStore newModelStore, String newDefaultDocumentUri,
			IModelCopyManager newDefaultCopyManager) {
		Objects.requireNonNull(newModelStore, "Model store can not be null");
		Objects.requireNonNull(newDefaultDocumentUri, "Document URI can not be null");
		Objects.requireNonNull(newDefaultCopyManager, "Copy manager can not be null");
		lock.writeLock().lock();
		try {
			defaultStore = newModelStore;
			defaultDocumentUri = newDefaultDocumentUri;
			defaultCopyManager = newDefaultCopyManager;
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * @return the default copy manager
	 * @throws DefaultStoreNotInitialized if the <code>initialize(...)</code> was not called prior
	 */
	public static IModelCopyManager getDefaultCopyManager() throws DefaultStoreNotInitialized {
		lock.readLock().lock();
		try {
			if (Objects.isNull(defaultCopyManager)) {
				throw new DefaultStoreNotInitialized(NOT_INITIALIZED_MSG);
			}
			return defaultCopyManager;
		} finally {
			lock.readLock().unlock();
		}
	}

}
