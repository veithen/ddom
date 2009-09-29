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
