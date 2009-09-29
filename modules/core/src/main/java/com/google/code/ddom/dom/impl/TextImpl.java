package com.google.code.ddom.dom.impl;

import org.w3c.dom.Text;

public class TextImpl extends TextNode {
    public TextImpl(DocumentImpl document, String data) {
        super(document, data);
    }
    
    public final short getNodeType() {
        return TEXT_NODE;
    }

    public final String getNodeName() {
        return "#text";
    }

    @Override
    protected TextNode createNewTextNode(String data) {
        DocumentImpl document = getDocument();
        return document.getNodeFactory().createText(document, data);
    }
}
