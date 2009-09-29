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
