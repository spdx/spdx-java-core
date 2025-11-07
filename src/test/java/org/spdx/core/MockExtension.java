/*
 * SPDX-FileCopyrightText: Copyright (c) 2025 Source Auditor Inc.
 * SPDX-FileType: SOURCE
 * SPDX-License-Identifier: Apache-2.0
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spdx.core;

import org.spdx.storage.IModelStore;

public class MockExtension extends MockModelType {

    public static final String MOCK_EXTENSION_TYPE = "extensiontype";

    public MockExtension(IModelStore modelStore, String objectUri, IModelCopyManager copyManager, boolean create, String specVersion, String idPrefix) throws InvalidSPDXAnalysisException {
        super(modelStore, objectUri, copyManager, create, specVersion, idPrefix);
    }

    @Override
    public String getType() {
        return MOCK_EXTENSION_TYPE;
    }
}
