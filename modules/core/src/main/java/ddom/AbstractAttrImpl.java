package ddom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.TypeInfo;

public abstract class AbstractAttrImpl extends ParentNodeImpl implements Attr, OptimizedParentNode {
    /**
     * The owner of the attribute. This is either a {@link DocumentImpl} if the attribute is not linked
     * to an element, or an {@link ElementImpl} if the attribute has been added to an element.
     */
    private Object owner;
    
    private Object value;
    private AbstractAttrImpl nextAttribute;

    public AbstractAttrImpl(DocumentImpl document, String value) {
        owner = document;
        this.value = value;
    }
    
    final void internalSetNextAttribute(AbstractAttrImpl attr) {
        nextAttribute = attr;
    }
    
    final void internalSetOwnerElement(ElementImpl newOwner) {
        if (newOwner == null) {
            // TODO: owner could already be a document!
            owner = ((ElementImpl)owner).getOwnerDocument();
        } else {
            owner = newOwner;
        }
    }
    
    final AbstractAttrImpl internalGetNextAttribute() {
        return nextAttribute;
    }

    public final void internalSetFirstChild(ChildNode child) {
        value = child;
    }

    public final void notifyChildrenModified(int delta) {
        // Ignore this; we don't store the number of children
    }

    @Override
    protected final void validateChildType(ChildNode newChild) {
        if (!(newChild instanceof TextImpl || newChild instanceof EntityReferenceImpl)) {
            throw DOMExceptionUtil.newDOMException(DOMException.HIERARCHY_REQUEST_ERR);
        }
    }

    public final Object getContent() {
        return value;
    }

    public final int getLength() {
        if (value instanceof String) {
            return 1;
        } else {
            int length = 0;
            for (ChildNode child = (ChildNode)value; child != null; child = child.getNextSibling()) {
                length++;
            }
            return length;
        }
    }

    public final ChildNode getFirstChild() {
        return OptimizedParentNodeHelper.getFirstChild(this);
    }
    
    public final ElementImpl getOwnerElement() {
        return owner instanceof ElementImpl ? (ElementImpl)owner : null;
    }

    public final DocumentImpl getDocument() {
        if (owner instanceof DocumentImpl) {
            return (DocumentImpl)owner;
        } else {
            return ((ElementImpl)owner).getDocument();
        }
    }

    public final Document getOwnerDocument() {
        return getDocument();
    }
    
    public final Node getParentNode() {
        return null;
    }

    public final String getValue() {
        // TODO: this should also be applicable for other OptimizedParentNodes
        if (value instanceof String) {
            return (String)value;
        } else {
            return getTextContent();
        }
    }

    public final void setValue(String value) throws DOMException {
        // TODO: what if arg is null?
        this.value = value;
    }

    public final String getNodeValue() throws DOMException {
        return getValue();
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        setValue(nodeValue);
    }
    
    public final short getNodeType() {
        return ATTRIBUTE_NODE;
    }

    public final String getNodeName() {
        return getName();
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Node cloneNode(boolean deep) {
        // TODO: optimize!
        // Attributes are always deep cloned
        return deepClone();
    }

    public final boolean getSpecified() {
        // TODO
        return true;
    }

    public final TypeInfo getSchemaTypeInfo() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
