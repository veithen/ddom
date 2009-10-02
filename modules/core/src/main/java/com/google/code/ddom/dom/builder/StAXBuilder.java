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
package com.google.code.ddom.dom.builder;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.google.code.ddom.dom.DeferredParsingException;
import com.google.code.ddom.dom.impl.DocumentImpl;
import com.google.code.ddom.dom.impl.DocumentTypeImpl;
import com.google.code.ddom.dom.impl.NSDecl;
import com.google.code.ddom.dom.impl.NodeFactory;
import com.google.code.ddom.dom.model.DOMAttribute;
import com.google.code.ddom.dom.model.DOMElement;
import com.google.code.ddom.dom.model.TypedAttribute;

class StAXBuilder extends AbstractBuilder {
    private final XMLStreamReader reader;
    private final boolean parserIsNamespaceAware;
    
    public StAXBuilder(NodeFactory nodeFactory, DocumentImpl document, Consumer consumer, XMLStreamReader reader) {
        super(nodeFactory, document, consumer);
        this.reader = reader;
        parserIsNamespaceAware = (Boolean)reader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
    }

    private String emptyToNull(String value) {
        return value == null || value.length() == 0 ? null : value;
    }
    
    public void proceed() throws DeferredParsingException {
        try {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    DOMElement element = parserIsNamespaceAware
                            ? nodeFactory.createElement(document, emptyToNull(reader.getNamespaceURI()), reader.getLocalName(), emptyToNull(reader.getPrefix()), false)
                            : nodeFactory.createElement(document, reader.getLocalName(), false);
                    consumer.appendNode(element);
                    DOMAttribute firstAttr = null;
                    DOMAttribute previousAttr = null;
                    for (int i=0; i<reader.getAttributeCount(); i++) {
                        TypedAttribute attr = parserIsNamespaceAware
                                ? nodeFactory.createAttribute(document, emptyToNull(reader.getAttributeNamespace(i)), reader.getAttributeLocalName(i), emptyToNull(reader.getAttributePrefix(i)), reader.getAttributeValue(i), reader.getAttributeType(i))
                                : nodeFactory.createAttribute(document, reader.getAttributeLocalName(i), reader.getAttributeValue(i), reader.getAttributeType(i));
                        if (firstAttr == null) {
                            firstAttr = attr;
                        } else {
                            previousAttr.internalSetNextAttribute(attr);
                        }
                        previousAttr = attr;
                        attr.internalSetOwnerElement(element);
                    }
                    if (parserIsNamespaceAware) {
                        for (int i=0; i<reader.getNamespaceCount(); i++) {
                            NSDecl attr = nodeFactory.createNSDecl(document, emptyToNull(reader.getNamespacePrefix(i)), reader.getNamespaceURI(i));
                            if (firstAttr == null) {
                                firstAttr = attr;
                            } else {
                                previousAttr.internalSetNextAttribute(attr);
                            }
                            previousAttr = attr;
                            attr.internalSetOwnerElement(element);
                        }
                    }
                    if (firstAttr != null) {
                        element.internalSetFirstAttribute(firstAttr);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                case XMLStreamReader.END_DOCUMENT:
                    consumer.nodeCompleted();
                    break;
                case XMLStreamReader.PROCESSING_INSTRUCTION:
                    consumer.appendNode(nodeFactory.createProcessingInstruction(document, reader.getPITarget(), reader.getPIData()));
                    break;
                case XMLStreamReader.DTD:
                    DTDInfo dtdInfo = (DTDInfo)reader;
                    DocumentTypeImpl docType = nodeFactory.createDocumentType(document, dtdInfo.getDTDRootName(), dtdInfo.getDTDPublicId(), dtdInfo.getDTDSystemId());
                    consumer.appendNode(docType);
                    break;
                case XMLStreamReader.COMMENT:
                case XMLStreamReader.SPACE:
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    String text;
                    try {
                        // Some StAX implementations may throw a RuntimeException here if an I/O error occurs
                        text = reader.getText();
                    } catch (RuntimeException ex) {
                        throw new DeferredParsingException("Parse error", ex);
                    }
                    switch (eventType) {
                        case XMLStreamReader.COMMENT:
                            consumer.appendNode(nodeFactory.createComment(document, text));
                            break;
                        case XMLStreamReader.SPACE:
                        case XMLStreamReader.CHARACTERS:
                            // TODO: optimize if possible
                            consumer.appendNode(nodeFactory.createText(document, text));
                            break;
                        case XMLStreamReader.CDATA:
                            consumer.appendNode(nodeFactory.createCDATASection(document, text));
                    }
                    break;
                case XMLStreamReader.ENTITY_REFERENCE:
                    consumer.appendNode(nodeFactory.createEntityReference(document, reader.getText()));
                    break;
                default:
                    throw new RuntimeException("Unexpected event " + reader.getEventType()); // TODO
            }
        } catch (XMLStreamException ex) {
            throw new DeferredParsingException("Parse error", ex);
        }
    }
}
