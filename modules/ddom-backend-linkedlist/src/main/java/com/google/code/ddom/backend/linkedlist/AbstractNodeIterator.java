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
package com.google.code.ddom.backend.linkedlist;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.code.ddom.backend.Axis;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreParentNode;

public abstract class AbstractNodeIterator<T extends CoreChildNode> implements Iterator<T> {
    /**
     * There has been no call to {@link #hasNext()} since the last call to {@link #next()}
     * completed. {@link #node} is the node returned by the last call to {@link #next()}, or
     * <code>null</code> if the iterator has just been created.
     */
    private static final int NEED_NEXT = 1;
    
    /**
     * {@link #hasNext()} has been called and the iterator expects a call to {@link #next()}.
     * {@link #node} is the node that will be returned by the next call to {@link #next()}.
     */
    private static final int HAS_NEXT = 2;
    
    /**
     * {@link #hasNext()} has been called and returned <code>false</code>.
     */
    private static final int NO_MORE_NODES = 3;
    
    private final CoreParentNode startNode;
    private final Class<T> nodeClass;
    private final Axis axis;
    private CoreChildNode node;
    private int state = NEED_NEXT;
    private int depth;
    
    public AbstractNodeIterator(CoreParentNode startNode, Class<T> nodeClass, Axis axis) {
        this.startNode = startNode;
        this.nodeClass = nodeClass;
        this.axis = axis;
    }

    protected abstract boolean matches(T node);

    public boolean hasNext() {
        if (state == NEED_NEXT) {
            CoreChildNode node = this.node;
            while (true) {
                // Get to the next node
                switch (axis) {
                    case CHILDREN:
                        if (node == null) {
                            node = startNode.coreGetFirstChild();
                        } else {
                            node = node.coreGetNextSibling();
                        }
                        break;
                    case DESCENDANTS:
                        if (node == null) {
                            node = startNode.coreGetFirstChild();
                        } else {
                            boolean visitChildren = true;
                            while (true) {
                                if (visitChildren && node instanceof CoreElement) {
                                    CoreChildNode firstChild = ((CoreElement)node).coreGetFirstChild();
                                    if (firstChild != null) {
                                        depth++;
                                        node = firstChild;
                                        break;
                                    }
                                }
                                CoreChildNode nextSibling = node.coreGetNextSibling();
                                if (nextSibling != null) {
                                    node = nextSibling;
                                    break;
                                }
                                if (depth > 0) {
                                    depth--;
                                    node = (CoreChildNode)node.coreGetParent();
                                    visitChildren = false;
                                } else {
                                    node = null;
                                    break;
                                }
                            }
                        }
                }
                if (node == null) {
                    state = NO_MORE_NODES;
                    break;
                }
                if (nodeClass.isInstance(node) && matches(nodeClass.cast(node))) {
                    state = HAS_NEXT;
                    break;
                }
            }
            this.node = node;
        }
        return state == HAS_NEXT;
    }

    public T next() {
        if (hasNext()) {
            state = NEED_NEXT;
            return nodeClass.cast(node);
        } else {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
