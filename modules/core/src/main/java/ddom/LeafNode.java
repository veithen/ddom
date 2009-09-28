package ddom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class LeafNode extends NodeImpl implements ChildNode {
    private DocumentImpl document;
    private ParentNode parent;
    private ChildNode nextSibling;
    
    public LeafNode(DocumentImpl document) {
        this.document = document;
    }

    public final void internalSetParent(ParentNode parent) {
        this.parent = parent;
    }
    
    public final void internalSetDocument(DocumentImpl document) {
        this.document = document;
    }
    
    public final ChildNode internalGetNextSibling() {
        return nextSibling;
    }

    public final void internalSetNextSibling(ChildNode nextSibling) {
        this.nextSibling = nextSibling;
    }
    
    public final DocumentImpl getDocument() {
        return document;
    }

    public final Document getOwnerDocument() {
        return document;
    }

    public final ParentNode getParentNode() {
        return parent;
    }
    
    public final ChildNode getNextSibling() {
        return ChildNodeHelper.getNextSibling(this);
    }

    public final ChildNode getPreviousSibling() {
        return ChildNodeHelper.getPreviousSibling(this);
    }

    public final boolean hasChildNodes() {
        return false;
    }

    public final NodeList getChildNodes() {
        return EmptyNodeList.INSTANCE;
    }

    public final Node getFirstChild() {
        return null;
    }

    public final Node getLastChild() {
        return null;
    }

    public final Node appendChild(Node newChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
    }

    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node removeChild(Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw DOMExceptionUtil.newDOMException(DOMException.NOT_FOUND_ERR);
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
    }
}
