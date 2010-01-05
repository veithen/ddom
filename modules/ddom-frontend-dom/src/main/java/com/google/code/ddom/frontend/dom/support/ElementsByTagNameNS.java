package com.google.code.ddom.frontend.dom.support;

import java.util.Iterator;

import org.w3c.dom.Element;

import com.google.code.ddom.backend.Axis;
import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.frontend.dom.intf.DOMParentNode;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;
import com.google.code.ddom.utils.dom.iterator.ElementLocalNameFilterIterator;
import com.google.code.ddom.utils.dom.iterator.ElementNamespaceFilterIterator;

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
        Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, node);
        if (nsWildcard && localNameWildcard) {
            return iterator;
        } else if (nsWildcard) {
            return new ElementLocalNameFilterIterator(iterator, localName);
        } else if (localNameWildcard) {
            return new ElementNamespaceFilterIterator(iterator, namespaceURI);
        } else {
            // TODO: handle the cast problem properly
            return (Iterator)node.coreGetElementsByName(Axis.DESCENDANTS, namespaceURI, localName);
//            return new ElementNameFilterIterator(iterator, namespaceURI, localName);
        }
    }
}
