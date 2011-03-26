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
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

public class NamespaceRepairingFilter extends SimpleXmlFilter {
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();
    private boolean inNamespaceDeclaration;
    private String prefix;
    private final StringAccumulator namespaceURI = new StringAccumulator();
    
    @Override
    protected void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        super.startElement(namespaceURI, localName, prefix);
        context.startScope();
        if (!context.isBound(prefix, namespaceURI)) {
            context.setPrefix(prefix, namespaceURI);
        }
    }

    @Override
    protected void startElement(String tagName) throws StreamException {
        super.startElement(tagName);
        context.startScope();
    }

    @Override
    protected void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        super.startAttribute(namespaceURI, localName, prefix, type);
        if (namespaceURI.length() != 0 && !context.isBound(prefix, namespaceURI)) {
            context.setPrefix(prefix, namespaceURI);
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
            if (!context.isBound(prefix, namespaceURI)) {
                context.setPrefix(prefix, namespaceURI);
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
        for (int i=context.getFirstBindingInCurrentScope(), l=context.getBindingsCount(); i<l; i++) {
            super.startNamespaceDeclaration(context.getPrefix(i));
            super.processCharacterData(context.getNamespaceURI(i), false);
            super.endAttribute();
        }
        super.attributesCompleted();
    }

    @Override
    protected void endElement() throws StreamException {
        super.endElement();
        context.endScope();
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
