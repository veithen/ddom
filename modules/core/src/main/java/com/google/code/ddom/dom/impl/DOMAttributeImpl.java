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

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.google.code.ddom.dom.model.ChildNode;
import com.google.code.ddom.dom.model.DOMAttribute;
import com.google.code.ddom.dom.model.DOMDocument;
import com.google.code.ddom.dom.model.DOMElement;
import com.google.code.ddom.dom.model.DOMEntityReference;
import com.google.code.ddom.dom.model.DOMText;

public abstract class DOMAttributeImpl extends ParentNodeImpl implements DOMAttribute {
    /**
     * The owner of the attribute. This is either a {@link DOMDocument} if the attribute is not linked
     * to an element, or an {@link DOMElement} if the attribute has been added to an element.
     */
    private Object owner;
    
    private Object value;
    private DOMAttribute nextAttribute;

    public DOMAttributeImpl(DOMDocument document, String value) {
        owner = document;
        this.value = value;
    }
    
    public final void internalSetNextAttribute(DOMAttribute attr) {
        nextAttribute = attr;
    }
    
    public final void internalSetOwnerElement(DOMElement newOwner) {
        if (newOwner == null) {
            // TODO: owner could already be a document!
            owner = ((DOMElement)owner).getOwnerDocument();
        } else {
            owner = newOwner;
        }
    }
    
    public final DOMAttribute internalGetNextAttribute() {
        return nextAttribute;
    }

    public final void internalSetFirstChild(ChildNode child) {
        value = child;
    }

    public final void notifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    @Override
    protected final void validateChildType(ChildNode newChild) {
        if (!(newChild instanceof DOMText || newChild instanceof DOMEntityReference)) {
            throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
        }
    }

    public final Object getContent() {
        return value;
    }

    public final int getLength() {
        if (value instanceof String) {
            return 1;
        } else {
            int length = 0;
            for (ChildNode child = (ChildNode)value; child != null; child = child.getNextSibling()) {
                length++;
            }
            return length;
        }
    }

    public final ChildNode getFirstChild() {
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    public final DOMElement getOwnerElement() {
        return owner instanceof DOMElement ? (DOMElement)owner : null;
    }

    public final DOMDocument getDocument() {
        if (owner instanceof DOMDocument) {
            return (DOMDocument)owner;
        } else {
            return ((DOMElement)owner).getDocument();
        }
    }

    public final Document getOwnerDocument() {
        return getDocument();
    }
    
    public final Node getParentNode() {
        return null;
    }

    public final String getValue() {
        // TODO: this should also be applicable for other OptimizedParentNodes
        if (value instanceof String) {
            return (String)value;
        } else {
            return getTextContent();
        }
    }

    public final void setValue(String value) throws DOMException {
        // TODO: what if arg is null?
        this.value = value;
    }

    public final String getNodeValue() throws DOMException {
        return getValue();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }
    
    public final short getNodeType() {
        return ATTRIBUTE_NODE;
    }

    public final String getNodeName() {
        return getName();
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }

    public final boolean getSpecified() {
        // TODO
        return true;
    }

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String lookupNamespaceURI(String prefix) {
        // TODO: needs to be checked
        return null;
    }

    public final String lookupPrefix(String namespaceURI) {
        // TODO: needs to be checked
        return null;
    }
}
