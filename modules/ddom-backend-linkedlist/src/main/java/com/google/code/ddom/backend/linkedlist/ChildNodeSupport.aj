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

    public final ChildNode ChildNode.internalGetNextSiblingIfMaterialized() {
        return nextSibling;
    }

    public final void ChildNode.internalSetNextSibling(ChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final ChildNode ChildNode.internalGetNextSibling() throws DeferredParsingException {
        if (parent == null) {
            return null;
        } else {
            if (nextSibling == null && !parent.coreIsComplete()) {
                Builder builder = internalGetDocument().getBuilderFor(parent);
                do {
                    builder.next();
                } while (nextSibling == null && !parent.coreIsComplete());
            }
            return nextSibling;
        }
    }
    
    public final CoreChildNode ChildNode.coreGetNextSibling() throws DeferredParsingException {
        return internalGetNextSibling();
    }
    
    public final ChildNode ChildNode.internalGetPreviousSibling() {
        if (parent == null) {
            return null;
        } else {
            ChildNode previousSibling = null;
            ChildNode sibling = parent.internalGetFirstChild();
            while (sibling != this) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSiblingIfMaterialized();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public final CoreChildNode ChildNode.coreGetPreviousSibling() {
        return internalGetPreviousSibling();
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
    
    public final void ChildNode.coreInsertSiblingsAfter(CoreDocumentFragment coreFragment) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        if (parent == null) {
            throw new NoParentException();
        } else {
            // TODO: we need to validate the children types; note that this is especially tricky if the children will be added later during deferred parsing
            validateOwnerDocument(fragment);
            if (parent.coreIsComplete() && nextSibling == null && !coreFragment.coreIsComplete()) {
                // This is a special case: we don't need to build the fragment, but only to move
                // the already materialized children and then to migrate the builder. This is
                // possible because we have a builder of type 2.
                internalGetDocument().migrateBuilder(fragment, parent);
                ChildNode sibling = fragment.internalGetFirstChild();
                nextSibling = sibling;
                int siblingCount = 0;
                while (sibling != null) {
                    sibling.internalSetParent(parent);
                    sibling = sibling.internalGetNextSiblingIfMaterialized();
                    siblingCount++;
                }
                fragment.internalSetFirstChild(null);
                fragment.notifyChildrenCleared();
                parent.notifyChildrenModified(siblingCount);
            } else {
                // TODO
            }
        }
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
            while (node != this) {
                previousSibling = node;
                node = node.internalGetNextSiblingIfMaterialized();
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
    
    public final void ChildNode.coreInsertSiblingsBefore(CoreDocumentFragment coreFragment) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        if (parent == null) {
            throw new NoParentException();
        } else {
            // TODO: handle empty fragment?
            fragment.coreBuild();
            ChildNode node = (ChildNode)fragment.coreGetFirstChild();
            ChildNode previousSibling = internalGetPreviousSibling();
            if (previousSibling == null) {
                parent.internalSetFirstChild(node);
            } else {
                previousSibling.internalSetNextSibling(node);
            }
            int nodeCount = 0;
            ChildNode previousNode;
            do {
                node.internalSetParent(parent);
                nodeCount++;
                previousNode = node;
                node = node.internalGetNextSibling();
            } while (node != null);
            previousNode.internalSetNextSibling(this);
            fragment.internalSetFirstChild(null);
            fragment.notifyChildrenCleared();
            parent.notifyChildrenModified(nodeCount);
        }
    }
    
    public final void ChildNode.coreDetach() throws DeferredParsingException {
        if (parent != null) {
            ChildNode previousSibling = internalGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            if (previousSibling == null) {
                parent.internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            nextSibling = null;
            parent.notifyChildrenModified(-1);
            parent = null;
        }
    }
    
    public final void ChildNode.coreReplaceWith(CoreChildNode newNode) throws CoreModelException {
        parent.coreReplaceChild(newNode, this);
    }
    
    public final void ChildNode.coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException {
        parent.coreReplaceChild(newNodes, this);
    }
}
