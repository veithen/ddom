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
import com.google.code.ddom.dom.model.DOMCDATASection;
import com.google.code.ddom.dom.model.DOMComment;
import com.google.code.ddom.dom.model.DOMDocument;
import com.google.code.ddom.dom.model.DOMDocumentFragment;
import com.google.code.ddom.dom.model.DOMDocumentType;
import com.google.code.ddom.dom.model.DOMEntityReference;
import com.google.code.ddom.dom.model.DOMProcessingInstruction;
import com.google.code.ddom.dom.model.DOMText;
import com.google.code.ddom.dom.model.NamespaceDeclaration;

public interface NodeFactory {
    /**
     * 
     * @param document may be <code>null</code> (if called by {@link org.w3c.dom.DOMImplementation#createDocumentType(String, String, String)})
     * @return
     */
    DOMDocumentType createDocumentType(DOMDocument document, String rootName, String publicId, String systemId);
    
    DOM1Element createElement(DOMDocument document, String tagName, boolean complete);
    
    DOM2Element createElement(DOMDocument document, String namespaceURI, String localName, String prefix, boolean complete);
    
    DOM1TypedAttribute createAttribute(DOMDocument document, String name, String value, String type);
    
    DOM2TypedAttribute createAttribute(DOMDocument document, String namespaceURI, String localName, String prefix, String value, String type);
    
    NamespaceDeclaration createNSDecl(DOMDocument document, String prefix, String namespaceURI);
    
    DOMProcessingInstruction createProcessingInstruction(DOMDocument document, String target, String data);
    
    DOMDocumentFragment createDocumentFragment(DOMDocument document);

    DOMText createText(DOMDocument document, String data);

    DOMComment createComment(DOMDocument document, String data);

    DOMCDATASection createCDATASection(DOMDocument document, String data);

    DOMEntityReference createEntityReference(DOMDocument document, String name);
}
