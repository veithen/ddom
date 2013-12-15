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
package com.googlecode.ddom.stream.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.util.collection.ObjectRingBuffer;

public class XMLStreamWriterHandler implements XmlHandler {
    static class Attribute {
        int type;
        String namespaceURI;
        String name;
        String prefix;
        String value;
    }
    
    private static final int ATT_NS_AWARE = 1;
    private static final int ATT_NS_UNAWARE = 2;
    private static final int ATT_NAMESPACE_DECLARATION = 3;
    
    private final XMLStreamWriter writer;
    private boolean fragment;
    private boolean coalescing;
    private String unresolvedElementPrefix;
    private String unresolvedElementLocalName;
    private final StringAccumulator buffer = new StringAccumulator();
    private final ObjectRingBuffer<Attribute> attributes = new ObjectRingBuffer<Attribute>() {
        @Override
        protected Attribute createObject() {
            return new Attribute();
        }

        @Override
        protected void recycleObject(Attribute attribute) {
            attribute.type = 0;
            attribute.namespaceURI = null;
            attribute.name = null;
            attribute.prefix = null;
            attribute.value = null;
        }
    };
    private Attribute currentAttribute;
    private int firstAttributeIndex;
    private String piTarget;

    public XMLStreamWriterHandler(XMLStreamWriter writer) {
        this.writer = writer;
    }
    
    private void startCoalescing() {
        coalescing = true;
    }
    
    private String endCoalescing() {
        coalescing = false;
        String data = buffer.toString();
        buffer.clear();
        return data;
    }
    
    public void startEntity(boolean fragment, String inputEncoding) {
        this.fragment = fragment;
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // TODO
//        throw new UnsupportedOperationException();
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        // TODO
//        throw new UnsupportedOperationException();
    }

    public void endDocumentTypeDeclaration() throws StreamException {
        // TODO
//        throw new UnsupportedOperationException();
    }

    public void startElement(String tagName) throws StreamException {
        try {
            writer.writeStartElement(tagName);
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (namespaceURI == null) {
            unresolvedElementPrefix = prefix;
            unresolvedElementLocalName = localName;
        } else {
            try {
                writer.writeStartElement(prefix, localName, namespaceURI);
            } catch (XMLStreamException ex) {
                throw StAXExceptionUtil.toStreamException(ex);
            }
        }
    }

    public void endElement() throws StreamException {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
    }

    private void flushAttributes() throws StreamException {
        if (unresolvedElementLocalName == null) {
            while (!attributes.isEmpty()) {
                Attribute attribute = attributes.peek();
                try {
                    switch (attribute.type) {
                        case ATT_NS_AWARE:
                            if (attribute.namespaceURI == null) {
                                return;
                            }
                            writer.writeAttribute(attribute.prefix, attribute.namespaceURI, attribute.name, attribute.value);
                            break;
                        case ATT_NS_UNAWARE:
                            writer.writeAttribute(attribute.name, attribute.value);
                            break;
                        case ATT_NAMESPACE_DECLARATION:
                            writer.writeNamespace(attribute.prefix, attribute.value);
                            break;
                    }
                } catch (XMLStreamException ex) {
                    throw StAXExceptionUtil.toStreamException(ex);
                }
                attributes.pop();
                firstAttributeIndex++;
            }
        }
    }
    
    private Attribute newAttribute() {
        startCoalescing();
        return currentAttribute = attributes.allocate();
    }
    
    public void startAttribute(String name, String type) throws StreamException {
        Attribute attribute = newAttribute();
        attribute.type = ATT_NS_UNAWARE;
        attribute.name = name;
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        Attribute attribute = newAttribute();
        attribute.type = ATT_NS_AWARE;
        attribute.namespaceURI = namespaceURI;
        attribute.name = localName;
        attribute.prefix = prefix;
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        Attribute attribute = newAttribute();
        attribute.type = ATT_NAMESPACE_DECLARATION;
        attribute.prefix = prefix;
    }

    public void endAttribute() throws StreamException {
        currentAttribute.value = endCoalescing();
        flushAttributes();
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        try {
            writer.writeStartElement(unresolvedElementPrefix, unresolvedElementLocalName, namespaceURI);
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
        unresolvedElementPrefix = null;
        unresolvedElementLocalName = null;
        flushAttributes();
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        attributes.peek(index-firstAttributeIndex).namespaceURI = namespaceURI;
        flushAttributes();
    }

    public void attributesCompleted() {
        if (!attributes.isEmpty()) {
            throw new IllegalStateException();
        }
        firstAttributeIndex = 0;
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (coalescing) {
            buffer.append(data);
        } else {
            try {
                writer.writeCharacters(data);
            } catch (XMLStreamException ex) {
                throw StAXExceptionUtil.toStreamException(ex);
            }
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        piTarget = target;
        startCoalescing();
    }

    public void endProcessingInstruction() throws StreamException {
        try {
            writer.writeProcessingInstruction(piTarget, endCoalescing());
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
    }

    public void startComment() throws StreamException {
        startCoalescing();
    }

    public void endComment() throws StreamException {
        try {
            writer.writeComment(endCoalescing());
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
    }

    public void startCDATASection() throws StreamException {
        startCoalescing();
    }

    public void endCDATASection() throws StreamException {
        try {
            writer.writeCData(endCoalescing());
        } catch (XMLStreamException ex) {
            throw StAXExceptionUtil.toStreamException(ex);
        }
    }

    public void processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void completed() throws StreamException {
        if (!fragment) {
            try {
                writer.writeEndDocument();
            } catch (XMLStreamException ex) {
                throw StAXExceptionUtil.toStreamException(ex);
            }
        }
    }
}
