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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreEntityReference;
import com.google.code.ddom.backend.CoreText;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;

@Implementation
public abstract class Attribute extends ParentNode implements CoreAttribute {
    /**
     * The owner of the attribute. This is either a {@link Document} if the attribute is not linked
     * to an element, or an {@link Element} if the attribute has been added to an element.
     */
    private Object owner;
    
    private CoreAttribute nextAttribute;

    public Attribute(Document document, String value) {
        super(value);
        owner = document;
    }
    
    public final void internalSetNextAttribute(CoreAttribute attr) {
        nextAttribute = attr;
    }
    
    public final void internalSetOwnerElement(CoreElement newOwner) {
        if (newOwner == null) {
            // TODO: owner could already be a document!
            owner = ((Element)owner).getDocument();
        } else {
            owner = newOwner;
        }
    }
    
    public final CoreAttribute coreGetNextAttribute() {
        return nextAttribute;
    }

    public final CoreAttribute coreGetPreviousAttribute() {
        if (owner instanceof Element) {
            Element ownerElement = (Element)owner;
            CoreAttribute previousAttr = ownerElement.coreGetFirstAttribute();
            while (previousAttr != null) {
                CoreAttribute nextAttr = previousAttr.coreGetNextAttribute();
                if (nextAttr == this) {
                    break;
                }
                previousAttr = nextAttr;
            }
            return previousAttr;
        } else {
            return null;
        }
    }

    public final void notifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    @Override
    protected final void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException {
        if (!(newChild instanceof CoreText || newChild instanceof CoreEntityReference)) {
            throw new ChildTypeNotAllowedException();
        }
    }

    public final int coreGetChildCount() throws DeferredParsingException {
        if (coreGetContent() instanceof String) {
            return 1;
        } else {
            int length = 0;
            for (CoreChildNode child = coreGetFirstChild(); child != null; child = child.coreGetNextSibling()) {
                length++;
            }
            return length;
        }
    }

    public final CoreElement coreGetOwnerElement() {
        return owner instanceof CoreElement ? (CoreElement)owner : null;
    }

    @Override
    final Document getDocument() {
        if (owner instanceof Document) {
            return (Document)owner;
        } else {
            return ((Element)owner).getDocument();
        }
    }

    public final void coreInsertAttributeAfter(CoreAttribute attr) {
        // TODO: throw exception if attribute already has an owner
        attr.internalSetOwnerElement(coreGetOwnerElement());
        if (nextAttribute != null) {
            attr.internalSetNextAttribute(nextAttribute);
        }
        nextAttribute = attr;
    }

    public final boolean coreRemove() {
        if (owner instanceof Element) {
            Element ownerElement = (Element)owner;
            CoreAttribute previousAttr = coreGetPreviousAttribute();
            internalSetOwnerElement(null);
            if (previousAttr == null) {
                ownerElement.setFirstAttribute(coreGetNextAttribute());
            } else {
                previousAttr.internalSetNextAttribute(coreGetNextAttribute());
            }
            return true;
        } else {
            return false;
        }
    }
}
