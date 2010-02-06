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
package com.google.code.ddom.frontend.dom.support;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.NamedNodeMap;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentType;
import com.google.code.ddom.frontend.dom.intf.DOMDocumentTypeDeclaration;

public class DocumentTypeImpl implements DOMDocumentType {
    private DOMDocumentTypeDeclaration declaration;
    private Map<String,UserData> userData;
    private DOMImplementation domImplementation;
    private String name;
    private String publicId;
    private String systemId;

    public DocumentTypeImpl(DOMImplementation domImplementation, String name, String publicId, String systemId) {
        this.domImplementation = domImplementation;
        this.name = name;
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public DocumentTypeImpl(DOMDocumentTypeDeclaration declaration) {
        this.declaration = declaration;
    }
    
    public final DOMDocumentTypeDeclaration getDeclaration() {
        return declaration;
    }

    public final DOMDocumentTypeDeclaration attach(DOMDocument document) {
        if (declaration != null) {
            throw new IllegalStateException();
        }
        declaration = (DOMDocumentTypeDeclaration)document.getNodeFactory().createDocumentTypeDeclaration(document, name, publicId, systemId);
        name = null;
        publicId = null;
        systemId = null;
        return declaration;
    }

    public final Map<String,UserData> getUserDataMap(boolean create) {
        if (create && userData == null) {
            userData = new HashMap<String,UserData>();
        }
        return userData;
    }

    public final DOMImplementation getDOMImplementation() {
        return declaration == null ? domImplementation : ((DOMDocument)declaration.coreGetDocument()).getImplementation();
    }

    public final String getName() {
        return declaration == null ? name : declaration.coreGetRootName();
    }

    public final String getPublicId() {
        return declaration == null ? publicId : declaration.coreGetPublicId();
    }

    public final String getSystemId() {
        return declaration == null ? systemId : declaration.coreGetSystemId();
    }

    public final NamedNodeMap getEntities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getInternalSubset() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final NamedNodeMap getNotations() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
