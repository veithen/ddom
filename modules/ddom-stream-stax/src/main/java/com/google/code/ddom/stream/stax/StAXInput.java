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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.Symbols;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlInput;

public class StAXInput extends XmlInput {
    private final XMLStreamReader reader;
    private final DTDInfo dtdInfo;
    private final boolean parserIsNamespaceAware;
    private final Symbols symbols;
    private boolean callNext;

    public StAXInput(XMLStreamReader reader, Symbols symbols) {
        this.reader = reader;
        dtdInfo = (DTDInfo)reader;
        parserIsNamespaceAware = (Boolean)reader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
        this.symbols = symbols;
    }

    public Symbols getSymbols() {
        return symbols;
    }

    private String emptyToNull(String value) {
        return value == null || value.length() == 0 ? null : value;
    }
    
    @Override
    protected void proceed() throws StreamException {
        if (callNext) {
            try {
                reader.next();
            } catch (XMLStreamException ex) {
                throw new StreamException(ex);
            }
        } else {
            callNext = true;
        }
        XmlHandler handler = getHandler();
        switch (reader.getEventType()) {
            case XMLStreamReader.START_DOCUMENT:
                handler.setDocumentInfo(reader.getVersion(), reader.getCharacterEncodingScheme(), reader.getEncoding(), reader.isStandalone());
                break;
            case XMLStreamReader.END_DOCUMENT:
                handler.completed();
                break;
            case XMLStreamReader.DTD:
                handler.processDocumentType(dtdInfo.getDTDRootName(), dtdInfo.getDTDPublicId(), dtdInfo.getDTDSystemId(), reader.getText());
                break;
            case XMLStreamReader.START_ELEMENT:
                if (parserIsNamespaceAware) {
                    handler.startElement(emptyToNull(reader.getNamespaceURI()), reader.getLocalName(), emptyToNull(reader.getPrefix()));
                    for (int count = reader.getAttributeCount(), i=0; i<count; i++) {
                        handler.startAttribute(emptyToNull(reader.getAttributeNamespace(i)), reader.getAttributeLocalName(i), emptyToNull(reader.getAttributePrefix(i)), reader.getAttributeType(i));
                        String value = reader.getAttributeValue(i);
                        if (value.length() > 0) {
                            handler.processText(value, false);
                        }
                        handler.endAttribute();
                    }
                    for (int count = reader.getNamespaceCount(), i=0; i<count; i++) {
                        handler.startNamespaceDeclaration(emptyToNull(reader.getNamespacePrefix(i)));
                        String uri = reader.getNamespaceURI(i);
                        if (uri.length() > 0) {
                            handler.processText(uri, false);
                        }
                        handler.endAttribute();
                    }
                    handler.attributesCompleted();
                } else {
                    handler.startElement(reader.getLocalName());
                    for (int count = reader.getAttributeCount(), i=0; i<count; i++) {
                        handler.startAttribute(reader.getAttributeLocalName(i), reader.getAttributeType(i));
                        String value = reader.getAttributeValue(i);
                        if (value.length() > 0) {
                            handler.processText(value, false);
                        }
                        handler.endAttribute();
                    }
                    handler.attributesCompleted();
                }
                break;
            case XMLStreamReader.END_ELEMENT:
                handler.endElement();
                break;
            case XMLStreamReader.PROCESSING_INSTRUCTION:
                handler.processProcessingInstruction(reader.getPITarget(), reader.getPIData());
                break;
            case XMLStreamReader.CHARACTERS:
                handler.processText(reader.getText(), false);
                break;
            case XMLStreamReader.SPACE:
                handler.processText(reader.getText(), true);
                break;
            case XMLStreamReader.CDATA:
                handler.startCDATASection();
                handler.processText(reader.getText(), false);
                handler.endCDATASection();
                break;
            case XMLStreamReader.COMMENT:
                handler.processComment(reader.getText());
                break;
            case XMLStreamReader.ENTITY_REFERENCE:
                handler.processEntityReference(reader.getLocalName());
                break;
            default:
                throw new StreamException("Unexpected StAX event: " + reader.getEventType());
        }
    }

    public void dispose() {
        // TODO: this doesn't close the stream
        try {
            reader.close();
        } catch (XMLStreamException ex) {
            // Ignore this; we can't do more.
        }
    }
}
