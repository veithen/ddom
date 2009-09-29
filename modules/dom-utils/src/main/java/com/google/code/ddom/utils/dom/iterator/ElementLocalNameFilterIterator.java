package com.google.code.ddom.utils.dom.iterator;

import java.util.Iterator;

import org.w3c.dom.Element;

public class ElementLocalNameFilterIterator extends FilterIterator<Element> {
    private final String localName;

    public ElementLocalNameFilterIterator(Iterator<Element> parent, String localName) {
        super(parent);
        if (localName == null) {
            throw new IllegalArgumentException("localName can't be null");
        }
        this.localName = localName;
    }

    @Override
    protected boolean matches(Element element) {
        return localName.equals(element.getLocalName());
    }
}
