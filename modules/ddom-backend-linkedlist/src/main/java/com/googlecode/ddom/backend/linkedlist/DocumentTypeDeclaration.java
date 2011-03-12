/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.Implementation;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.stream.XmlHandler;

// @Implementation
public class DocumentTypeDeclaration extends LeafNode implements CoreDocumentTypeDeclaration {
    private final String rootName;
    private final String publicId;
    private final String systemId;
    private final String data; // TODO: has no accessors!
    
    public DocumentTypeDeclaration(Document document, String rootName, String publicId, String systemId, String data) {
        super(document);
        this.rootName = rootName;
        this.publicId = publicId;
        this.systemId = systemId;
        this.data = data;
    }

    public final int coreGetNodeType() {
        return DOCUMENT_TYPE_DECLARATION_NODE;
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

    public final void internalGenerateEvents(XmlHandler handler) {
        handler.processDocumentType(rootName, publicId, systemId, data);
    }
}
