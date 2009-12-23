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
package com.google.code.ddom.core.model;

import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreCharacterData;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.CoreEntityReference;
import com.google.code.ddom.spi.model.CoreText;
import com.google.code.ddom.spi.model.HierarchyException;

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
            owner = ((CoreElement)owner).getDocument();
        } else {
            owner = newOwner;
        }
    }
    
    public final CoreAttribute coreGetNextAttribute() {
        return nextAttribute;
    }

    public final void internalSetFirstChild(CoreChildNode child) {
        value = child;
    }

    public final void notifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    @Override
    protected final void validateChildType(CoreChildNode newChild) throws HierarchyException {
        if (!(newChild instanceof CoreText || newChild instanceof CoreEntityReference)) {
            throw new HierarchyException();
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
            for (CoreChildNode child = (CoreChildNode)value; child != null; child = child.coreGetNextSibling()) {
                length++;
            }
            return length;
        }
    }

    public final CoreChildNode coreGetFirstChild() {
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    public final CoreElement coreGetOwnerElement() {
        return owner instanceof CoreElement ? (CoreElement)owner : null;
    }

    public final CoreDocument getDocument() {
        if (owner instanceof CoreDocument) {
            return (CoreDocument)owner;
        } else {
            return ((CoreElement)owner).getDocument();
        }
    }

    public final String coreGetValue() {
        // TODO: this should also be applicable for other OptimizedParentNodes
        if (value instanceof String) {
            return (String)value;
        } else {
            // TODO: get the getTextContent feature back into the core model
            StringBuilder buffer = new StringBuilder();
            CoreChildNode child = (CoreChildNode)value;
            while (child != null) {
                buffer.append(((CoreCharacterData)child).coreGetData());
                child = child.coreGetNextSibling();
            }
            return buffer.toString();
//            return getTextContent();
        }
    }

    public final void coreSetValue(String value) {
        // TODO: what if arg is null?
        this.value = value;
    }

    public final void coreInsertAttributeAfter(CoreAttribute attr) {
        // TODO: throw exception if attribute already has an owner
        attr.internalSetOwnerElement(coreGetOwnerElement());
        if (nextAttribute != null) {
            attr.internalSetNextAttribute(nextAttribute);
        }
        nextAttribute = attr;
    }
}
