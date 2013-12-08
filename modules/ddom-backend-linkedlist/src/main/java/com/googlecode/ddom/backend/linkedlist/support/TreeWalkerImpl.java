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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.backend.linkedlist.intf.LLLeafNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.NodeConsumedException;
import com.googlecode.ddom.stream.IncludeXmlOutput;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlReader;
import com.googlecode.ddom.stream.XmlSource;

final class TreeWalkerImpl implements XmlReader {
    private static final Log log = LogFactory.getLog(TreeWalkerImpl.class);
    
    private static final int STATE_NONE = 0;
    
    /**
     * Indicates that the serializer is synthesizing a start fragment event. This state can only be
     * reached if the root node is not a document.
     */
    private static final int STATE_START_FRAGMENT = 1;
    
    /**
     * Indicates that the current node is a leaf node.
     */
    private static final int STATE_LEAF = 2;
    
    /**
     * Indicates that the current node is a parent node and that events for child nodes have not yet
     * been generated.
     */
    private static final int STATE_NOT_VISITED = 3;
    
    /**
     * Indicates that the current node is an element and that events for its attribute nodes have
     * already been generated.
     */
    private static final int STATE_ATTRIBUTES_VISITED = 4;
    
    /**
     * Indicates that the current node is a parent node and that events for child nodes have already
     * been generated.
     */
    private static final int STATE_VISITED = 5;
    
    /**
     * Indicates that the current node is a parent node for which the builder has been
     * put into pass through mode. In this state, events are not synthesized from the
     * object model but passed through from the underlying XML source used to build the
     * tree. This state is only reachable if {@link #preserve} is <code>true</code>.
     */
    private static final int STATE_PASS_THROUGH = 6;
    
    /**
     * Indicates that the serializer is streaming the content from a parent node as set using
     * {@link CoreParentNode#coreSetContent(XmlSource)}.
     */
    private static final int STATE_STREAMING_CONTENT = 7;
    
    /**
     * Indicates that the serializer is streaming the source of an element as set using
     * {@link CoreElement#coreSetSource(XmlSource)}.
     */
    private static final int STATE_STREAMING_SOURCE = 8;
    
    private final XmlHandler handler;
    private final LLParentNode root;
    private final boolean preserve;
    private LLNode node;
    
    /**
     * The input context for the current node. This is only set if {@link #state} is
     * {@link #STATE_PASS_THROUGH}.
     */
    private InputContext inputContext;
    
    /**
     * The stream from which events are included. This is only set if {@link #state} is
     * {@link #STATE_STREAMING_CONTENT} or {@link #STATE_STREAMING_SOURCE}.
     */
    private Stream stream;
    
    private int state = STATE_NONE;
    private LLDocument document;
    
    public TreeWalkerImpl(XmlHandler handler, LLParentNode root, boolean preserve) {
        this.handler = handler;
        this.root = root;
        this.preserve = preserve;
    }
    
    private LLDocument getDocument() {
        if (document == null) {
            document = root.internalGetOwnerDocument(false);
        }
        return document;
    }
    
    public void proceed(boolean flush) throws StreamException {
        try {
            // Determine the next node (i.e. the node for which the next event is generated) and
            // update the state
            final LLNode previousNode = node;
            final LLNode nextNode;
            if (state == STATE_PASS_THROUGH || state == STATE_STREAMING_SOURCE) {
                nextNode = previousNode;
            } else if (state == STATE_STREAMING_CONTENT) {
                nextNode = previousNode;
                if (stream.isComplete()) {
                    state = STATE_VISITED;
                    stream = null;
                }
            } else if (previousNode == null) {
                if (state == STATE_NONE && !(root instanceof LLDocument)) {
                    nextNode = null;
                    state = STATE_START_FRAGMENT;
                } else {
                    nextNode = root;
                    state = STATE_NOT_VISITED;
                }
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
                int nodeState = parent.internalGetState();
                if (preserve && !(nodeState == CoreParentNode.STATE_CONTENT_SET && !((XmlSource)parent.coreGetContent()).isDestructive())
                        || nodeState == CoreParentNode.STATE_EXPANDED || nodeState == CoreParentNode.STATE_VALUE_SET) {
                    // TODO: bad because it will expand the node if the state is "Value set"
                    LLChildNode child = parent.internalGetFirstChild();
                    if (child == null) {
                        nextNode = parent;
                        state = STATE_VISITED;
                    } else {
                        nextNode = child;
                        state = STATE_NOT_VISITED;
                    }
                } else if (nodeState == CoreParentNode.STATE_CONTENT_SET) {
                    stream = createStream(parent, flush, handler);
                    state = STATE_STREAMING_CONTENT;
                    // TODO: update node state
                    nextNode = parent;
                } else {
                    LLChildNode child = parent.internalGetFirstChildIfMaterialized();
                    if (child == null) {
                        nextNode = parent;
                        if (nodeState == CoreParentNode.STATE_CONSUMED) {
                            throw new NodeConsumedException();
                        }
                        inputContext = getDocument().internalGetInputContext(parent);
                        inputContext.setPassThroughHandler(handler);
                        state = STATE_PASS_THROUGH;
                    } else {
                        nextNode = child;
                        state = STATE_NOT_VISITED;
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
                        state = STATE_NOT_VISITED;
                    }
                } else {
                    LLChildNode sibling = previousChildNode.internalGetNextSiblingIfMaterialized();
                    if (sibling == null) {
                        LLParentNode parent = previousChildNode.internalGetParent();
                        nextNode = parent;
                        int nodeState = parent.internalGetState();
                        if (nodeState == CoreParentNode.STATE_EXPANDED) {
                            state = STATE_VISITED;
                        } else if (nodeState == CoreParentNode.STATE_CONSUMED) {
                            throw new NodeConsumedException();
                        } else {
                            inputContext = getDocument().internalGetInputContext(parent);
                            inputContext.setPassThroughHandler(handler);
                            state = STATE_PASS_THROUGH;
                        }
                    } else {
                        nextNode = sibling;
                        state = STATE_NOT_VISITED;
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
            
            // More closely examine the case where we move to a node that has not
            // been visited yet. It may be a sourced element or a leaf node
            if (state == STATE_NOT_VISITED) {
                if (nextNode instanceof LLElement) {
                    LLElement element = (LLElement)nextNode;
                    if (element.internalGetState() == CoreParentNode.STATE_SOURCE_SET
                            && !(preserve && ((XmlSource)element.coreGetContent()).isDestructive())) {
                        // TODO: this may give an unexpected result if the source contains other information items than the element
                        // TODO: should we also compare the name of the element from the source with what is specified in the CoreElement node?
                        stream = createStream(element, flush, handler);
                        state = STATE_STREAMING_SOURCE;
                    }
                } else if (!(nextNode instanceof LLParentNode)) {
                    state = STATE_LEAF;
                }
            }
            
            switch (state) {
                case STATE_START_FRAGMENT:
                    handler.startEntity(true, null);
                    break;
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
                    inputContext.next(false);
                    if (!inputContext.isPassThroughEnabled()) {
                        state = STATE_VISITED;
                        inputContext = null;
                    }
                    break;
                case STATE_STREAMING_CONTENT:
                    stream.proceed();
                    break;
                case STATE_STREAMING_SOURCE:
                    stream.proceed();
                    if (stream.isComplete()) {
                        state = STATE_VISITED;
                        stream = null;
                    }
                    break;
                default:
                    throw new IllegalStateException();
            }
            node = nextNode;
        } catch (DeferredParsingException ex) {
            throw ex.getStreamException();
        } catch (DeferredBuildingException ex) {
            // We get here if part of the tree has been consumed
            throw new CoreModelStreamException(ex);
        }
    }

    private Stream createStream(CoreParentNode node, final boolean flush, XmlHandler handler) throws StreamException {
        XmlSource source = (XmlSource)node.coreGetContent(); // TODO: should be an internal method
        if (log.isDebugEnabled()) {
            log.debug("Starting to stream content from " + source);
        }
        XmlSource.Hints hints = new XmlSource.Hints() {
            public boolean isPreferPush() {
                return flush;
            }
        };
        return new Stream(source.getInput(hints), new IncludeXmlOutput(handler));
    }
}
