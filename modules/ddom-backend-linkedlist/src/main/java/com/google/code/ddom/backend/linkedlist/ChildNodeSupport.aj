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

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.NoParentException;
import com.google.code.ddom.backend.SelfRelationshipException;

public aspect ChildNodeSupport {
    private ParentNode ChildNode.parent;
    
    public final ParentNode ChildNode.internalGetParent() {
        return parent;
    }
    
    public final void ChildNode.internalSetParent(ParentNode parent) {
        this.parent = parent;
    }
    
    public final CoreParentNode ChildNode.coreGetParent() {
        return parent;
    }

    public final boolean ChildNode.coreHasParent() {
        return parent != null;
    }

    public final CoreElement ChildNode.coreGetParentElement() {
        return parent instanceof CoreElement ? (CoreElement)parent : null;
    }

    public final CoreChildNode ChildNode.coreGetNextSibling() throws DeferredParsingException {
        if (parent == null) {
            return null;
        } else {
            // TODO: try to avoid the cast here
            Document document = internalGetDocument();
            CoreChildNode nextSibling = internalGetNextSibling();
            if (nextSibling == null && !parent.coreIsComplete()) {
                Builder builder = document.getBuilderFor(parent);
                do {
                    builder.next();
                    nextSibling = internalGetNextSibling();
                } while (nextSibling == null && !parent.coreIsComplete());
            }
            return nextSibling;
        }
    }
    
    public final CoreChildNode ChildNode.coreGetPreviousSibling() throws DeferredParsingException {
        if (parent == null) {
            return null;
        } else {
            CoreChildNode previousSibling = null;
            CoreChildNode sibling = parent.coreGetFirstChild();
            while (sibling != null && sibling != this) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSibling();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public final void ChildNode.coreInsertSiblingAfter(CoreChildNode sibling_) throws CoreModelException {
        ChildNode sibling = (ChildNode)sibling_;
        if (sibling == this) {
            throw new SelfRelationshipException();
        }
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.validateChildType(sibling, null);
            parent.prepareNewChild(sibling);
            sibling.coreDetach();
            sibling.internalSetNextSibling(coreGetNextSibling());
            internalSetNextSibling(sibling);
            sibling.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
    }
    
    public final void ChildNode.coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws CoreModelException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final void ChildNode.coreInsertSiblingBefore(CoreChildNode sibling) throws CoreModelException {
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.coreInsertChildBefore(sibling, this);
        }
    }
    
    public final void ChildNode.coreInsertSiblingsBefore(CoreDocumentFragment fragment) throws CoreModelException {
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.coreInsertChildBefore(fragment, this);
        }
    }
    
    public final void ChildNode.coreDetach() throws DeferredParsingException {
        if (parent != null) {
            CoreChildNode previousSibling = coreGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            CoreChildNode nextSibling = internalGetNextSibling();
            internalSetNextSibling(null);
            if (previousSibling == null) {
                parent.internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            parent.notifyChildrenModified(-1);
            parent = null;
        }
    }
}
