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

/**
 * Node factory. The frontend code MUST use this interface to create new nodes. To do so, it MUST
 * obtain an instance using {@link CoreDocument#getNodeFactory()}. On the other hand, the frontend
 * MUST NOT assume that all nodes are created using this factory. The backend internally MAY create
 * nodes without using this factory, in particular during deferred parsing. Thus, frontend aspects
 * SHOULD NOT apply advices to this interface or its implementing classes.
 * <p>
 * Parameters passed to methods defined by this interface are not required to be canonicalized. It
 * is the responsibility of the implementation to canonicalize names, namespace URIs and prefixes as
 * necessary.
 * 
 * @author Andreas Veithen
 */
public interface NodeFactory {
    CoreDocument createDocument();
    
    /**
     * 
     * @param document may be <code>null</code> (if called by {@link org.w3c.dom.DOMImplementation#createDocumentType(String, String, String)})
     * @return
     */
    CoreDocumentType createDocumentType(CoreDocument document, String rootName, String publicId, String systemId);
    
    CoreNSUnawareElement createElement(CoreDocument document, String tagName);
    
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
    CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix);
    
    CoreNSUnawareAttribute createAttribute(CoreDocument document, String name, String value, String type);
    
    CoreNSAwareAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type);
    
    CoreNamespaceDeclaration createNamespaceDeclaration(CoreDocument document, String prefix, String namespaceURI);
    
    CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data);
    
    CoreDocumentFragment createDocumentFragment(CoreDocument document);

    CoreText createText(CoreDocument document, String data);

    CoreComment createComment(CoreDocument document, String data);

    CoreCDATASection createCDATASection(CoreDocument document, String data);

    CoreEntityReference createEntityReference(CoreDocument document, String name);
}
