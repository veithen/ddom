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

import com.google.code.ddom.spi.model.DOMNode;

public abstract class NodeImpl implements DOMNode {
    protected final void validateOwnerDocument(Node node) {
        if (node.getOwnerDocument() != getDocument()) {
            throw DOMExceptionUtil.newDOMException(DOMException.WRONG_DOCUMENT_ERR);
        }
    }

    public void normalize() {
        // TODO Auto-generated method stub
    }
    
    public final String getTextContent() throws DOMException {
        CharSequence content = collectTextContent(null);
        return content == null ? "" : content.toString();
    }
    
    public final boolean isSupported(String feature, String version) {
        return getDocument().getImplementation().hasFeature(feature, version);
    }

    public final Object getFeature(String feature, String version) {
        return this;
    }

    public final boolean isDefaultNamespace(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean isSameNode(Node other) {
        return other == this;
    }

    public final boolean isEqualNode(Node arg) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setTextContent(String textContent) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
