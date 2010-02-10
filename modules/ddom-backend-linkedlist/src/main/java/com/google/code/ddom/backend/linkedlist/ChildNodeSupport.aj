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
    private LLParentNode LLChildNode.parent;
    private LLChildNode LLChildNode.nextSibling;
    
    public final LLParentNode LLChildNode.internalGetParent() {
        return parent;
    }
    
    public final void LLChildNode.internalSetParent(LLParentNode parent) {
        this.parent = parent;
    }
    
    public final CoreParentNode LLChildNode.coreGetParent() {
        return parent;
    }

    public final boolean LLChildNode.coreHasParent() {
        return parent != null;
    }

    public final CoreElement LLChildNode.coreGetParentElement() {
        return parent instanceof CoreElement ? (CoreElement)parent : null;
    }

    public final LLChildNode LLChildNode.internalGetNextSiblingIfMaterialized() {
        return nextSibling;
    }

    public final void LLChildNode.internalSetNextSibling(LLChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final LLChildNode LLChildNode.internalGetNextSibling() throws DeferredParsingException {
        if (parent == null) {
            return null;
        } else {
            if (nextSibling == null && !parent.coreIsComplete()) {
                Builder builder = internalGetDocument().internalGetBuilderFor(parent);
                do {
                    builder.next();
                } while (nextSibling == null && !parent.coreIsComplete());
            }
            return nextSibling;
        }
    }
    
    public final CoreChildNode LLChildNode.coreGetNextSibling() throws DeferredParsingException {
        return internalGetNextSibling();
    }
    
    public final LLChildNode LLChildNode.internalGetPreviousSibling() {
        if (parent == null) {
            return null;
        } else {
            LLChildNode previousSibling = null;
            LLChildNode sibling = parent.internalGetFirstChildIfMaterialized();
            while (sibling != this) {
                previousSibling = sibling;
                sibling = sibling.internalGetNextSiblingIfMaterialized();
            }
            return sibling == null ? null : previousSibling;
        }
    }
    
    public final CoreChildNode LLChildNode.coreGetPreviousSibling() {
        return internalGetPreviousSibling();
    }
    
    public final void LLChildNode.coreInsertSiblingAfter(CoreChildNode coreSibling) throws CoreModelException {
        LLChildNode sibling = (LLChildNode)coreSibling;
        if (sibling == this) {
            throw new SelfRelationshipException();
        }
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.internalValidateChildType(sibling, null);
            parent.internalPrepareNewChild(sibling);
            sibling.coreDetach();
            // Note: since we have a builder of type 2, we don't need to materialize the next sibling
            // and we can use nextSibling instead of coreGetNextSibling()
            sibling.internalSetNextSibling(nextSibling);
            nextSibling = sibling;
            sibling.internalSetParent(parent);
            parent.internalNotifyChildrenModified(1);
        }
    }
    
    public final void LLChildNode.coreInsertSiblingsAfter(CoreDocumentFragment coreFragment) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        if (parent == null) {
            throw new NoParentException();
        } else {
            // TODO: we need to validate the children types; note that this is especially tricky if the children will be added later during deferred parsing
            internalValidateOwnerDocument(fragment);
            if (parent.coreIsComplete() && nextSibling == null && !fragment.coreIsComplete()) {
                // This is a special case: we don't need to build the fragment, but only to move
                // the already materialized children and then to migrate the builder. This is
                // possible because we have a builder of type 2.
                internalGetDocument().internalMigrateBuilder(fragment, parent);
                LLChildNode node = fragment.internalGetFirstChildIfMaterialized();
                nextSibling = node;
                int nodeCount = 0;
                while (node != null) {
                    node.internalSetParent(parent);
                    node = node.internalGetNextSiblingIfMaterialized();
                    nodeCount++;
                }
                fragment.internalSetFirstChild(null);
                fragment.internalNotifyChildrenCleared();
                parent.internalNotifyChildrenModified(nodeCount);
            } else {
                // TODO
            }
        }
    }
    
    public final void LLChildNode.coreInsertSiblingBefore(CoreChildNode coreSibling) throws CoreModelException {
        LLChildNode sibling = (LLChildNode)coreSibling;
        if (sibling == this) {
            throw new SelfRelationshipException();
        }
        if (parent == null) {
            throw new NoParentException();
        } else {
            parent.internalValidateChildType(sibling, null);
            parent.internalPrepareNewChild(sibling);
            sibling.coreDetach();
            LLChildNode previousSibling = null;
            LLChildNode node = parent.internalGetFirstChildIfMaterialized();
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
            parent.internalNotifyChildrenModified(1);
        }
    }
    
    public final void LLChildNode.coreInsertSiblingsBefore(CoreDocumentFragment coreFragment) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)coreFragment;
        if (parent == null) {
            throw new NoParentException();
        } else {
            // TODO: handle empty fragment?
            internalValidateOwnerDocument(fragment);
            fragment.coreBuild();
            LLChildNode node = (LLChildNode)fragment.coreGetFirstChild(); // TODO: internal... method here
            LLChildNode previousSibling = internalGetPreviousSibling();
            if (previousSibling == null) {
                parent.internalSetFirstChild(node);
            } else {
                previousSibling.internalSetNextSibling(node);
            }
            int nodeCount = 0;
            LLChildNode previousNode;
            do {
                node.internalSetParent(parent);
                previousNode = node;
                node = node.internalGetNextSibling();
                nodeCount++;
            } while (node != null);
            previousNode.internalSetNextSibling(this);
            fragment.internalSetFirstChild(null);
            fragment.internalNotifyChildrenCleared();
            parent.internalNotifyChildrenModified(nodeCount);
        }
    }
    
    public final void LLChildNode.coreDetach() throws DeferredParsingException {
        if (parent != null) {
            LLChildNode previousSibling = internalGetPreviousSibling();
            // We have a builder of type 2; thus we don't need to build
            // the node being detached. Therefore we can use internalGetNextSibling
            // instead of coreGetNextSibling.
            if (previousSibling == null) {
                parent.internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            nextSibling = null;
            parent.internalNotifyChildrenModified(-1);
            parent = null;
        }
    }
    
    public final void LLChildNode.coreReplaceWith(CoreChildNode coreNewNode) throws CoreModelException {
        LLChildNode newNode = (LLChildNode)coreNewNode;
        if (parent == null) {
            throw new NoParentException();
        } else if (newNode != this) {
            parent.internalValidateChildType(newNode, this);
            parent.internalPrepareNewChild(newNode);
            newNode.coreDetach();
            LLChildNode previousSibling = internalGetPreviousSibling();
            if (previousSibling == null) {
                parent.internalSetFirstChild(newNode);
            } else {
                previousSibling.internalSetNextSibling(newNode);
            }
            newNode.internalSetNextSibling(nextSibling);
            newNode.internalSetParent(parent);
            nextSibling = null;
            parent = null;
        }
    }
    
    public final void LLChildNode.coreReplaceWith(CoreDocumentFragment newNodes) throws CoreModelException {
        DocumentFragment fragment = (DocumentFragment)newNodes;
        if (parent == null) {
            throw new NoParentException();
        } else {
            internalValidateOwnerDocument(newNodes);
            LLChildNode previousSibling = internalGetPreviousSibling();
            int nodeCount = 0;
            if (parent.coreIsComplete() && nextSibling == null && !fragment.coreIsComplete()) {
                // This is the same case as considered in coreInsertSiblingsAfter
                internalGetDocument().internalMigrateBuilder(fragment, parent);
                LLChildNode node = fragment.internalGetFirstChildIfMaterialized();
                if (previousSibling == null) {
                    parent.internalSetFirstChild(node);
                } else {
                    previousSibling.internalSetNextSibling(node);
                }
                while (node != null) {
                    node.internalSetParent(parent);
                    node = node.internalGetNextSiblingIfMaterialized();
                    nodeCount++;
                }
            } else {
                fragment.coreBuild(); // Avoids repetitive lookup of builder; anyway we need all nodes
                LLChildNode node = (LLChildNode)fragment.coreGetFirstChild(); // TODO: internal... method here
                if (previousSibling == null) {
                    parent.internalSetFirstChild(node);
                } else {
                    previousSibling.internalSetNextSibling(node);
                }
                LLChildNode previousNode;
                do {
                    node.internalSetParent(parent);
                    previousNode = node;
                    node = node.internalGetNextSibling();
                    nodeCount++;
                } while (node != null);
                previousNode.internalSetNextSibling(nextSibling);
            }
            parent.internalNotifyChildrenModified(nodeCount-1);
            fragment.internalSetFirstChild(null);
            fragment.internalNotifyChildrenCleared();
            parent = null;
        }
    }
}
