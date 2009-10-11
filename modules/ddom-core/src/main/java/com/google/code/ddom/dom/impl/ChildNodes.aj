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
import org.w3c.dom.NodeList;

import com.google.code.ddom.core.model.*;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreModelException;
import com.google.code.ddom.spi.model.CoreNode;

/**
 * Aspect implementing {@link Node#getFirstChild()}, {@link Node#getLastChild()}, {@link Node#hasChildNodes()},
 * {@link Node#getChildNodes()}, {@link Node#appendChild(Node)}, {@link Node#insertBefore(Node, Node)},
 * {@link Node#removeChild(Node)} and {@link Node#replaceChild(Node, Node)}.
 * 
 * @author Andreas Veithen
 */
public aspect ChildNodes {
    declare parents: ParentNodeImpl implements NodeList;

    public final Node LeafNode.getFirstChild() {
        return null;
    }
    
    public final Node LeafNode.getLastChild() {
        return null;
    }

    public final boolean LeafNode.hasChildNodes() {
        return false;
    }
    
    public final NodeList LeafNode.getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final boolean ParentNodeImpl.hasChildNodes() {
        // TODO: not the best way if content is optimized
        return getFirstChild() != null;
    }
    
    public final Node ParentNodeImpl.getFirstChild() {
        return (Node)coreGetFirstChild();
    }
    
    public final Node ParentNodeImpl.getLastChild() {
        return (Node)coreGetLastChild();
    }
    
    public final NodeList ParentNodeImpl.getChildNodes() {
        return (NodeList)this; // TODO: cast necessary??
    }
    
    public final int ParentNodeImpl.getLength() {
        return coreGetChildCount();
    }
    
    public final Node ParentNodeImpl.item(int index) {
        // TODO: need unit test to check that this works when parsing is deferred
        // TODO: wrong result for negavite indexes
        CoreChildNode node = coreGetFirstChild();
        for (int i=0; i<index && node != null; i++) {
            node = node.coreGetNextSibling();
        }
        return (Node)node;
    }

    public final Node ParentNodeImpl.appendChild(Node newChild) throws DOMException {
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

    public final Node ParentNodeImpl.insertBefore(Node newChild, Node refChild) throws DOMException {
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

    public final Node ParentNodeImpl.removeChild(Node oldChild) throws DOMException {
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

    public final Node ParentNodeImpl.replaceChild(Node newChild, Node oldChild) throws DOMException {
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

    public final Node LeafNode.appendChild(Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node LeafNode.insertBefore(Node newChild, Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node LeafNode.removeChild(Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node LeafNode.replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }
}
