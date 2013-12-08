/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.core;

/**
 * Node factory. The front-end code MUST use this interface to create new nodes. To do so, it MUST
 * obtain an instance using {@link CoreDocument#coreGetNodeFactory()}. On the other hand, the
 * front-end MUST NOT assume that all nodes are created using this factory. The back-end internally
 * MAY create nodes without using this factory, in particular during deferred parsing.
 * <p>
 * The lifecycle of a {@link NodeFactory} implementation is managed as follows. The implementation
 * class MUST declare a public static final field named <code>INSTANCE</code> initialized with an
 * instance of the implementation. The class name of that implementation MUST be returned by
 * {@link com.google.code.ddom.backend.Backend#getNodeFactoryClassName()}. This information is used
 * by the model loaders to locate the singleton instance.
 * <p>
 * In addition, the {@link CoreNode#coreGetNodeFactory()} method MUST be implemented such that it
 * returns the same singleton instance.
 * 
 * @author Andreas Veithen
 */
public interface NodeFactory {
    CoreDocument createDocument();
    
    <T extends CoreDocument> T createDocument(Class<T> extensionInterface);

    /**
     * 
     * @param document
     * @return
     */
    CoreDocumentTypeDeclaration createDocumentTypeDeclaration(CoreDocument document, String rootName, String publicId, String systemId);
    
    /**
     * Create a namespace unaware element. The name of the element may be left unspecified, in which
     * case {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)} should be used
     * to set a source for the element.
     * 
     * @param document
     * @param tagName
     *            the name of the element, or <code>null</code> if the name is unspecified
     * @return
     */
    CoreNSUnawareElement createElement(CoreDocument document, String tagName);
    
    /**
     * Create a namespace aware element. The name (or some of its components) of the element may be
     * left unspecified, in which case
     * {@link CoreElement#coreSetSource(com.googlecode.ddom.stream.XmlSource)} should be used to set
     * a source for the element.
     * 
     * @param document
     * @param namespaceURI
     *            the namespace URI of the element, the empty string if the element has no
     *            namespace, or <code>null</code> if the namespace URI is unspecified
     * @param localName
     *            the local part of the element's name, or <code>null</code> if the local name is
     *            unspecified
     * @param prefix
     *            the prefix of the element, the empty string if the element has no prefix, or
     *            <code>null</code> if the prefix is unspecified
     * @return the element
     */
    CoreNSAwareElement createElement(CoreDocument document, String namespaceURI, String localName, String prefix);
    
    <T extends CoreNSAwareElement> T createElement(CoreDocument document, Class<T> extensionInterface, String namespaceURI, String localName, String prefix);
    
    CoreNSUnawareAttribute createAttribute(CoreDocument document, String name, String value, String type);
    
    CoreNSAwareAttribute createAttribute(CoreDocument document, String namespaceURI, String localName, String prefix, String value, String type);
    
    CoreNamespaceDeclaration createNamespaceDeclaration(CoreDocument document, String prefix, String namespaceURI);
    
    CoreProcessingInstruction createProcessingInstruction(CoreDocument document, String target, String data);
    
    CoreDocumentFragment createDocumentFragment(CoreDocument document);

    CoreCharacterData createCharacterData(CoreDocument document, String data);

    CoreComment createComment(CoreDocument document, String data);

    CoreCDATASection createCDATASection(CoreDocument document, String data);

    CoreEntityReference createEntityReference(CoreDocument document, String name);
}
