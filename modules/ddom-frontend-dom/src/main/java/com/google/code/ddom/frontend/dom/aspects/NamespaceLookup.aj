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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.Node;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Implements the namespace lookup methods using the algorithms described in <a
 * href="http://www.w3.org/TR/DOM-Level-3-Core/namespaces-algorithms.html">Appendix B</a> of the DOM
 * Level 3 Core specification.
 * 
 * @author Andreas Veithen
 */
public aspect NamespaceLookup {
    public final CoreElement DOMElement.getNamespaceContext() {
        return this;
    }
    
    public final CoreElement DOMAttribute.getNamespaceContext() {
        return coreGetOwnerElement();
    }
    
    public final CoreElement DOMDocument.getNamespaceContext() {
        return coreGetDocumentElement();
    }
    
    public final CoreElement DOMDocumentFragment.getNamespaceContext() {
        return null;
    }
    
    public final CoreElement DOMDocumentType.getNamespaceContext() {
        return null;
    }
    
    public final CoreElement DOMCharacterData.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final CoreElement DOMProcessingInstruction.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final CoreElement DOMEntityReference.getNamespaceContext() {
        return coreGetParentElement();
    }
    
    public final String DOMNode.lookupNamespaceURI(String prefix) {
        CoreElement contextElement = getNamespaceContext();
        return contextElement == null ? null : contextElement.coreLookupNamespaceURI(prefix, false);
    }

    public final String DOMNode.lookupPrefix(String namespaceURI) {
        CoreElement contextElement = getNamespaceContext();
        if (contextElement == null) {
            return null;
        }
        // TODO: this is not entirely correct because the namespace declaration for this prefix may be hidden by a namespace declaration in a nested scope; need to check if this is covered by the DOM3 test suite
        for (CoreAttribute attr = contextElement.coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
            if (attr instanceof CoreNamespaceDeclaration) {
                CoreNamespaceDeclaration decl = (CoreNamespaceDeclaration)attr;
                if (decl.coreGetDeclaredNamespaceURI().equals(namespaceURI)) {
                    return decl.coreGetDeclaredPrefix();
                }
            }
        }
        Node parent = ((DOMElement)contextElement).getParentNode();
        return parent == null ? null : parent.lookupPrefix(namespaceURI);
    }

    public final boolean DOMNode.isDefaultNamespace(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
