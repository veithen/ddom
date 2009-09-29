package com.google.code.ddom.dom.impl;

public interface ChildNode extends AbstractNode {
    ParentNode getParentNode();
    ChildNode getNextSibling();
    ChildNode getPreviousSibling();
    void internalSetParent(ParentNode parent);
    ChildNode internalGetNextSibling();
    void internalSetNextSibling(ChildNode nextSibling);
}
