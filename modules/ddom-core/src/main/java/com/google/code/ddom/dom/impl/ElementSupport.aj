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

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.CoreNode;
import com.google.code.ddom.spi.model.CoreTypedAttribute;
import com.google.code.ddom.spi.model.NodeFactory;

public aspect ElementSupport {
    declare parents: ElementImpl implements DOMElement;
    
    private static final int ATTR_DOM1 = 1;
    private static final int ATTR_DOM2 = 2;
    private static final int ATTR_NSDECL = 3;

    private boolean ElementImpl.testAttribute(DOMAttribute attr, String namespaceURI, String localName, int mode) {
        switch (mode) {
            case ATTR_DOM1:
                // Note: a lookup using DOM 1 methods may return any kind of attribute, including NSDecl
                return localName.equals(attr.getName());
            case ATTR_DOM2:
                return attr instanceof CoreTypedAttribute
                        && (namespaceURI == null && attr.getNamespaceURI() == null
                                || namespaceURI != null && namespaceURI.equals(attr.getNamespaceURI()))
                        && localName.equals(attr.getLocalName());
            case ATTR_NSDECL:
                if (attr instanceof NSDecl) {
                    String prefix = ((NSDecl)attr).getDeclaredPrefix();
                    return localName == null && prefix == null || localName != null && localName.equals(prefix);
                } else {
                    return false;
                }
            default:
                throw new IllegalArgumentException();
        }
    }
    
    public final Attr ElementImpl.getAttributeNode(String name) {
        return getAttributeNode(null, name, ATTR_DOM1);
    }

    public final Attr ElementImpl.getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return getAttributeNode(null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? null : localName, ATTR_NSDECL);
        } else {
            return getAttributeNode(namespaceURI, localName, ATTR_DOM2);
        }
    }
    
    private DOMAttribute ElementImpl.getAttributeNode(String namespaceURI, String localName, int mode) throws DOMException {
        DOMAttribute attr = (DOMAttribute)internalGetFirstAttribute();
        while (attr != null && !testAttribute(attr, namespaceURI, localName, mode)) {
            attr = (DOMAttribute)attr.internalGetNextAttribute();
        }
        return attr;
    }

    public final String ElementImpl.getAttribute(String name) {
        Attr attr = getAttributeNode(name);
        return attr == null ? "" : attr.getValue();
    }

    public final String ElementImpl.getAttributeNS(String namespaceURI, String localName) throws DOMException {
        Attr attr = getAttributeNodeNS(namespaceURI, localName);
        return attr == null ? "" : attr.getValue();
    }

    public final boolean ElementImpl.hasAttribute(String name) {
        return getAttributeNode(name) != null;
    }

    public final boolean ElementImpl.hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return getAttributeNodeNS(namespaceURI, localName) != null;
    }

    public final void ElementImpl.setAttribute(String name, String value) throws DOMException {
        NSUtil.validateName(name);
        setAttribute(null, name, null, ATTR_DOM1, value);
    }

    public final void ElementImpl.setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
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
            setAttribute(null, NSUtil.getDeclaredPrefix(localName, prefix), null, ATTR_NSDECL, value);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            setAttribute(namespaceURI, localName, prefix, ATTR_DOM2, value);
        }
    }
    
    private void ElementImpl.setAttribute(String namespaceURI, String localName, String prefix, int mode, String value) throws DOMException {
        DOMAttribute attr = (DOMAttribute)internalGetFirstAttribute();
        CoreAttribute previousAttr = null;
        while (attr != null && !testAttribute(attr, namespaceURI, localName, mode)) {
            previousAttr = attr;
            attr = (DOMAttribute)attr.internalGetNextAttribute();
        }
        if (attr == null) {
            CoreDocument document = getDocument();
            NodeFactory factory = document.getNodeFactory();
            CoreAttribute newAttr;
            switch (mode) {
                case ATTR_DOM1:
                    newAttr = factory.createAttribute(document, localName, value, null);
                    break;
                case ATTR_DOM2:
                    newAttr = factory.createAttribute(document, namespaceURI, localName, prefix, value, null);
                    break;
                case ATTR_NSDECL:
                    // TODO: documentation here (localName instead of prefix is not a mistake...)
                    newAttr = factory.createNSDecl(document, localName, value);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            newAttr.internalSetOwnerElement(this);
            if (previousAttr == null) {
                internalSetFirstAttribute(newAttr);
            } else {
                previousAttr.internalSetNextAttribute(newAttr);
            }
        } else {
            attr.setValue(value);
            if (mode == ATTR_DOM2) {
                attr.setPrefix(prefix);
            }
        }
    }

    public final Attr ElementImpl.setAttributeNode(Attr newAttr) throws DOMException {
        return setAttributeNodeNS(newAttr);
    }
    
    public final Attr ElementImpl.setAttributeNodeNS(Attr _newAttr) throws DOMException {
        validateOwnerDocument((CoreNode)_newAttr);
        DOMAttribute newAttr = (DOMAttribute)_newAttr;
        CoreElement owner = newAttr.coreGetOwnerElement();
        if (owner == this) {
            // This means that the "new" attribute is already linked to the element
            // and replaces itself.
            return newAttr;
        } else if (owner != null) {
            throw DOMExceptionUtil.newDOMException(DOMException.INUSE_ATTRIBUTE_ERR);
        } else {
            DOMAttribute existingAttr = (DOMAttribute)internalGetFirstAttribute();
            DOMAttribute previousAttr = null;
            String localName = newAttr.getLocalName();
            String namespaceURI;
            int mode;
            // TODO: ATTR_NSDECL case missing here
            if (localName == null) {
                namespaceURI = null;
                localName = newAttr.getName();
                mode = ATTR_DOM1;
            } else {
                namespaceURI = newAttr.getNamespaceURI();
                mode = ATTR_DOM2;
            }
            while (existingAttr != null && !testAttribute(existingAttr, namespaceURI, localName, mode)) {
                previousAttr = existingAttr;
                existingAttr = (DOMAttribute)existingAttr.internalGetNextAttribute();
            }
            newAttr.internalSetOwnerElement(this);
            if (existingAttr == null) {
                if (previousAttr == null) {
                    internalSetFirstAttribute(newAttr);
                } else {
                    previousAttr.internalSetNextAttribute(newAttr);
                }
                return null;
            } else {
                if (previousAttr == null) {
                    internalSetFirstAttribute(newAttr);
                } else {
                    previousAttr.internalSetNextAttribute(newAttr);
                }
                existingAttr.internalSetOwnerElement(null);
                newAttr.internalSetNextAttribute(existingAttr.internalGetNextAttribute());
                existingAttr.internalSetNextAttribute(null);
                return existingAttr;
            }
        }
    }

    public final Attr ElementImpl.removeAttributeNode(Attr _oldAttr) throws DOMException {
        DOMAttribute oldAttr = (DOMAttribute)_oldAttr;
        if (oldAttr.getOwnerElement() == this) {
            CoreAttribute previousAttr = internalGetFirstAttribute();
            while (previousAttr != null) {
                CoreAttribute nextAttr = previousAttr.internalGetNextAttribute();
                if (nextAttr == oldAttr) {
                    break;
                }
                previousAttr = nextAttr;
            }
            oldAttr.internalSetOwnerElement(null);
            if (previousAttr == null) {
                internalSetFirstAttribute(oldAttr.internalGetNextAttribute());
            } else {
                previousAttr.internalSetNextAttribute(oldAttr.internalGetNextAttribute());
            }
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
        return oldAttr;
    }

    public final void ElementImpl.removeAttribute(String name) throws DOMException {
        removeAttributeNode(getAttributeNode(name));
    }

    public final void ElementImpl.removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        removeAttributeNode(getAttributeNodeNS(namespaceURI, localName));
    }

    public final void ElementImpl.setIdAttribute(String name, boolean isId) throws DOMException {
        CoreAttribute attr = getAttributeNode(null, name, ATTR_DOM1);
        if (attr == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)attr).setType(isId ? "ID" : "CDATA");
        }
    }

    public final void ElementImpl.setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        // Here, we assume that a namespace declaration can never be an ID attribute
        CoreAttribute attr = getAttributeNode(namespaceURI, localName, ATTR_DOM2);
        if (attr == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)attr).setType(isId ? "ID" : "CDATA");
        }
    }

    public final void ElementImpl.setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
