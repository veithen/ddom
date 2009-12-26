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

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreTypedAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.support.DOM1AttributeMatcher;
import com.google.code.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.DOMNamespaceDeclarationMatcher;
import com.google.code.ddom.frontend.dom.support.NSUtil;

public aspect ElementSupport {
    public final Attr DOMElement.getAttributeNode(String name) {
        return (DOMAttribute)coreGetAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
    }

    public final Attr DOMElement.getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return (DOMAttribute)coreGetAttribute(DOMNamespaceDeclarationMatcher.INSTANCE, null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? null : localName);
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
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            coreSetAttribute(DOMNamespaceDeclarationMatcher.INSTANCE, null, NSUtil.getDeclaredPrefix(localName, prefix), null, value);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            coreSetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName, prefix, value);
        }
    }
    
    public final Attr DOMElement.setAttributeNode(Attr newAttr) throws DOMException {
        return setAttributeNodeNS(newAttr);
    }
    
    public final Attr DOMElement.setAttributeNodeNS(Attr _newAttr) throws DOMException {
        try {
            // TODO: shouldn't this be handled by the core model?
            validateOwnerDocument((CoreNode)_newAttr);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        DOMAttribute newAttr = (DOMAttribute)_newAttr;
        CoreElement owner = newAttr.coreGetOwnerElement();
        if (owner == this) {
            // This means that the "new" attribute is already linked to the element
            // and replaces itself.
            return newAttr;
        } else if (owner != null) {
            throw DOMExceptionUtil.newDOMException(DOMException.INUSE_ATTRIBUTE_ERR);
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
            return (DOMAttribute)coreSetAttribute(matcher, namespaceURI, name, newAttr);
        }
    }

    public final Attr DOMElement.removeAttributeNode(Attr _oldAttr) throws DOMException {
        DOMAttribute oldAttr = (DOMAttribute)_oldAttr;
        if (oldAttr.getOwnerElement() == this) {
            CoreAttribute previousAttr = coreGetFirstAttribute();
            while (previousAttr != null) {
                CoreAttribute nextAttr = previousAttr.coreGetNextAttribute();
                if (nextAttr == oldAttr) {
                    break;
                }
                previousAttr = nextAttr;
            }
            oldAttr.internalSetOwnerElement(null);
            if (previousAttr == null) {
                internalSetFirstAttribute(oldAttr.coreGetNextAttribute());
            } else {
                previousAttr.internalSetNextAttribute(oldAttr.coreGetNextAttribute());
            }
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
        return oldAttr;
    }

    public final void DOMElement.removeAttribute(String name) throws DOMException {
        removeAttributeNode(getAttributeNode(name));
    }

    public final void DOMElement.removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        removeAttributeNode(getAttributeNodeNS(namespaceURI, localName));
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
        // TODO
        throw new UnsupportedOperationException();
    }
}
