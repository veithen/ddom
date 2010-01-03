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
package com.google.code.ddom.backend;

import com.google.code.ddom.DocumentFactory;
import com.google.code.ddom.stream.spi.Producer;

public interface NodeFactory extends DocumentFactory {
    CoreDocument createDocument(Producer producer);
    
    /**
     * 
     * @param document may be <code>null</code> (if called by {@link org.w3c.dom.DOMImplementation#createDocumentType(String, String, String)})
     * @return
     */
    CoreDocumentType createDocumentType(CoreDocument document, String rootName, String publicId, String systemId);
    
    CoreNSUnawareElement createElement(CoreDocument document, String tagName, boolean complete);
    
    /**
     * Create a namespace aware element.
     * 
     * @param document the owner document of the new element
     * @param namespaceURI the namespace URI of the element, or <code>null</code> if the element has no
     *                     namespace
     * @param localName the local part of the element's name
     * @param prefix the prefix of the element, or <code>null</code> if the element has no prefix
     * @param complete
     * @return the element
     */
    CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix, boolean complete);
    
    CoreNSUnawareAttribute createAttribute(CoreDocument document, String name, String value, String type);
    
    CoreNSAwareAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type);
    
    CoreNamespaceDeclaration createNSDecl(CoreDocument document, String prefix, String namespaceURI);
    
    CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data);
    
    CoreDocumentFragment createDocumentFragment(CoreDocument document);

    CoreText createText(CoreDocument document, String data);

    CoreComment createComment(CoreDocument document, String data);

    CoreCDATASection createCDATASection(CoreDocument document, String data);

    CoreEntityReference createEntityReference(CoreDocument document, String name);
}
