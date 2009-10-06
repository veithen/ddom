/*
 * Copyright 2009 Andreas Veithen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
