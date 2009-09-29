package com.google.code.ddom.utils.dom.iterator;

import org.w3c.dom.Node;

/**
 * Iterator that iterates over all descendants in document order.
 */
public class DescendantsIterator<T extends Node> extends AbstractNodeIterator<T> {
    private final Class<T> nodeType;
    private final Node root;
    private int level;
    
    public DescendantsIterator(Class<T> nodeType, Node root) {
        this.nodeType = nodeType;
        this.root = root;
    }

    @Override
    protected T getNextNode(T currentNode) {
        Node node = currentNode;
        do {
            if (node == null) {
                node = root.getFirstChild();
            } else {
                Node firstChild = node.getFirstChild();
                if (firstChild != null) {
                    level++;
                    node = firstChild;
                } else {
                    while (true) {
                        Node nextSibling = node.getNextSibling();
                        if (nextSibling != null) {
                            node = nextSibling;
                            break;
                        } else if (level == 0) {
                            node = null;
                            break;
                        } else {
                            node = node.getParentNode();
                            level--;
                        }
                    }
                }
            }
        } while (node != null && !nodeType.isInstance(node));
        return nodeType.cast(node);
    }
}
