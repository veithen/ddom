package ddom;

import javax.xml.XMLConstants;

import org.w3c.dom.DOMException;

public class DOM2NamedNodeHelper {
    public static void setPrefix(DOM2NamedNode node, String prefix) throws DOMException {
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null) {
            throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
        } else {
            NSUtil.validatePrefix(prefix);
            if (XMLConstants.XML_NS_PREFIX.equals(prefix) && !XMLConstants.XML_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix) && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                throw DOMExceptionUtil.newDOMException(DOMException.NAMESPACE_ERR);
            }
            node.internalSetPrefix(prefix);
        }
    }
    
    public static String getName(DOM2NamedNode node) {
        String prefix = node.getPrefix();
        String localName = node.getLocalName();
        if (prefix == null) {
            return localName;
        } else {
            return prefix + ":" + localName;
        }
    }
}
