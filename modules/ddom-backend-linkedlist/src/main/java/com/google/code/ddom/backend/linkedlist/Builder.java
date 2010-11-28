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

import com.google.code.ddom.backend.ExtensionFactoryLocator;
import com.google.code.ddom.collections.ArrayStack;
import com.google.code.ddom.collections.ObjectStack;
import com.google.code.ddom.collections.Stack;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.core.ext.ModelExtensionMapper;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlInput;
import com.google.code.ddom.stream.spi.XmlOutput;

// TODO: also allow for deferred building of attributes
public class Builder extends XmlOutput {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    private final XmlInput input; // TODO: not sure if we still need this
    private final ModelExtensionMapper modelExtensionMapper;
    private final Document document;
    private final ObjectStack<BuilderState> stateStack = new ObjectStack<BuilderState>() {
        @Override
        protected BuilderState createObject() {
            return new BuilderState();
        }

        @Override
        protected void recycleObject(BuilderState state) {
            state.setNode(null);
            state.setHandler(null);
        }
    };
    private StreamException streamException;
    private BuilderState state; // The current node being built
    private LLChildNode lastSibling; // The last child of the current node
    private Attribute lastAttribute;
    private String pendingText; // Text that has not yet been added to the tree
    private boolean nodeAppended;

    public Builder(XmlInput input, ModelExtension modelExtension, Document document, LLParentNode target) {
        this.input = input;
        modelExtensionMapper = modelExtension.newMapper();
        this.document = document;
        state = stateStack.allocate();
        state.setNode(target);
    }

    public final boolean isBuilderFor(LLParentNode target) {
        for (int i = 0, s = stateStack.size(); i<s; i++) {
            if (stateStack.get(i).getNode() == target) {
                return true;
            }
        }
        return false;
    }

    public final boolean migrateBuilder(LLParentNode from, LLParentNode to) {
        for (int i = 0, s = stateStack.size(); i<s; i++) {
            BuilderState state = stateStack.get(i);
            if (state.getNode() == from) {
                state.setNode(to);
                return true;
            }
        }
        return false;
    }
    
    public final void next() throws DeferredParsingException {
        if (streamException == null) {
            try {
                nodeAppended = false; 
                do {
                    input.proceed();
                } while (state != null && !nodeAppended);
            } catch (StreamException ex) {
                streamException = ex;
            }
        }
        if (streamException != null) {
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }

    protected final void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        document.coreSetXmlVersion(xmlVersion);
        document.coreSetXmlEncoding(xmlEncoding);
        document.coreSetInputEncoding(inputEncoding);
        document.coreSetStandalone(standalone);
    }

    protected final void processDocumentType(String rootName, String publicId, String systemId) {
        appendNode(new DocumentTypeDeclaration(document, rootName, publicId, systemId));
    }
    
    protected final void processElement(String tagName) {
        appendNode(new NSUnawareElement(document, tagName, false));
    }
    
    protected final void processElement(String namespaceURI, String localName, String prefix) {
        Class<?> extensionInterface = modelExtensionMapper.startElement(namespaceURI, localName);
        appendNode(nsAwareElementFactory.create(extensionInterface, document, namespaceURI, localName, prefix, false));
    }
    
    protected final void processAttribute(String name, String value, String type) {
        appendAttribute(new NSUnawareAttribute(document, name, value, type));
    }

    protected final void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        appendAttribute(new NSAwareAttribute(document, namespaceURI, localName, prefix, value, type));
    }

    protected final void processNamespaceDeclaration(String prefix, String namespaceURI) {
        appendAttribute(new NamespaceDeclaration(document, prefix, namespaceURI));
    }

    protected final void attributesCompleted() {
        nodeAppended = true;
    }

    protected final void processProcessingInstruction(String target, String data) {
        appendNode(new ProcessingInstruction(document, target, data));
    }
    
    protected final void processText(String data) {
        if (lastSibling == null && pendingText == null) {
            pendingText = data;
        } else {
            appendNode(new Text(document, data));
        }
    }
    
    protected final void processComment(String data) {
        appendNode(new Comment(document, data));
    }
    
    protected final void processCDATASection() {
        appendNode(new CDATASection(document));
    }
    
    protected final void processEntityReference(String name) {
        appendNode(new EntityReference(document, name));
    }
    
    private void refreshLastSibling() {
        LLParentNode parent = state.getNode();
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
        LLParentNode parent = state.getNode();
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
            appendSibling(new Text(document, pendingText));
            pendingText = null;
        }
    }
    
    private void appendNode(LLChildNode node) {
        refreshLastSibling();
        flushPendingText();
        appendSibling(node);
        if (node instanceof Container) {
            // TODO: this assumes that elements are always created as incomplete
            state = stateStack.allocate();
            state.setNode((Container)node);
            lastSibling = null;
        } else {
            nodeAppended = true;
        }
        lastAttribute = null;
    }
    
    private void appendAttribute(Attribute attr) {
        Element element = (Element)state.getNode();
        if (lastAttribute == null) {
            element.internalAppendAttribute(attr);
        } else {
            lastAttribute.insertAttributeAfter(attr);
        }
        lastAttribute = attr;
    }
    
    protected final void nodeCompleted() {
        LLParentNode parent = state.getNode();
        if (pendingText != null) {
            refreshLastSibling();
            if (lastSibling == null) {
                parent.internalSetValue(pendingText);
                parent.internalNotifyChildrenModified(1);
                pendingText = null;
            } else {
                flushPendingText();
            }
        }
        modelExtensionMapper.endElement(); // TODO: not entirely correct
        parent.internalSetComplete(true);
        stateStack.pop();
        if (stateStack.isEmpty()) {
            state = null;
        } else {
            lastSibling = (LLChildNode)parent;
            // This is important for being a builder of type 2: instead of getting the
            // parent from the current node, we get it from the node stack.
            state = stateStack.peek();
        }
    }

    public final void dispose() {
        input.dispose();
    }
}
