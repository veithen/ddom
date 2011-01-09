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
package com.google.code.ddom.stream.stax;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.buffer.XmlPivot;

public class StAXPivot extends XmlPivot implements XMLStreamReader {
    private static final int INITIAL_ELEMENT_STACK_SIZE = 8;
    private static final int INITIAL_NAMESPACE_STACK_SIZE = 16;
    private static final int INITIAL_ATTRIBUTE_STACK_SIZE = 8;
    
    private int eventType = START_DOCUMENT;
    private int elementStackSize = INITIAL_ELEMENT_STACK_SIZE;
    private int depth;
    private String[] elementStack = new String[INITIAL_ELEMENT_STACK_SIZE*3];
    private int[] scopeStack = new int[INITIAL_ELEMENT_STACK_SIZE];
    private int namespaceStackSize = INITIAL_NAMESPACE_STACK_SIZE;
    private String[] namespaceStack = new String[INITIAL_ATTRIBUTE_STACK_SIZE*2];
    private int attributeStackSize = INITIAL_ATTRIBUTE_STACK_SIZE;
    private String[] attributeStack = new String[INITIAL_ATTRIBUTE_STACK_SIZE*5];
    private int bindings;
    private String namespaceURI;
    private String localName;
    private String prefix;
    private int attributeCount;
    private boolean isNamespaceDeclaration;
    private boolean coalesce;
    private final StringAccumulator accumulator = new StringAccumulator();
    private String data;
    
    private final NamespaceContext namespaceContext = new NamespaceContext() {
        public Iterator<String> getPrefixes(String namespaceURI) {
            return StAXPivot.this.getPrefixes(namespaceURI);
        }
        
        public String getPrefix(String namespaceURI) {
            return StAXPivot.this.getPrefix(namespaceURI);
        }
        
        public String getNamespaceURI(String prefix) {
            String uri = StAXPivot.this.getNamespaceURI(prefix);
            return uri == null ? XMLConstants.NULL_NS_URI : uri;
        }
    };
    
    private String stopCoalescing() {
        String data = accumulator.toString();
        accumulator.clear();
        coalesce = false;
        return data;
    }
    
    @Override
    protected void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding,
            boolean standalone) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean processDocumentType(String rootName, String publicId, String systemId, String data) {
        eventType = DTD;
        this.data = data;
        return false;
    }

    @Override
    protected boolean startElement(String tagName) {
        return startElement(null, tagName, null);
    }

    @Override
    protected boolean startElement(String namespaceURI, String localName, String prefix) {
        eventType = START_ELEMENT;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
        attributeCount = 0;
        if (depth == elementStackSize) {
            elementStackSize *= 2;
            String[] newElementStack = new String[elementStackSize*3];
            System.arraycopy(elementStack, 0, newElementStack, 0, depth*3);
            elementStack = newElementStack;
            int[] newScopeStack = new int[elementStackSize];
            System.arraycopy(scopeStack, 0, newScopeStack, 0, depth);
            scopeStack = newScopeStack;
        }
        elementStack[depth*3] = namespaceURI;
        elementStack[depth*3+1] = localName;
        elementStack[depth*3+2] = prefix;
        scopeStack[depth] = bindings;
        return true;
    }

    @Override
    protected boolean endElement() {
        eventType = END_ELEMENT;
        depth--;
        namespaceURI = elementStack[depth*3];
        localName = elementStack[depth*3+1];
        prefix = elementStack[depth*3+2];
        return false;
    }

    @Override
    protected boolean startAttribute(String name, String type) {
        return startAttribute(null, name, null, type);
    }

    @Override
    protected boolean startAttribute(String namespaceURI, String localName, String prefix, String type) {
        isNamespaceDeclaration = false;
        coalesce = true;
        if (attributeCount == attributeStackSize) {
            attributeStackSize *= 2;
            String[] newStack = new String[attributeStackSize*5];
            System.arraycopy(attributeStack, 0, newStack, 0, attributeCount*5);
            attributeStack = newStack;
        }
        attributeStack[attributeCount*5] = namespaceURI;
        attributeStack[attributeCount*5+1] = localName;
        attributeStack[attributeCount*5+2] = prefix;
        attributeStack[attributeCount*5+3] = type;
        return true;
    }

    @Override
    protected boolean startNamespaceDeclaration(String prefix) {
        isNamespaceDeclaration = true;
        coalesce = true;
        if (bindings == namespaceStackSize) {
            namespaceStackSize *= 2;
            String[] newStack = new String[namespaceStackSize*5];
            System.arraycopy(namespaceStack, 0, newStack, 0, attributeStackSize*5);
            namespaceStack = newStack;
        }
        namespaceStack[bindings*2] = prefix;
        return true;
    }

    @Override
    protected boolean endAttribute() {
        if (isNamespaceDeclaration) {
            namespaceStack[bindings*2+1] = stopCoalescing();
            bindings++;
        } else {
            attributeStack[attributeCount*5+4] = stopCoalescing();
            attributeCount++;
        }
        return true;
    }

    @Override
    protected boolean attributesCompleted() {
        return false;
    }

    @Override
    protected boolean processProcessingInstruction(String target, String data) {
        eventType = PROCESSING_INSTRUCTION;
        localName = target;
        this.data = data;
        return false;
    }

    @Override
    protected boolean processText(String data, boolean ignorable) {
        if (coalesce) {
            accumulator.append(data);
            return true;
        } else {
            eventType = ignorable ? SPACE : CHARACTERS;
            this.data = data;
            return false;
        }
    }

    @Override
    protected boolean processComment(String data) {
        eventType = COMMENT;
        this.data = data;
        return false;
    }

    @Override
    protected boolean startCDATASection() {
//        eventType = CDATA;
//        coalesce = true;
//        return true;
        return true;
    }

    @Override
    protected boolean endCDATASection() {
//        text = stopCoalescing();
//        return false;
        return true;
    }

    @Override
    protected boolean processEntityReference(String name) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    protected void completed() {
        eventType = END_DOCUMENT;
    }

    public int getEventType() {
        return eventType;
    }

    public boolean isCharacters() {
        return eventType == CHARACTERS;
    }

    public boolean isStartElement() {
        return eventType == START_ELEMENT;
    }

    public boolean isEndElement() {
        return eventType == END_ELEMENT;
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#getVersion()
     */
    public String getVersion() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#getCharacterEncodingScheme()
     */
    public String getCharacterEncodingScheme() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#isStandalone()
     */
    public boolean isStandalone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#standaloneSet()
     */
    public boolean standaloneSet() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasName() {
        return eventType == START_ELEMENT || eventType == END_ELEMENT;
    }

    public String getNamespaceURI() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return namespaceURI;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getLocalName() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return localName;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getPrefix() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return prefix;
        } else {
            throw new IllegalStateException();
        }
    }

    public QName getName() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            if (namespaceURI == null) {
                return new QName(localName);
            } else {
                if (prefix == null) {
                    return new QName(namespaceURI, localName);
                } else {
                    return new QName(namespaceURI, localName, prefix);
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public int getAttributeCount() {
        if (eventType == START_ELEMENT) {
            return attributeCount;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributeNamespace(int index) {
        if (eventType == START_ELEMENT) {
            return attributeStack[index*5];
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributeLocalName(int index) {
        if (eventType == START_ELEMENT) {
            return attributeStack[index*5+1];
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributePrefix(int index) {
        if (eventType == START_ELEMENT) {
            return attributeStack[index*5+2];
        } else {
            throw new IllegalStateException();
        }
    }

    public QName getAttributeName(int index) {
        if (eventType == START_ELEMENT) {
            String namespaceURI = attributeStack[index*5];
            String localName = attributeStack[index*5+1];
            String prefix = attributeStack[index*5+2];
            if (namespaceURI == null) {
                return new QName(localName);
            } else {
                if (prefix == null) {
                    return new QName(namespaceURI, localName);
                } else {
                    return new QName(namespaceURI, localName, prefix);
                }
            }
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributeType(int index) {
        if (eventType == START_ELEMENT) {
            return attributeStack[index*5+3];
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributeValue(int index) {
        if (eventType == START_ELEMENT) {
            return attributeStack[index*5+4];
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isAttributeSpecified(int index) {
        if (eventType == START_ELEMENT) {
            // TODO
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getAttributeValue(String arg0, String arg1) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int getNamespaceCount() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return bindings-scopeStack[depth];
        } else {
            throw new IllegalStateException();
        }
    }

    public String getNamespacePrefix(int index) {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return namespaceStack[(scopeStack[depth]+index)*2];
        } else {
            throw new IllegalStateException();
        }
    }

    public String getNamespaceURI(int index) {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return namespaceStack[(scopeStack[depth]+index)*2+1];
        } else {
            throw new IllegalStateException();
        }
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#getElementText()
     */
    public String getElementText() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#getEncoding()
     */
    public String getEncoding() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#getLocation()
     */
    public Location getLocation() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasText() {
        return eventType == CHARACTERS || eventType == COMMENT || eventType == DTD || eventType == SPACE;
    }

    public String getText() {
        if (hasText()) {
            return data;
        } else {
            throw new IllegalStateException();
        }
    }

    public char[] getTextCharacters() {
        if (hasText() && eventType != DTD) {
            return data.toCharArray();
        } else {
            throw new IllegalStateException();
        }
    }

    public int getTextCharacters(int arg0, char[] arg1, int arg2, int arg3)
            throws XMLStreamException {
        if (!hasText()) {
            throw new IllegalStateException();
        }
        // TODO
        throw new UnsupportedOperationException();
    }

    public int getTextLength() {
        if (hasText() && eventType != DTD) {
            return data.length();
        } else {
            throw new IllegalStateException();
        }
    }

    public int getTextStart() {
        if (hasText() && eventType != DTD) {
            return 0;
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean isWhiteSpace() {
        switch (eventType) {
            case SPACE:
                return true;
            case CHARACTERS:
                // XMLStreamReader Javadoc says that isWhiteSpace "returns true if the cursor
                // points to a character data event that consists of all whitespace". This
                // means that this method may return true for a CHARACTER event and we need
                // to scan the text of the node.
                for (int i=0; i<data.length(); i++) {
                    char c = data.charAt(i);
                    if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public String getPITarget() {
        if (eventType == PROCESSING_INSTRUCTION) {
            return localName;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getPIData() {
        if (eventType == PROCESSING_INSTRUCTION) {
            return data;
        } else {
            throw new IllegalStateException();
        }
    }

    public Object getProperty(String arg0) throws IllegalArgumentException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#hasNext()
     */
    public boolean hasNext() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int next() throws XMLStreamException {
        switch (eventType) {
            case START_ELEMENT:
                depth++;
                break;
            case END_ELEMENT:
                // Remove namespace bindings that go out of scope
                bindings = scopeStack[depth];
                break;
            case END_DOCUMENT:
                throw new NoSuchElementException();
        }
        eventType = -1;
        try {
            nextEvent();
        } catch (StreamException ex) {
            throw new XMLStreamException(ex);
        }
        return eventType;
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#nextTag()
     */
    public int nextTag() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#require(int, java.lang.String, java.lang.String)
     */
    public void require(int arg0, String arg1, String arg2) throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }
    
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix can't be null");
        } else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        } else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else {
            if (prefix.length() == 0) {
                prefix = null;
            }
            for (int i=(bindings-1)*2; i>=0; i-=2) {
                if (ObjectUtils.equals(prefix, namespaceStack[i])) {
                    return namespaceStack[i+1];
                }
            }
            return null;
        }
    }

    String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI can't be null");
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            outer: for (int i=(bindings-1)*2; i>=0; i-=2) {
                if (namespaceURI.equals(namespaceStack[i+1])) {
                    String prefix = namespaceStack[i];
                    // Now check that the prefix is not masked
                    for (int j=i+2; j<bindings*2; j++) {
                        if (ObjectUtils.equals(prefix, namespaceStack[j])) {
                            continue outer;
                        }
                    }
                    return prefix == null ? "" : prefix;
                }
            }
            return null;
        }
    }

    Iterator<String> getPrefixes(final String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI can't be null");
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return Collections.singleton(XMLConstants.XML_NS_PREFIX).iterator();
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return Collections.singleton(XMLConstants.XMLNS_ATTRIBUTE).iterator();
        } else {
            return new Iterator<String>() {
                private int binding = bindings;
                private String next;
    
                public boolean hasNext() {
                    if (next == null) {
                        outer: while (--binding >= 0) {
                            if (namespaceURI.equals(namespaceStack[binding*2+1])) {
                                String prefix = namespaceStack[binding*2];
                                // Now check that the prefix is not masked
                                for (int j=binding+1; j<bindings; j++) {
                                    if (ObjectUtils.equals(prefix, namespaceStack[j*2])) {
                                        continue outer;
                                    }
                                }
                                next = prefix == null ? "" : prefix;
                                break;
                            }
                        }
                    }
                    return next != null;
                }
    
                public String next() {
                    if (hasNext()) {
                        String result = next;
                        next = null;
                        return result;
                    } else {
                        throw new NoSuchElementException();
                    }
                }
    
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
    
    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#close()
     */
    public void close() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
