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
package com.google.code.ddom.backend.linkedlist.support;

import com.google.code.ddom.backend.linkedlist.intf.LLBuilder;
import com.google.code.ddom.backend.linkedlist.intf.LLChildNode;
import com.google.code.ddom.backend.linkedlist.intf.LLDocument;
import com.google.code.ddom.backend.linkedlist.intf.LLElement;
import com.google.code.ddom.backend.linkedlist.intf.LLLeafNode;
import com.google.code.ddom.backend.linkedlist.intf.LLNode;
import com.google.code.ddom.backend.linkedlist.intf.LLParentNode;
import com.google.code.ddom.core.CoreAttribute;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlInput;

public class TreeSerializer extends XmlInput {
    private static final int STATE_NONE = 0;
    
    /**
     * Indicates that the current node is a leaf node.
     */
    private static final int STATE_LEAF = 1;
    
    /**
     * Indicates that the current node is a parent node and that events for child nodes have not yet
     * been generated.
     */
    private static final int STATE_NOT_VISITED = 2;
    
    /**
     * Indicates that the current node is an element and that events for its attribute nodes have
     * already been generated.
     */
    private static final int STATE_ATTRIBUTES_VISITED = 3;
    
    /**
     * Indicates that the current node is a parent node and that events for child nodes have already
     * been generated.
     */
    private static final int STATE_VISITED = 4;
    
    /**
     * Indicates that the current node is a parent node for which the builder has been
     * put into pass through mode. In this state, events are not synthesized from the
     * object model but passed through from the underlying XML source used to build the
     * tree. This state is only reachable if {@link #preserve} is <code>true</code>.
     */
    private static final int STATE_PASS_THROUGH = 5;
    
    private final LLParentNode root;
    private final boolean preserve;
    private LLNode node;
    
    /**
     * The builder for the current node. This is only set if {@link #state} is
     * {@link #STATE_PASS_THROUGH}.
     */
    private LLBuilder builder;
    
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
    public void proceed() throws StreamException {
        XmlHandler handler = getHandler();
        try {
            if (state == STATE_PASS_THROUGH && !builder.isPassThroughEnabled()) {
                state = STATE_VISITED;
                builder = null;
            }
            final LLNode previousNode = node;
            final LLNode nextNode;
            if (state == STATE_PASS_THROUGH) {
                nextNode = previousNode;
            } else {
                if (previousNode == null) {
                    nextNode = root;
                    state = STATE_NOT_VISITED;
                } else if (state == STATE_VISITED && previousNode == root) {
                    nextNode = null;
                } else if (state == STATE_NOT_VISITED && previousNode instanceof LLElement) {
                    final LLElement element = (LLElement)previousNode;
                    // TODO: handle case with preserve == false
                    CoreAttribute firstAttribute = element.coreGetFirstAttribute();
                    if (firstAttribute == null) {
                        nextNode = element;
                        state = STATE_ATTRIBUTES_VISITED;
                    } else {
                        nextNode = (LLNode)firstAttribute;
                        state = STATE_NOT_VISITED;
                    }
                } else if (state == STATE_NOT_VISITED || state == STATE_ATTRIBUTES_VISITED) {
                    final LLParentNode parent = (LLParentNode)previousNode;
                    if (preserve || parent.coreIsComplete()) {
                        // TODO: bad because it will expand the node
                        LLChildNode child = parent.internalGetFirstChild();
                        if (child == null) {
                            nextNode = parent;
                            state = STATE_VISITED;
                        } else {
                            nextNode = child;
                            state = nextNode instanceof LLParentNode ? STATE_NOT_VISITED : STATE_LEAF;
                        }
                    } else {
                        LLChildNode child = parent.internalGetFirstChildIfMaterialized();
                        if (child == null) {
                            nextNode = parent;
                            builder = getDocument().internalGetBuilderFor(parent);
                            builder.setPassThroughHandler(handler);
                            state = STATE_PASS_THROUGH;
                        } else {
                            nextNode = child;
                            state = nextNode instanceof LLParentNode ? STATE_NOT_VISITED : STATE_LEAF;
                        }
                    }
                } else if (previousNode instanceof LLChildNode) {
                    final LLChildNode previousChildNode = (LLChildNode)previousNode;
                    if (preserve) {
                        LLChildNode sibling = previousChildNode.internalGetNextSibling();
                        if (sibling == null) {
                            nextNode = previousChildNode.internalGetParent();
                            state = STATE_VISITED;
                        } else {
                            nextNode = sibling;
                            state = nextNode instanceof LLParentNode ? STATE_NOT_VISITED : STATE_LEAF;
                        }
                    } else {
                        LLChildNode sibling = previousChildNode.internalGetNextSiblingIfMaterialized();
                        if (sibling == null) {
                            LLParentNode parent = previousChildNode.internalGetParent();
                            nextNode = parent;
                            if (parent.coreIsComplete()) {
                                state = STATE_VISITED;
                            } else {
                                builder = getDocument().internalGetBuilderFor(parent);
                                builder.setPassThroughHandler(handler);
                                state = STATE_PASS_THROUGH;
                            }
                        } else {
                            nextNode = sibling;
                            state = nextNode instanceof LLParentNode ? STATE_NOT_VISITED : STATE_LEAF;
                        }
                    }
                } else {
                    final CoreAttribute attribute = (CoreAttribute)previousNode;
                    // TODO: handle case with preserve == false
                    CoreAttribute nextAttribute = attribute.coreGetNextAttribute();
                    if (nextAttribute == null) {
                        nextNode = (LLNode)attribute.coreGetOwnerElement();
                        state = STATE_ATTRIBUTES_VISITED;
                    } else {
                        nextNode = (LLNode)nextAttribute;
                        state = STATE_NOT_VISITED;
                    }
                }
            }
            switch (state) {
                case STATE_LEAF:
                    ((LLLeafNode)nextNode).internalGenerateEvents(handler);
                    break;
                case STATE_NOT_VISITED:
                    ((LLParentNode)nextNode).internalGenerateStartEvent(handler);
                    break;
                case STATE_ATTRIBUTES_VISITED:
                    handler.attributesCompleted();
                    break;
                case STATE_VISITED:
                    if (nextNode == null) {
                        handler.completed();
                    } else {
                        ((LLParentNode)nextNode).internalGenerateEndEvent(handler);
                    }
                    break;
                case STATE_PASS_THROUGH:
                    builder.next();
                    break;
                default:
                    throw new IllegalStateException();
            }
            node = nextNode;
        } catch (CoreModelException ex) {
            // TODO: if it's a DeferredParsingException, maybe we can unwrap the exception?
            throw new StreamException(ex);
        }
    }

    @Override
    public void dispose() {
        // TODO
        throw new UnsupportedOperationException();
    }
}
