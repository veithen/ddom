/*
 * Copyright 2009-2012 Andreas Veithen
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

import java.util.HashMap;
import java.util.Map;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;

final class NamespaceURIInterningFilterHandler extends XmlHandlerWrapper {
    private final Map<String,String> lookupMap = new HashMap<String,String>();
    private boolean inNamespaceDeclaration;
    private final StringAccumulator namespaceURI = new StringAccumulator();
    
    NamespaceURIInterningFilterHandler(XmlHandler parent) {
        super(parent);
    }
    
    private String intern(String s) {
        if (s == null) {
            return null;
        } else if (s.length() == 0) {
            return "";
        } else {
            String interned = lookupMap.get(s);
            if (interned == null) {
                interned = s.intern();
                // Even if both strings are equal, we prefer to use the original string
                // as key. It is likely that subsequent events have namespace URI strings
                // with the same identity. In that case, lookup will be faster.
                lookupMap.put(s, interned);
            }
            return interned;
        }
    }

    @Override
    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        super.startElement(intern(namespaceURI), localName, prefix);
    }

    @Override
    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        super.startAttribute(intern(namespaceURI), localName, prefix, type);
    }

    @Override
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        super.startNamespaceDeclaration(prefix);
        inNamespaceDeclaration = true;
    }

    @Override
    public void endAttribute() throws StreamException {
        if (inNamespaceDeclaration) {
            super.processCharacterData(intern(this.namespaceURI.toString()), false);
            this.namespaceURI.clear();
            inNamespaceDeclaration = false;
        }
        super.endAttribute();
    }

    @Override
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (inNamespaceDeclaration) {
            namespaceURI.append(data);
        } else {
            super.processCharacterData(data, ignorable);
        }
    }
}
