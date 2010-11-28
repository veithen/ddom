/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.googlecode.ddom.cxf;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.cxf.staxutils.W3CDOMStreamReader;

import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.stax.StAXInput;

public class StreamSwitch extends StAXInput implements XMLStreamReader {
    private XMLStreamReader reader;
    private final SOAPBody body;
    private boolean inputAccessed;
    private boolean readerAccessed;
    private boolean isOriginalReader;

    public StreamSwitch(XMLStreamReader reader, SOAPBody body) {
        super(reader, null);
        this.reader = reader;
        this.body = body;
        isOriginalReader = true;
    }

    @Override
    public boolean proceed() throws StreamException {
        if (readerAccessed && isOriginalReader) {
            throw new IllegalStateException("The original XMLStreamReader has already been accessed; " +
            		"it is no longer available to build the SOAP body");
        }
        inputAccessed = true;
        return super.proceed();
    }
    
    private void accessReader() {
        if (!readerAccessed) {
            // Data has already been pulled from the original reader through the Input interface.
            // Create a new XMLStreamReader to generate the events from the SAAJ representation
            // of the SOAP body.
            if (inputAccessed) {
                reader = new W3CDOMStreamReader(body);
                // Move the reader to the right position
                try {
                    reader.nextTag();
                    reader.nextTag();
                } catch (XMLStreamException ex) {
                    throw new RuntimeException("Failed to position W3CDOMStreamReader", ex);
                }
                isOriginalReader = false;
            }
            readerAccessed = true;
        }
    }

    public void close() throws XMLStreamException {
        accessReader();
        reader.close();
    }

    public int getAttributeCount() {
        accessReader();
        return reader.getAttributeCount();
    }

    public String getAttributeLocalName(int index) {
        accessReader();
        return reader.getAttributeLocalName(index);
    }

    public QName getAttributeName(int index) {
        accessReader();
        return reader.getAttributeName(index);
    }

    public String getAttributeNamespace(int index) {
        accessReader();
        return reader.getAttributeNamespace(index);
    }

    public String getAttributePrefix(int index) {
        accessReader();
        return reader.getAttributePrefix(index);
    }

    public String getAttributeType(int index) {
        accessReader();
        return reader.getAttributeType(index);
    }

    public String getAttributeValue(int index) {
        accessReader();
        return reader.getAttributeValue(index);
    }

    public String getAttributeValue(String namespaceURI, String localName) {
        accessReader();
        return reader.getAttributeValue(namespaceURI, localName);
    }

    public String getCharacterEncodingScheme() {
        accessReader();
        return reader.getCharacterEncodingScheme();
    }

    public String getElementText() throws XMLStreamException {
        accessReader();
        return reader.getElementText();
    }

    public String getEncoding() {
        accessReader();
        return reader.getEncoding();
    }

    public int getEventType() {
        accessReader();
        return reader.getEventType();
    }

    public String getLocalName() {
        accessReader();
        return reader.getLocalName();
    }

    public Location getLocation() {
        accessReader();
        return reader.getLocation();
    }

    public QName getName() {
        accessReader();
        return reader.getName();
    }

    public NamespaceContext getNamespaceContext() {
        accessReader();
        return reader.getNamespaceContext();
    }

    public int getNamespaceCount() {
        accessReader();
        return reader.getNamespaceCount();
    }

    public String getNamespacePrefix(int index) {
        accessReader();
        return reader.getNamespacePrefix(index);
    }

    public String getNamespaceURI() {
        accessReader();
        return reader.getNamespaceURI();
    }

    public String getNamespaceURI(int index) {
        accessReader();
        return reader.getNamespaceURI(index);
    }

    public String getNamespaceURI(String prefix) {
        accessReader();
        return reader.getNamespaceURI(prefix);
    }

    public String getPIData() {
        accessReader();
        return reader.getPIData();
    }

    public String getPITarget() {
        accessReader();
        return reader.getPITarget();
    }

    public String getPrefix() {
        accessReader();
        return reader.getPrefix();
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        accessReader();
        return reader.getProperty(name);
    }

    public String getText() {
        accessReader();
        return reader.getText();
    }

    public char[] getTextCharacters() {
        accessReader();
        return reader.getTextCharacters();
    }

    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
            throws XMLStreamException {
        accessReader();
        return reader.getTextCharacters(sourceStart, target, targetStart, length);
    }

    public int getTextLength() {
        accessReader();
        return reader.getTextLength();
    }

    public int getTextStart() {
        accessReader();
        return reader.getTextStart();
    }

    public String getVersion() {
        accessReader();
        return reader.getVersion();
    }

    public boolean hasName() {
        accessReader();
        return reader.hasName();
    }

    public boolean hasNext() throws XMLStreamException {
        accessReader();
        return reader.hasNext();
    }

    public boolean hasText() {
        accessReader();
        return reader.hasText();
    }

    public boolean isAttributeSpecified(int index) {
        accessReader();
        return reader.isAttributeSpecified(index);
    }

    public boolean isCharacters() {
        accessReader();
        return reader.isCharacters();
    }

    public boolean isEndElement() {
        accessReader();
        return reader.isEndElement();
    }

    public boolean isStandalone() {
        accessReader();
        return reader.isStandalone();
    }

    public boolean isStartElement() {
        accessReader();
        return reader.isStartElement();
    }

    public boolean isWhiteSpace() {
        accessReader();
        return reader.isWhiteSpace();
    }

    public int next() throws XMLStreamException {
        accessReader();
        return reader.next();
    }

    public int nextTag() throws XMLStreamException {
        accessReader();
        return reader.nextTag();
    }

    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        accessReader();
        reader.require(type, namespaceURI, localName);
    }

    public boolean standaloneSet() {
        accessReader();
        return reader.standaloneSet();
    }
}
