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
package com.googlecode.ddom.stream.stax;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

public final class XmlHandlerStreamWriter implements XMLStreamWriter {
    private final XmlHandler handler;
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();

    public XmlHandlerStreamWriter(XmlHandler handler) {
        this.handler = handler;
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#getProperty(java.lang.String)
     */
    public Object getProperty(String arg0) throws IllegalArgumentException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#getNamespaceContext()
     */
    public NamespaceContext getNamespaceContext() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#setNamespaceContext(javax.xml.namespace.NamespaceContext)
     */
    public void setNamespaceContext(NamespaceContext arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getPrefix(String uri) throws XMLStreamException {
        return context.getPrefix(uri);
    }

    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        context.setPrefix(prefix, uri);
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#setDefaultNamespace(java.lang.String)
     */
    public void setDefaultNamespace(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeStartDocument()
     */
    public void writeStartDocument() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String)
     */
    public void writeStartDocument(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeStartDocument(java.lang.String, java.lang.String)
     */
    public void writeStartDocument(String arg0, String arg1) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeEndDocument()
     */
    public void writeEndDocument() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeStartElement(java.lang.String)
     */
    public void writeStartElement(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        String prefix = context.getPrefix(namespaceURI);
        if (prefix == null) {
            throw new XMLStreamException("Unbound namespace URI '" + namespaceURI + "'");
        } else {
            writeStartElement(prefix, localName, namespaceURI);
        }
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        try {
            handler.startElement(namespaceURI, localName, prefix);
        } catch (StreamException ex) {
            throw new XMLStreamException(ex);
        }
    }

    public void writeEndElement() throws XMLStreamException {
        try {
            handler.endElement();
        } catch (StreamException ex) {
            throw new XMLStreamException(ex);
        }
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String)
     */
    public void writeAttribute(String arg0, String arg1) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String)
     */
    public void writeAttribute(String arg0, String arg1, String arg2) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeAttribute(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void writeAttribute(String arg0, String arg1, String arg2, String arg3)
            throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        if (prefix == null) {
            prefix = "";
        }
        try {
            handler.startNamespaceDeclaration(prefix);
            handler.processCharacterData(namespaceURI, false);
            handler.endAttribute();
        } catch (StreamException ex) {
            throw new XMLStreamException(ex);
        }
    }

    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        writeNamespace("", namespaceURI);
    }

    public void writeCharacters(String text) throws XMLStreamException {
        try {
            handler.processCharacterData(text, false);
        } catch (StreamException ex) {
            throw new XMLStreamException(ex);
        }
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeCharacters(char[], int, int)
     */
    public void writeCharacters(char[] arg0, int arg1, int arg2) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeCData(java.lang.String)
     */
    public void writeCData(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeComment(java.lang.String)
     */
    public void writeComment(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeDTD(java.lang.String)
     */
    public void writeDTD(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String)
     */
    public void writeEmptyElement(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String)
     */
    public void writeEmptyElement(String arg0, String arg1) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeEmptyElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public void writeEmptyElement(String arg0, String arg1, String arg2) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeEntityRef(java.lang.String)
     */
    public void writeEntityRef(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String)
     */
    public void writeProcessingInstruction(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamWriter#writeProcessingInstruction(java.lang.String, java.lang.String)
     */
    public void writeProcessingInstruction(String arg0, String arg1) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void flush() throws XMLStreamException {
    }

    public void close() throws XMLStreamException {
    }

}
