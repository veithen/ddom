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
package com.google.code.ddom.frontend.dom.aspects;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNode;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.EmptyNodeList;

import com.google.code.ddom.frontend.dom.intf.*;

/**
 * Aspect implementing {@link Node#getFirstChild()}, {@link Node#getLastChild()}, {@link Node#hasChildNodes()},
 * {@link Node#getChildNodes()}, {@link Node#appendChild(Node)}, {@link Node#insertBefore(Node, Node)},
 * {@link Node#removeChild(Node)} and {@link Node#replaceChild(Node, Node)}.
 * 
 * @author Andreas Veithen
 */
public aspect ChildNodes {
    public final Node DOMLeafNode.getFirstChild() {
        return null;
    }
    
    public final Node DOMLeafNode.getLastChild() {
        return null;
    }

    public final boolean DOMLeafNode.hasChildNodes() {
        return false;
    }
    
    public final NodeList DOMLeafNode.getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final boolean DOMParentNode.hasChildNodes() {
        // TODO: not the best way if content is optimized
        return getFirstChild() != null;
    }
    
    public final Node DOMParentNode.getFirstChild() {
        return (Node)coreGetFirstChild();
    }
    
    public final Node DOMParentNode.getLastChild() {
        return (Node)coreGetLastChild();
    }
    
    public final NodeList DOMParentNode.getChildNodes() {
        return (NodeList)this; // TODO: cast necessary??
    }
    
    public final int DOMParentNode.getLength() {
        return coreGetChildCount();
    }
    
    public final Node DOMParentNode.item(int index) {
        // TODO: need unit test to check that this works when parsing is deferred
        // TODO: wrong result for negavite indexes
        CoreChildNode node = coreGetFirstChild();
        for (int i=0; i<index && node != null; i++) {
            node = node.coreGetNextSibling();
        }
        return (Node)node;
    }

    public final Node DOMParentNode.appendChild(Node newChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            coreAppendChild((CoreNode)newChild);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    public final Node DOMParentNode.insertBefore(Node newChild, Node refChild) throws DOMException {
        // Note: The specification of the insertBefore method says that "if refChild
        // is null, insert newChild at the end of the list of children". That is, in this
        // case the behavior is identical to appendChild. (This is covered by the DOM 1
        // test suite)
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        try {
            coreInsertChildBefore((CoreNode)newChild, (CoreChildNode)refChild);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return newChild;
    }

    public final Node DOMParentNode.removeChild(Node oldChild) throws DOMException {
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        if (oldChild instanceof CoreChildNode) {
            try {
                coreRemoveChild((CoreChildNode)oldChild);
            } catch (CoreModelException ex) {
                throw DOMExceptionUtil.translate(ex);
            }
            return oldChild;
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
        }
    }

    public final Node DOMParentNode.replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild == null) {
            throw new NullPointerException("newChild must not be null");
        }
        if (oldChild == null) {
            throw new NullPointerException("oldChild must not be null");
        }
        try {
            coreReplaceChild((CoreNode)newChild, (CoreChildNode)oldChild);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
        return oldChild;
    }

    public final Node DOMLeafNode.appendChild(@SuppressWarnings("unused") Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node DOMLeafNode.insertBefore(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node DOMLeafNode.removeChild(@SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node DOMLeafNode.replaceChild(@SuppressWarnings("unused") Node newChild, @SuppressWarnings("unused") Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }
}
