/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.backend.linkedlist.support;

import com.google.code.ddom.backend.linkedlist.intf.LLChildNode;
import com.google.code.ddom.backend.linkedlist.intf.LLDocument;
import com.google.code.ddom.backend.linkedlist.intf.LLNode;
import com.google.code.ddom.backend.linkedlist.intf.LLParentNode;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlInput;

public class TreeSerializer extends XmlInput {
    private static final int STATE_NONE = 0;
    
    /**
     * Indicates that the current node is a parent node for which the start events have
     * already been generated.
     */
    private static final int STATE_VISITED = 1;
    
    /**
     * Indicates that the current node is a parent node for which the builder has been
     * put into pass through mode. In this state, events are not synthesized from the
     * object model but passed through from the underlying XML source used to build the
     * tree. This state is only reachable if {@link #preserve} is <code>true</code>.
     */
    private static final int STATE_PASS_THROUGH = 2;
    
    private final LLParentNode root;
    private final boolean preserve;
    private LLNode node;
    private int state;
    private LLDocument document;
    
    public TreeSerializer(LLParentNode root, boolean preserve) {
        this.root = root;
        this.preserve = preserve;
    }
    
    private LLDocument getDocument() {
        if (document == null) {
            document = root.internalGetOwnerDocument();
        }
        return document;
    }
    
    @Override
    public boolean proceed() throws StreamException {
        XmlHandler handler = getHandler();
        try {
            final LLNode previousNode = node;
            final LLNode nextNode;
            if (previousNode == null) {
                nextNode = root;
            } else if (previousNode instanceof LLParentNode && state != STATE_VISITED) {
                final LLParentNode parent = (LLParentNode)previousNode;
                if (preserve) {
                    LLChildNode child = parent.internalGetFirstChild();
                    if (child == null) {
                        nextNode = parent;
                        state = STATE_VISITED;
                    } else {
                        nextNode = child;
                    }
                } else {
                    LLChildNode child = parent.internalGetFirstChildIfMaterialized();
                    if (child == null) {
                        nextNode = parent;
                        if (parent.coreIsComplete()) {
                            state = STATE_VISITED;
                        } else {
                            getDocument().internalGetBuilderFor(parent).setPassThroughHandler(handler);
                            state = STATE_PASS_THROUGH;
                        }
                    } else {
                        nextNode = child;
                    }
                }
            } else {
                // If we get here, then the previous node can't be a document or document fragment and
                // therefore must be a child node
                final LLChildNode previousChildNode = (LLChildNode)previousNode;
                if (preserve) {
                    LLChildNode sibling = previousChildNode.internalGetNextSibling();
                    if (sibling == null) {
                        nextNode = previousChildNode.internalGetParent();
                        state = STATE_VISITED;
                    } else {
                        nextNode = sibling;
                    }
                } else {
                    LLChildNode sibling = previousChildNode.internalGetNextSiblingIfMaterialized();
                    if (sibling == null) {
                        LLParentNode parent = previousChildNode.internalGetParent();
                        nextNode = parent;
                        if (parent.coreIsComplete()) {
                            state = STATE_VISITED;
                        } else {
                            getDocument().internalGetBuilderFor(parent).setPassThroughHandler(handler);
                            state = STATE_PASS_THROUGH;
                        }
                    } else {
                        nextNode = sibling;
                    }
                }
            }
            switch (state) {
                case STATE_VISITED:
                    handler.completed();
                    if (nextNode == root) {
                        node = null;
                        return false;
                    } else {
                        node = nextNode;
                        return true;
                    }
                case STATE_NONE:
                    nextNode.internalGenerateEvents(handler);
                    node = nextNode;
                    return true;
                default:
                    throw new IllegalStateException();
            }
        } catch (CoreModelException ex) {
            throw new StreamException(ex);
        }
    }

    @Override
    public void dispose() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
