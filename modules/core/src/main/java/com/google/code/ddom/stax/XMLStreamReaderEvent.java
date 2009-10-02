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
package com.google.code.ddom.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.google.code.ddom.spi.parser.CharacterDataSource;
import com.google.code.ddom.spi.parser.Event;
import com.google.code.ddom.spi.parser.ParseException;

public class XMLStreamReaderEvent implements Event, CharacterDataSource {
    private final XMLStreamReader reader;
    private final DTDInfo dtdInfo;
    private final boolean parserIsNamespaceAware;
    
    public XMLStreamReaderEvent(XMLStreamReader reader) {
        this.reader = reader;
        dtdInfo = (DTDInfo)reader;        
        parserIsNamespaceAware = (Boolean)reader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
    }
    
    private String emptyToNull(String value) {
        return value == null || value.length() == 0 ? null : value;
    }
    
    public int getEventType() {
        // TODO: probably, mapping using an array would be more efficient
        switch (reader.getEventType()) {
            case XMLStreamReader.DTD:
                return Event.DTD;
            case XMLStreamReader.START_ELEMENT:
                return parserIsNamespaceAware ? DOM2_ELEMENT : DOM1_ELEMENT;
            case XMLStreamReader.END_ELEMENT:
            case XMLStreamReader.END_DOCUMENT:
                return NODE_COMPLETE;
            case XMLStreamReader.PROCESSING_INSTRUCTION:
                return PROCESSING_INSTRUCTION;
            case XMLStreamReader.CHARACTERS:
                return CHARACTERS;
            case XMLStreamReader.SPACE:
                return SPACE;
            case XMLStreamReader.CDATA:
                return CDATA;
            case XMLStreamReader.ENTITY_REFERENCE:
                return ENTITY_REFERENCE;
            case XMLStreamReader.COMMENT:
                return COMMENT;
            default:
                throw new RuntimeException("Unexpected event " + reader.getEventType()); // TODO
        }
    }

    public int getAttributeCount() {
        return reader.getAttributeCount() + reader.getNamespaceCount();
    }

    public int getAttributeClass(int index) {
        if (index < reader.getAttributeCount()) {
            return parserIsNamespaceAware ? DOM2_ATTRIBUTE : DOM1_ATTRIBUTE;
        } else {
            return NS_DECL;
        }
    }

    public String getAttributeLocalName(int index) {
        return reader.getAttributeLocalName(index);
    }

    public String getAttributeNamespace(int index) {
        return emptyToNull(reader.getAttributeNamespace(index));
    }

    public String getAttributePrefix(int index) {
        return emptyToNull(reader.getAttributePrefix(index));
    }

    public String getAttributeValue(int index) {
        return reader.getAttributeValue(index);
    }

    public String getAttributeType(int index) {
        return reader.getAttributeType(index);
    }

    public String getLocalName() {
        // TODO: maybe define a separate method for entity reference name?
        return reader.getEventType() == XMLStreamReader.ENTITY_REFERENCE ? reader.getText() : reader.getLocalName();
    }

    public String getNamespacePrefix(int index) {
        return emptyToNull(reader.getNamespacePrefix(index - reader.getAttributeCount()));
    }

    public String getNamespaceURI(int index) {
        return emptyToNull(reader.getNamespaceURI(index - reader.getAttributeCount()));
    }

    public String getNamespaceURI() {
        return emptyToNull(reader.getNamespaceURI());
    }

    public String getPIData() {
        return reader.getPIData();
    }

    public String getPITarget() {
        return reader.getPITarget();
    }

    public String getPrefix() {
        return emptyToNull(reader.getPrefix());
    }

    public CharacterDataSource getCharacterDataSource() {
        return this;
    }

    public String getString() throws ParseException {
        try {
            // Some StAX implementations may throw a RuntimeException here if an I/O error occurs
            return reader.getText();
        } catch (RuntimeException ex) {
            throw new ParseException("Exception while reading character data", ex);
        }
    }

    public String getDTDPublicId() {
        return dtdInfo.getDTDPublicId();
    }

    public String getDTDRootName() {
        return dtdInfo.getDTDRootName();
    }

    public String getDTDSystemId() {
        return dtdInfo.getDTDSystemId();
    }
}
