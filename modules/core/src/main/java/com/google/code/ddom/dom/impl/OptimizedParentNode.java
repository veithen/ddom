package com.google.code.ddom.dom.impl;

public interface OptimizedParentNode extends ParentNode {
    /**
     * 
     * either a String or a ChildNode
     * 
     * @return
     */
    Object getContent();
}
