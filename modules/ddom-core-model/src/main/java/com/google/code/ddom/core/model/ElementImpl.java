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
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.CoreNode;
import com.google.code.ddom.spi.model.CoreParentNode;

public abstract class ElementImpl extends ParentNodeImpl implements CoreElement {
    private final CoreDocument document;
    private Object content;
    private boolean complete;
    private int children;
    private CoreParentNode parent;
    private CoreChildNode nextSibling;
    private CoreAttribute firstAttribute;

    public ElementImpl(CoreDocument document, boolean complete) {
        this.document = document;
        this.complete = complete;
    }

    public final void internalSetParent(CoreParentNode parent) {
        this.parent = parent;
    }
    
    public final CoreChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(CoreChildNode nextSibling) {
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
    protected void validateChildType(CoreChildNode newChild) {
        // All node type are allowed
    }

    public final int coreGetChildCount() {
        build();
        return children;
    }

    public final Object getContent() {
        return content;
    }
    
    public final void internalSetFirstChild(CoreChildNode child) {
        content = child;
    }

    public final void coreSetValue(String value) {
        // TODO: need to remove any existing children!
        this.content = value;
    }

    public final CoreChildNode coreGetFirstChild() {
        if (content == null && !complete) {
            document.next();
        }
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    public final CoreAttribute internalGetFirstAttribute() {
        return firstAttribute;
    }
    
    public final void internalSetFirstAttribute(CoreAttribute attr) {
        firstAttribute = attr;
    }

    public final CoreDocument getDocument() {
        return document;
    }

    public final CoreParentNode coreGetParent() {
        return parent;
    }
    
    public final CoreChildNode coreGetNextSibling() {
        return ChildNodeHelper.coreGetNextSibling(this);
    }

    public final CoreChildNode coreGetPreviousSibling() {
        return ChildNodeHelper.coreGetPreviousSibling(this);
    }

    public void coreInsertSiblingAfter(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingAfter(this, sibling);
    }

    public void coreInsertSiblingBefore(CoreNode sibling) throws CoreModelException {
        ChildNodeHelper.coreInsertSiblingBefore(this, sibling);
    }
    
    public void coreDetach() {
        ChildNodeHelper.coreDetach(this);
    }

    public CoreAttribute coreGetLastAttribute() {
        CoreAttribute previousAttribute = null;
        CoreAttribute attribute = firstAttribute;
        while (attribute != null) {
            previousAttribute = attribute;
            attribute = attribute.internalGetNextAttribute();
        }
        return previousAttribute;
    }

    public void coreAppendAttribute(CoreAttribute attr) {
        // TODO: throw exception if attribute already has an owner (see also coreInsertAttributeAfter)
        attr.internalSetOwnerElement(this);
        if (firstAttribute == null) {
            firstAttribute = attr;
        } else {
            coreGetLastAttribute().coreInsertAttributeAfter(attr);
        }
    }
}
