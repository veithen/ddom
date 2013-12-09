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

import static com.googlecode.ddom.stream.stax.Utils.nullToEmptyString;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

public final class XmlHandlerStreamWriter implements XMLStreamWriter {
    private final XmlHandler handler;
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();
    private boolean isNamespaceAwareElement;
    private boolean attributesPending;

    public XmlHandlerStreamWriter(XmlHandler handler) {
        this.handler = handler;
    }

    private void flushAttributes() throws StreamException {
        if (attributesPending) {
            handler.attributesCompleted();
            attributesPending = false;
        }
    }
    
    public Object getProperty(String name) throws IllegalArgumentException {
        throw new IllegalArgumentException("Property '" + name + "' not supported");
    }

    public NamespaceContext getNamespaceContext() {
        return context;
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

    public void setDefaultNamespace(String uri) throws XMLStreamException {
        context.setPrefix("", uri);
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

    public void writeEndDocument() throws XMLStreamException {
        // TODO: should we call XmlHandler#completed() here or in StAXPushInput?
    }

    public void writeStartElement(String localName) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startElement(localName);
            isNamespaceAwareElement = false;
            attributesPending = true;
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        writeElement(namespaceURI, localName, false);
    }
    
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        writeElement(namespaceURI, localName, true);
    }
    
    private void writeElement(String namespaceURI, String localName, boolean empty) throws XMLStreamException {
        namespaceURI = nullToEmptyString(namespaceURI);
        String prefix = context.getPrefix(namespaceURI);
        if (prefix == null) {
            throw new XMLStreamException("Unbound namespace URI '" + namespaceURI + "'");
        } else {
            writeElement(prefix, localName, namespaceURI, empty);
        }
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        writeElement(prefix, localName, namespaceURI, false);
    }
    
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        writeElement(prefix, localName, namespaceURI, true);
    }
    
    private void writeElement(String prefix, String localName, String namespaceURI, boolean empty) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startElement(nullToEmptyString(namespaceURI), localName, nullToEmptyString(prefix));
            if (empty) {
                handler.attributesCompleted();
                handler.endElement();
            } else {
                isNamespaceAwareElement = true;
                attributesPending = true;
            }
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeEndElement() throws XMLStreamException {
        try {
            flushAttributes();
            handler.endElement();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeAttribute(String localName, String value) throws XMLStreamException {
        try {
            // Since unprefixed attributes have no namespace it is common to use the
            // namespace unaware variant of the writeAttribute method even when the document
            // uses namespaces. We transform these events into namespace aware events
            if (isNamespaceAwareElement) {
                handler.startAttribute("", localName, "", "CDATA");
            } else {
                handler.startAttribute(localName, "CDATA");
            }
            handler.processCharacterData(value, false);
            handler.endAttribute();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
        namespaceURI = nullToEmptyString(namespaceURI);
        String prefix = context.getPrefix(namespaceURI);
        if (prefix == null) {
            throw new XMLStreamException("Unbound namespace URI '" + namespaceURI + "'");
        } else {
            writeAttribute(prefix, namespaceURI, localName, value);
        }
    }

    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        try {
            handler.startAttribute(nullToEmptyString(namespaceURI), localName, nullToEmptyString(prefix), "CDATA");
            handler.processCharacterData(value, false);
            handler.endAttribute();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        try {
            handler.startNamespaceDeclaration(nullToEmptyString(prefix));
            if (namespaceURI != null && namespaceURI.length() > 0) {
                handler.processCharacterData(namespaceURI, false);
            }
            handler.endAttribute();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        writeNamespace("", namespaceURI);
    }

    public void writeCharacters(String text) throws XMLStreamException {
        try {
            flushAttributes();
            handler.processCharacterData(text, false);
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        writeCharacters(new String(text, start, len));
    }

    public void writeCData(String data) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startCDATASection();
            handler.processCharacterData(data, false);
            handler.endCDATASection();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeComment(String data) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startComment();
            handler.processCharacterData(data, false);
            handler.endComment();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeProcessingInstruction(String target) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startProcessingInstruction(target);
            handler.endProcessingInstruction();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
    }

    public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
        try {
            flushAttributes();
            handler.startProcessingInstruction(target);
            handler.processCharacterData(data, false);
            handler.endProcessingInstruction();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
        }
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
     * @see javax.xml.stream.XMLStreamWriter#writeEntityRef(java.lang.String)
     */
    public void writeEntityRef(String arg0) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void flush() throws XMLStreamException {
    }

    public void close() throws XMLStreamException {
    }

}
