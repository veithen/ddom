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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreTypedAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.support.AttributesNamedNodeMap;
import com.google.code.ddom.frontend.dom.support.DOM1AttributeMatcher;
import com.google.code.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.NSUtil;
import com.google.code.ddom.frontend.dom.support.Policies;
import com.google.code.ddom.stream.spi.Symbols;

public aspect ElementSupport {
    public final boolean DOMElement.hasAttributes() {
        return coreGetFirstAttribute() != null;
    }

    public final NamedNodeMap DOMElement.getAttributes() {
        return new AttributesNamedNodeMap(this);
    }
    
    public final Attr DOMElement.getAttributeNode(String name) {
        return (DOMAttribute)coreGetAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
    }

    public final Attr DOMElement.getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return (DOMAttribute)coreGetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? null : localName);
        } else {
            return (DOMAttribute)coreGetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName);
        }
    }
    
    public final String DOMElement.getAttribute(String name) {
        Attr attr = getAttributeNode(name);
        return attr == null ? "" : attr.getValue();
    }

    public final String DOMElement.getAttributeNS(String namespaceURI, String localName) throws DOMException {
        Attr attr = getAttributeNodeNS(namespaceURI, localName);
        return attr == null ? "" : attr.getValue();
    }

    public final boolean DOMElement.hasAttribute(String name) {
        return getAttributeNode(name) != null;
    }

    public final boolean DOMElement.hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return getAttributeNodeNS(namespaceURI, localName) != null;
    }

    public final void DOMElement.setAttribute(String name, String value) throws DOMException {
        NSUtil.validateName(name);
        coreSetAttribute(DOM1AttributeMatcher.INSTANCE, null, name, null, value);
    }

    public final void DOMElement.setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = coreGetDocument().getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, NSUtil.getDeclaredPrefix(localName, prefix), null, value);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            coreSetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName, prefix, value);
        }
    }
    
    public final Attr DOMElement.setAttributeNode(Attr newAttr) throws DOMException {
        return setAttributeNodeNS(newAttr);
    }
    
    public final Attr DOMElement.setAttributeNodeNS(Attr _newAttr) throws DOMException {
        DOMAttribute newAttr = (DOMAttribute)_newAttr;
        if (newAttr.coreGetOwnerElement() == this) {
            // This means that the "new" attribute is already linked to the element
            // and replaces itself.
            return newAttr;
        } else {
            String namespaceURI;
            String name = newAttr.getLocalName();
            AttributeMatcher matcher;
            // TODO: ATTR_NSDECL case missing here
            if (name == null) {
                namespaceURI = null;
                name = newAttr.getName();
                matcher = DOM1AttributeMatcher.INSTANCE;
            } else {
                namespaceURI = newAttr.getNamespaceURI();
                matcher = DOM2AttributeMatcher.INSTANCE;
            }
            try {
                return (DOMAttribute)coreSetAttribute(matcher, namespaceURI, name, newAttr, Policies.ATTRIBUTE_MIGRATION_POLICY);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
    }

    public final Attr DOMElement.removeAttributeNode(Attr oldAttr) throws DOMException {
        DOMAttribute attr = (DOMAttribute)oldAttr;
        if (attr.coreGetOwnerElement() != this) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            attr.coreRemove();
        }
        return attr;
    }

    public final void DOMElement.removeAttribute(String name) throws DOMException {
        // Specs: "If no attribute with this name is found, this method has no effect."
        Attr attr = getAttributeNode(name);
        if (attr != null) {
            removeAttributeNode(attr);
        }
    }

    public final void DOMElement.removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        // Specs: "If no attribute with this local name and namespace URI is found, this method has no effect."
        Attr attr = getAttributeNodeNS(namespaceURI, localName);
        if (attr != null) {
            removeAttributeNode(attr);
        }
    }

    public final void DOMElement.setIdAttribute(String name, boolean isId) throws DOMException {
        CoreAttribute attr = coreGetAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
        if (attr == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)attr).coreSetType(isId ? "ID" : "CDATA");
        }
    }

    public final void DOMElement.setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        // Here, we assume that a namespace declaration can never be an ID attribute
        CoreAttribute attr = coreGetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName);
        if (attr == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)attr).coreSetType(isId ? "ID" : "CDATA");
        }
    }

    public final void DOMElement.setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        if (idAttr.getOwnerElement() != this) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)idAttr).coreSetType(isId ? "ID" : "CDATA");
        }
    }
}
