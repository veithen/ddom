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
package com.googlecode.ddom.backend.testsuite;

import static junit.framework.Assert.assertEquals;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.pivot.XmlPivot;

public class StreamAssert extends XmlPivot {
    private final StringAccumulator buffer = new StringAccumulator();
    private Event expectedEvent;
    private String expectedNamespaceURI;
    private String expectedName;
    private String expectedPrefix;
    private String expectedData;
    
    protected boolean startEntity(boolean fragment, String inputEncoding) {
        assertEquals(expectedEvent, fragment ? Event.START_FRAGMENT : Event.START_DOCUMENT);
        return false;
    }
    
    @Override
    protected boolean processXmlDeclaration(String version, String encoding, Boolean standalone) {
        assertEquals(expectedEvent, Event.XML_DECLARATION);
        // TODO: check XML declaration info
        return false;
    }

    protected boolean startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        assertEquals(expectedEvent, Event.START_DOCUMENT_TYPE_DECLARATION);
        return false;
    }

    protected boolean endDocumentTypeDeclaration() {
        assertEquals(expectedEvent, Event.END_DOCUMENT_TYPE_DECLARATION);
        return false;
    }

    protected boolean startElement(String tagName) {
        assertEquals(expectedEvent, Event.START_NS_UNAWARE_ELEMENT);
        return false;
    }
    
    protected boolean startElement(String namespaceURI, String localName, String prefix) {
        assertEquals(expectedEvent, Event.START_NS_AWARE_ELEMENT);
        assertEquals(expectedNamespaceURI, namespaceURI);
        assertEquals(expectedName, localName);
        assertEquals(expectedPrefix, prefix);
        return false;
    }
    
    protected boolean endElement() {
        assertEquals(expectedEvent, Event.END_ELEMENT);
        return false;
    }
    
    protected boolean startAttribute(String name, String type) {
        assertEquals(expectedEvent, Event.START_NS_UNAWARE_ATTRIBUTE);
        return false;
    }
    
    protected boolean startAttribute(String namespaceURI, String localName, String prefix, String type) {
        assertEquals(expectedEvent, Event.START_NS_AWARE_ATTRIBUTE);
        return false;
    }
    
    protected boolean startNamespaceDeclaration(String prefix) {
        assertEquals(expectedEvent, Event.START_NAMESPACE_DECLARATION);
        return false;
    }
    
    protected boolean endAttribute() {
        assertEquals(expectedEvent, Event.END_ATTRIBUTE);
        return false;
    }
    
    protected boolean attributesCompleted() {
        assertEquals(expectedEvent, Event.ATTRIBUTES_COMPLETED);
        return false;
    }
    
    protected boolean processCharacterData(String data, boolean ignorable) {
        assertEquals(expectedEvent, Event.CHARACTER_DATA);
        buffer.append(data);
        if (buffer.length() < expectedData.length()) {
            return true;
        } else {
            assertEquals(expectedData, buffer.toString());
            buffer.clear();
            return false;
        }
    }
    
    protected boolean startProcessingInstruction(String target) {
        assertEquals(expectedEvent, Event.START_PROCESSING_INSTRUCTION);
        return false;
    }
    
    protected boolean endProcessingInstruction() {
        assertEquals(expectedEvent, Event.END_PROCESSING_INSTRUCTION);
        return false;
    }
    
    protected boolean startComment() {
        assertEquals(expectedEvent, Event.START_COMMENT);
        return false;
    }
    
    protected boolean endComment() {
        assertEquals(expectedEvent, Event.END_COMMENT);
        return false;
    }
    
    protected boolean startCDATASection() {
        assertEquals(expectedEvent, Event.START_CDATA_SECTION);
        return false;
    }
    
    protected boolean endCDATASection() {
        assertEquals(expectedEvent, Event.END_CDATA_SECTION);
        return false;
    }
    
    protected boolean processEntityReference(String name) {
        assertEquals(expectedEvent, Event.ENTITY_REFERENCE);
        return false;
    }
    
    protected void completed() {
        assertEquals(expectedEvent, Event.COMPLETED);
    }
    
    public void assertStartEntity(boolean fragment) throws StreamException {
        expectedEvent = fragment ? Event.START_FRAGMENT : Event.START_DOCUMENT;
        nextEvent();
    }
    
    public void assertXmlDeclaration() throws StreamException {
        expectedEvent = Event.XML_DECLARATION;
        nextEvent();
    }
    
    public void assertStartElement(String namespaceURI, String localName, String prefix) throws StreamException {
        expectedEvent = Event.START_NS_AWARE_ELEMENT;
        expectedNamespaceURI = namespaceURI;
        expectedName = localName;
        expectedPrefix = prefix;
        nextEvent();
    }
    
    public void assertEndElement() throws StreamException {
        expectedEvent = Event.END_ELEMENT;
        nextEvent();
    }
    
    public void assertAttributesCompleted() throws StreamException {
        expectedEvent = Event.ATTRIBUTES_COMPLETED;
        nextEvent();
    }
    
    public void assertCharacterData(String data) throws StreamException {
        expectedEvent = Event.CHARACTER_DATA;
        expectedData = data;
        nextEvent();
    }
}
