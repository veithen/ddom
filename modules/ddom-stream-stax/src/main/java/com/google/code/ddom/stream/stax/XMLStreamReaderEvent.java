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
package com.google.code.ddom.stream.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.google.code.ddom.stream.spi.AttributeData;
import com.google.code.ddom.stream.spi.CharacterData;
import com.google.code.ddom.stream.spi.Event;
import com.google.code.ddom.stream.spi.StreamException;

public class XMLStreamReaderEvent implements Event, AttributeData, CharacterData {
    public enum Mode { NODE, ATTRIBUTE, NS_DECL, ATTRIBUTES_COMPLETE }
    
    private final XMLStreamReader reader;
    private final DTDInfo dtdInfo;
    private final boolean parserIsNamespaceAware;
    private Mode mode = Mode.NODE;
    private int index;
    
    public XMLStreamReaderEvent(XMLStreamReader reader) {
        this.reader = reader;
        dtdInfo = (DTDInfo)reader;        
        parserIsNamespaceAware = (Boolean)reader.getProperty(XMLInputFactory.IS_NAMESPACE_AWARE);
    }
    
    public void updateState(Mode mode, int index) {
        this.mode = mode;
        this.index = index;
    }
    
    public Mode getMode() {
        return mode;
    }

    public int getIndex() {
        return index;
    }

    private String emptyToNull(String value) {
        return value == null || value.length() == 0 ? null : value;
    }
    
    public Scope getScope() {
        return Scope.PARSER_INVOCATION;
    }

    public Event.Type getEventType() {
        switch (mode) {
            case NODE:
                // TODO: probably, mapping using an array would be more efficient
                switch (reader.getEventType()) {
                    case XMLStreamReader.DTD:
                        return Event.Type.DTD;
                    case XMLStreamReader.START_ELEMENT:
                        return parserIsNamespaceAware ? Event.Type.NS_AWARE_ELEMENT : Event.Type.NS_UNAWARE_ELEMENT;
                    case XMLStreamReader.END_ELEMENT:
                    case XMLStreamReader.END_DOCUMENT:
                        return Event.Type.NODE_COMPLETE;
                    case XMLStreamReader.PROCESSING_INSTRUCTION:
                        return Event.Type.PROCESSING_INSTRUCTION;
                    case XMLStreamReader.CHARACTERS:
                        return Event.Type.CHARACTERS;
                    case XMLStreamReader.SPACE:
                        return Event.Type.SPACE;
                    case XMLStreamReader.CDATA:
                        return Event.Type.CDATA;
                    case XMLStreamReader.ENTITY_REFERENCE:
                        return Event.Type.ENTITY_REFERENCE;
                    case XMLStreamReader.COMMENT:
                        return Event.Type.COMMENT;
                    default:
                        throw new RuntimeException("Unexpected event " + reader.getEventType()); // TODO
                }
            case ATTRIBUTE:
                return parserIsNamespaceAware ? Event.Type.NS_AWARE_ATTRIBUTE : Event.Type.NS_UNAWARE_ATTRIBUTE;
            case NS_DECL:
                return Event.Type.NAMESPACE_DECLARATION;
            case ATTRIBUTES_COMPLETE:
                return Event.Type.ATTRIBUTES_COMPLETE;
            default:
                return null;
        }
    }

    public AttributeData getAttributes() {
        return this;
    }

    public int getLength() {
        return reader.getAttributeCount() + reader.getNamespaceCount();
    }

    public AttributeData.Type getType(int index) {
        if (index < reader.getAttributeCount()) {
            return parserIsNamespaceAware ? AttributeData.Type.DOM2 : AttributeData.Type.DOM1;
        } else {
            return AttributeData.Type.NS_DECL;
        }
    }

    public String getName(int index) {
        return reader.getAttributeLocalName(index);
    }

    public String getNamespaceURI(int index) {
        int c = reader.getAttributeCount();
        return emptyToNull(index < c ? reader.getAttributeNamespace(index)
                                     : reader.getNamespaceURI(index-c));
    }

    public String getPrefix(int index) {
        int c = reader.getAttributeCount();
        return emptyToNull(index < c ? reader.getAttributePrefix(index)
                                     : reader.getNamespacePrefix(index-c));
    }

    public String getValue(int index) {
        return reader.getAttributeValue(index);
    }

    public String getDataType(int index) {
        return reader.getAttributeType(index);
    }

    public String getName() {
        switch (mode) {
            case NODE:
                switch (reader.getEventType()) {
                    case XMLStreamReader.PROCESSING_INSTRUCTION:
                        return reader.getPITarget();
                    default:
                        return reader.getLocalName();
                }
            case ATTRIBUTE:
                return reader.getAttributeLocalName(index);
            default:
                return null;
        }
    }

    public String getNamespaceURI() {
        switch (mode) {
            case NODE:
                return emptyToNull(reader.getNamespaceURI());
            case ATTRIBUTE:
                return emptyToNull(reader.getAttributeNamespace(index));
            case NS_DECL:
                return emptyToNull(reader.getNamespaceURI(index));
            default:
                return null;
        }
    }

    public String getPrefix() {
        switch (mode) {
            case NODE:
                return emptyToNull(reader.getPrefix());
            case ATTRIBUTE:
                return emptyToNull(reader.getAttributePrefix(index));
            case NS_DECL:
                return emptyToNull(reader.getNamespacePrefix(index));
            default:
                return null;
        }
    }

    public CharacterData getData() {
        return this;
    }

    public String getDataType() {
        return reader.getAttributeType(index);
    }

    public String getValue() {
        return reader.getAttributeValue(index);
    }

    public String getString() throws StreamException {
        try {
            // Some StAX implementations may throw a RuntimeException here if an I/O error occurs
            switch (reader.getEventType()) {
                case XMLStreamReader.PROCESSING_INSTRUCTION:
                    return reader.getPIData();
                default:
                    return reader.getText();
            }
        } catch (RuntimeException ex) {
            throw new StreamException("Exception while reading character data", ex);
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
