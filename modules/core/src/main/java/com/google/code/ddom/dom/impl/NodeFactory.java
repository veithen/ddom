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

import com.google.code.ddom.dom.model.DOM1Element;
import com.google.code.ddom.dom.model.DOM1TypedAttribute;
import com.google.code.ddom.dom.model.DOM2Element;
import com.google.code.ddom.dom.model.DOM2TypedAttribute;

public interface NodeFactory {
    /**
     * 
     * @param document may be <code>null</code> (if called by {@link org.w3c.dom.DOMImplementation#createDocumentType(String, String, String)})
     * @return
     */
    DocumentTypeImpl createDocumentType(DocumentImpl document, String rootName, String publicId, String systemId);
    
    DOM1Element createElement(DocumentImpl document, String tagName, boolean complete);
    
    DOM2Element createElement(DocumentImpl document, String namespaceURI, String localName, String prefix, boolean complete);
    
    DOM1TypedAttribute createAttribute(DocumentImpl document, String name, String value, String type);
    
    DOM2TypedAttribute createAttribute(DocumentImpl document, String namespaceURI, String localName, String prefix, String value, String type);
    
    NSDecl createNSDecl(DocumentImpl document, String prefix, String namespaceURI);
    
    ProcessingInstructionImpl createProcessingInstruction(DocumentImpl document, String target, String data);
    
    DocumentFragmentImpl createDocumentFragment(DocumentImpl document);

    TextImpl createText(DocumentImpl document, String data);

    CommentImpl createComment(DocumentImpl document, String data);

    CDATASectionImpl createCDATASection(DocumentImpl document, String data);

    EntityReferenceImpl createEntityReference(DocumentImpl document, String name);
}
