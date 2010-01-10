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
import com.google.code.ddom.backend.DeferredParsingException;

public abstract class AbstractNodeIterator<T extends CoreChildNode> implements Iterator<T> {
    private final CoreParentNode startNode;
    private final Class<T> type;
    private final Axis axis;
    private CoreChildNode node;
    private boolean hasNext;
    private int depth;
    
    public AbstractNodeIterator(CoreParentNode startNode, Class<T> type, Axis axis) {
        this.startNode = startNode;
        this.type = type;
        this.axis = axis;
    }

    protected abstract boolean matches(T node);

    public final boolean hasNext() {
        if (!hasNext) {
            CoreChildNode node = this.node;
            do {
                try {
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
                } catch (DeferredParsingException ex) {
                    // TODO
                    throw new RuntimeException(ex);
                }
            } while (node != null && (!type.isInstance(node) || !matches(type.cast(node))));
            this.node = node;
            hasNext = true;
        }
        return node != null;
    }

    public final T next() {
        if (hasNext()) {
            hasNext = false;
            return type.cast(node);
        } else {
            throw new NoSuchElementException();
        }
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
