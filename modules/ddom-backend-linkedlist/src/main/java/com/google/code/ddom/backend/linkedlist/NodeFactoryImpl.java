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

import com.google.code.ddom.backend.CoreCDATASection;
import com.google.code.ddom.backend.CoreComment;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreDocumentTypeDeclaration;
import com.google.code.ddom.backend.CoreEntityReference;
import com.google.code.ddom.backend.CoreNSAwareElement;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.CoreNSUnawareElement;
import com.google.code.ddom.backend.CoreNSUnawareAttribute;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.CoreProcessingInstruction;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.NodeFactory;

public class NodeFactoryImpl implements NodeFactory {
    public CoreDocument createDocument() {
        return new Document(this);
    }

    public CoreDocumentTypeDeclaration createDocumentTypeDeclaration(CoreDocument document, String rootName, String publicId, String systemId) {
        return new DocumentTypeDeclaration((Document)document, rootName, publicId, systemId);
    }

    public CoreNSUnawareElement createElement(CoreDocument document, String tagName) {
        return new NSUnawareElement((Document)document, tagName, true);
    }
    
    public CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix) {
        return new NSAwareElement((Document)document, namespaceURI, localName, prefix, true);
    }
    
    public CoreNSUnawareAttribute createAttribute(CoreDocument document, String name, String value, String type) {
        return new NSUnawareAttribute((Document)document, name, value, type);
    }
    
    public CoreNSAwareAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new NSAwareAttribute((Document)document, namespaceURI, localName, prefix, value, type);
    }
    
    public CoreNamespaceDeclaration createNamespaceDeclaration(CoreDocument document, String prefix, String namespaceURI) {
        return new NamespaceDeclaration((Document)document, prefix, namespaceURI);
    }
    
    public CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data) {
        return new ProcessingInstruction((Document)document, target, data);
    }
    
    public CoreDocumentFragment createDocumentFragment(CoreDocument document) {
        return new DocumentFragment((Document)document);
    }

    public CoreText createText(CoreDocument document, String data) {
        return new Text((Document)document, data);
    }

    public CoreComment createComment(CoreDocument document, String data) {
        return new Comment((Document)document, data);
    }

    public CoreCDATASection createCDATASection(CoreDocument document, String data) {
        return new CDATASection((Document)document, data);
    }

    public CoreEntityReference createEntityReference(CoreDocument document, String name) {
        return new EntityReference((Document)document, name);
    }
}
