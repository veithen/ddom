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
import com.googlecode.ddom.stream.XmlReader;

final class StAXPullReader implements XmlReader {
    private final XmlHandler handler;
    private final XMLStreamReader reader;
    private final boolean parserIsNamespaceAware;
    private boolean callNext;

    StAXPullReader(XmlHandler handler, XMLStreamReader reader) {
        this.handler = handler;
        this.reader = reader;
        boolean parserIsNamespaceAware;
        try {
            Boolean prop = (Boolean)reader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
            parserIsNamespaceAware = prop == null ? true : prop;
        } catch (IllegalArgumentException ex) {
            parserIsNamespaceAware = true;
        }
        this.parserIsNamespaceAware = parserIsNamespaceAware;
    }

    private String nullToEmptyString(String value) {
        return value == null ? "" : value;
    }
    
    public void proceed(boolean flush) throws StreamException {
        if (callNext) {
            try {
                reader.next();
            } catch (XMLStreamException ex) {
                throw StAXExceptionUtil.toStreamException(ex);
            }
        } else {
            callNext = true;
        }
        try {
            switch (reader.getEventType()) {
                case XMLStreamReader.START_DOCUMENT:
                    // TODO: quick and dirty hack for a failure in the Axiom test suite
                    String encoding = reader.getEncoding();
                    if (encoding == null) {
                        encoding = "UTF-8";
                    }
                    handler.startEntity(false, encoding);
                    handler.processXmlDeclaration(reader.getVersion(), reader.getCharacterEncodingScheme(), reader.standaloneSet() ? reader.isStandalone() : null);
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    handler.completed();
                    break;
                case XMLStreamReader.DTD:
                    if (reader instanceof DTDInfo) {
                        DTDInfo dtdInfo = (DTDInfo)reader;
                        handler.startDocumentTypeDeclaration(dtdInfo.getDTDRootName(), dtdInfo.getDTDPublicId(), dtdInfo.getDTDSystemId());
                        // TODO: do something with reader.getText(), i.e. the internal subset
                        handler.endDocumentTypeDeclaration();
                    } else {
                        throw new UnsupportedOperationException();
                    }
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
}
