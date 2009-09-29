package com.google.code.ddom.dom.impl;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmptyNodeList implements NodeList {
    public static final EmptyNodeList INSTANCE = new EmptyNodeList();
    
    private EmptyNodeList() {
    }

    public final int getLength() {
        return 0;
    }

    public final Node item(int index) {
        return null;
    }
}
