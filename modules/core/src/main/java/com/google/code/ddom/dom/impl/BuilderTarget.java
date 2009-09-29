package com.google.code.ddom.dom.impl;

import com.google.code.ddom.dom.DeferredNode;

public interface BuilderTarget extends ParentNode, DeferredNode {
    void internalSetComplete();
}
