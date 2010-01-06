package com.google.code.ddom.frontend.dom.support;

import java.util.Iterator;

import org.w3c.dom.Element;

import com.google.code.ddom.backend.Axis;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;

// TODO: clean this up
public class ElementsByTagNameNS extends ElementsBy {
    private final DOMParentNode node;
    private final String namespaceURI;
    private final String localName;
    
    public ElementsByTagNameNS(DOMDocument document, DOMParentNode node, String namespaceURI,
            String localName) {
        super(document);
        this.node = node;
        this.namespaceURI = namespaceURI;
        this.localName = localName;
    }

    @Override
    protected Iterator<Element> createIterator() {
        boolean nsWildcard = "*".equals(namespaceURI);
        boolean localNameWildcard = localName.equals("*");
        if (nsWildcard && localNameWildcard) {
            // TODO: there seems to be no unit test checking whether the iterator should return DOM1 elements!
            return (Iterator)node.coreGetChildrenByType(Axis.DESCENDANTS, DOMElement.class);
        } else if (nsWildcard) {
            return (Iterator)node.coreGetElementsByLocalName(Axis.DESCENDANTS, localName);
        } else if (localNameWildcard) {
            return (Iterator)node.coreGetElementsByNamespace(Axis.DESCENDANTS, namespaceURI);
        } else {
            // TODO: handle the cast problem properly
            return (Iterator)node.coreGetElementsByName(Axis.DESCENDANTS, namespaceURI, localName);
//            return new ElementNameFilterIterator(iterator, namespaceURI, localName);
        }
    }
}
