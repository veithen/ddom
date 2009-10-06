package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.google.code.ddom.spi.model.ParentNode;
import com.google.code.ddom.spi.model.TextNode;

public aspect TextSupport {
    declare parents: TextNodeImpl implements Text;
    
    public final Text TextNodeImpl.splitText(int offset) throws DOMException {
        String text = getData();
        if (offset < 0 || offset > text.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        setData(text.substring(0, offset));
        TextNode newNode = createNewTextNode(text.substring(offset));
        ParentNode parent = getParentNode();
        if (parent != null) {
            newNode.internalSetNextSibling(getNextSibling());
            internalSetNextSibling(newNode);
            newNode.internalSetParent(parent);
            parent.notifyChildrenModified(1);
        }
        return (Text)newNode; // TODO
    }
    
    public final String TextNodeImpl.getWholeText() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text TextNodeImpl.replaceWholeText(String content) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean TextNodeImpl.isElementContentWhitespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
}