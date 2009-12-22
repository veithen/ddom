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

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.NSUtil;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect NamedNodeSupport {
    public final String DOMNSUnawareNamedNode.getNamespaceURI() {
        return null;
    }
    
    public final String DOMNSUnawareNamedNode.getPrefix() {
        return null;
    }
    
    public final void DOMNSUnawareNamedNode.setPrefix(String prefix) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }
    
    public final String DOMNSUnawareNamedNode.getLocalName() {
        return null;
    }

    public final String DOMNSAwareNamedNode.getNamespaceURI() {
        return coreGetNamespaceURI();
    }
    
    public final String DOMNSAwareNamedNode.getPrefix() {
        return coreGetPrefix();
    }
    
    public final void DOMNSAwareNamedNode.setPrefix(String prefix) throws DOMException {
        String namespaceURI = getNamespaceURI();
        if (namespaceURI == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
        } else {
            NSUtil.validatePrefix(prefix);
            if (XMLConstants.XML_NS_PREFIX.equals(prefix) && !XMLConstants.XML_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix) && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            coreSetPrefix(prefix);
        }
    }
    
    public final String DOMNSAwareNamedNode.getLocalName() {
        return coreGetLocalName();
    }
    
    public final String DOMNSUnawareElement.getTagName() {
        return coreGetName();
    }
    
    public final String DOMNSUnawareTypedAttribute.getName() {
        return coreGetName();
    }

    private String DOMNSAwareNamedNode.internalGetName() {
        String prefix = getPrefix();
        String localName = getLocalName();
        if (prefix == null) {
            return localName;
        } else {
            return prefix + ":" + localName;
        }
    }
    
    public final String DOMNSAwareElement.getTagName() {
        return internalGetName();
    }
    
    public final String DOMNSAwareTypedAttribute.getName() {
        return internalGetName();
    }

    public final String DOMNamespaceDeclaration.getNamespaceURI() {
        return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    }

    public final String DOMNamespaceDeclaration.getPrefix() {
        return getDeclaredPrefix() == null ? null : XMLConstants.XMLNS_ATTRIBUTE;
    }

    public final void DOMNamespaceDeclaration.setPrefix(String prefix) throws DOMException {
        // Other DOM implementations allow changing the prefix, but this means that a namespace
        // declaration is transformed into a normal attribute. We don't support this.
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String DOMNamespaceDeclaration.getLocalName() {
        String declaredPrefix = getDeclaredPrefix();
        return declaredPrefix == null ? XMLConstants.XMLNS_ATTRIBUTE : declaredPrefix;
    }

    public final String DOMNamespaceDeclaration.getName() {
        String declaredPrefix = getDeclaredPrefix();
        if (declaredPrefix == null) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return XMLConstants.XMLNS_ATTRIBUTE + ":" + declaredPrefix;
        }
    }

    public final String DOMDocumentFragment.getNamespaceURI() {
        return null;
    }

    public final String DOMDocumentFragment.getPrefix() {
        return null;
    }

    public final void DOMDocumentFragment.setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String DOMDocumentFragment.getLocalName() {
        return null;
    }
    
    public final String DOMDocument.getNamespaceURI() {
        return null;
    }

    public final String DOMDocument.getPrefix() {
        return null;
    }

    public final void DOMDocument.setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String DOMDocument.getLocalName() {
        return null;
    }

    public final String DOMLeafNode.getNamespaceURI() {
        return null;
    }

    public final String DOMLeafNode.getPrefix() {
        return null;
    }

    public final void DOMLeafNode.setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String DOMLeafNode.getLocalName() {
        return null;
    }
}
