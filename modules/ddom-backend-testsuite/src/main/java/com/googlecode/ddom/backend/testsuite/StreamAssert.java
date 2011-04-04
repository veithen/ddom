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
package com.googlecode.ddom.backend.testsuite;

import static junit.framework.Assert.assertEquals;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.pivot.XmlPivot;

public class StreamAssert extends XmlPivot {
    private static final int START_DOCUMENT = 1;
    private static final int START_FRAGMENT = 2;
    private static final int XML_DECLARATION = 3;
    private static final int DOCUMENT_TYPE = 4;
    private static final int START_NS_UNAWARE_ELEMENT = 5;
    private static final int START_NS_AWARE_ELEMENT = 6;
    private static final int END_ELEMENT = 7;
    private static final int START_NS_UNAWARE_ATTRIBUTE = 8;
    private static final int START_NS_AWARE_ATTRIBUTE = 9;
    private static final int START_NAMESPACE_DECLARATION = 10;
    private static final int END_ATTRIBUTE = 11;
    private static final int CHARACTER_DATA = 12;
    private static final int START_PROCESSING_INSTRUCTION = 13;
    private static final int END_PROCESSING_INSTRUCTION = 14;
    private static final int START_COMMENT = 15;
    private static final int END_COMMENT = 16;
    private static final int START_CDATA_SECTION = 17;
    private static final int END_CDATA_SECTION = 18;
    private static final int ENTITY_REFERENCE = 19;
    private static final int COMPLETED = 20;
    
    private final StringAccumulator buffer = new StringAccumulator();
    private int expectedEvent;
    private String expectedNamespaceURI;
    private String expectedName;
    private String expectedPrefix;
    private String expectedData;
    
    protected boolean startEntity(boolean fragment, String inputEncoding) {
        assertEquals(expectedEvent, fragment ? START_FRAGMENT : START_DOCUMENT);
        return false;
    }
    
    @Override
    protected boolean processXmlDeclaration(String version, String encoding, Boolean standalone) {
        assertEquals(expectedEvent, XML_DECLARATION);
        // TODO: check XML declaration info
        return false;
    }

    protected boolean processDocumentType(String rootName, String publicId, String systemId, String data) {
        assertEquals(expectedEvent, DOCUMENT_TYPE);
        return false;
    }

    protected boolean startElement(String tagName) {
        assertEquals(expectedEvent, START_NS_UNAWARE_ELEMENT);
        return false;
    }
    
    protected boolean startElement(String namespaceURI, String localName, String prefix) {
        assertEquals(expectedEvent, START_NS_AWARE_ELEMENT);
        assertEquals(expectedNamespaceURI, namespaceURI);
        assertEquals(expectedName, localName);
        assertEquals(expectedPrefix, prefix);
        return false;
    }
    
    protected boolean endElement() {
        assertEquals(expectedEvent, END_ELEMENT);
        return false;
    }
    
    protected boolean startAttribute(String name, String type) {
        assertEquals(expectedEvent, START_NS_UNAWARE_ATTRIBUTE);
        return false;
    }
    
    protected boolean startAttribute(String namespaceURI, String localName, String prefix, String type) {
        assertEquals(expectedEvent, START_NS_AWARE_ATTRIBUTE);
        return false;
    }
    
    protected boolean startNamespaceDeclaration(String prefix) {
        assertEquals(expectedEvent, START_NAMESPACE_DECLARATION);
        return false;
    }
    
    protected boolean endAttribute() {
        assertEquals(expectedEvent, END_ATTRIBUTE);
        return false;
    }
    
    protected boolean attributesCompleted() {
        return true;
    }
    
    protected boolean processCharacterData(String data, boolean ignorable) {
        assertEquals(expectedEvent, CHARACTER_DATA);
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
        assertEquals(expectedEvent, START_PROCESSING_INSTRUCTION);
        return false;
    }
    
    protected boolean endProcessingInstruction() {
        assertEquals(expectedEvent, END_PROCESSING_INSTRUCTION);
        return false;
    }
    
    protected boolean startComment() {
        assertEquals(expectedEvent, START_COMMENT);
        return false;
    }
    
    protected boolean endComment() {
        assertEquals(expectedEvent, END_COMMENT);
        return false;
    }
    
    protected boolean startCDATASection() {
        assertEquals(expectedEvent, START_CDATA_SECTION);
        return false;
    }
    
    protected boolean endCDATASection() {
        assertEquals(expectedEvent, END_CDATA_SECTION);
        return false;
    }
    
    protected boolean processEntityReference(String name) {
        assertEquals(expectedEvent, ENTITY_REFERENCE);
        return false;
    }
    
    protected void completed() {
        assertEquals(expectedEvent, COMPLETED);
    }
    
    public void assertStartEntity(boolean fragment) throws StreamException {
        expectedEvent = fragment ? START_FRAGMENT : START_DOCUMENT;
        nextEvent();
    }
    
    public void assertXmlDeclaration() throws StreamException {
        expectedEvent = XML_DECLARATION;
        nextEvent();
    }
    
    public void assertStartElement(String namespaceURI, String localName, String prefix) throws StreamException {
        expectedEvent = START_NS_AWARE_ELEMENT;
        expectedNamespaceURI = namespaceURI;
        expectedName = localName;
        expectedPrefix = prefix;
        nextEvent();
    }
    
    public void assertEndElement() throws StreamException {
        expectedEvent = END_ELEMENT;
        nextEvent();
    }
    
    public void assertCharacterData(String data) throws StreamException {
        expectedEvent = CHARACTER_DATA;
        expectedData = data;
        nextEvent();
    }
}
