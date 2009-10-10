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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreParentNode;

public abstract class LeafNode extends NodeImpl implements CoreChildNode {
    private CoreDocument document;
    private CoreParentNode parent;
    private CoreChildNode nextSibling;
    
    public LeafNode(CoreDocument document) {
        this.document = document;
    }

    public final void internalSetParent(CoreParentNode parent) {
        this.parent = parent;
    }
    
    public final void internalSetDocument(CoreDocument document) {
        this.document = document;
    }
    
    public final CoreChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(CoreChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final CoreDocument getDocument() {
        return document;
    }

    public final CoreParentNode getParentNode() {
        return parent;
    }
    
    public final CoreChildNode getNextSibling() {
        return ChildNodeHelper.getNextSibling(this);
    }

    public final CoreChildNode getPreviousSibling() {
        return ChildNodeHelper.getPreviousSibling(this);
    }

    public final boolean hasChildNodes() {
        return false;
    }

    public final NodeList getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final Node getFirstChild() {
        return null;
    }

    public final Node getLastChild() {
        return null;
    }

    public final Node appendChild(Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node removeChild(Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }

    public final String lookupNamespaceURI(String prefix) {
        return parent == null ? null : parent.lookupNamespaceURI(prefix);
    }
    
    public final String lookupPrefix(String namespaceURI) {
        return parent == null ? null : parent.lookupPrefix(namespaceURI);
    }
}
