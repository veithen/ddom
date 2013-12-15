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
package com.googlecode.ddom.stream.sax;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

public class XmlHandlerAdapter implements XmlHandler, Attributes {
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_COALESCE = 1;
    private static final int STATE_IGNORE_CDATA = 2;
    
    private static final int INITIAL_ELEMENT_STACK_SIZE = 8;
    private static final int INITIAL_ATTRIBUTE_STACK_SIZE = 8;
    
    private final ContentHandler contentHandler;
    private final LexicalHandler lexicalHandler;

    private int depth;
    private int elementStackSize = INITIAL_ELEMENT_STACK_SIZE;
    private String[] elementStack = new String[INITIAL_ELEMENT_STACK_SIZE*3];
    private String elementPrefix;
    private final ScopedNamespaceContext namespaceContext = new ScopedNamespaceContext();
    private int attributeStackSize = INITIAL_ATTRIBUTE_STACK_SIZE;
    private String[] attributeStack = new String[INITIAL_ATTRIBUTE_STACK_SIZE*6];
    private int attributeCount;
    private String declaredPrefix;
    private String piTarget;
    private int state = STATE_DEFAULT;
    private final StringAccumulator accumulator = new StringAccumulator();
    
    public XmlHandlerAdapter(ContentHandler contentHandler, LexicalHandler lexicalHandler) {
        this.contentHandler = contentHandler;
        this.lexicalHandler = lexicalHandler;
    }

    private String stopCoalescing() {
        String data = accumulator.toString();
        accumulator.clear();
        state = STATE_DEFAULT;
        return data;
    }
    
    public void startEntity(boolean fragment, String inputEncoding) throws StreamException {
        try {
            contentHandler.startDocument();
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void processXmlDeclaration(String version, String encoding, Boolean standalone) {
    }

    public void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
    }

    public void endDocumentTypeDeclaration() throws StreamException {
    }

    public void startElement(String tagName) throws StreamException {
        internalStartElement("", "", null, tagName);
    }

    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        internalStartElement(namespaceURI, localName, prefix, prefix.length() == 0 ? localName : (prefix + ":" + localName));
    }
    
    private void internalStartElement(String uri, String localName, String prefix, String qName) {
        attributeCount = 0;
        if (depth == elementStackSize) {
            elementStackSize *= 2;
            String[] newElementStack = new String[elementStackSize*3];
            System.arraycopy(elementStack, 0, newElementStack, 0, depth*3);
            elementStack = newElementStack;
        }
        elementStack[depth*3] = uri;
        elementStack[depth*3+1] = localName;
        elementStack[depth*3+2] = qName;
        elementPrefix = prefix;
        namespaceContext.startScope();
    }

    public void endElement() throws StreamException {
        try {
            depth--;
            contentHandler.endElement(elementStack[depth*3], elementStack[depth*3+1], elementStack[depth*3+2]);
            for (int i=namespaceContext.getBindingsCount(); i<namespaceContext.getBindingsCount(); i++) {
                contentHandler.endPrefixMapping(namespaceContext.getPrefix(i));
            }
            namespaceContext.endScope();
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void startAttribute(String name, String type) throws StreamException {
        internalStartAttribute("", "", null, name, type);
    }

    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        // We pass null as qName so that it will be calculated lazily
        internalStartAttribute(namespaceURI, localName, prefix, null, type);
    }
    
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        declaredPrefix = prefix;
        // TODO: we need to generate an attribute in this case too (if the namespacePrefixes property is true)
        state = STATE_COALESCE;
    }

    private void internalStartAttribute(String uri, String localName, String prefix, String qName, String type) {
        state = STATE_COALESCE;
        if (attributeCount == attributeStackSize) {
            attributeStackSize *= 2;
            String[] newStack = new String[attributeStackSize*6];
            System.arraycopy(attributeStack, 0, newStack, 0, attributeCount*6);
            attributeStack = newStack;
        }
        attributeStack[attributeCount*6] = uri;
        attributeStack[attributeCount*6+1] = localName;
        // SAX doesn't give access to the prefix, but we need it to resolve namespaces and to calculate
        // the qName on demand
        attributeStack[attributeCount*6+2] = prefix;
        attributeStack[attributeCount*6+3] = qName;
        attributeStack[attributeCount*6+4] = type;
    }

    public void endAttribute() throws StreamException {
        try {
            if (declaredPrefix != null) {
                String namespaceURI = stopCoalescing();
                namespaceContext.setPrefix(declaredPrefix, namespaceURI);
                contentHandler.startPrefixMapping(declaredPrefix, namespaceURI);
                declaredPrefix = null;
            } else {
                attributeStack[attributeCount*6+5] = stopCoalescing();
                attributeCount++;
            }
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        elementStack[depth*3] = namespaceURI;
    }

    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        attributeStack[index*6] = namespaceURI;
    }

    public void attributesCompleted() throws StreamException {
        try {
            contentHandler.startElement(elementStack[depth*3], elementStack[depth*3+1], elementStack[depth*3+2], this);
            depth++;
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        try {
            switch (state) {
                case STATE_COALESCE:
                    accumulator.append(data);
                    break;
                case STATE_DEFAULT:
                    char[] ch = data.toCharArray();
                    if (ignorable) {
                        contentHandler.ignorableWhitespace(ch, 0, ch.length);
                    } else {
                        contentHandler.characters(ch, 0, ch.length);
                    }
            }
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void startProcessingInstruction(String target) throws StreamException {
        piTarget = target;
        state = STATE_COALESCE;
    }

    public void endProcessingInstruction() throws StreamException {
        try {
            contentHandler.processingInstruction(piTarget, stopCoalescing());
            piTarget = null;
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void startComment() throws StreamException {
        state = lexicalHandler != null ? STATE_COALESCE : STATE_IGNORE_CDATA;
    }

    public void endComment() throws StreamException {
        try {
            if (lexicalHandler != null) {
                char[] ch = stopCoalescing().toCharArray();
                lexicalHandler.comment(ch, 0, ch.length);
            }
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void startCDATASection() throws StreamException {
        try {
            if (lexicalHandler != null) {
                lexicalHandler.startCDATA();
            }
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void endCDATASection() throws StreamException {
        try {
            if (lexicalHandler != null) {
                lexicalHandler.endCDATA();
            }
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void processEntityReference(String name) throws StreamException {
        try {
            contentHandler.skippedEntity(name);
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public void completed() throws StreamException {
        try {
            contentHandler.endDocument();
        } catch (SAXException ex) {
            throw new StreamException(ex);
        }
    }

    public int getLength() {
        return attributeCount;
    }

    public int getIndex(String uri, String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public int getIndex(String qName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getURI(int index) {
        return attributeStack[index*6];
    }

    public String getLocalName(int index) {
        return attributeStack[index*6+1];
    }

    public String getQName(int index) {
        String qName = attributeStack[index*6+3];
        if (qName == null) {
            String prefix = attributeStack[index*6+2];
            String localName = attributeStack[index*6+1];
            qName = prefix.length() == 0 ? localName : (prefix + ":" + localName);
            attributeStack[index*6+2] = qName;
        }
        return qName;
    }

    public String getType(int index) {
        return attributeStack[index*6+4];
    }

    public String getValue(int index) {
        return attributeStack[index*6+5];
    }

    public String getType(String uri, String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getType(String qName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getValue(String uri, String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getValue(String qName) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
