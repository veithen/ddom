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
package com.googlecode.ddom.stream.filter;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.SimpleXmlFilter;
import com.googlecode.ddom.stream.StreamException;

public class NamespaceRepairingFilter extends SimpleXmlFilter {
    private String[] namespaceStack = new String[32];
    private int bindings;
    private int[] scopeStack = new int[8];
    private int scopes;
    private boolean inNamespaceDeclaration;
    private String prefix;
    private final StringAccumulator namespaceURI = new StringAccumulator();
    
    private boolean isBound(String prefix, String namespaceURI) {
        for (int i=(bindings-1)*2; i>=0; i-=2) {
            if (prefix.equals(namespaceStack[i])) {
                return namespaceURI.equals(namespaceStack[i+1]);
            }
        }
        return prefix.length() == 0 && namespaceURI.length() == 0;
    }
    
    private void setPrefix(String prefix, String namespaceURI) {
        if (bindings*2 == namespaceStack.length) {
            int len = namespaceStack.length;
            String[] newNamespaceStack = new String[len*2];
            System.arraycopy(namespaceStack, 0, newNamespaceStack, 0, len);
        }
        namespaceStack[bindings*2] = prefix;
        namespaceStack[bindings*2+1] = namespaceURI;
        bindings++;
    }
    
    private void startScope() {
        if (scopes == scopeStack.length) {
            int[] newScopeStack = new int[scopeStack.length*2];
            System.arraycopy(scopeStack, 0, newScopeStack, 0, scopeStack.length);
            scopeStack = newScopeStack;
        }
        scopeStack[scopes++] = bindings;
    }
    
    private void endScope() {
        bindings = scopeStack[--scopes];
    }

    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        super.startElement(namespaceURI, localName, prefix);
        startScope();
        if (!isBound(prefix, namespaceURI)) {
            setPrefix(prefix, namespaceURI);
        }
    }

    @Override
    protected void startElement(String tagName) throws StreamException {
        super.startElement(tagName);
        startScope();
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        super.startAttribute(namespaceURI, localName, prefix, type);
        if (namespaceURI.length() != 0 && !isBound(prefix, namespaceURI)) {
            setPrefix(prefix, namespaceURI);
        }
    }

    @Override
    protected void startNamespaceDeclaration(String prefix) throws StreamException {
        inNamespaceDeclaration = true;
        this.prefix = prefix;
    }

    @Override
    protected void endAttribute() throws StreamException {
        if (inNamespaceDeclaration) {
            String namespaceURI = this.namespaceURI.toString();
            if (!isBound(prefix, namespaceURI)) {
                setPrefix(prefix, namespaceURI);
            }
            prefix = null;
            this.namespaceURI.clear();
            inNamespaceDeclaration = false;
        } else {
            super.endAttribute();
        }
    }

    @Override
    protected void attributesCompleted() throws StreamException {
        for (int i=scopeStack[scopes-1]; i<bindings; i++) {
            super.startNamespaceDeclaration(namespaceStack[i*2]);
            super.processCharacterData(namespaceStack[i*2+1], false);
            super.endAttribute();
        }
        super.attributesCompleted();
    }

    @Override
    protected void endElement() throws StreamException {
        super.endElement();
        endScope();
    }

    @Override
    protected void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (inNamespaceDeclaration) {
            namespaceURI.append(data);
        } else {
            super.processCharacterData(data, ignorable);
        }
    }
}
