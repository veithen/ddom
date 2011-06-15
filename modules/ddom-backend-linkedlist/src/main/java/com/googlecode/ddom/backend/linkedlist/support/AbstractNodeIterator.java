/*
 * Copyright 2009-2011 Andreas Veithen
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
package com.googlecode.ddom.backend.linkedlist.support;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.ChildIterator;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredParsingException;

public abstract class AbstractNodeIterator<T extends CoreChildNode> implements ChildIterator<T> {
    private final CoreParentNode startNode;
    private final Class<T> type;
    private final Axis axis;
    private CoreChildNode currentNode;
    
    /**
     * The parent of the current node. This is used to detect concurrent modifications.
     */
    private CoreParentNode currentParent;
    
    private CoreChildNode nextNode;
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
            CoreChildNode node = currentNode;
            if (node != null && node.coreGetParent() != currentParent) {
                throw new ConcurrentModificationException("The current node has been removed using a method other than Iterator#remove()");
            }
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
            nextNode = node;
            hasNext = true;
        }
        return nextNode != null;
    }

    public final T next() {
        if (hasNext()) {
            currentNode = nextNode;
            currentParent = currentNode.coreGetParent();
            hasNext = false;
            return type.cast(currentNode);
        } else {
            throw new NoSuchElementException();
        }
    }

    public final void remove() {
        if (currentNode == null) {
            throw new IllegalStateException();
        }
        // Move to next node before replacing the current one
        hasNext();
        try {
            currentNode.coreDetach();
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
        currentNode = null;
    }

    public final void replace(CoreChildNode newNode) throws CoreModelException {
        // Move to next node before replacing the current one
        hasNext();
        currentNode.coreReplaceWith(newNode);
    }
}
