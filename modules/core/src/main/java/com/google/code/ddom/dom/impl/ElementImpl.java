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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

public abstract class ElementImpl extends ParentNodeImpl implements Element, ChildNode, BuilderTarget, OptimizedParentNode {
    private class Attributes implements NamedNodeMap {
        public int getLength() {
            int length = 0;
            AbstractAttrImpl attr = firstAttribute;
            while (attr != null) {
                attr = attr.internalGetNextAttribute();
                length++;
            }
            return length;
        }
        
        public Node item(int index) {
            // TODO: wrong result for negavite indexes
            AbstractAttrImpl attr = firstAttribute;
            for (int i=0; i<index && attr != null; i++) {
                attr = attr.internalGetNextAttribute();
            }
            return attr;
        }

        public Node getNamedItem(String name) {
            return getAttributeNode(name);
        }

        public Node getNamedItemNS(String namespaceURI, String localName) throws DOMException {
            return getAttributeNodeNS(namespaceURI, localName);
        }

        public Node setNamedItem(Node arg) throws DOMException {
            if (arg instanceof AttrImpl) {
                return setAttributeNode((AttrImpl)arg);
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
        }

        public Node setNamedItemNS(Node arg) throws DOMException {
            if (arg instanceof AttrImpl) {
                return setAttributeNodeNS((AttrImpl)arg);
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
        }

        public Node removeNamedItem(String name) throws DOMException {
            // TODO: try to merge with corresponding method in ElementImpl
            Attr attr = getAttributeNode(name);
            if (attr != null) {
                removeAttributeNode(attr);
                return attr;
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            }
        }

        public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
            // TODO: try to merge with corresponding method in ElementImpl
            Attr attr = getAttributeNodeNS(namespaceURI, localName);
            if (attr != null) {
                removeAttributeNode(attr);
                return attr;
            } else {
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
            }
        }
    }
    
    private static final int ATTR_DOM1 = 1;
    private static final int ATTR_DOM2 = 2;
    private static final int ATTR_NSDECL = 3;
    
    private final DocumentImpl document;
    private Object content;
    private boolean complete;
    private int children;
    private ParentNode parent;
    private ChildNode nextSibling;
    private AbstractAttrImpl firstAttribute;

    public ElementImpl(DocumentImpl document, boolean complete) {
        this.document = document;
        this.complete = complete;
    }

    public final void internalSetParent(ParentNode parent) {
        this.parent = parent;
    }
    
    public final ChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(ChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final boolean isComplete() {
        return complete;
    }
    
    public final void build() {
        BuilderTargetHelper.build(this);
    }
    
    public final void internalSetComplete() {
        complete = true;
    }
    
    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(ChildNode newChild) {
        // All node type are allowed
    }

    public final int getLength() {
        build();
        return children;
    }

    public final Object getContent() {
        return content;
    }
    
    public final void internalSetFirstChild(ChildNode child) {
        content = child;
    }

    public final ChildNode getFirstChild() {
        if (content == null && !complete) {
            document.next();
        }
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    final AbstractAttrImpl internalGetFirstAttribute() {
        return firstAttribute;
    }
    
    public final void internalSetFirstAttribute(AbstractAttrImpl attr) {
        firstAttribute = attr;
    }

    public final DocumentImpl getDocument() {
        return document;
    }

    public final Document getOwnerDocument() {
        return document;
    }

    public final ParentNode getParentNode() {
        return parent;
    }
    
    public final ChildNode getNextSibling() {
        return ChildNodeHelper.getNextSibling(this);
    }

    public final ChildNode getPreviousSibling() {
        return ChildNodeHelper.getPreviousSibling(this);
    }

    public final short getNodeType() {
        return ELEMENT_NODE;
    }

    public final String getNodeName() {
        return getTagName();
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final NamedNodeMap getAttributes() {
        return new Attributes();
    }
    
    private boolean testAttribute(Attr attr, String namespaceURI, String localName, int mode) {
        switch (mode) {
            case ATTR_DOM1:
            	// Note: a lookup using DOM 1 methods may return any kind of attribute, including NSDecl
                return localName.equals(attr.getName());
            case ATTR_DOM2:
                return attr instanceof AttrImpl
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
    
    public final Attr getAttributeNode(String name) {
        return getAttributeNode(null, name, ATTR_DOM1);
    }

    public final Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return getAttributeNode(null, localName.equals(XMLConstants.XMLNS_ATTRIBUTE) ? null : localName, ATTR_NSDECL);
        } else {
            return getAttributeNode(namespaceURI, localName, ATTR_DOM2);
        }
    }
    
    private Attr getAttributeNode(String namespaceURI, String localName, int mode) throws DOMException {
        AbstractAttrImpl attr = firstAttribute;
        while (attr != null && !testAttribute(attr, namespaceURI, localName, mode)) {
            attr = attr.internalGetNextAttribute();
        }
        return attr;
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

    public final boolean hasAttributes() {
        return firstAttribute != null;
    }

    public final void setAttribute(String name, String value) throws DOMException {
        NSUtil.validateName(name);
        setAttribute(null, name, null, ATTR_DOM1, value);
    }

    public final void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
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
    
    private void setAttribute(String namespaceURI, String localName, String prefix, int mode, String value) throws DOMException {
        AbstractAttrImpl attr = firstAttribute;
        AbstractAttrImpl previousAttr = null;
        while (attr != null && !testAttribute(attr, namespaceURI, localName, mode)) {
            previousAttr = attr;
            attr = attr.internalGetNextAttribute();
        }
        if (attr == null) {
            DocumentImpl document = getDocument();
            NodeFactory factory = document.getNodeFactory();
            AbstractAttrImpl newAttr;
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
                firstAttribute = newAttr;
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

    public final Attr setAttributeNode(Attr newAttr) throws DOMException {
        return setAttributeNodeNS(newAttr);
    }
    
    public final Attr setAttributeNodeNS(Attr _newAttr) throws DOMException {
        validateOwnerDocument(_newAttr);
        AbstractAttrImpl newAttr = (AbstractAttrImpl)_newAttr;
        ElementImpl owner = newAttr.getOwnerElement();
        if (owner == this) {
            // This means that the "new" attribute is already linked to the element
            // and replaces itself.
            return newAttr;
        } else if (owner != null) {
            throw DOMExceptionUtil.newDOMException(DOMException.INUSE_ATTRIBUTE_ERR);
        } else {
            AbstractAttrImpl existingAttr = firstAttribute;
            AbstractAttrImpl previousAttr = null;
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
                existingAttr = existingAttr.internalGetNextAttribute();
            }
            newAttr.internalSetOwnerElement(this);
            if (existingAttr == null) {
                if (previousAttr == null) {
                    firstAttribute = newAttr;
                } else {
                    previousAttr.internalSetNextAttribute(newAttr);
                }
                return null;
            } else {
                if (previousAttr == null) {
                    firstAttribute = newAttr;
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

    public final Attr removeAttributeNode(Attr _oldAttr) throws DOMException {
        AbstractAttrImpl oldAttr = (AbstractAttrImpl)_oldAttr;
        if (oldAttr.getOwnerElement() == this) {
            AbstractAttrImpl previousAttr = firstAttribute;
            while (previousAttr != null) {
                AbstractAttrImpl nextAttr = previousAttr.internalGetNextAttribute();
                if (nextAttr == oldAttr) {
                    break;
                }
                previousAttr = nextAttr;
            }
            oldAttr.internalSetOwnerElement(null);
            if (previousAttr == null) {
                firstAttribute = oldAttr.internalGetNextAttribute();
            } else {
                previousAttr.internalSetNextAttribute(oldAttr.internalGetNextAttribute());
            }
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
        return oldAttr;
    }

    public final void removeAttribute(String name) throws DOMException {
        removeAttributeNode(getAttributeNode(name));
    }

    public final void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        removeAttributeNode(getAttributeNodeNS(namespaceURI, localName));
    }

    public final Node cloneNode(boolean deep) {
        return deep ? deepClone() : shallowClone();
    }

    @Override
    protected final Node shallowClone() {
        ElementImpl clone = shallowCloneWithoutAttributes();
        AbstractAttrImpl attr = firstAttribute;
        while (attr != null) {
            // TODO: this could be optimized
            clone.setAttributeNode((AttrImpl)attr.cloneNode(false));
            attr = attr.internalGetNextAttribute();
        }
        return clone;
    }
    
    protected abstract ElementImpl shallowCloneWithoutAttributes();

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setIdAttribute(String name, boolean isId) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setIdAttributeNS(String namespaceURI, String localName, boolean isId)
            throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String lookupNamespaceURI(String prefix) {
        for (AbstractAttrImpl attr = firstAttribute; attr != null; attr = attr.internalGetNextAttribute()) {
            if (attr instanceof NSDecl) {
                NSDecl decl = (NSDecl)attr;
                if (decl.getDeclaredPrefix().equals(prefix)) {
                    return decl.getDeclaredNamespaceURI();
                }
            }
        }
        return parent == null ? null : parent.lookupNamespaceURI(prefix);
    }

    public final String lookupPrefix(String namespaceURI) {
        // TODO: this is not entirely correct because the namespace declaration for this prefix may be hidden by a namespace declaration in a nested scope; need to check if this is covered by the DOM3 test suite
        for (AbstractAttrImpl attr = firstAttribute; attr != null; attr = attr.internalGetNextAttribute()) {
            if (attr instanceof NSDecl) {
                NSDecl decl = (NSDecl)attr;
                if (decl.getDeclaredNamespaceURI().equals(namespaceURI)) {
                    return decl.getDeclaredPrefix();
                }
            }
        }
        return parent == null ? null : parent.lookupPrefix(namespaceURI);
    }
}
