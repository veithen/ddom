package com.google.code.ddom.dom.impl;

import org.w3c.dom.Node;

public class DOM2AttrImpl extends AttrImpl implements DOM2NamedNode {
    private final String namespaceURI;
    private final String localName;
    private String prefix;

    public DOM2AttrImpl(DocumentImpl document, String namespaceURI, String localName, String prefix, String value, String type) {
        super(document, value, type);
        this.namespaceURI = namespaceURI;
        this.localName = localName;
        this.prefix = prefix;
    }

    public final String getNamespaceURI() {
        return namespaceURI;
    }

    public final String getPrefix() {
        return prefix;
    }

    public final void internalSetPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public final void setPrefix(String prefix) {
        DOM2NamedNodeHelper.setPrefix(this, prefix);
    }

    public final String getLocalName() {
        return localName;
    }
    
    public final String getName() {
        return DOM2NamedNodeHelper.getName(this);
    }
    
    @Override
    protected final Node shallowClone() {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createAttribute(document, namespaceURI, localName, prefix, null, getType());
    }
}
