/*
 * Copyright 2009 Andreas Veithen
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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.Node;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreNamespaceDeclaration;

public aspect NamespaceLookup {
    public final boolean NodeImpl.isDefaultNamespace(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String AttributeImpl.lookupNamespaceURI(String prefix) {
        // TODO: needs to be checked
        return null;
    }

    public final String AttributeImpl.lookupPrefix(String namespaceURI) {
        // TODO: needs to be checked
        return null;
    }

    public final String DocumentFragmentImpl.lookupNamespaceURI(String prefix) {
        return null;
    }

    public final String DocumentFragmentImpl.lookupPrefix(String namespaceURI) {
        return null;
    }

    public final String DocumentImpl.lookupNamespaceURI(String prefix) {
        return null;
    }

    public final String DocumentImpl.lookupPrefix(String namespaceURI) {
        return null;
    }

    public final String DOMElement.lookupNamespaceURI(String prefix) {
        for (CoreAttribute attr = internalGetFirstAttribute(); attr != null; attr = attr.internalGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                if (decl.getDeclaredPrefix().equals(prefix)) {
                    return decl.getDeclaredNamespaceURI();
                }
            }
        }
        Node parent = getParentNode();
        return parent == null ? null : parent.lookupNamespaceURI(prefix);
    }

    public final String DOMElement.lookupPrefix(String namespaceURI) {
        // TODO: this is not entirely correct because the namespace declaration for this prefix may be hidden by a namespace declaration in a nested scope; need to check if this is covered by the DOM3 test suite
        for (CoreAttribute attr = internalGetFirstAttribute(); attr != null; attr = attr.internalGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                if (decl.getDeclaredNamespaceURI().equals(namespaceURI)) {
                    return decl.getDeclaredPrefix();
                }
            }
        }
        Node parent = getParentNode();
        return parent == null ? null : parent.lookupPrefix(namespaceURI);
    }

    public final String LeafNode.lookupNamespaceURI(String prefix) {
        Node parent = getParentNode();
        return parent == null ? null : parent.lookupNamespaceURI(prefix);
    }
    
    public final String LeafNode.lookupPrefix(String namespaceURI) {
        Node parent = getParentNode();
        return parent == null ? null : parent.lookupPrefix(namespaceURI);
    }
}
