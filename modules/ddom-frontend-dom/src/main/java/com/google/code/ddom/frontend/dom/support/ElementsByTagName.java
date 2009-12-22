package com.google.code.ddom.frontend.dom.support;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.code.ddom.frontend.dom.intf.DOMDocument;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;
import com.google.code.ddom.utils.dom.iterator.FilterIterator;

// TODO: clean this up
public class ElementsByTagName extends ElementsBy {
    private final Node parent;
    private final String tagname;
    
    public ElementsByTagName(DOMDocument document, Node parent, String tagname) {
        super(document);
        this.parent = parent;
        this.tagname = tagname;
    }

    @Override
    protected Iterator<Element> createIterator() {
        Iterator<Element> iterator = new DescendantsIterator<Element>(Element.class, parent);
        if (tagname.equals("*")) {
            return iterator;
        } else {
            return new FilterIterator<Element>(iterator) {
                @Override
                protected boolean matches(Element element) {
                    return tagname.equals(element.getTagName());
                }
            };
        }
    }
}
