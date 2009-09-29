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
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;

public class EntityReferenceImpl extends LeafNode implements EntityReference {
    private String name;
    
    public EntityReferenceImpl(DocumentImpl document, String name) {
        super(document);
        this.name = name;
    }

    public final short getNodeType() {
        return ENTITY_REFERENCE_NODE;
    }

    public final String getNodeName() {
        return name;
    }

    public final Node cloneNode(boolean deep) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createEntityReference(document, name);
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getNodeValue() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
