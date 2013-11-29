/*
 * Copyright 2009-2013 Andreas Veithen
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
package com.googlecode.ddom.frontend.dom.mixin;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.CoreTypedAttribute;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.AbortNormalizationException;
import com.googlecode.ddom.frontend.dom.intf.DOMAttribute;
import com.googlecode.ddom.frontend.dom.intf.DOMElement;
import com.googlecode.ddom.frontend.dom.intf.DOMParentNode;
import com.googlecode.ddom.frontend.dom.intf.NormalizationConfig;
import com.googlecode.ddom.frontend.dom.support.AttributesNamedNodeMap;
import com.googlecode.ddom.frontend.dom.support.DOM1AttributeMatcher;
import com.googlecode.ddom.frontend.dom.support.DOM2AttributeMatcher;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionUtil;
import com.googlecode.ddom.frontend.dom.support.NSUtil;
import com.googlecode.ddom.frontend.dom.support.Policies;
import com.googlecode.ddom.symbols.Symbols;

@Mixin(CoreElement.class)
public abstract class ElementSupport implements DOMElement {
    public final boolean hasAttributes() {
        try {
            return coreGetFirstAttribute() != null;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final NamedNodeMap getAttributes() {
        return new AttributesNamedNodeMap(this);
    }
    
    public final Attr getAttributeNode(String name) {
        try {
            return (DOMAttribute)coreGetAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        try {
            if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                return (DOMAttribute)coreGetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? "" : localName);
            } else {
                return (DOMAttribute)coreGetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI == null ? "" : namespaceURI, localName);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final String getAttribute(String name) {
        Attr attr = getAttributeNode(name);
        return attr == null ? "" : attr.getValue();
    }

    public final String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        Attr attr = getAttributeNodeNS(namespaceURI, localName);
        return attr == null ? "" : attr.getValue();
    }

    public final boolean hasAttribute(String name) {
        return getAttributeNode(name) != null;
    }

    public final boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        return getAttributeNodeNS(namespaceURI, localName) != null;
    }

    public final void setAttribute(String name, String value) throws DOMException {
        NSUtil.validateName(name);
        try {
            coreSetAttribute(DOM1AttributeMatcher.INSTANCE, null, name, null, value);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = coreGetOwnerDocument(true).getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        try {
            if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, NSUtil.getDeclaredPrefix(localName, prefix), null, value);
            } else {
                namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
                NSUtil.validateAttributeName(namespaceURI, localName, prefix);
                coreSetAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI, localName, prefix, value);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Attr setAttributeNode(Attr newAttr) throws DOMException {
        return setAttributeNodeNS(newAttr);
    }
    
    public final Attr setAttributeNodeNS(Attr _newAttr) throws DOMException {
        DOMAttribute newAttr = (DOMAttribute)_newAttr;
        if (newAttr.coreGetOwnerElement() == this) {
            // This means that the "new" attribute is already linked to the element
            // and replaces itself.
            return newAttr;
        } else {
            AttributeMatcher matcher;
            if (newAttr instanceof CoreNSAwareAttribute) {
                matcher = DOM2AttributeMatcher.INSTANCE;
            } else if (newAttr instanceof CoreNamespaceDeclaration) {
                matcher = AttributeMatcher.NAMESPACE_DECLARATION;
            } else {
                // Must be a DOM1 (namespace unaware) attribute
                matcher = DOM1AttributeMatcher.INSTANCE;
            }
            try {
                return (DOMAttribute)coreSetAttribute(matcher, newAttr, Policies.ATTRIBUTE_MIGRATION_POLICY, ReturnValue.REPLACED_ATTRIBUTE);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
        }
    }

    public final Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        DOMAttribute attr = (DOMAttribute)oldAttr;
        if (attr.coreGetOwnerElement() != this) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            attr.coreRemove();
        }
        return attr;
    }

    public final void removeAttribute(String name) throws DOMException {
        // Specs: "If no attribute with this name is found, this method has no effect."
        try {
            coreRemoveAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        // Specs: "If no attribute with this local name and namespace URI is found, this method has no effect."
        try {
            if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                coreRemoveAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? "" : localName);
            } else {
                coreRemoveAttribute(DOM2AttributeMatcher.INSTANCE, namespaceURI == null ? "" : namespaceURI, localName);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setIdAttribute(String name, boolean isId) throws DOMException {
        try {
            CoreAttribute attr = coreGetAttribute(DOM1AttributeMatcher.INSTANCE, null, name);
            if (attr == null) {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            } else {
                ((CoreTypedAttribute)attr).coreSetType(isId ? "ID" : "CDATA");
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        try {
            // Here, we assume that a namespace declaration can never be an ID attribute
            CoreAttribute attr = coreGetAttribute(DOM2AttributeMatcher.INSTANCE, NSUtil.normalizeNamespaceURI(namespaceURI), localName);
            if (attr == null) {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            } else {
                ((CoreTypedAttribute)attr).coreSetType(isId ? "ID" : "CDATA");
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        if (idAttr.getOwnerElement() != this) {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        } else {
            ((CoreTypedAttribute)idAttr).coreSetType(isId ? "ID" : "CDATA");
        }
    }

    public final DOMParentNode shallowClone() throws DeferredParsingException {
        DOMElement clone = shallowCloneWithoutAttributes();
        CoreAttribute attr = coreGetFirstAttribute();
        while (attr != null) {
            // TODO: this could be optimized
            clone.setAttributeNode((Attr)((Attr)attr).cloneNode(false));
            attr = attr.coreGetNextAttribute();
        }
        return clone;
    }
    
    public final String getTextContent() {
        try {
            return coreGetTextContent(Policies.GET_TEXT_CONTENT);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setTextContent(String textContent) {
        try {
            coreSetValue(textContent);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final short getNodeType() {
        return Node.ELEMENT_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getNodeName() {
        return getTagName();
    }

    public final CoreElement getNamespaceContext() {
        return this;
    }
    
    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        try {
            coreCoalesce(!config.isKeepCDATASections());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        normalizeChildren(config);
    }

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
