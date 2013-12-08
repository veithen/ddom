/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreCharacterData;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreEntityReference;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

public abstract class Attribute extends ParentNode implements CoreAttribute {
    /**
     * The owner of the attribute. This is either a {@link Document} if the attribute is not linked
     * to an element, or an {@link Element} if the attribute has been added to an element.
     */
    private Object owner;
    
    private Attribute nextAttribute;

    public Attribute(Document document, String value) {
        super(value);
        owner = document;
    }
    
    public Attribute(Document document, boolean complete) {
        super(complete ? STATE_EXPANDED : STATE_CHILDREN_PENDING);
        owner = document;
    }
    
    final void setNextAttribute(Attribute attr) {
        nextAttribute = attr;
    }
    
    final void setOwnerElement(CoreElement newOwner) {
        owner = newOwner;
    }
    
    final void unsetOwnerElement(CoreDocument newOwnerDocument) {
        owner = newOwnerDocument;
    }
    
    public final CoreAttribute coreGetNextAttribute() throws DeferredBuildingException {
        if (nextAttribute != null) {
            return nextAttribute;
        } else if (owner instanceof Element) {
            Element ownerElement = (Element)owner;
            if (ownerElement.internalGetState() == STATE_ATTRIBUTES_PENDING) {
                InputContext context = ownerElement.internalGetOrCreateInputContext();
                while (nextAttribute == null && ownerElement.internalGetState() == STATE_ATTRIBUTES_PENDING) {
                    context.next(false);
                }
            }
            return nextAttribute;
        } else {
            return null;
        }
    }

    final Attribute internalGetNextAttributeIfMaterialized() {
        return nextAttribute;
    }

    public final CoreAttribute coreGetPreviousAttribute() {
        if (owner instanceof Element) {
            Element ownerElement = (Element)owner;
            Attribute previousAttr = ownerElement.internalGetFirstAttributeIfMaterialized();
            while (previousAttr != null) {
                Attribute nextAttr = previousAttr.internalGetNextAttributeIfMaterialized();
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

    public final void internalNotifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    public final void internalNotifyChildrenCleared() {
        // Ignore this; we don't store the number of children
    }

    public final void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildNotAllowedException {
        if (!(newChild instanceof CoreCharacterData || newChild instanceof CoreEntityReference)) {
            throw new ChildNotAllowedException();
        }
    }

    public final int coreGetChildCount() throws DeferredBuildingException {
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

    public final boolean coreHasOwnerElement() {
        return owner instanceof CoreElement;
    }

    public final LLDocument internalGetOwnerDocument(boolean create) {
        if (owner == null) {
            if (create) {
                LLDocument document = new Document(ModelExtension.NULL);
                owner = document;
                return document;
            } else {
                return null;
            }
        } else if (owner instanceof Document) {
            return (Document)owner;
        } else {
            return ((Element)owner).internalGetOwnerDocument(create);
        }
    }

    final void insertAttributeAfter(Attribute attr) {
        // TODO: throw exception if attribute already has an owner
        attr.setOwnerElement(coreGetOwnerElement());
        if (nextAttribute != null) {
            attr.setNextAttribute(nextAttribute);
        }
        nextAttribute = attr;
    }

    public final boolean coreRemove() {
        return remove(false, null);
    }

    public final boolean coreRemove(CoreDocument document) {
        return remove(true, (Document)document);
    }

    private boolean remove(boolean newOwnerDocument, Document ownerDocument) {
        if (owner instanceof Element) {
            Element ownerElement = (Element)owner;
            Attribute previousAttr = (Attribute)coreGetPreviousAttribute();
            owner = newOwnerDocument ? ownerDocument : internalGetOwnerDocument(false); // TODO: create?
            if (previousAttr == null) {
                ownerElement.setFirstAttribute((Attribute)nextAttribute);
            } else {
                previousAttr.setNextAttribute(internalGetNextAttributeIfMaterialized());
            }
            return true;
        } else {
            if (newOwnerDocument) {
                owner = ownerDocument;
            }
            return false;
        }
    }

    public final void internalGenerateEndEvent(XmlHandler handler) throws StreamException {
        handler.endAttribute();
    }
}
