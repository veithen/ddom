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
package com.googlecode.ddom.stream;

public interface XmlHandler {
    /**
     * Notify the handler of the beginning of a document or fragment. This must be the first method
     * called on a given handler instance.
     * 
     * @param fragment
     *            <code>true</code> if the entity is a fragment, <code>false</code> if the entity is
     *            a document. The next call to the handler must be an invocation of the
     *            {@link #processXmlDeclaration(String, String, Boolean)} method if and only if this
     *            parameter is set to <code>false</code>
     * @param inputEncoding
     *            the charset encoding used by the entity, or <code>null</code> if the encoding is
     *            is not know, such as when the entity was constructor in memory or from a character
     *            stream
     * @throws StreamException 
     *             if an error occurs when processing the event
     */
    void startEntity(boolean fragment, String inputEncoding) throws StreamException;
    
    void processXmlDeclaration(String version, String encoding, Boolean standalone) throws StreamException;

    /**
     * Notify the handler of the beginning of a document type declaration. If the declaration has no
     * internal subset, then the next event will be an invocation of
     * {@link #endDocumentTypeDeclaration()}.
     * 
     * @param rootName
     * @param publicId
     * @param systemId
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException;

    /**
     * Notify the handler of the end of a document type declaration.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endDocumentTypeDeclaration() throws StreamException;
    
    /**
     * Notify the handler of the beginning of an element in non namespace aware mode.
     * 
     * @param tagName
     *            the name of the element
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startElement(String tagName) throws StreamException;
    
    /**
     * Notify the handler of the beginning of an element in namespace aware mode. Since this method
     * is invoked before all attributes have been processed, the namespace may be unresolved. In
     * that case, <code>null</code> is passed to the <code>namespaceURI</code> parameter and
     * {@link #resolveElementNamespace(String)} is invoked before the next call to
     * {@link #attributesCompleted()}.
     * 
     * @param namespaceURI
     *            the namespace URI of the element, the empty string if the element has no
     *            namespace, or <code>null</code> if the namespace has not been resolved yet
     * @param localName
     *            the local part of the element's name
     * @param prefix
     *            the prefix of the element, or the empty string if the element has no prefix
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startElement(String namespaceURI, String localName, String prefix) throws StreamException;
    
    /**
     * Notify the handler of the end of an element.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endElement() throws StreamException;
    
    /**
     * Notify the handler of the beginning of an attribute in non namespace aware mode.
     * 
     * @param name
     * @param type
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startAttribute(String name, String type) throws StreamException;
    
    /**
     * Notify the handler of the beginning of an attribute in namespace aware mode. Since this
     * method is invoked before all attributes have been processed, the namespace may be unresolved.
     * In that case, <code>null</code> is passed to the <code>namespaceURI</code> parameter and
     * {@link #resolveAttributeNamespace(String, String)} is invoked before the next call to
     * {@link #attributesCompleted()}.
     * <p>
     * Since unprefixed attributes have no namespace (and therefore namespace resolution is
     * trivial), this method MUST NOT be called with an empty prefix and a <code>null</code>
     * <code>namespaceURI</code>.
     * 
     * @param namespaceURI
     *            the namespace URI of the attribute, the empty string if the attribute has no
     *            namespace, or <code>null</code> if the namespace has not been resolved yet
     * @param localName
     *            the local part of the attribute's name
     * @param prefix
     *            the prefix of the attribute, or the empty string if the element has no prefix
     * @param type
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException;
    
    /**
     * Notify the handler of the beginning of a namespace declaration.
     * 
     * @param prefix
     *            the prefix being declared, or the empty string if the declaration sets the default
     *            namespace
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startNamespaceDeclaration(String prefix) throws StreamException;
    
    /**
     * Notify the handler of the end of an attribute.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endAttribute() throws StreamException;
    
    /**
     * Notify the handler that the namespace of the current element has been resolved. This method
     * MUST be called once and only once between invocations of
     * {@link #startElement(String, String, String)} and {@link #attributesCompleted()} if the
     * namespace URI passed to the {@link #startElement(String, String, String)} method was
     * <code>null</code>.
     * 
     * @param namespaceURI
     *            the namespace URI of the current element; never <code>null</code>
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void resolveElementNamespace(String namespaceURI) throws StreamException;
    
    /**
     * Notify the handler that the namespace of an attribute has been resolved. This method MUST be
     * called once and only once for each attribute with unresolved prefix, i.e. for each invocation
     * of {@link #startAttribute(String, String, String, String)} with a <code>null</code> value for
     * the <code>namespaceURI</code> parameter. The call MUST occur before the call to
     * {@link #attributesCompleted()}.
     * 
     * @param index
     *            the index of the attribute
     * @param namespaceURI
     *            the namespace URI bound to the prefix
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException;
    
    void attributesCompleted() throws StreamException;
    
    /**
     * TODO
     * 
     * @param data
     * @param ignorable
     *            Specifies if the character data represents element content whitespace (also called
     *            "ignorable whitespace"). This should always be <code>false</code> if the character
     *            data appears in a comment or processing instruction.
     * @throws StreamException
     */
    // TODO: what about ignorable in CDATA sections???
    void processCharacterData(String data, boolean ignorable) throws StreamException;
    
    /**
     * Notify the handler of the beginning of a processing instruction.
     * 
     * @param target
     *            the target of the processing instruction
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startProcessingInstruction(String target) throws StreamException;
    
    /**
     * Notify the handler of the end of a processing instruction.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endProcessingInstruction() throws StreamException;
    
    /**
     * Notify the handler of the beginning of a comment.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startComment() throws StreamException;
    
    /**
     * Notify the handler of the end of a comment.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endComment() throws StreamException;
    
    /**
     * Notify the handler of the beginning of a CDATA section.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void startCDATASection() throws StreamException;
    
    /**
     * Notify the handler of the end of a CDATA section.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void endCDATASection() throws StreamException;
    
    void processEntityReference(String name) throws StreamException;
    
    /**
     * Notify the handler that the document or fragment is complete.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    void completed() throws StreamException;
}
