package com.google.code.ddom.dom.impl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

public abstract class NodeImpl implements AbstractNode {
    protected final void validateOwnerDocument(Node node) {
        if (node.getOwnerDocument() != getDocument()) {
            throw DOMExceptionUtil.newDOMException(DOMException.WRONG_DOCUMENT_ERR);
        }
    }

    public void normalize() {
        // TODO Auto-generated method stub
    }
    
    public final String getTextContent() throws DOMException {
        CharSequence content = collectTextContent(null);
        return content == null ? "" : content.toString();
    }
    
    public final boolean isSupported(String feature, String version) {
        return getDocument().getImplementation().hasFeature(feature, version);
    }

    public final Object getFeature(String feature, String version) {
        return this;
    }

    public final String lookupNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String lookupPrefix(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean isDefaultNamespace(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean isSameNode(Node other) {
        return other == this;
    }

    public final boolean isEqualNode(Node arg) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setTextContent(String textContent) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Object getUserData(String key) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Object setUserData(String key, Object data, UserDataHandler handler) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
