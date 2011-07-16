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

import java.util.NoSuchElementException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.pivot.XmlPivot;
import com.googlecode.ddom.util.lang.ObjectUtils;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

public class StAXPivot extends XmlPivot implements XMLStreamReader {
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_COALESCE = 1;
    private static final int STATE_COLLECT_TEXT = 2;
    private static final int STATE_SKIP_COMMENT = 3;
    
    private static final int INITIAL_ELEMENT_STACK_SIZE = 8;
    private static final int INITIAL_ATTRIBUTE_STACK_SIZE = 8;
    
    private int eventType = -1;
    private int elementStackSize = INITIAL_ELEMENT_STACK_SIZE;
    private int depth;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private Boolean standalone;
    private String[] elementStack = new String[INITIAL_ELEMENT_STACK_SIZE*3];
    private final ScopedNamespaceContext namespaceContext = new ScopedNamespaceContext();
    private int attributeStackSize = INITIAL_ATTRIBUTE_STACK_SIZE;
    private String[] attributeStack = new String[INITIAL_ATTRIBUTE_STACK_SIZE*5];
    private String namespaceURI;
    private String localName;
    private String prefix;
    private int attributeCount;
    private String declaredPrefix;
    private int state = STATE_DEFAULT;
    private final StringAccumulator accumulator = new StringAccumulator();
    private String data;
    
    private String stopCoalescing() {
        String data = accumulator.toString();
        accumulator.clear();
        state = STATE_DEFAULT;
        return data;
    }
    
    @Override
    protected boolean startEntity(boolean fragment, String inputEncoding) {
        this.inputEncoding = inputEncoding;
        if (fragment) {
            eventType = START_DOCUMENT;
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected boolean processXmlDeclaration(String version, String encoding, Boolean standalone) {
        this.xmlVersion = version;
        this.xmlEncoding = encoding;
        this.standalone = standalone;
        eventType = START_DOCUMENT;
        return false;
    }

    @Override
    protected boolean startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        eventType = DTD;
        // TODO
//        this.data = data;
        return true;
    }

    @Override
    protected boolean endDocumentTypeDeclaration() {
        return false;
    }

    @Override
    protected boolean startElement(String tagName) {
        return startElement(null, tagName, null);
    }

    @Override
    protected boolean startElement(String namespaceURI, String localName, String prefix) {
        eventType = START_ELEMENT;
        if (namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        if (prefix.length() == 0) {
            prefix = null;
        }
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
        attributeCount = 0;
        if (depth == elementStackSize) {
            elementStackSize *= 2;
            String[] newElementStack = new String[elementStackSize*3];
            System.arraycopy(elementStack, 0, newElementStack, 0, depth*3);
            elementStack = newElementStack;
        }
        elementStack[depth*3] = namespaceURI;
        elementStack[depth*3+1] = localName;
        elementStack[depth*3+2] = prefix;
        namespaceContext.startScope();
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
        return startAttribute("", name, "", type);
    }

    @Override
    protected boolean startAttribute(String namespaceURI, String localName, String prefix, String type) {
        state = STATE_COALESCE;
        if (attributeCount == attributeStackSize) {
            attributeStackSize *= 2;
            String[] newStack = new String[attributeStackSize*5];
            System.arraycopy(attributeStack, 0, newStack, 0, attributeCount*5);
            attributeStack = newStack;
        }
        attributeStack[attributeCount*5] = namespaceURI.length() == 0 ? null : namespaceURI;
        attributeStack[attributeCount*5+1] = localName;
        attributeStack[attributeCount*5+2] = prefix.length() == 0 ? null : prefix;
        attributeStack[attributeCount*5+3] = type;
        return true;
    }

    @Override
    protected boolean startNamespaceDeclaration(String prefix) {
        declaredPrefix = prefix;
        state = STATE_COALESCE;
        return true;
    }

    @Override
    protected boolean endAttribute() {
        if (declaredPrefix != null) {
            namespaceContext.setPrefix(declaredPrefix, stopCoalescing());
            declaredPrefix = null;
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
    protected boolean processCharacterData(String data, boolean ignorable) {
        switch (state) {
            case STATE_DEFAULT:
                eventType = ignorable ? SPACE : CHARACTERS;
                this.data = data;
                return false;
            case STATE_COALESCE:
            case STATE_COLLECT_TEXT:
                accumulator.append(data);
                return true;
            case STATE_SKIP_COMMENT:
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected boolean startProcessingInstruction(String target) {
        eventType = PROCESSING_INSTRUCTION;
        localName = target;
        state = STATE_COALESCE;
        return true;
    }

    @Override
    protected boolean endProcessingInstruction() {
        data = stopCoalescing();
        return false;
    }

    @Override
    protected boolean startComment() {
        if (state == STATE_COLLECT_TEXT) {
            state = STATE_SKIP_COMMENT;
        } else {
            eventType = COMMENT;
            state = STATE_COALESCE;
        }
        return true;
    }

    @Override
    protected boolean endComment() {
        if (state == STATE_SKIP_COMMENT) {
            state = STATE_COLLECT_TEXT;
            return true;
        } else {
            data = stopCoalescing();
            return false;
        }
    }

    @Override
    protected boolean startCDATASection() {
        switch (state) {
            case STATE_DEFAULT:
                eventType = CDATA;
                state = STATE_COALESCE;
                return true;
            case STATE_COLLECT_TEXT:
                accumulator.append(data);
                return true;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected boolean endCDATASection() {
        switch (state) {
            case STATE_COALESCE:
                data = stopCoalescing();
                return false;
            case STATE_COLLECT_TEXT:
                return true;
            default:
                throw new IllegalStateException();
        }
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

    public Location getLocation() {
        return DummyLocation.INSTANCE;
    }

    public int getEventType() {
        if (eventType == -1) {
            try {
                nextEvent();
            } catch (StreamException ex) {
                throw new RuntimeException(ex);
            }
        }
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

    private void ensureStartDocument() {
        if (eventType == -1) {
            try {
                nextEvent();
            } catch (StreamException ex) {
                throw new RuntimeException(ex);
            }
        } else if (eventType != START_DOCUMENT) {
            throw new IllegalStateException();
        }
    }
    
    public String getEncoding() {
        ensureStartDocument();
        return inputEncoding;
    }

    public String getVersion() {
        ensureStartDocument();
        return xmlVersion;
    }

    public String getCharacterEncodingScheme() {
        ensureStartDocument();
        return xmlEncoding;
    }

    public boolean isStandalone() {
        ensureStartDocument();
        return standalone != null && standalone.booleanValue();
    }

    public boolean standaloneSet() {
        ensureStartDocument();
        return standalone != null;
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

    public String getAttributeValue(String namespaceURI, String localName) {
        if (eventType == START_ELEMENT) {
            if (namespaceURI != null && namespaceURI.length() == 0) {
                namespaceURI = null;
            }
            for (int i=0; i<attributeCount; i++) {
                if (localName.equals(attributeStack[i*5+1]) && ObjectUtils.equals(namespaceURI, attributeStack[i*5])) {
                    return attributeStack[i*5+4];
                }
            }
            return null;
        } else {
            throw new IllegalStateException();
        }
    }

    public int getNamespaceCount() {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return namespaceContext.getBindingsCount()-namespaceContext.getFirstBindingInCurrentScope();
        } else {
            throw new IllegalStateException();
        }
    }

    public String getNamespacePrefix(int index) {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            String prefix = namespaceContext.getPrefix(namespaceContext.getFirstBindingInCurrentScope() + index);
            return prefix.length() == 0 ? null : prefix;
        } else {
            throw new IllegalStateException();
        }
    }

    public String getNamespaceURI(int index) {
        if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
            return namespaceContext.getNamespaceURI(namespaceContext.getFirstBindingInCurrentScope() + index);
        } else {
            throw new IllegalStateException();
        }
    }

    public boolean hasText() {
        return eventType == CHARACTERS || eventType == CDATA || eventType == COMMENT || eventType == DTD || eventType == SPACE;
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

    public Object getProperty(String name) throws IllegalArgumentException {
        // TODO: support other standard properties
        if (name.equals(XMLInputFactory.IS_NAMESPACE_AWARE)) {
            return Boolean.TRUE;
        } else {
            throw new IllegalArgumentException(); // TODO: or return null??
        }
    }

    public boolean hasNext() throws XMLStreamException {
        return eventType != END_DOCUMENT;
    }

    public int next() throws XMLStreamException {
        try {
            // Make sure that we process the START_DOCUMENT event, even if no other
            // method is called in this state
            if (eventType == -1) {
                nextEvent();
            }
            switch (eventType) {
                case START_ELEMENT:
                    depth++;
                    break;
                case END_ELEMENT:
                    namespaceContext.endScope();
                    break;
                case END_DOCUMENT:
                    throw new NoSuchElementException();
            }
            eventType = -1;
            nextEvent();
        } catch (StreamException ex) {
            throw StAXExceptionUtil.toXMLStreamException(ex);
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

    public String getElementText() throws XMLStreamException {
        // TODO: check correct state
        // TODO: need to make sure that in this state, other events will cause an exception
        state = STATE_COLLECT_TEXT;
        next();
        return stopCoalescing();
    }

    public NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }
    
    public String getNamespaceURI(String prefix) {
        String namespaceURI = namespaceContext.getNamespaceURI(prefix);
        return namespaceURI.length() == 0 ? null : namespaceURI;
    }

    /* (non-Javadoc)
     * @see javax.xml.stream.XMLStreamReader#close()
     */
    public void close() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
