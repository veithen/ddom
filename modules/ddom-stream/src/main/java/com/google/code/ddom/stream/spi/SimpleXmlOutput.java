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
package com.google.code.ddom.stream.spi;

/**
 * Base class for {@link XmlOutput} implementations that have no need for a separate
 * {@link XmlHandler} implementation.
 */
public abstract class SimpleXmlOutput extends XmlOutput {
    @Override
    protected final XmlHandler createXmlHandler() {
        return new SimpleXmlOutputHandler(this);
    }

    protected abstract void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone);
    
    protected abstract void processDocumentType(String rootName, String publicId, String systemId, String data);
    
    /**
     * Receive notification of the beginning of an element in non namespace aware mode.
     * 
     * @param tagName
     *            the name of the element
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startElement(String tagName) throws StreamException;
    
    /**
     * Receive notification of the beginning of an element in namespace aware mode.
     * 
     * @param namespaceURI
     *            the namespace URI of the element, or <code>null</code> if the element has no
     *            namespace
     * @param localName
     *            the local part of the element's name
     * @param prefix
     *            the prefix of the element, or <code>null</code> if the element has no prefix
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startElement(String namespaceURI, String localName, String prefix) throws StreamException;
    
    /**
     * Receive notification of the end of an element.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void endElement() throws StreamException;
    
    /**
     * Receive notification of the beginning of an attribute in non namespace aware mode.
     * 
     * @param name
     * @param type
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startAttribute(String name, String type) throws StreamException;
    
    /**
     * Receive notification of the beginning of an attribute in namespace aware mode.
     * 
     * @param namespaceURI
     * @param localName
     * @param prefix
     * @param type
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException;
    
    /**
     * Receive notification of the beginning of a namespace declaration.
     * 
     * @param prefix
     *            the prefix being declared, or <code>null</code> if the declaration sets the
     *            default namespace
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startNamespaceDeclaration(String prefix) throws StreamException;
    
    /**
     * Receive notification of the end of an attribute.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void endAttribute() throws StreamException;
    
    protected abstract void attributesCompleted();
    
    protected abstract void processProcessingInstruction(String target, String data) throws StreamException;
    protected abstract void processText(String data, boolean ignorable) throws StreamException;
    protected abstract void processComment(String data) throws StreamException;
    
    /**
     * Receive notification of the beginning of a CDATA section.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void startCDATASection() throws StreamException;
    
    /**
     * Receive notification of the end of a CDATA section.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void endCDATASection() throws StreamException;
    
    protected abstract void processEntityReference(String name);
    
    /**
     * Receive notification that the document or fragment is complete.
     * 
     * @throws StreamException
     *             if an error occurs when processing the event
     */
    protected abstract void completed() throws StreamException;
}
