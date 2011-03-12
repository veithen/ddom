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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.symbols.Symbols;

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

    private String nullToEmptyString(String value) {
        return value == null ? "" : value;
    }
    
    @Override
    protected void proceed() throws StreamException {
        if (callNext) {
            try {
                reader.next();
            } catch (XMLStreamException ex) {
                throw new StAXException(ex);
            }
        } else {
            callNext = true;
        }
        XmlHandler handler = getHandler();
        try {
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
                        handler.startElement(nullToEmptyString(reader.getNamespaceURI()), reader.getLocalName(), nullToEmptyString(reader.getPrefix()));
                        for (int count = reader.getAttributeCount(), i=0; i<count; i++) {
                            handler.startAttribute(nullToEmptyString(reader.getAttributeNamespace(i)), reader.getAttributeLocalName(i), nullToEmptyString(reader.getAttributePrefix(i)), reader.getAttributeType(i));
                            String value = reader.getAttributeValue(i);
                            if (value.length() > 0) {
                                handler.processCharacterData(value, false);
                            }
                            handler.endAttribute();
                        }
                        for (int count = reader.getNamespaceCount(), i=0; i<count; i++) {
                            handler.startNamespaceDeclaration(nullToEmptyString(reader.getNamespacePrefix(i)));
                            String uri = reader.getNamespaceURI(i);
                            if (uri.length() > 0) {
                                handler.processCharacterData(uri, false);
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
                                handler.processCharacterData(value, false);
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
                    handler.startProcessingInstruction(reader.getPITarget());
                    handler.processCharacterData(reader.getPIData(), false);
                    handler.endProcessingInstruction();
                    break;
                case XMLStreamReader.CHARACTERS:
                    handler.processCharacterData(reader.getText(), false);
                    break;
                case XMLStreamReader.SPACE:
                    handler.processCharacterData(reader.getText(), true);
                    break;
                case XMLStreamReader.CDATA:
                    handler.startCDATASection();
                    handler.processCharacterData(reader.getText(), false);
                    handler.endCDATASection();
                    break;
                case XMLStreamReader.COMMENT:
                    handler.startComment();
                    handler.processCharacterData(reader.getText(), false);
                    handler.endComment();
                    break;
                case XMLStreamReader.ENTITY_REFERENCE:
                    handler.processEntityReference(reader.getLocalName());
                    break;
                default:
                    throw new StreamException("Unexpected StAX event: " + reader.getEventType());
            }
        } catch (RuntimeException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof XMLStreamException) {
                throw new StAXException((XMLStreamException)cause);
            } else {
                throw ex;
            }
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
