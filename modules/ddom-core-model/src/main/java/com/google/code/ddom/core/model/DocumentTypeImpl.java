/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.ddom.core.model;

import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreDocumentType;
import com.google.code.ddom.spi.model.Implementation;

@Implementation
public class DocumentTypeImpl extends LeafNodeImpl implements CoreDocumentType {
    private final String rootName;
    private final String publicId;
    private final String systemId;
    
    public DocumentTypeImpl(CoreDocument document, String rootName, String publicId, String systemId) {
        super(document);
        this.rootName = rootName;
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public final String coreGetRootName() {
        return rootName;
    }

    public final String coreGetPublicId() {
        return publicId;
    }

    public final String coreGetSystemId() {
        return systemId;
    }
}
