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
package com.googlecode.ddom.stream.parser;

import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.symbols.Symbols;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

final class NSAwareElementHandler extends ElementHandler {
    private static final int STATE_NONE = 0;
    private static final int STATE_START = 1;
    private static final int STATE_CONTENT = 2;
    private static final int STATE_END = 3;
    private static final int STATE_COMPLETED = 4;
    
    static class Attribute {
        boolean isNamespaceDeclaration;
        String prefix;
        String localName;
        String value;
    }
    
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();
    private final String xmlnsSymbol;
    private String prefix;
    private String localName;
    private Attribute[] attributes = new Attribute[16];
    private int attributeCount;
    private int attribute;
    private int state = STATE_NONE;
    
    NSAwareElementHandler(Symbols symbols, XmlHandler handler) {
        super(symbols, handler);
        xmlnsSymbol = symbols.getSymbol("xmlns");
    }

    @Override
    boolean pushPendingEvent() throws StreamException {
        switch (state) {
            case STATE_START:
                Attribute att = attributes[attribute];
                if (att.isNamespaceDeclaration) {
                    handler.startNamespaceDeclaration(att.prefix);
                } else if (att.prefix.length() == 0) {
                    handler.startAttribute("", att.localName, "", "CDATA");
                } else {
                    handler.startAttribute(resolvePrefix(att.prefix), att.localName, att.prefix, "CDATA");
                }
                state = STATE_CONTENT;
                return true;
            case STATE_CONTENT:
                handler.processCharacterData(attributes[attribute].value, false);
                state = STATE_END;
                return true;
            case STATE_END:
                handler.endAttribute();
                if (++attribute == attributeCount) {
                    state = STATE_COMPLETED;
                } else {
                    state = STATE_START;
                }
                return true;
            case STATE_COMPLETED:
                handler.attributesCompleted();
                state = STATE_NONE;
                return true;
            default:
                return false;
        }
    }

    private int searchColon(char[] name, int len) {
        for (int i=0; i<len; i++) {
            if (name[i] == ':') {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    boolean handleStartElement(char[] name, int len) {
        int idx = searchColon(name, len);
        if (idx == -1) {
            prefix = "";
            localName = symbols.getSymbol(name, 0, len);
        } else {
            prefix = symbols.getSymbol(name, 0, idx);
            localName = symbols.getSymbol(name, idx+1, len-idx-1);
        }
        attributeCount = 0;
        context.startScope();
        return false;
    }

    @Override
    boolean handleStartAttribute(char[] name, int len) {
        if (attributes.length == attributeCount) {
            Attribute[] newAttributes = new Attribute[attributeCount*2];
            System.arraycopy(attributes, 0, newAttributes, 0, attributeCount);
            attributes = newAttributes;
        }
        Attribute att = attributes[attributeCount];
        if (att == null) {
            att = new Attribute();
            attributes[attributeCount] = att;
        }
        att.value = "";
        int idx = searchColon(name, len);
        if (idx == -1) {
            att.prefix = "";
            String localName = symbols.getSymbol(name, 0, len);
            if (localName == xmlnsSymbol) {
                att.isNamespaceDeclaration = true;
            } else {
                att.isNamespaceDeclaration = false;
                att.localName = localName;
            }
        } else {
            String prefix = symbols.getSymbol(name, 0, idx);
            String localName = symbols.getSymbol(name, idx+1, len-idx-1);
            if (prefix == xmlnsSymbol) {
                att.isNamespaceDeclaration = true;
                att.prefix = localName;
            } else {
                att.isNamespaceDeclaration = false;
                att.prefix = prefix;
                att.localName = localName;
            }
        }
        return false;
    }

    @Override
    boolean handleCharacterData(String data) throws StreamException {
        // TODO
        attributes[attributeCount].value = data;
        return false;
    }

    @Override
    boolean handleEndAttribute() {
        attributeCount++;
        return false;
    }

    private String resolvePrefix(String prefix) throws StreamException {
        String namespaceURI = context.getNamespaceURI(prefix);
        if (prefix.length() > 0 && namespaceURI.length() == 0) {
            throw new StreamException("Unbound prefix '" + prefix + "'");
        }
        return namespaceURI;
    }
    
    @Override
    void attributesCompleted() throws StreamException {
        for (int i=0; i<attributeCount; i++) {
            Attribute att = attributes[i];
            if (att.isNamespaceDeclaration) {
                context.setPrefix(att.prefix, att.value);
            }
        }
        handler.startElement(resolvePrefix(prefix), localName, prefix);
        if (attributeCount > 0) {
            attribute = 0;
            state = STATE_START;
        } else {
            state = STATE_COMPLETED;
        }
    }

    @Override
    void handleEndElement(char[] name, int len) throws StreamException {
        // TODO: check that element name matches
        handler.endElement();
        context.endScope();
    }
}
