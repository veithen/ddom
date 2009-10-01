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
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.code.ddom.dom.model.ChildNode;
import com.google.code.ddom.dom.model.ParentNode;

public abstract class LeafNode extends NodeImpl implements ChildNode {
    private DocumentImpl document;
    private ParentNode parent;
    private ChildNode nextSibling;
    
    public LeafNode(DocumentImpl document) {
        this.document = document;
    }

    public final void internalSetParent(ParentNode parent) {
        this.parent = parent;
    }
    
    public final void internalSetDocument(DocumentImpl document) {
        this.document = document;
    }
    
    public final ChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(ChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final DocumentImpl getDocument() {
        return document;
    }

    public final Document getOwnerDocument() {
        return document;
    }

    public final ParentNode getParentNode() {
        return parent;
    }
    
    public final ChildNode getNextSibling() {
        return ChildNodeHelper.getNextSibling(this);
    }

    public final ChildNode getPreviousSibling() {
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
