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
package com.googlecode.ddom.backend.linkedlist;

import com.google.code.ddom.collections.ObjectStack;
import com.googlecode.ddom.backend.ExtensionFactoryLocator;
import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.core.ext.ModelExtensionMapper;
import com.googlecode.ddom.stream.SimpleXmlOutput;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;

// TODO: also allow for deferred building of attributes
public class Builder extends SimpleXmlOutput {
    final class Context implements InputContext {
        private LLParentNode targetNode;
        
        /**
         * The {@link XmlHandler} object to send events to if pass-through is enabled. See
         * {@link InputContext#setPassThroughHandler(XmlHandler)} for more details.
         */
        private XmlHandler passThroughHandler;
        
        void init(LLParentNode targetNode) {
            this.targetNode = targetNode;
        }
        
        public void next() throws DeferredParsingException {
            Builder.this.next();
        }

        LLParentNode getTargetNode() {
            return targetNode;
        }

        public void setTargetNode(LLParentNode targetNode) {
            this.targetNode.internalSetState(Flags.STATE_EXPANDED);
            targetNode.internalSetState(Flags.STATE_CHILDREN_PENDING);
            this.targetNode = targetNode;
        }

        XmlHandler getPassThroughHandler() {
            return passThroughHandler;
        }

        public void setPassThroughHandler(XmlHandler passThroughHandler) {
            if (this.passThroughHandler != null) {
                throw new IllegalStateException("A pass-through handler has already been set for this context");
            }
            this.passThroughHandler = passThroughHandler;
            targetNode = null;
        }
        
        public boolean isPassThroughEnabled() {
            return passThroughHandler != null;
        }

        void recycle() {
            targetNode = null;
            passThroughHandler = null;
        }
    }
    
    private static final int FRAGMENT = 0;
    private static final int ELEMENT = 1;
    private static final int ATTRIBUTE = 2;
    private static final int CDATA_SECTION = 3;
    private static final int COMMENT = 4;
    private static final int PROCESSING_INSTRUCTION = 5;
    
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    private final XmlInput input; // TODO: not sure if we still need this
    private final ModelExtensionMapper modelExtensionMapper;
    private final Document document;
    private final ObjectStack<Context> contextStack = new ObjectStack<Context>() {
        @Override
        protected Context createObject() {
            return new Context();
        }

        @Override
        protected void recycleObject(Context context) {
            context.recycle();
        }
    };
    private StreamException streamException;
    
    /**
     * The current input context. This is always the top element of {@link #contextStack}.
     */
    private Context context;
    
    private LLChildNode lastSibling; // The last child of the current node
    private Attribute lastAttribute;
    private String pendingText; // Text that has not yet been added to the tree
    private boolean nodeAppended;
    
    /**
     * Tracks the nesting depth when pass-through is enabled.
     */
    private int passThroughDepth;

    public Builder(XmlInput input, ModelExtension modelExtension, Document document, LLParentNode target) {
        this.input = input;
        modelExtensionMapper = modelExtension.newMapper();
        this.document = document;
        context = contextStack.allocate();
        context.init(target);
    }

    public final InputContext getInputContext(LLParentNode target) {
        for (int i = 0, s = contextStack.size(); i<s; i++) {
            Context context = contextStack.get(i);
            if (context.getTargetNode() == target) {
                return context;
            }
        }
        return null;
    }
    
    final void next() throws DeferredParsingException {
        if (streamException == null) {
            try {
                nodeAppended = false; 
                do {
                    getStream().proceed();
                } while (context != null && !nodeAppended);
            } catch (StreamException ex) {
                streamException = ex;
            }
        }
        if (streamException != null) {
            throw new DeferredParsingException(streamException);
        }
    }

    @Override
    protected final void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        // TODO: how to handle pass-through here??
        document.coreSetXmlVersion(xmlVersion);
        document.coreSetXmlEncoding(xmlEncoding);
        document.coreSetInputEncoding(inputEncoding);
        document.coreSetStandalone(standalone);
    }

    @Override
    protected final void processDocumentType(String rootName, String publicId, String systemId, String data) {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new DocumentTypeDeclaration(document, rootName, publicId, systemId, data));
        } else {
            passThroughHandler.processDocumentType(rootName, publicId, systemId, data);
        }
    }
    
    @Override
    protected final void startElement(String tagName) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new NSUnawareElement(document, tagName, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startElement(tagName);
        }
    }
    
    @Override
    protected final void startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            Class<?> extensionInterface = modelExtensionMapper.startElement(namespaceURI, localName);
            appendNode(nsAwareElementFactory.create(extensionInterface, document, namespaceURI, localName, prefix, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startElement(namespaceURI, localName, prefix);
        }
    }
    
    @Override
    protected final void endElement() throws StreamException {
        nodeCompleted(ELEMENT);
    }

    @Override
    protected final void startAttribute(String name, String type) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendAttribute(new NSUnawareAttribute(document, name, type, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startAttribute(name, type);
        }
    }

    @Override
    protected final void startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendAttribute(new NSAwareAttribute(document, namespaceURI, localName, prefix, type, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startAttribute(namespaceURI, localName, prefix, type);
        }
    }

    @Override
    protected final void startNamespaceDeclaration(String prefix) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendAttribute(new NamespaceDeclaration(document, prefix, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startNamespaceDeclaration(prefix);
        }
    }

    @Override
    protected final void endAttribute() throws StreamException {
        nodeCompleted(ATTRIBUTE);
    }

    @Override
    protected final void attributesCompleted() throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            context.getTargetNode().internalSetState(Flags.STATE_CHILDREN_PENDING);
            nodeAppended = true;
        } else {
            passThroughHandler.attributesCompleted();
        }
    }

    @Override
    protected final void processCharacterData(String data, boolean ignorable) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            // If the character data is ignorable whitespace, then we know that there will
            // be (very likely) at least one child element in addition to the text node
            if (lastSibling == null && pendingText == null && !ignorable) {
                pendingText = data;
            } else {
                appendNode(new CharacterData(document, data, ignorable));
            }
        } else {
            passThroughHandler.processCharacterData(data, ignorable);
        }
    }
    
    @Override
    protected final void startProcessingInstruction(String target) throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new ProcessingInstruction(document, target, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startProcessingInstruction(target);
        }
    }
    
    @Override
    protected final void endProcessingInstruction() throws StreamException {
        nodeCompleted(PROCESSING_INSTRUCTION);
    }

    @Override
    protected final void startComment() throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new Comment(document, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startComment();
        }
    }
    
    @Override
    protected final void endComment() throws StreamException {
        nodeCompleted(COMMENT);
    }
    
    @Override
    protected final void startCDATASection() throws StreamException {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new CDATASection(document, false));
        } else {
            passThroughDepth++;
            passThroughHandler.startCDATASection();
        }
    }
    
    @Override
    protected final void endCDATASection() throws StreamException {
        nodeCompleted(CDATA_SECTION);
    }

    @Override
    protected final void processEntityReference(String name) {
        XmlHandler passThroughHandler = context.getPassThroughHandler();
        if (passThroughHandler == null) {
            appendNode(new EntityReference(document, name));
        } else {
            passThroughHandler.processEntityReference(name);
        }
    }
    
    private void refreshLastSibling() {
        LLParentNode parent = context.getTargetNode();
        if (lastSibling == null && parent.internalGetFirstChildIfMaterialized() != null
                || lastSibling != null && (lastSibling.coreGetParent() != parent || lastSibling.internalGetNextSiblingIfMaterialized() != null)) {
            // We get here if the children of the node being built have been modified
            // without building the node, e.g. if the previous node created by the builder has already been
            // detached or moved elsewhere (potentially as a child of the same parent, but
            // in a different position). This is possible because this is a type 2 builder.
            // If this happens, we need to get again to the last materialized child of the
            // node being built:
            lastSibling = null;
            LLChildNode child = parent.internalGetFirstChildIfMaterialized();
            while (child != null) {
                lastSibling = child;
                child = child.internalGetNextSiblingIfMaterialized();
            }
        }
    }
    
    private void appendSibling(LLChildNode node) {
        LLParentNode parent = context.getTargetNode();
        if (lastSibling == null) {
            parent.internalSetFirstChild(node);
        } else {
            lastSibling.internalSetNextSibling(node);
        }
        parent.internalNotifyChildrenModified(1);
        node.internalSetParent(parent);
        lastSibling = node;
    }
    
    private void flushPendingText() {
        if (pendingText != null) {
            // We only defer creation of the text node if the character data is not ignorable.
            // Therefore we can set ignorable=false here.
            appendSibling(new CharacterData(document, pendingText, false));
            pendingText = null;
        }
    }
    
    private void appendNode(LLChildNode node) {
        refreshLastSibling();
        flushPendingText();
        appendSibling(node);
        if (node instanceof Container) {
            // TODO: this assumes that elements are always created as incomplete
            context = contextStack.allocate();
            context.init((Container)node);
            lastSibling = null;
        } else {
            nodeAppended = true;
        }
        lastAttribute = null;
    }
    
    private void appendAttribute(Attribute attr) {
        Element element = (Element)context.getTargetNode();
        if (lastAttribute == null) {
            element.internalAppendAttribute(attr);
        } else {
            lastAttribute.insertAttributeAfter(attr);
        }
        context = contextStack.allocate();
        context.init(attr);
        lastAttribute = attr;
    }
    
    @Override
    protected final void completed() throws StreamException {
        nodeCompleted(FRAGMENT);
    }
    
    private void nodeCompleted(int nodeType) throws StreamException {
        LLParentNode parent = context.getTargetNode();
        boolean pop;
        if (parent != null) {
            if (pendingText != null) {
                refreshLastSibling();
                if (lastSibling == null) {
                    parent.internalSetValue(pendingText);
                    parent.internalNotifyChildrenModified(1);
                    parent.internalSetState(Flags.STATE_VALUE_SET);
                    pendingText = null;
                } else {
                    flushPendingText();
                    parent.internalSetState(Flags.STATE_EXPANDED);
                }
            } else {
                parent.internalSetState(Flags.STATE_EXPANDED);
            }
            // TODO: this only applies to namespace aware elements!
            if (nodeType == ELEMENT) {
                modelExtensionMapper.endElement();
            }
            pop = true;
        } else {
            XmlHandler passThroughHandler = context.getPassThroughHandler();
            // TODO: do we need to check pendingText???
            switch (nodeType) {
                case FRAGMENT:
                    passThroughHandler.completed();
                    break;
                case ELEMENT:
                    passThroughHandler.endElement();
                    break;
                case ATTRIBUTE:
                    passThroughHandler.endAttribute();
                    break;
                case PROCESSING_INSTRUCTION:
                    passThroughHandler.endProcessingInstruction();
                    break;
                case COMMENT:
                    passThroughHandler.endComment();
                    break;
                case CDATA_SECTION:
                    passThroughHandler.endCDATASection();
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (passThroughDepth == 0) {
                pop = true;
            } else {
                passThroughDepth--;
                pop = false;
            }
        }
        if (pop) {
            contextStack.pop();
            if (contextStack.isEmpty()) {
                context = null;
            } else {
                if (nodeType != ATTRIBUTE) {
                    lastSibling = (LLChildNode)parent;
                }
                // This is important for being a builder of type 2: instead of getting the
                // parent from the current node, we get it from the stack.
                context = contextStack.peek();
            }
            passThroughDepth = 0;
        }
    }

    public final void dispose() {
        input.dispose();
    }
}
