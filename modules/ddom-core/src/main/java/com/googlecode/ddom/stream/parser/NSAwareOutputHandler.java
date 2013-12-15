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
package com.googlecode.ddom.stream.parser;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.symbols.Symbols;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

final class NSAwareOutputHandler extends OutputHandler {
    // TODO: don't use ScopedNamespaceContext here; we can optimize things because we are using a symbol table
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();
    private final String xmlnsSymbol;
    
    /**
     * The prefix of the current element if the namespace of the element has not been resolved yet.
     * This attribute is set to <code>null</code> when the prefix of the element has been resolved.
     */
    private String unresolvedElementPrefix;
    
    private int attributeCount;
    
    /**
     * Array containing unresolved attribute prefixes. There is an entry for each attribute of the
     * element. When a prefix has been resolved, the corresponding item is set to <code>null</code>.
     */
    private String[] unresolvedAttributePrefixes = new String[16];
        
    /**
     * The prefix of the namespace binding if the handler is currently processing a namespace
     * declaration.
     */
    private String declaredPrefix;
        
    private final StringAccumulator declaredNamespaceURI = new StringAccumulator();
        
    private String[] nameStack = new String[16];
    private int nameStackIndex;
    
    NSAwareOutputHandler(Symbols symbols, XmlHandler handler) {
        super(symbols, handler);
        xmlnsSymbol = symbols.getSymbol("xmlns");
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
    void startElement(char[] name, int len) throws StreamException {
        String prefix;
        String localName;
        int idx = searchColon(name, len);
        if (idx == -1) {
            prefix = "";
            localName = symbols.getSymbol(name, 0, len);
        } else {
            prefix = symbols.getSymbol(name, 0, idx);
            localName = symbols.getSymbol(name, idx+1, len-idx-1);
        }
        handler.startElement(null, localName, prefix);
        attributeCount = 0;
        unresolvedElementPrefix = prefix;
        context.startScope();
        if (nameStackIndex == nameStack.length) {
            String[] newNameStack = new String[nameStack.length*2];
            System.arraycopy(nameStack, 0, newNameStack, 0, nameStack.length);
            nameStack = newNameStack;
        }
        nameStack[nameStackIndex] = prefix;
        nameStack[nameStackIndex+1] = localName;
        nameStackIndex += 2;
    }

    @Override
    void startAttribute(char[] name, int len) throws StreamException {
        if (unresolvedAttributePrefixes.length == attributeCount) {
            String[] newArray = new String[attributeCount*2];
            System.arraycopy(unresolvedAttributePrefixes, 0, newArray, 0, attributeCount);
            unresolvedAttributePrefixes = newArray;
        }
        String unresolvedPrefix;
        int idx = searchColon(name, len);
        if (idx == -1) {
            String localName = symbols.getSymbol(name, 0, len);
            if (localName == xmlnsSymbol) {
                handler.startNamespaceDeclaration("");
                declaredPrefix = "";
            } else {
                handler.startAttribute("", localName, "", "CDATA");
            }
            unresolvedPrefix = null;
        } else {
            String prefix = symbols.getSymbol(name, 0, idx);
            String localName = symbols.getSymbol(name, idx+1, len-idx-1);
            if (prefix == xmlnsSymbol) {
                handler.startNamespaceDeclaration(localName);
                declaredPrefix = localName;
                unresolvedPrefix = null;
            } else {
                // TODO: we can do better here:
                //  1) if we have seen a matching namespace declaration on the _current_ element, we know the namespace URI
                //  2) we can resolve the "xml" prefix already here
                handler.startAttribute(null, localName, prefix, "CDATA");
                unresolvedPrefix = prefix;
            }
        }
        unresolvedAttributePrefixes[attributeCount++] = unresolvedPrefix;
    }

    @Override
    void processCharacterData(String data) throws StreamException {
        if (declaredPrefix != null) {
            declaredNamespaceURI.append(data);
        }
        handler.processCharacterData(data, false);
    }

    @Override
    void endAttribute() throws StreamException {
        handler.endAttribute();
        if (declaredPrefix != null) {
            String namespaceURI = declaredNamespaceURI.toString();
            declaredNamespaceURI.clear();
            if (unresolvedElementPrefix == declaredPrefix) {
                handler.resolveElementNamespace(namespaceURI);
                unresolvedElementPrefix = null;
            }
            for (int i=0; i<attributeCount; i++) {
                if (unresolvedAttributePrefixes[i] == declaredPrefix) {
                    handler.resolveAttributeNamespace(i, namespaceURI);
                    unresolvedAttributePrefixes[i] = null;
                }
            }
            context.setPrefix(declaredPrefix, namespaceURI);
            declaredPrefix = null;
        }
    }

    @Override
    void attributesCompleted() throws StreamException {
        for (int i=-1; i<attributeCount; i++) {
            String prefix = i == -1 ? unresolvedElementPrefix : unresolvedAttributePrefixes[i];
            if (prefix != null) {
                String namespaceURI = context.getNamespaceURI(prefix);
                if (prefix.length() > 0 && namespaceURI.length() == 0) {
                    throw new XmlSyntaxException("Unbound prefix '" + prefix + "'");
                } else {
                    if (i == -1) {
                        handler.resolveElementNamespace(namespaceURI);
                        unresolvedElementPrefix = null;
                    } else {
                        handler.resolveAttributeNamespace(i, namespaceURI);
                        unresolvedAttributePrefixes[i] = null;
                    }
                    // Avoid multiple lookups for the same prefix
                    for (int j=i+1; j<attributeCount; j++) {
                        if (unresolvedAttributePrefixes[j] == prefix) {
                            handler.resolveAttributeNamespace(j, namespaceURI);
                            unresolvedAttributePrefixes[j] = null;
                        }
                    }
                }
            }
        }
        handler.attributesCompleted();
    }

    @Override
    void endElement(char[] name, int len) throws StreamException {
        nameStackIndex -= 2;
        if (name != null) {
            String prefix = nameStack[nameStackIndex];
            String localName = nameStack[nameStackIndex+1];
            if (prefix.length() == 0) {
                if (len != localName.length() || !Utils.equals(localName, name, 0)) {
                    throw new XmlSyntaxException("The element type \"" + localName + "\" must be terminated by the matching end-tag \"</" + localName + ">\"");
                }
            } else {
                if (len != prefix.length() + localName.length() + 1 || !Utils.equals(prefix, name, 0) || !Utils.equals(localName, name, prefix.length()+1)) {
                    throw new XmlSyntaxException("The element type \"" + prefix + ":" + localName + "\" must be terminated by the matching end-tag \"</" + prefix + ":" + localName + ">\"");
                }
            }
        }
        handler.endElement();
        context.endScope();
    }
}
