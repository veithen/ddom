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
            LLNode previousNode = node;
            if (node == null) {
                node = root;
            } else if (node instanceof LLParentNode && state == STATE_VISITED) {
                
            } else {
                // If we get here, then the previous node can't be a document or document fragment and
                // therefore must be a child node
                LLChildNode previousSibling = (LLChildNode)previousNode;
                if (preserve) {
                    LLChildNode child = previousSibling.internalGetNextSibling();
                    if (child == null) {
                        node = previousSibling.internalGetParent();
                        state = STATE_VISITED;
                    } else {
                        node = child;
                    }
                } else {
                    LLChildNode child = previousSibling.internalGetNextSiblingIfMaterialized();
                    if (child == null) {
                        LLParentNode parent = previousSibling.internalGetParent();
                        if (parent.coreIsComplete()) {
                            node = parent;
                            state = STATE_VISITED;
                        } else {
                            getDocument().internalGetBuilderFor(parent);
                        }
                    } else {
                        node = child;
                    }
                }
            }
            
            // TODO
            return true;
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
