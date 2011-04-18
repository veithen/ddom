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
package com.googlecode.ddom.stream.pivot;

import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class XmlPivotHandler implements XmlHandler {
    // TODO: renumber the constants once we are done with adding new ones...
    private static final int XML_DECLARATION = 1;
    private static final int START_DOCUMENT_TYPE_DECLARATION = 2;
    private static final int END_DOCUMENT_TYPE_DECLARATION = 21;
    private static final int START_NS_UNAWARE_ELEMENT = 3;
    private static final int START_NS_AWARE_ELEMENT = 4;
    private static final int END_ELEMENT = 5;
    private static final int START_NS_UNAWARE_ATTRIBUTE = 6;
    private static final int START_NS_AWARE_ATTRIBUTE = 7;
    private static final int START_NAMESPACE_DECLARATION = 8;
    private static final int END_ATTRIBUTE = 9;
    private static final int ATTRIBUTES_COMPLETED = 10;
    private static final int TEXT = 11;
    private static final int IGNORABLE_TEXT = 12;
    private static final int START_PROCESSING_INSTRUCTION = 13;
    private static final int END_PROCESSING_INSTRUCTION = 14;
    private static final int START_COMMENT = 15;
    private static final int END_COMMENT = 16;
    private static final int START_CDATA_SECTION = 17;
    private static final int END_CDATA_SECTION = 18;
    private static final int ENTITY_REFERENCE = 19;
    private static final int COMPLETED = 20;
    
    private final XmlPivot pivot;
    private final Stream stream;
    private boolean passThrough = true;
    
    private int[] events = new int[16];
    private int currentEventIndex;
    private int nextEventIndex;

    private String[] tokens = new String[16];
    private int currentTokenIndex;
    private int nextTokenIndex;
    
    XmlPivotHandler(XmlPivot pivot, Stream stream) {
        this.pivot = pivot;
        this.stream = stream;
    }
    
    private void addEvent(int event) {
        events[nextEventIndex] = event;
        nextEventIndex++;
        int currentSize = events.length;
        if (nextEventIndex == currentSize) {
            nextEventIndex = 0;
        }
        if (nextEventIndex == currentEventIndex) {
            int[] newBuffer = new int[currentSize*2];
            System.arraycopy(events, 0, newBuffer, 0, currentEventIndex);
            System.arraycopy(events, currentEventIndex, newBuffer, currentEventIndex+currentSize, currentSize-currentEventIndex);
            currentEventIndex += currentSize;
            events = newBuffer;
        }
    }
    
    private void addToken(String token) {
        tokens[nextTokenIndex] = token;
        nextTokenIndex++;
        int currentSize = tokens.length;
        if (nextTokenIndex == currentSize) {
            nextTokenIndex = 0;
        }
        if (nextTokenIndex == currentTokenIndex) {
            String[] newBuffer = new String[currentSize*2];
            System.arraycopy(tokens, 0, newBuffer, 0, currentTokenIndex);
            System.arraycopy(tokens, currentTokenIndex, newBuffer, currentTokenIndex+currentSize, currentSize-currentTokenIndex);
            currentTokenIndex += currentSize;
            tokens = newBuffer;
        }
    }
    
    private boolean hasEvents() {
        return currentEventIndex != nextEventIndex;
    }
    
    private int getEvent() {
        int event = events[currentEventIndex++];
        if (currentEventIndex == events.length) {
            currentEventIndex = 0;
        }
        return event;
    }
    
    private String getToken() {
        String token = tokens[currentTokenIndex++];
        if (currentTokenIndex == tokens.length) {
            currentTokenIndex = 0;
        }
        return token;
    }

    public void startEntity(boolean fragment, String inputEncoding) {
        if (!passThrough) {
            // Since this must always be the first call, passThrough must always be true
            throw new IllegalStateException();
        }
        passThrough = pivot.startEntity(fragment, inputEncoding);
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        if (passThrough) {
            passThrough = pivot.processXmlDeclaration(version, encoding, standalone);
        } else {
            addEvent(XML_DECLARATION);
            addToken(version);
            addToken(encoding);
            addToken(standalone == null ? null : standalone.toString());
        }
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        if (passThrough) {
            passThrough = pivot.startDocumentTypeDeclaration(rootName, publicId, systemId);
        } else {
            addEvent(START_DOCUMENT_TYPE_DECLARATION);
            addToken(rootName);
            addToken(publicId);
            addToken(systemId);
        }
    }
    
    public void endDocumentTypeDeclaration() {
        if (passThrough) {
            passThrough = pivot.endDocumentTypeDeclaration();
        } else {
            addEvent(END_DOCUMENT_TYPE_DECLARATION);
        }
    }
    
    public void startElement(String tagName) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startElement(tagName);
        } else {
            addEvent(START_NS_UNAWARE_ELEMENT);
            addToken(tagName);
        }
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startElement(namespaceURI, localName, prefix);
        } else {
            addEvent(START_NS_AWARE_ELEMENT);
            addToken(namespaceURI);
            addToken(localName);
            addToken(prefix);
        }
    }

    public void endElement() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endElement();
        } else {
            addEvent(END_ELEMENT);
        }
    }

    public void startAttribute(String name, String type) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startAttribute(name, type);
        } else {
            addEvent(START_NS_UNAWARE_ATTRIBUTE);
            addToken(name);
            addToken(type);
        }
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startAttribute(namespaceURI, localName, prefix, type);
        } else {
            addEvent(START_NS_AWARE_ATTRIBUTE);
            addToken(namespaceURI);
            addToken(localName);
            addToken(prefix);
            addToken(type);
        }
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startNamespaceDeclaration(prefix);
        } else {
            addEvent(START_NAMESPACE_DECLARATION);
            addToken(prefix);
        }
    }
    
    public void endAttribute() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endAttribute();
        } else {
            addEvent(END_ATTRIBUTE);
        }
    }

    public void attributesCompleted() {
        if (passThrough) {
            passThrough = pivot.attributesCompleted();
        } else {
            addEvent(ATTRIBUTES_COMPLETED);
        }
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (passThrough) {
            passThrough = pivot.processCharacterData(data, ignorable);
        } else {
            addEvent(ignorable ? IGNORABLE_TEXT : TEXT);
            addToken(data);
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startProcessingInstruction(target);
        } else {
            addEvent(START_PROCESSING_INSTRUCTION);
            addToken(target);
        }
    }

    public void endProcessingInstruction() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endProcessingInstruction();
        } else {
            addEvent(END_PROCESSING_INSTRUCTION);
        }
    }

    public void startComment() throws StreamException {
        if (passThrough) {
            passThrough = pivot.startComment();
        } else {
            addEvent(START_COMMENT);
        }
    }

    public void endComment() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endComment();
        } else {
            addEvent(END_COMMENT);
        }
    }

    public void startCDATASection() throws StreamException {
        if (passThrough) {
            passThrough = pivot.startCDATASection();
        } else {
            addEvent(START_CDATA_SECTION);
        }
    }

    public void endCDATASection() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endCDATASection();
        } else {
            addEvent(END_CDATA_SECTION);
        }
    }

    public void processEntityReference(String name) {
        if (passThrough) {
            passThrough = pivot.processEntityReference(name);
        } else {
            addEvent(ENTITY_REFERENCE);
            addToken(name);
        }
    }

    public void completed() throws StreamException {
        if (passThrough) {
            pivot.completed();
            passThrough = false;
        } else {
            addEvent(COMPLETED);
        }
    }
    
    void next() throws StreamException {
        while (hasEvents()) {
            boolean result;
            switch (getEvent()) {
                case XML_DECLARATION:
                    String version = getToken();
                    String encoding = getToken();
                    String standalone = getToken();
                    result = pivot.processXmlDeclaration(version, encoding, standalone == null ? null : Boolean.valueOf(standalone));
                    break;
                case START_DOCUMENT_TYPE_DECLARATION:
                    result = pivot.startDocumentTypeDeclaration(getToken(), getToken(), getToken());
                    break;
                case END_DOCUMENT_TYPE_DECLARATION:
                    result = pivot.endDocumentTypeDeclaration();
                    break;
                case START_NS_UNAWARE_ELEMENT:
                    result = pivot.startElement(getToken());
                    break;
                case START_NS_AWARE_ELEMENT:
                    result = pivot.startElement(getToken(), getToken(), getToken());
                    break;
                case END_ELEMENT:
                    result = pivot.endElement();
                    break;
                case START_NS_UNAWARE_ATTRIBUTE:
                    result = pivot.startAttribute(getToken(), getToken());
                    break;
                case START_NS_AWARE_ATTRIBUTE:
                    result = pivot.startAttribute(getToken(), getToken(), getToken(), getToken());
                    break;
                case START_NAMESPACE_DECLARATION:
                    result = pivot.startNamespaceDeclaration(getToken());
                    break;
                case END_ATTRIBUTE:
                    result = pivot.endAttribute();
                    break;
                case ATTRIBUTES_COMPLETED:
                    result = pivot.attributesCompleted();
                    break;
                case TEXT:
                    result = pivot.processCharacterData(getToken(), false);
                    break;
                case IGNORABLE_TEXT:
                    result = pivot.processCharacterData(getToken(), true);
                    break;
                case START_PROCESSING_INSTRUCTION:
                    result = pivot.startProcessingInstruction(getToken());
                    break;
                case END_PROCESSING_INSTRUCTION:
                    result = pivot.endProcessingInstruction();
                    break;
                case START_COMMENT:
                    result = pivot.startComment();
                    break;
                case END_COMMENT:
                    result = pivot.endComment();
                    break;
                case START_CDATA_SECTION:
                    result = pivot.startCDATASection();
                    break;
                case END_CDATA_SECTION:
                    result = pivot.endCDATASection();
                    break;
                case ENTITY_REFERENCE:
                    result = pivot.processEntityReference(getToken());
                    break;
                case COMPLETED:
                    pivot.completed();
                    result = false;
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (!result) {
                return;
            }
        }
        passThrough = true;
        do {
            stream.proceed();
        } while (passThrough);
    }
}
