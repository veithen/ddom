package com.google.code.ddom.dom.impl;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

public abstract class CharacterDataImpl extends LeafNode implements CharacterData {
    private String data;

    public CharacterDataImpl(DocumentImpl document, String data) {
        super(document);
        this.data = data;
    }

    public final String getData() {
        return data;
    }

    public final void setData(String data) throws DOMException {
        this.data = data;
    }

    public final String getNodeValue() throws DOMException {
        return getData();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        this.setData(nodeValue);
    }

    public final int getLength() {
        return data.length();
    }

    public final void appendData(String arg) throws DOMException {
        data += arg;
    }

    public final void deleteData(int offset, int count) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + data.substring(Math.min(offset + count, data.length()));
    }

    public final void insertData(int offset, String arg) throws DOMException {
        if (offset < 0 || offset > data.length()) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + arg + data.substring(offset);
    }

    public final void replaceData(int offset, int count, String arg) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        data = data.substring(0, offset) + arg + data.substring(Math.min(offset + count, data.length()));
    }

    public final String substringData(int offset, int count) throws DOMException {
        if (offset < 0 || offset > data.length() || count < 0) {
            throw DOMExceptionUtil.newDOMException(DOMException.INDEX_SIZE_ERR);
        }
        return data.substring(offset, Math.min(offset + count, data.length()));
    }
}
