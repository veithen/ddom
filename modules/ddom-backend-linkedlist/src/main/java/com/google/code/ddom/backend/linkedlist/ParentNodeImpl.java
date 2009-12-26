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

import com.google.code.ddom.backend.BuilderTarget;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocumentFragment;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.HierarchyException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.NodeNotFoundException;

@Implementation
public abstract class ParentNodeImpl extends NodeImpl implements CoreParentNode {
    public final CoreChildNode coreGetLastChild() {
        CoreChildNode previousChild = null;
        CoreChildNode child = coreGetFirstChild();
        while (child != null) {
            previousChild = child;
            child = child.coreGetNextSibling();
        }
        return previousChild;
    }
    
    private void prepareNewChild(CoreChildNode newChild) throws CoreModelException {
        validateOwnerDocument(newChild);
        
        // Check that the new node is not an ancestor of this node
        CoreParentNode current = this;
        do {
            if (current == newChild) {
                throw new HierarchyException();
            }
            if (current instanceof CoreChildNode) {
                current = ((CoreChildNode)current).coreGetParent();
            } else {
                break;
            }
        } while (current != null);
        
        if (newChild instanceof BuilderTarget) {
            ((BuilderTarget)newChild).build();
        }
    }
    
    /**
     * Check if the given node is allowed as a child.
     * 
     * @param newChild the child that will be added
     * @throws HierarchyException if the child is not allowed
     */
    protected abstract void validateChildType(CoreChildNode newChild) throws HierarchyException;
    
    // insertBefore: newChild != null, refChild != null, removeRefChild == false
    // appendChild:  newChild != null, refChild == null, removeRefChild == false
    // replaceChild: newChild != null, refChild != null, removeRefChild == true
    // removeChild:  newChild == null, refChild != null, removeRefChild == true
    private void merge(CoreNode newChild, CoreChildNode refChild, boolean removeRefChild) throws CoreModelException {
        if (newChild instanceof CoreChildNode) {
            prepareNewChild((CoreChildNode)newChild);
        }
        CoreChildNode previousSibling; // The sibling that will precede the new child
        CoreChildNode nextSibling; // The sibling that will follow the new child
        if (refChild == null) { // implies removeRefChild == false
            previousSibling = coreGetLastChild();
            nextSibling = null;
        } else {
            previousSibling = null;
            CoreChildNode node = coreGetFirstChild();
            while (node != null && node != refChild) {
                previousSibling = node;
                node = node.coreGetNextSibling();
            }
            if (node == null) {
                throw new NodeNotFoundException();
            }
            nextSibling = removeRefChild ? node.coreGetNextSibling() : node;
        }
        if (newChild == null && removeRefChild) {
            if (previousSibling == null) {
                internalSetFirstChild(nextSibling);
            } else {
                previousSibling.internalSetNextSibling(nextSibling);
            }
            notifyChildrenModified(-1);
        } else {
            CoreChildNode firstNodeToInsert;
            CoreChildNode lastNodeToInsert;
            int delta; // The difference in number of children before and after the operation
            if (newChild instanceof CoreDocumentFragment) {
                CoreDocumentFragment fragment = (CoreDocumentFragment)newChild;
                firstNodeToInsert = fragment.coreGetFirstChild();
                lastNodeToInsert = null;
                for (CoreChildNode node = firstNodeToInsert; node != null; node = node.coreGetNextSibling()) {
                    // TODO: if validateChildType throws an exception, this will leave the DOM tree in a corrupt state!
                    validateChildType(node);
                    node.internalSetParent(this);
                    lastNodeToInsert = node;
                }
                delta = fragment.coreGetChildCount();
                // TODO: need to clear the document fragment?
            } else if (newChild instanceof CoreChildNode) {
                // TODO: what if this is already a child of some container?
                firstNodeToInsert = lastNodeToInsert = (CoreChildNode)newChild;
                validateChildType(firstNodeToInsert);
                firstNodeToInsert.internalSetParent(this);
                delta = 1;
            } else {
                throw new HierarchyException();
            }
            if (removeRefChild) {
                delta--;
            }
            if (delta != 0) {
                notifyChildrenModified(delta);
            }
            if (previousSibling == null) {
                internalSetFirstChild(firstNodeToInsert);
            } else {
                previousSibling.internalSetNextSibling(firstNodeToInsert);
            }
            if (nextSibling != null) {
                lastNodeToInsert.internalSetNextSibling(nextSibling);
            }
        }
        if (removeRefChild) {
            refChild.internalSetParent(null);
        }
    }

    public void coreAppendChild(CoreNode newChild) throws CoreModelException {
        merge(newChild, null, false);
    }

    public void coreInsertChildAfter(CoreNode newChild, CoreChildNode refChild) throws CoreModelException {
        merge(newChild, refChild.coreGetNextSibling(), false);
    }

    public void coreInsertChildBefore(CoreNode newChild, CoreChildNode refChild) throws CoreModelException {
        merge(newChild, refChild, false);
    }

    public void coreRemoveChild(CoreChildNode child) throws CoreModelException {
        merge(null, child, true);
    }

    public void coreReplaceChild(CoreNode newChild, CoreChildNode oldChild) throws CoreModelException {
        merge(newChild, oldChild, true);
    }
}
