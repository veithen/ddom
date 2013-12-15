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
    private static final int START_NS_AWARE_ELEMENT_UNRESOLVED = 22;
    private static final int END_ELEMENT = 5;
    private static final int START_NS_UNAWARE_ATTRIBUTE = 6;
    private static final int START_NS_AWARE_ATTRIBUTE = 7;
    private static final int START_NS_AWARE_ATTRIBUTE_UNRESOLVED = 23;
    private static final int START_NAMESPACE_DECLARATION = 8;
    private static final int END_ATTRIBUTE = 9;
    private static final int RESOLVE_ELEMENT_NAMESPACE = 24;
    private static final int RESOLVE_ATTRIBUTE_NAMESPACE = 25;
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
    
    private int[] intTokens = new int[16];
    private int currentIntTokenIndex;
    private int nextIntTokenIndex;

    private String[] stringTokens = new String[16];
    private int currentStringTokenIndex;
    private int nextStringTokenIndex;
    
    XmlPivotHandler(XmlPivot pivot, Stream stream) {
        this.pivot = pivot;
        this.stream = stream;
    }
    
    private void addIntToken(int event) {
        intTokens[nextIntTokenIndex] = event;
        nextIntTokenIndex++;
        int currentSize = intTokens.length;
        if (nextIntTokenIndex == currentSize) {
            nextIntTokenIndex = 0;
        }
        if (nextIntTokenIndex == currentIntTokenIndex) {
            int[] newBuffer = new int[currentSize*2];
            System.arraycopy(intTokens, 0, newBuffer, 0, currentIntTokenIndex);
            System.arraycopy(intTokens, currentIntTokenIndex, newBuffer, currentIntTokenIndex+currentSize, currentSize-currentIntTokenIndex);
            currentIntTokenIndex += currentSize;
            intTokens = newBuffer;
        }
    }
    
    private void addToken(String token) {
        stringTokens[nextStringTokenIndex] = token;
        nextStringTokenIndex++;
        int currentSize = stringTokens.length;
        if (nextStringTokenIndex == currentSize) {
            nextStringTokenIndex = 0;
        }
        if (nextStringTokenIndex == currentStringTokenIndex) {
            String[] newBuffer = new String[currentSize*2];
            System.arraycopy(stringTokens, 0, newBuffer, 0, currentStringTokenIndex);
            System.arraycopy(stringTokens, currentStringTokenIndex, newBuffer, currentStringTokenIndex+currentSize, currentSize-currentStringTokenIndex);
            currentStringTokenIndex += currentSize;
            stringTokens = newBuffer;
        }
    }
    
    private boolean hasEvents() {
        return currentIntTokenIndex != nextIntTokenIndex;
    }
    
    private int getIntToken() {
        int event = intTokens[currentIntTokenIndex++];
        if (currentIntTokenIndex == intTokens.length) {
            currentIntTokenIndex = 0;
        }
        return event;
    }
    
    private String getStringToken() {
        String token = stringTokens[currentStringTokenIndex++];
        if (currentStringTokenIndex == stringTokens.length) {
            currentStringTokenIndex = 0;
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
            addIntToken(XML_DECLARATION);
            addToken(version);
            addToken(encoding);
            addToken(standalone == null ? null : standalone.toString());
        }
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        if (passThrough) {
            passThrough = pivot.startDocumentTypeDeclaration(rootName, publicId, systemId);
        } else {
            addIntToken(START_DOCUMENT_TYPE_DECLARATION);
            addToken(rootName);
            addToken(publicId);
            addToken(systemId);
        }
    }
    
    public void endDocumentTypeDeclaration() {
        if (passThrough) {
            passThrough = pivot.endDocumentTypeDeclaration();
        } else {
            addIntToken(END_DOCUMENT_TYPE_DECLARATION);
        }
    }
    
    public void startElement(String tagName) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startElement(tagName);
        } else {
            addIntToken(START_NS_UNAWARE_ELEMENT);
            addToken(tagName);
        }
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startElement(namespaceURI, localName, prefix);
        } else {
            if (namespaceURI == null) {
                addIntToken(START_NS_AWARE_ELEMENT_UNRESOLVED);
            } else {
                addIntToken(START_NS_AWARE_ELEMENT);
                addToken(namespaceURI);
            }
            addToken(localName);
            addToken(prefix);
        }
    }

    public void endElement() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endElement();
        } else {
            addIntToken(END_ELEMENT);
        }
    }

    public void startAttribute(String name, String type) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startAttribute(name, type);
        } else {
            addIntToken(START_NS_UNAWARE_ATTRIBUTE);
            addToken(name);
            addToken(type);
        }
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startAttribute(namespaceURI, localName, prefix, type);
        } else {
            if (namespaceURI == null) {
                addIntToken(START_NS_AWARE_ATTRIBUTE_UNRESOLVED);
            } else {
                addIntToken(START_NS_AWARE_ATTRIBUTE);
                addToken(namespaceURI);
            }
            addToken(localName);
            addToken(prefix);
            addToken(type);
        }
    }

    public void startNamespaceDeclaration(String prefix) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startNamespaceDeclaration(prefix);
        } else {
            addIntToken(START_NAMESPACE_DECLARATION);
            addToken(prefix);
        }
    }
    
    public void endAttribute() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endAttribute();
        } else {
            addIntToken(END_ATTRIBUTE);
        }
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        if (passThrough) {
            passThrough = pivot.resolveElementNamespace(namespaceURI);
        } else {
            addIntToken(RESOLVE_ELEMENT_NAMESPACE);
            addToken(namespaceURI);
        }
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        if (passThrough) {
            passThrough = pivot.resolveAttributeNamespace(index, namespaceURI);
        } else {
            addIntToken(RESOLVE_ATTRIBUTE_NAMESPACE);
            addIntToken(index);
            addToken(namespaceURI);
        }
    }

    public void attributesCompleted() {
        if (passThrough) {
            passThrough = pivot.attributesCompleted();
        } else {
            addIntToken(ATTRIBUTES_COMPLETED);
        }
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (passThrough) {
            passThrough = pivot.processCharacterData(data, ignorable);
        } else {
            addIntToken(ignorable ? IGNORABLE_TEXT : TEXT);
            addToken(data);
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        if (passThrough) {
            passThrough = pivot.startProcessingInstruction(target);
        } else {
            addIntToken(START_PROCESSING_INSTRUCTION);
            addToken(target);
        }
    }

    public void endProcessingInstruction() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endProcessingInstruction();
        } else {
            addIntToken(END_PROCESSING_INSTRUCTION);
        }
    }

    public void startComment() throws StreamException {
        if (passThrough) {
            passThrough = pivot.startComment();
        } else {
            addIntToken(START_COMMENT);
        }
    }

    public void endComment() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endComment();
        } else {
            addIntToken(END_COMMENT);
        }
    }

    public void startCDATASection() throws StreamException {
        if (passThrough) {
            passThrough = pivot.startCDATASection();
        } else {
            addIntToken(START_CDATA_SECTION);
        }
    }

    public void endCDATASection() throws StreamException {
        if (passThrough) {
            passThrough = pivot.endCDATASection();
        } else {
            addIntToken(END_CDATA_SECTION);
        }
    }

    public void processEntityReference(String name) {
        if (passThrough) {
            passThrough = pivot.processEntityReference(name);
        } else {
            addIntToken(ENTITY_REFERENCE);
            addToken(name);
        }
    }

    public void completed() throws StreamException {
        if (passThrough) {
            pivot.completed();
            passThrough = false;
        } else {
            addIntToken(COMPLETED);
        }
    }
    
    void next() throws StreamException {
        while (hasEvents()) {
            boolean result;
            switch (getIntToken()) {
                case XML_DECLARATION:
                    String version = getStringToken();
                    String encoding = getStringToken();
                    String standalone = getStringToken();
                    result = pivot.processXmlDeclaration(version, encoding, standalone == null ? null : Boolean.valueOf(standalone));
                    break;
                case START_DOCUMENT_TYPE_DECLARATION:
                    result = pivot.startDocumentTypeDeclaration(getStringToken(), getStringToken(), getStringToken());
                    break;
                case END_DOCUMENT_TYPE_DECLARATION:
                    result = pivot.endDocumentTypeDeclaration();
                    break;
                case START_NS_UNAWARE_ELEMENT:
                    result = pivot.startElement(getStringToken());
                    break;
                case START_NS_AWARE_ELEMENT:
                    result = pivot.startElement(getStringToken(), getStringToken(), getStringToken());
                    break;
                case START_NS_AWARE_ELEMENT_UNRESOLVED:
                    result = pivot.startElement(null, getStringToken(), getStringToken());
                    break;
                case END_ELEMENT:
                    result = pivot.endElement();
                    break;
                case START_NS_UNAWARE_ATTRIBUTE:
                    result = pivot.startAttribute(getStringToken(), getStringToken());
                    break;
                case START_NS_AWARE_ATTRIBUTE:
                    result = pivot.startAttribute(getStringToken(), getStringToken(), getStringToken(), getStringToken());
                    break;
                case START_NS_AWARE_ATTRIBUTE_UNRESOLVED:
                    result = pivot.startAttribute(null, getStringToken(), getStringToken(), getStringToken());
                    break;
                case START_NAMESPACE_DECLARATION:
                    result = pivot.startNamespaceDeclaration(getStringToken());
                    break;
                case END_ATTRIBUTE:
                    result = pivot.endAttribute();
                    break;
                case RESOLVE_ELEMENT_NAMESPACE:
                    result = pivot.resolveElementNamespace(getStringToken());
                    break;
                case RESOLVE_ATTRIBUTE_NAMESPACE:
                    result = pivot.resolveAttributeNamespace(getIntToken(), getStringToken());
                    break;
                case ATTRIBUTES_COMPLETED:
                    result = pivot.attributesCompleted();
                    break;
                case TEXT:
                    result = pivot.processCharacterData(getStringToken(), false);
                    break;
                case IGNORABLE_TEXT:
                    result = pivot.processCharacterData(getStringToken(), true);
                    break;
                case START_PROCESSING_INSTRUCTION:
                    result = pivot.startProcessingInstruction(getStringToken());
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
                    result = pivot.processEntityReference(getStringToken());
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
