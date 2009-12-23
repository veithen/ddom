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

import com.google.code.ddom.spi.model.AttributeMatcher;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.CoreNode;
import com.google.code.ddom.spi.model.CoreParentNode;
import com.google.code.ddom.spi.model.NodeFactory;

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
    
    public final CoreAttribute coreGetFirstAttribute() {
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
            attribute = attribute.coreGetNextAttribute();
        }
        return previousAttribute;
    }

    public final CoreAttribute coreGetAttribute(AttributeMatcher matcher, String namespaceURI, String name) {
        CoreAttribute attr = firstAttribute;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            attr = attr.coreGetNextAttribute();
        }
        return attr;
    }

    public final void coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, String prefix, String value) {
        CoreAttribute attr = firstAttribute;
        CoreAttribute previousAttr = null;
        while (attr != null && !matcher.matches(attr, namespaceURI, name)) {
            previousAttr = attr;
            attr = attr.coreGetNextAttribute();
        }
        if (attr == null) {
            CoreDocument document = getDocument();
            NodeFactory factory = document.getNodeFactory();
            CoreAttribute newAttr = matcher.createAttribute(factory, document, namespaceURI, name, prefix, value);
            if (previousAttr == null) {
                coreAppendAttribute(newAttr);
            } else {
                previousAttr.coreInsertAttributeAfter(newAttr);
            }
        } else {
            matcher.update(attr, prefix, value);
        }
    }

    public final CoreAttribute coreSetAttribute(AttributeMatcher matcher, String namespaceURI, String name, CoreAttribute attr) {
        CoreAttribute existingAttr = firstAttribute;
        CoreAttribute previousAttr = null;
        while (existingAttr != null && !matcher.matches(existingAttr, namespaceURI, name)) {
            previousAttr = existingAttr;
            existingAttr = existingAttr.coreGetNextAttribute();
        }
        attr.internalSetOwnerElement(this);
        if (existingAttr == null) {
            if (previousAttr == null) {
                internalSetFirstAttribute(attr);
            } else {
                previousAttr.internalSetNextAttribute(attr);
            }
            return null;
        } else {
            if (previousAttr == null) {
                internalSetFirstAttribute(attr);
            } else {
                previousAttr.internalSetNextAttribute(attr);
            }
            existingAttr.internalSetOwnerElement(null);
            attr.internalSetNextAttribute(existingAttr.coreGetNextAttribute());
            existingAttr.internalSetNextAttribute(null);
            return existingAttr;
        }
    }

    // TODO: check if we still need this as public method
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
