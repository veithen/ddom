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
package com.google.code.ddom.dom.impl;

import com.google.code.ddom.spi.model.DOM1Element;
import com.google.code.ddom.spi.model.DOM1TypedAttribute;
import com.google.code.ddom.spi.model.DOM2Element;
import com.google.code.ddom.spi.model.DOM2TypedAttribute;
import com.google.code.ddom.spi.model.DOMCDATASection;
import com.google.code.ddom.spi.model.DOMComment;
import com.google.code.ddom.spi.model.DOMDocument;
import com.google.code.ddom.spi.model.DOMDocumentFragment;
import com.google.code.ddom.spi.model.DOMDocumentType;
import com.google.code.ddom.spi.model.DOMEntityReference;
import com.google.code.ddom.spi.model.DOMProcessingInstruction;
import com.google.code.ddom.spi.model.DOMText;
import com.google.code.ddom.spi.model.NamespaceDeclaration;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.spi.parser.Producer;

public class DOMNodeFactory implements NodeFactory {
    public DOMDocument createDocument(Producer producer) {
        return new DocumentImpl(this, producer);
    }

    public DOMDocumentType createDocumentType(DOMDocument document, String rootName, String publicId, String systemId) {
        return new DocumentTypeImpl(document, rootName, publicId, systemId);
    }

    public DOM1Element createElement(DOMDocument document, String tagName, boolean complete) {
        return new DOM1ElementImpl(document, tagName, complete);
    }
    
    public DOM2Element createElement(DOMDocument document, String namespaceURI, String localName, String prefix, boolean complete) {
        return new DOM2ElementImpl(document, namespaceURI, localName, prefix, complete);
    }
    
    public DOM1TypedAttribute createAttribute(DOMDocument document, String name, String value, String type) {
        return new DOM1TypedAttributeImpl(document, name, value, type);
    }
    
    public DOM2TypedAttribute createAttribute(DOMDocument document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new DOM2TypedAttributeImpl(document, namespaceURI, localName, prefix, value, type);
    }
    
    public NamespaceDeclaration createNSDecl(DOMDocument document, String prefix, String namespaceURI) {
        return new NSDecl(document, prefix, namespaceURI);
    }
    
    public DOMProcessingInstruction createProcessingInstruction(DOMDocument document, String target, String data) {
        return new ProcessingInstructionImpl(document, target, data);
    }
    
    public DOMDocumentFragment createDocumentFragment(DOMDocument document) {
        return new DocumentFragmentImpl(document);
    }

    public DOMText createText(DOMDocument document, String data) {
        return new TextImpl(document, data);
    }

    public DOMComment createComment(DOMDocument document, String data) {
        return new CommentImpl(document, data);
    }

    public DOMCDATASection createCDATASection(DOMDocument document, String data) {
        return new CDATASectionImpl(document, data);
    }

    public DOMEntityReference createEntityReference(DOMDocument document, String name) {
        return new EntityReferenceImpl(document, name);
    }
}
