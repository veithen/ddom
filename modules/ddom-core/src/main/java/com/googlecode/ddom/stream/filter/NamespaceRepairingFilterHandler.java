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
package com.googlecode.ddom.stream.filter;

import com.google.code.ddom.commons.lang.StringAccumulator;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.filter.util.XmlHandlerWrapper;
import com.googlecode.ddom.util.namespace.ScopedNamespaceContext;

final class NamespaceRepairingFilterHandler extends XmlHandlerWrapper {
    // Implementation note: the attributes of this class have a structure similar to NSAwareOutputHandler
    
    private final ScopedNamespaceContext context = new ScopedNamespaceContext();
    
    /**
     * The prefix of the current element if the namespace of the element has not been resolved yet.
     * This attribute is set to <code>null</code> when the prefix of the element has been resolved.
     */
    private String unresolvedElementPrefix;
    
    private int inputAttributeCount;
    private int outputAttributeCount;
    
    /**
     * Array containing unresolved attribute prefixes. There is an entry for each attribute of the
     * element. When a prefix has been resolved, the corresponding item is set to <code>null</code>.
     */
    private String[] unresolvedAttributePrefixes;
    
    /**
     * Array mapping attribute indexes for {@link XmlHandler#resolveAttributeNamespace(int, String)}.
     */
    private int[] unresolvedAttributeRefs;
    
    /**
     * The prefix of the namespace binding if the handler is currently processing a namespace
     * declaration.
     */
    private String declaredPrefix;
    
    private final StringAccumulator declaredNamespaceURI = new StringAccumulator();
    
    NamespaceRepairingFilterHandler(XmlHandler parent) {
        super(parent);
    }

    private void processElementNamespace(String prefix, String namespaceURI) {
        if (!context.isBound(prefix, namespaceURI)) {
            context.setPrefix(prefix, namespaceURI);
        }
    }
    
    private void processAttributeNamespace(String prefix, String namespaceURI) {
        if (namespaceURI.length() != 0 && !context.isBound(prefix, namespaceURI)) {
            context.setPrefix(prefix, namespaceURI);
        }
    }
    
    @Override
    public void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        super.startElement(namespaceURI, localName, prefix);
        context.startScope();
        if (namespaceURI == null) {
            unresolvedElementPrefix = prefix;
        } else {
            processElementNamespace(prefix, namespaceURI);
        }
    }

    @Override
    public void startElement(String tagName) throws StreamException {
        super.startElement(tagName);
        context.startScope();
    }

    private void ensureCapacity() {
        int size = unresolvedAttributePrefixes == null ? 16 : unresolvedAttributePrefixes.length;
        while (size <= inputAttributeCount) {
            size *= 2;
        }
        if (unresolvedAttributePrefixes == null) {
            unresolvedAttributePrefixes = new String[size];
            unresolvedAttributeRefs = new int[size];
        } else if (size != unresolvedAttributePrefixes.length) {
            String[] newUnresolvedAttributePrefixes = new String[size];
            System.arraycopy(unresolvedAttributePrefixes, 0, newUnresolvedAttributePrefixes, 0, unresolvedAttributePrefixes.length);
            unresolvedAttributePrefixes = newUnresolvedAttributePrefixes;
            int[] newUnresolvedAttributeRefs = new int[size];
            System.arraycopy(unresolvedAttributeRefs, 0, newUnresolvedAttributeRefs, 0, unresolvedAttributeRefs.length);
            unresolvedAttributeRefs = newUnresolvedAttributeRefs;
        }
    }
    
    @Override
    public void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        super.startAttribute(namespaceURI, localName, prefix, type);
        if (namespaceURI == null) {
            ensureCapacity();
            unresolvedAttributePrefixes[inputAttributeCount] = prefix;
            unresolvedAttributeRefs[inputAttributeCount] = outputAttributeCount;
        } else {
            processAttributeNamespace(prefix, namespaceURI);
        }
        inputAttributeCount++;
        outputAttributeCount++;
    }

    @Override
    public void startNamespaceDeclaration(String prefix) throws StreamException {
        this.declaredPrefix = prefix;
        inputAttributeCount++;
    }

    @Override
    public void endAttribute() throws StreamException {
        if (declaredPrefix != null) {
            String namespaceURI = this.declaredNamespaceURI.toString();
            if (!context.isBound(declaredPrefix, namespaceURI)) {
                context.setPrefix(declaredPrefix, namespaceURI);
            }
            declaredPrefix = null;
            this.declaredNamespaceURI.clear();
        } else {
            super.endAttribute();
        }
    }

    private void markAttributeNamespacesAsResolved(String prefix) {
        if (unresolvedAttributePrefixes != null) {
            for (int i=0; i<inputAttributeCount; i++) {
                if (prefix.equals(unresolvedAttributePrefixes[i])) {
                    unresolvedAttributePrefixes[i] = null;
                }
            }
        }
    }
    
    @Override
    public void resolveElementNamespace(String namespaceURI) throws StreamException {
        super.resolveElementNamespace(namespaceURI);
        processElementNamespace(unresolvedElementPrefix, namespaceURI);
        markAttributeNamespacesAsResolved(unresolvedElementPrefix);
        unresolvedElementPrefix = null;
    }

    @Override
    public void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        super.resolveAttributeNamespace(unresolvedAttributeRefs[index], namespaceURI);
        String prefix = unresolvedAttributePrefixes[index];
        if (prefix != null) {
            processAttributeNamespace(prefix, namespaceURI);
            markAttributeNamespacesAsResolved(prefix);
        }
    }

    @Override
    public void attributesCompleted() throws StreamException {
        for (int i=context.getFirstBindingInCurrentScope(), l=context.getBindingsCount(); i<l; i++) {
            super.startNamespaceDeclaration(context.getPrefix(i));
            super.processCharacterData(context.getNamespaceURI(i), false);
            super.endAttribute();
        }
        inputAttributeCount = 0;
        outputAttributeCount = 0;
        super.attributesCompleted();
    }

    @Override
    public void endElement() throws StreamException {
        super.endElement();
        context.endScope();
    }

    @Override
    public void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (declaredPrefix != null) {
            declaredNamespaceURI.append(data);
        } else {
            super.processCharacterData(data, ignorable);
        }
    }
}
