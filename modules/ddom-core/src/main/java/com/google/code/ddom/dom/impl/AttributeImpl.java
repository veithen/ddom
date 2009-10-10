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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.CoreEntityReference;
import com.google.code.ddom.spi.model.CoreText;

public abstract class AttributeImpl extends ParentNodeImpl implements CoreAttribute {
    /**
     * The owner of the attribute. This is either a {@link CoreDocument} if the attribute is not linked
     * to an element, or an {@link CoreElement} if the attribute has been added to an element.
     */
    private Object owner;
    
    private Object value;
    private CoreAttribute nextAttribute;

    public AttributeImpl(CoreDocument document, String value) {
        owner = document;
        this.value = value;
    }
    
    public final void internalSetNextAttribute(CoreAttribute attr) {
        nextAttribute = attr;
    }
    
    public final void internalSetOwnerElement(CoreElement newOwner) {
        if (newOwner == null) {
            // TODO: owner could already be a document!
            owner = ((CoreElement)owner).getOwnerDocument();
        } else {
            owner = newOwner;
        }
    }
    
    public final CoreAttribute internalGetNextAttribute() {
        return nextAttribute;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        value = child;
    }

    public final void notifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    @Override
    protected final void validateChildType(CoreChildNode newChild) {
        if (!(newChild instanceof CoreText || newChild instanceof CoreEntityReference)) {
            throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
        }
    }

    public final Object getContent() {
        return value;
    }

    public final int coreGetChildCount() {
        if (value instanceof String) {
            return 1;
        } else {
            int length = 0;
            for (CoreChildNode child = (CoreChildNode)value; child != null; child = child.getNextSibling()) {
                length++;
            }
            return length;
        }
    }

    public final CoreChildNode getFirstChild() {
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    public final CoreElement getOwnerElement() {
        return owner instanceof CoreElement ? (CoreElement)owner : null;
    }

    public final CoreDocument getDocument() {
        if (owner instanceof CoreDocument) {
            return (CoreDocument)owner;
        } else {
            return ((CoreElement)owner).getDocument();
        }
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

    public final boolean getSpecified() {
        // TODO
        return true;
    }

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
