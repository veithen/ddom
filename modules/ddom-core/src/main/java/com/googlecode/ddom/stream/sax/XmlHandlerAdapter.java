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
package com.googlecode.ddom.stream.sax;

import org.xml.sax.ContentHandler;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public class XmlHandlerAdapter implements XmlHandler {
    private final ContentHandler handler;

    public XmlHandlerAdapter(ContentHandler handler) {
        this.handler = handler;
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#attributesCompleted()
     */
    public void attributesCompleted() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#completed()
     */
    public void completed() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#endAttribute()
     */
    public void endAttribute() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#endCDATASection()
     */
    public void endCDATASection() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#endElement()
     */
    public void endElement() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#processDocumentType(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#processEntityReference(java.lang.String)
     */
    public void processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#processText(java.lang.String)
     */
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void startEntity(boolean fragment, String inputEncoding) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startAttribute(java.lang.String, java.lang.String)
     */
    public void startAttribute(String name, String type) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void startAttribute(String namespaceURI, String localName, String prefix, String type)
            throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startCDATASection()
     */
    public void startCDATASection() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startElement(java.lang.String)
     */
    public void startElement(String tagName) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void startElement(String namespaceURI, String localName, String prefix)
            throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.google.code.ddom.stream.spi.XmlHandler#startNamespaceDeclaration(java.lang.String)
     */
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.XmlHandler#endComment()
     */
    public void endComment() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.XmlHandler#endProcessingInstruction()
     */
    public void endProcessingInstruction() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.XmlHandler#startComment()
     */
    public void startComment() throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see com.googlecode.ddom.stream.XmlHandler#startProcessingInstruction(java.lang.String)
     */
    public void startProcessingInstruction(String target) throws StreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

}
