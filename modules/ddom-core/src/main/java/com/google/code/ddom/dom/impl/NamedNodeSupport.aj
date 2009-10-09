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

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

public aspect NamedNodeSupport {
    declare parents: (NSAwareElementImpl || NSAwareTypedAttributeImpl) implements DOMNSAwareNamedNode;
    declare parents: (NSUnawareElementImpl || NSUnawareTypedAttributeImpl) implements DOMNSUnawareNamedNode;
    
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

    public void DOMNSAwareNamedNode.setPrefix(String prefix) throws DOMException {
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
            internalSetPrefix(prefix);
        }
    }
    
    public final String NSUnawareElementImpl.getTagName() {
        return coreGetName();
    }
    
    public final String NSUnawareTypedAttributeImpl.getName() {
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
    
    public final String NSAwareElementImpl.getTagName() {
        return internalGetName();
    }
    
    public final String NSAwareTypedAttributeImpl.getName() {
        return internalGetName();
    }
}
