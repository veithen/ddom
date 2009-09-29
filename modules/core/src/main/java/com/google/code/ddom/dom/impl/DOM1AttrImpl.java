package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class DOM1AttrImpl extends AttrImpl implements DOM1NamedNode {
    private final String name;

    public DOM1AttrImpl(DocumentImpl document, String name, String value, String type) {
        super(document, value, type);
        this.name = name;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String getLocalName() {
        return null;
    }

    public final String getName() {
        return name;
    }
    
    @Override
    protected final Node shallowClone() {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createAttribute(document, name, null, getType());
    }
}
