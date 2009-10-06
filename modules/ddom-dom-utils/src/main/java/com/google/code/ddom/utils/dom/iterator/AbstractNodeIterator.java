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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;

/**
 * Abstract base class for iterators over sets of OM nodes.
 */
public abstract class AbstractNodeIterator<T extends Node> implements Iterator<T> {
    private T currentNode;
    private T nextNode;
    private boolean noMoreNodes;

    /**
     * Get the next node.
     * 
     * @param currentNode
     *            the predecessor of the node to retrieve, or <code>null</code> if the
     *            implementation should return the first node
     * @return the next node
     */
    protected abstract T getNextNode(T currentNode);
    
    public boolean hasNext() {
        if (noMoreNodes) {
            return false;
        } else if (nextNode != null) {
            return true;
        } else {
            nextNode = getNextNode(currentNode);
            noMoreNodes = nextNode == null;
            return !noMoreNodes;
        }
    }

    public T next() {
        if (hasNext()) {
            currentNode = nextNode;
            nextNode = null;
            return currentNode;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
