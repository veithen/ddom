/*
 * Copyright 2009-2011,2013 Andreas Veithen
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
import com.googlecode.ddom.core.CoreNode;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;

public abstract class AbstractNodeIterator<T> implements ChildIterator<T> {
    private final CoreParentNode startNode;
    private final Axis axis;
    private final Class<T> type;
    private CoreNode currentNode;
    
    /**
     * The parent of the current node. This is used to detect concurrent modifications.
     */
    private CoreParentNode currentParent;
    
    private CoreNode nextNode;
    private boolean hasNext;
    private int depth;
    
    public AbstractNodeIterator(CoreParentNode startNode, Axis axis, Class<T> type) {
        this.startNode = startNode;
        this.axis = axis;
        this.type = type;
    }

    protected abstract boolean matches(CoreNode node);

    public final boolean hasNext() {
        if (!hasNext) {
            CoreNode node = currentNode;
            if (node instanceof CoreChildNode && ((CoreChildNode)node).coreGetParent() != currentParent) {
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
                                node = ((CoreChildNode)node).coreGetNextSibling();
                            }
                            break;
                        case DESCENDANTS:
                        case DESCENDANTS_OR_SELF:
                            if (node == null) {
                                if (axis == Axis.DESCENDANTS) {
                                    node = startNode.coreGetFirstChild();
                                    depth++;
                                } else {
                                    node = startNode;
                                }
                            } else {
                                boolean visitChildren = true;
                                while (true) {
                                    // TODO: test for CoreContainer instead????
                                    if (visitChildren && node instanceof CoreElement) {
                                        CoreChildNode firstChild = ((CoreElement)node).coreGetFirstChild();
                                        if (firstChild != null) {
                                            depth++;
                                            node = firstChild;
                                            break;
                                        }
                                    }
                                    if (depth == 0) {
                                        node = null;
                                        break;
                                    }
                                    CoreChildNode nextSibling = ((CoreChildNode)node).coreGetNextSibling();
                                    if (nextSibling != null) {
                                        node = nextSibling;
                                        break;
                                    }
                                    depth--;
                                    node = ((CoreChildNode)node).coreGetParent();
                                    visitChildren = false;
                                }
                            }
                    }
                } catch (DeferredBuildingException ex) {
                    // TODO
                    throw new RuntimeException(ex);
                }
            } while (node != null && !matches(node));
            nextNode = node;
            hasNext = true;
        }
        return nextNode != null;
    }

    public final T next() {
        if (hasNext()) {
            currentNode = nextNode;
            currentParent = currentNode instanceof CoreChildNode ? ((CoreChildNode)currentNode).coreGetParent() : null;
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
        if (currentNode instanceof CoreChildNode) {
            try {
                ((CoreChildNode)currentNode).coreDetach();
            } catch (DeferredParsingException ex) {
                // TODO
                throw new RuntimeException(ex);
            }
        }
        currentNode = null;
    }

    public final void replace(CoreChildNode newNode) throws CoreModelException {
        // Move to next node before replacing the current one
        hasNext();
        ((CoreChildNode)currentNode).coreReplaceWith(newNode);
    }
}
