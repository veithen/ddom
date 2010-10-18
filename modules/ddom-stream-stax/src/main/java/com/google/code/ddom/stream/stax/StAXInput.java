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
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.stax2.DTDInfo;

import com.google.code.ddom.stream.spi.Output;
import com.google.code.ddom.stream.spi.Input;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.Symbols;

public class StAXInput implements Input {
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
    
    private void processNSUnawareElement(Output output) {
        output.processElement(reader.getLocalName());
        for (int count = reader.getAttributeCount(), i=0; i<count; i++) {
            output.processAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i), reader.getAttributeType(i));
        }
        output.attributesCompleted();
    }
    
    private void processNSAwareElement(Output output) {
        output.processElement(emptyToNull(reader.getNamespaceURI()), reader.getLocalName(), emptyToNull(reader.getPrefix()));
        for (int count = reader.getAttributeCount(), i=0; i<count; i++) {
            output.processAttribute(emptyToNull(reader.getAttributeNamespace(i)), reader.getAttributeLocalName(i), emptyToNull(reader.getAttributePrefix(i)), reader.getAttributeValue(i), reader.getAttributeType(i));
        }
        for (int count = reader.getNamespaceCount(), i=0; i<count; i++) {
            output.processNamespaceDeclaration(emptyToNull(reader.getNamespacePrefix(i)), emptyToNull(reader.getNamespaceURI(i)));
        }
        output.attributesCompleted();
    }
    
    public boolean proceed(Output output) throws StreamException {
        if (callNext) {
            try {
                reader.next();
            } catch (XMLStreamException ex) {
                throw new StreamException(ex);
            }
        } else {
            callNext = true;
        }
        switch (reader.getEventType()) {
            case XMLStreamReader.START_DOCUMENT:
                output.setDocumentInfo(reader.getVersion(), reader.getCharacterEncodingScheme(), reader.getEncoding(), reader.isStandalone());
                return true;
            case XMLStreamReader.END_DOCUMENT:
                output.nodeCompleted();
                return false;
            case XMLStreamReader.DTD:
                output.processDocumentType(dtdInfo.getDTDRootName(), dtdInfo.getDTDPublicId(), dtdInfo.getDTDSystemId());
                return true;
            case XMLStreamReader.START_ELEMENT:
                if (parserIsNamespaceAware) {
                    processNSAwareElement(output);
                } else {
                    processNSUnawareElement(output);
                }
                return true;
            case XMLStreamReader.END_ELEMENT:
                output.nodeCompleted();
                return true;
            case XMLStreamReader.PROCESSING_INSTRUCTION:
                output.processProcessingInstruction(reader.getPITarget(), reader.getPIData());
                return true;
            case XMLStreamReader.CHARACTERS:
            case XMLStreamReader.SPACE: // TODO: these should be distinct events
                output.processText(reader.getText());
                return true;
            case XMLStreamReader.CDATA:
                output.processCDATASection();
                output.processText(reader.getText());
                output.nodeCompleted();
                return true;
            case XMLStreamReader.COMMENT:
                output.processComment(reader.getText());
                return true;
            case XMLStreamReader.ENTITY_REFERENCE:
                output.processEntityReference(reader.getLocalName());
                return true;
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
