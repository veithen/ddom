package ddom;

import org.w3c.dom.DOMException;

public class DOM1ElementImpl extends ElementImpl implements DOM1NamedNode {
    private final String tagName;

    public DOM1ElementImpl(DocumentImpl document, String tagName, boolean complete) {
        super(document, complete);
        this.tagName = tagName;
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

    public final String getTagName() {
        return tagName;
    }

    @Override
    protected final ElementImpl shallowCloneWithoutAttributes() {
        DocumentImpl document = getDocument();
        NodeFactory factory = document.getNodeFactory();
        return factory.createElement(document, tagName, true);
    }
}
