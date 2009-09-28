package ddom;

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class NSDecl extends AbstractAttrImpl {
    private String prefix;

    public NSDecl(DocumentImpl document, String prefix, String namespaceURI) {
        super(document, namespaceURI);
        this.prefix = prefix;
    }

    public final String getNamespaceURI() {
        return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
    }

    public final String getPrefix() {
        return prefix == null ? null : XMLConstants.XMLNS_ATTRIBUTE;
    }

    public void setPrefix(String prefix) throws DOMException {
        // Other DOM implementations allow changing the prefix, but this means that a namespace
        // declaration is transformed into a normal attribute. We don't support this.
        throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
    }

    public final String getLocalName() {
        return prefix == null ? XMLConstants.XMLNS_ATTRIBUTE : prefix;
    }

    public final String getName() {
        if (prefix == null) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            return XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix;
        }
    }

    public final boolean isId() {
        return false;
    }

    @Override
    protected Node shallowClone() {
        // TODO Auto-generated method stub
        return null;
    }
}
