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
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.code.ddom.dom.model.ChildNode;

public class DocumentFragmentImpl extends ParentNodeImpl implements DocumentFragment {
    private final DocumentImpl document;
    private ChildNode firstChild;
    private int children;
    
    public DocumentFragmentImpl(DocumentImpl document) {
        this.document = document;
    }

    public final void internalSetFirstChild(ChildNode child) {
        this.firstChild = child;
    }
    
    public final ChildNode getFirstChild() {
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(ChildNode newChild) {
        // All node type are allowed
    }

    public final int getLength() {
        return children;
    }

    public final short getNodeType() {
        return DOCUMENT_FRAGMENT_NODE;
    }

    public final String getNodeName() {
        return "#document-fragment";
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
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
    
    public final DocumentImpl getDocument() {
        return document;
    }

    public final Document getOwnerDocument() {
        return document;
    }

    public final Node getParentNode() {
        return null;
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: check this (maybe a fragment is always deeply cloned?)
        return deep ? deepClone() : shallowClone();
    }

    @Override
    protected final Node shallowClone() {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createDocumentFragment(document);
    }

    public final String lookupNamespaceURI(String prefix) {
        return null;
    }

    public final String lookupPrefix(String namespaceURI) {
        return null;
    }
}
