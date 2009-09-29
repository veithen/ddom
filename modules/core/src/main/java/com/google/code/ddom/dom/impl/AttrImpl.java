package com.google.code.ddom.dom.impl;

public abstract class AttrImpl extends AbstractAttrImpl {
    private String type;

    public AttrImpl(DocumentImpl document, String value, String type) {
        super(document, value);
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final boolean isId() {
        return "ID".equals(type);
    }
}
