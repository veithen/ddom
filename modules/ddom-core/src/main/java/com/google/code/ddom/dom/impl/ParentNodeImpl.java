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
package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.google.code.ddom.spi.model.BuilderTarget;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocumentFragment;
import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.CoreNode;
import com.google.code.ddom.spi.model.CoreParentNode;
import com.google.code.ddom.spi.model.HierarchyException;
import com.google.code.ddom.spi.model.NodeNotFoundException;

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
    
    public final Node appendChild(Node newChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            merge((CoreNode)newChild, null, false);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        // Note: The specification of the insertBefore method says that "if refChild
        // is null, insert newChild at the end of the list of children". That is, in this
        // case the behavior is identical to appendChild. (This is covered by the DOM 1
        // test suite)
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            merge((CoreNode)newChild, (CoreChildNode)refChild, false);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    private void prepareNewChild(Node newChild) {
        validateOwnerDocument(newChild);
        
        // Check that the new node is not an ancestor of this node
        Node current = this;
        do {
            if (current == newChild) {
                throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
            }
            current = current.getParentNode();
        } while (current != null);
        
        if (newChild instanceof BuilderTarget) {
            ((BuilderTarget)newChild).build();
        }
    }
    
    protected abstract void validateChildType(CoreChildNode newChild);
    
    public final Node removeChild(Node oldChild) throws DOMException {
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        try {
            merge(null, (CoreChildNode)oldChild, true);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return oldChild;
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        try {
            merge((CoreNode)newChild, (CoreChildNode)oldChild, true);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return oldChild;
    }

    // insertBefore: newChild != null, refChild != null, removeRefChild == false
    // appendChild:  newChild != null, refChild == null, removeRefChild == false
    // replaceChild: newChild != null, refChild != null, removeRefChild == true
    // removeChild:  newChild == null, refChild != null, removeRefChild == true
    public void merge(CoreNode newChild, CoreChildNode refChild, boolean removeRefChild) throws CoreModelException {
        if (newChild != null) {
            prepareNewChild(newChild);
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
                delta = fragment.getLength();
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
}
