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
package com.google.code.ddom.backend.linkedlist;

import com.google.code.ddom.backend.ExtensionFactoryLocator;
import com.google.code.ddom.backend.linkedlist.intf.LLBuilder;
import com.google.code.ddom.backend.linkedlist.intf.LLChildNode;
import com.google.code.ddom.backend.linkedlist.intf.LLParentNode;
import com.google.code.ddom.collections.ArrayStack;
import com.google.code.ddom.collections.Stack;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.core.ext.ModelExtensionMapper;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.spi.XmlHandler;
import com.google.code.ddom.stream.spi.XmlInput;
import com.google.code.ddom.stream.spi.XmlOutput;

// TODO: also allow for deferred building of attributes
public class Builder extends XmlOutput implements LLBuilder {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    private final XmlInput input; // TODO: not sure if we still need this
    private final ModelExtensionMapper modelExtensionMapper;
    private final Document document;
    private final Stack<LLParentNode> nodeStack = new ArrayStack<LLParentNode>();
    private StreamException streamException;
    private LLParentNode parent; // The current node being built
    private LLChildNode lastSibling; // The last child of the current node
    private Attribute lastAttribute;
    private String pendingText; // Text that has not yet been added to the tree
    private boolean nodeAppended;
    
    /**
     * The {@link XmlHandler} object to send events to if pass-through is enabled. See
     * {@link LLBuilder#setPassThroughHandler(XmlHandler)} for more details.
     */
    private XmlHandler passThroughHandler;
    
    /**
     * Tracks the nesting depth when pass-through is enabled.
     */
    private int passThroughDepth;

    public Builder(XmlInput input, ModelExtension modelExtension, Document document, LLParentNode target) {
        this.input = input;
        modelExtensionMapper = modelExtension.newMapper();
        this.document = document;
        parent = target;
    }

    public final boolean isBuilderFor(LLParentNode target) {
        return target == parent || nodeStack.contains(target);
    }

    public final boolean migrateBuilder(LLParentNode from, LLParentNode to) {
        if (parent == from) {
            parent = to;
            return true;
        } else {
            return nodeStack.replace(from, to);
        }
    }
    
    public final void next() throws DeferredParsingException {
        if (streamException == null) {
            try {
                nodeAppended = false; 
                do {
                    input.proceed();
                } while (parent != null && !nodeAppended);
            } catch (StreamException ex) {
                streamException = ex;
            }
        }
        if (streamException != null) {
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }

    public final void setPassThroughHandler(XmlHandler handler) {
        passThroughHandler = handler;
        passThroughDepth = 0;
    }

    protected final void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        // TODO: how to handle pass-through here??
        document.coreSetXmlVersion(xmlVersion);
        document.coreSetXmlEncoding(xmlEncoding);
        document.coreSetInputEncoding(inputEncoding);
        document.coreSetStandalone(standalone);
    }

    protected final void processDocumentType(String rootName, String publicId, String systemId) {
        if (passThroughHandler == null) {
            appendNode(new DocumentTypeDeclaration(document, rootName, publicId, systemId));
        } else {
            passThroughHandler.processDocumentType(rootName, publicId, systemId);
        }
    }
    
    protected final void processElement(String tagName) {
        if (passThroughHandler == null) {
            appendNode(new NSUnawareElement(document, tagName, false));
        } else {
            passThroughDepth++;
            passThroughHandler.processElement(tagName);
        }
    }
    
    protected final void processElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (passThroughHandler == null) {
            Class<?> extensionInterface = modelExtensionMapper.startElement(namespaceURI, localName);
            appendNode(nsAwareElementFactory.create(extensionInterface, document, namespaceURI, localName, prefix, false));
        } else {
            passThroughDepth++;
            passThroughHandler.processElement(namespaceURI, localName, prefix);
        }
    }
    
    protected final void processAttribute(String name, String value, String type) {
        if (passThroughHandler == null) {
            appendAttribute(new NSUnawareAttribute(document, name, value, type));
        } else {
            passThroughHandler.processAttribute(name, value, type);
        }
    }

    protected final void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        if (passThroughHandler == null) {
            appendAttribute(new NSAwareAttribute(document, namespaceURI, localName, prefix, value, type));
        } else {
            passThroughHandler.processAttribute(namespaceURI, localName, prefix, value, type);
        }
    }

    protected final void processNamespaceDeclaration(String prefix, String namespaceURI) {
        if (passThroughHandler == null) {
            appendAttribute(new NamespaceDeclaration(document, prefix, namespaceURI));
        } else {
            passThroughHandler.processNamespaceDeclaration(prefix, namespaceURI);
        }
    }

    protected final void attributesCompleted() {
        if (passThroughHandler == null) {
            nodeAppended = true;
        } else {
            passThroughHandler.attributesCompleted();
        }
    }

    protected final void processProcessingInstruction(String target, String data) {
        if (passThroughHandler == null) {
            appendNode(new ProcessingInstruction(document, target, data));
        } else {
            passThroughHandler.processProcessingInstruction(target, data);
        }
    }
    
    protected final void processText(String data) {
        if (passThroughHandler == null) {
            if (lastSibling == null && pendingText == null) {
                pendingText = data;
            } else {
                appendNode(new Text(document, data));
            }
        } else {
            passThroughHandler.processText(data);
        }
    }
    
    protected final void processComment(String data) {
        if (passThroughHandler == null) {
            appendNode(new Comment(document, data));
        } else {
            passThroughHandler.processComment(data);
        }
    }
    
    protected final void processCDATASection() {
        if (passThroughHandler == null) {
            appendNode(new CDATASection(document));
        } else {
            passThroughDepth++;
            passThroughHandler.processCDATASection();
        }
    }
    
    protected final void processEntityReference(String name) {
        if (passThroughHandler == null) {
            appendNode(new EntityReference(document, name));
        } else {
            passThroughHandler.processEntityReference(name);
        }
    }
    
    private void refreshLastSibling() {
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
            nodeStack.push(parent);
            parent = (Container)node;
            lastSibling = null;
        } else {
            nodeAppended = true;
        }
        lastAttribute = null;
    }
    
    private void appendAttribute(Attribute attr) {
        Element element = (Element)parent;
        if (lastAttribute == null) {
            element.internalAppendAttribute(attr);
        } else {
            lastAttribute.insertAttributeAfter(attr);
        }
        lastAttribute = attr;
    }
    
    protected final void nodeCompleted() throws StreamException {
        boolean pop;
        if (passThroughHandler == null) {
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
            pop = true;
        } else {
            // TODO: do we need to check pendingText???
            passThroughHandler.nodeCompleted();
            if (passThroughDepth == 0) {
                pop = true;
                passThroughHandler = null;
            } else {
                passThroughDepth--;
                pop = false;
            }
        }
        if (pop) {
            if (nodeStack.isEmpty()) {
                parent = null;
            } else {
                lastSibling = (LLChildNode)parent;
                // This is important for being a builder of type 2: instead of getting the
                // parent from the current node, we get it from the node stack.
                parent = nodeStack.pop();
            }
        }
    }

    public final void dispose() {
        input.dispose();
    }
}
