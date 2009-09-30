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
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DocumentTypeImpl extends LeafNode implements DocumentType {
    private String name;
    private String publicId;
    private String systemId;
    
    public DocumentTypeImpl(DocumentImpl document) {
        super(document);
    }

    public final short getNodeType() {
        return DOCUMENT_TYPE_NODE;
    }

    public final String getNodeName() {
        return getName();
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getPublicId() {
        return publicId;
    }

    public final void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public final String getSystemId() {
        return systemId;
    }

    public final void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public final NamedNodeMap getEntities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getInternalSubset() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final NamedNodeMap getNotations() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
        return appendTo;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: factory method here!
        DocumentTypeImpl clone = new DocumentTypeImpl(getDocument());
        clone.setName(name);
        clone.setPublicId(publicId);
        clone.setSystemId(systemId);
        return clone;
    }
}