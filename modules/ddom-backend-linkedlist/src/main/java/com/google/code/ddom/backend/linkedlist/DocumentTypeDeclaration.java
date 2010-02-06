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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.CoreDocumentTypeDeclaration;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;

@Implementation
public class DocumentTypeDeclaration extends LeafNode implements CoreDocumentTypeDeclaration {
    private final String rootName;
    private final String publicId;
    private final String systemId;
    
    public DocumentTypeDeclaration(Document document, String rootName, String publicId, String systemId) {
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

    @Override
    final CharSequence collectTextContent(CharSequence appendTo) throws DeferredParsingException {
        return appendTo;
    }
}
