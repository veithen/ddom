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

public class DOMNodeFactory implements NodeFactory {
    public DOM1ElementImpl createElement(DocumentImpl document, String tagName, boolean complete) {
        return new DOM1ElementImpl(document, tagName, complete);
    }
    
    public DOM2ElementImpl createElement(DocumentImpl document, String namespaceURI, String localName, String prefix, boolean complete) {
        return new DOM2ElementImpl(document, namespaceURI, localName, prefix, complete);
    }
    
    public DOM1AttrImpl createAttribute(DocumentImpl document, String name, String value, String type) {
        return new DOM1AttrImpl(document, name, value, type);
    }
    
    public DOM2AttrImpl createAttribute(DocumentImpl document, String namespaceURI, String localName, String prefix, String value, String type) {
        return new DOM2AttrImpl(document, namespaceURI, localName, prefix, value, type);
    }
    
    public NSDecl createNSDecl(DocumentImpl document, String prefix, String namespaceURI) {
        return new NSDecl(document, prefix, namespaceURI);
    }
    
    public ProcessingInstructionImpl createProcessingInstruction(DocumentImpl document, String target, String data) {
        return new ProcessingInstructionImpl(document, target, data);
    }
    
    public DocumentFragmentImpl createDocumentFragment(DocumentImpl document) {
        return new DocumentFragmentImpl(document);
    }

    public TextImpl createText(DocumentImpl document, String data) {
        return new TextImpl(document, data);
    }

    public CommentImpl createComment(DocumentImpl document, String data) {
        return new CommentImpl(document, data);
    }

    public CDATASectionImpl createCDATASection(DocumentImpl document, String data) {
        return new CDATASectionImpl(document, data);
    }

    public EntityReferenceImpl createEntityReference(DocumentImpl document, String name) {
        return new EntityReferenceImpl(document, name);
    }
}
