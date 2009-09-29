package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public abstract class TextNode extends CharacterDataImpl implements Text {
    public TextNode(DocumentImpl document, String data) {
        super(document, data);
    }

    public final Text splitText(int offset) throws DOMException {
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
        return newNode;
    }
    
    protected abstract TextNode createNewTextNode(String data);

    public final Node cloneNode(boolean deep) {
        return createNewTextNode(getData());
    }

    public final CharSequence collectTextContent(CharSequence appendTo) {
        String data = getData();
        if (appendTo == null) {
            return data;
        } else {
            StringBuilder builder;
            if (appendTo instanceof String) {
                String existing = (String)appendTo;
                builder = new StringBuilder(existing.length() + data.length());
                builder.append(existing);
            } else {
                builder = (StringBuilder)appendTo;
            }
            builder.append(data);
            return builder;
        }
    }

    public final boolean isElementContentWhitespace() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getWholeText() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text replaceWholeText(String content) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
