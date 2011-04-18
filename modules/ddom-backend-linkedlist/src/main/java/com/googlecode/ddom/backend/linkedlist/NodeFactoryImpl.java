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

import com.googlecode.ddom.backend.ExtensionFactoryLocator;
import com.googlecode.ddom.backend.Inject;
import com.googlecode.ddom.core.CoreCDATASection;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreComment;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreDocumentFragment;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.CoreNSUnawareElement;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.CoreProcessingInstruction;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.core.ext.ModelExtension;

public class NodeFactoryImpl implements NodeFactory {
    public static final NodeFactory INSTANCE = new NodeFactoryImpl();
    
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    @Inject
    private ModelExtension modelExtension; // = ModelExtension.NULL;
    
    private NodeFactoryImpl() {}
    
    public final CoreDocument createDocument() {
        return new Document(modelExtension == null ? ModelExtension.NULL : modelExtension);
    }

    public final CoreDocumentTypeDeclaration createDocumentTypeDeclaration(CoreDocument document, String rootName, String publicId, String systemId) {
        return new DocumentTypeDeclaration((Document)document, rootName, publicId, systemId);
    }

    public final CoreNSUnawareElement createElement(CoreDocument document, String tagName) {
        return new NSUnawareElement((Document)document, tagName, true);
    }
    
    public final CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix) {
        return nsAwareElementFactory.create(modelExtension == null ? null : modelExtension.mapElement(namespaceURI, localName), (Document)document, namespaceURI, localName, prefix, true);
    }
    
    public final <T extends CoreNSAwareElement> T createElement(CoreDocument document, Class<T> extensionInterface, String namespaceURI, String localName, String prefix) {
        return extensionInterface.cast(nsAwareElementFactory.create(extensionInterface, (Document)document, namespaceURI, localName, prefix, true));
    }
    
    public final CoreNSUnawareAttribute createAttribute(CoreDocument document, String name, String value, String type) {
        return new NSUnawareAttribute((Document)document, name, value, type);
    }
    
    public final CoreNSAwareAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new NSAwareAttribute((Document)document, namespaceURI, localName, prefix, value, type);
    }
    
    public final CoreNamespaceDeclaration createNamespaceDeclaration(CoreDocument document, String prefix, String namespaceURI) {
        return new NamespaceDeclaration((Document)document, prefix, namespaceURI);
    }
    
    public final CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data) {
        return new ProcessingInstruction((Document)document, target, data);
    }
    
    public final CoreDocumentFragment createDocumentFragment(CoreDocument document) {
        return new DocumentFragment((Document)document);
    }

    public final CoreCharacterData createCharacterData(CoreDocument document, String data) {
        return new CharacterData((Document)document, data, false);
    }

    public final CoreComment createComment(CoreDocument document, String data) {
        return new Comment((Document)document, data);
    }

    public final CoreCDATASection createCDATASection(CoreDocument document, String data) {
        return new CDATASection((Document)document, data);
    }

    public final CoreEntityReference createEntityReference(CoreDocument document, String name) {
        return new EntityReference((Document)document, name);
    }
}
