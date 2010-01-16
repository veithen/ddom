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
    private ChildNode ChildNode.nextSibling;
    
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

    public final ChildNode ChildNode.internalGetNextSibling() {
        return nextSibling;
    }

    public final void ChildNode.internalSetNextSibling(ChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final CoreChildNode ChildNode.coreGetNextSibling() throws DeferredParsingException {
        if (parent == null) {
            return null;
        } else {
            // TODO: try to avoid the cast here
            Document document = internalGetDocument();
            if (nextSibling == null && !parent.coreIsComplete()) {
                Builder builder = document.getBuilderFor(parent);
                do {
                    builder.next();
                } while (nextSibling == null && !parent.coreIsComplete());
            }
            return nextSibling;
        }
    }
    
    public final CoreChildNode ChildNode.coreGetPreviousSibling() {
        if (parent == null) {
            return null;
        } else {
            ChildNode previousSibling = null;
            ChildNode sibling = parent.internalGetFirstChild();
            while (sibling != null && sibling != this) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSibling();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public final void ChildNode.coreInsertSiblingAfter(CoreChildNode coreSibling) throws CoreModelException {
        ChildNode sibling = (ChildNode)coreSibling;
        if (sibling == this) {
            throw new SelfRelationshipException();
        }
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.validateChildType(sibling, null);
            parent.prepareNewChild(sibling);
            sibling.coreDetach();
            // Note: since we have a builder of type 2, we don't need to materialize the next sibling
            // and we can use nextSibling instead of coreGetNextSibling()
            sibling.internalSetNextSibling(nextSibling);
            nextSibling = sibling;
            sibling.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
    }
    
    public final void ChildNode.coreInsertSiblingsAfter(CoreDocumentFragment fragment) throws CoreModelException {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final void ChildNode.coreInsertSiblingBefore(CoreChildNode coreSibling) throws CoreModelException {
        ChildNode sibling = (ChildNode)coreSibling;
        if (sibling == this) {
            throw new SelfRelationshipException();
        }
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.validateChildType(sibling, null);
            parent.prepareNewChild(sibling);
            sibling.coreDetach();
            ChildNode previousSibling = null;
            ChildNode node = parent.internalGetFirstChild();
            while (node != null && node != this) {
                previousSibling = node;
                node = node.internalGetNextSibling();
            }
            sibling.internalSetNextSibling(this);
            if (previousSibling == null) {
                parent.internalSetFirstChild(sibling);
            } else {
                previousSibling.internalSetNextSibling(sibling);
            }
            sibling.internalSetParent(parent);
            parent.notifyChildrenModified(1);
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
            if (previousSibling == null) {
                parent.internalSetFirstChild(nextSibling);
            } else {
                // TODO: get rid of cast here
                ((ChildNode)previousSibling).internalSetNextSibling(nextSibling);
            }
            nextSibling = null;
            parent.notifyChildrenModified(-1);
            parent = null;
        }
    }
}
