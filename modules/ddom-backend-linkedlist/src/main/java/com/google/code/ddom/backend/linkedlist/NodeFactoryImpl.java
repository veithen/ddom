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

import com.google.code.ddom.spi.model.CoreCDATASection;
import com.google.code.ddom.spi.model.CoreComment;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreDocumentFragment;
import com.google.code.ddom.spi.model.CoreDocumentType;
import com.google.code.ddom.spi.model.CoreEntityReference;
import com.google.code.ddom.spi.model.CoreNSAwareElement;
import com.google.code.ddom.spi.model.CoreNSAwareTypedAttribute;
import com.google.code.ddom.spi.model.CoreNSUnawareElement;
import com.google.code.ddom.spi.model.CoreNSUnawareTypedAttribute;
import com.google.code.ddom.spi.model.CoreNamespaceDeclaration;
import com.google.code.ddom.spi.model.CoreProcessingInstruction;
import com.google.code.ddom.spi.model.CoreText;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.SimpleFragmentSource;

public class NodeFactoryImpl implements NodeFactory {
    // TODO: clean up the API
    public CoreDocument createDocument(Producer producer) {
        return new DocumentImpl(this, producer == null ? null : new SimpleFragmentSource(producer));
    }

    public CoreDocumentType createDocumentType(CoreDocument document, String rootName, String publicId, String systemId) {
        return new DocumentTypeImpl(document, rootName, publicId, systemId);
    }

    public CoreNSUnawareElement createElement(CoreDocument document, String tagName, boolean complete) {
        return new NSUnawareElementImpl(document, tagName, complete);
    }
    
    public CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix, boolean complete) {
        return new NSAwareElementImpl(document, namespaceURI, localName, prefix, complete);
    }
    
    public CoreNSUnawareTypedAttribute createAttribute(CoreDocument document, String name, String value, String type) {
        return new NSUnawareTypedAttributeImpl(document, name, value, type);
    }
    
    public CoreNSAwareTypedAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new NSAwareTypedAttributeImpl(document, namespaceURI, localName, prefix, value, type);
    }
    
    public CoreNamespaceDeclaration createNSDecl(CoreDocument document, String prefix, String namespaceURI) {
        return new NamespaceDeclarationImpl(document, prefix, namespaceURI);
    }
    
    public CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data) {
        return new ProcessingInstructionImpl(document, target, data);
    }
    
    public CoreDocumentFragment createDocumentFragment(CoreDocument document) {
        return new DocumentFragmentImpl(document);
    }

    public CoreText createText(CoreDocument document, String data) {
        return new TextImpl(document, data);
    }

    public CoreComment createComment(CoreDocument document, String data) {
        return new CommentImpl(document, data);
    }

    public CoreCDATASection createCDATASection(CoreDocument document, String data) {
        return new CDATASectionImpl(document, data);
    }

    public CoreEntityReference createEntityReference(CoreDocument document, String name) {
        return new EntityReferenceImpl(document, name);
    }
}
