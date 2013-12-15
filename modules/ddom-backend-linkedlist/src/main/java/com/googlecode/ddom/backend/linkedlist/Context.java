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
package com.googlecode.ddom.backend.linkedlist;

import com.googlecode.ddom.backend.ExtensionFactoryLocator;
import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLChildNode;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.CoreModelStreamException;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.ElementNameMismatchException;
import com.googlecode.ddom.core.ext.ModelExtensionMapper;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;

final class Context extends BuilderHandlerDelegate implements InputContext {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    private final BuilderHandlerDelegate parentContext;
    
    private LLParentNode targetNode;
    
    private String unresolvedElementPrefix;
    private String unresolvedElementLocalName;
    
    /**
     * The {@link XmlHandler} object to send events to if pass-through is enabled. See
     * {@link InputContext#setPassThroughHandler(XmlHandler)} for more details.
     */
    private XmlHandler passThroughHandler;
    
    /**
     * Tracks the nesting depth when pass-through is enabled.
     */
    private int passThroughDepth;
    
    private LLChildNode lastSibling; // The last child of the current node
    private String pendingText; // Text that has not yet been added to the tree
    
    private Attribute[] attributes;
    private int attributeCount;
    
    Context(Builder builder, BuilderHandlerDelegate parentContext, LLParentNode targetNode) {
        super(builder);
        this.parentContext = parentContext;
        init(targetNode);
    }

    void init(LLParentNode targetNode) {
        this.targetNode = targetNode;
    }
    
    public void next(boolean expand) throws DeferredBuildingException {
        builder.next(expand);
    }

    LLParentNode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(LLParentNode targetNode) {
        this.targetNode.internalSetState(CoreParentNode.STATE_EXPANDED);
        targetNode.internalSetState(CoreParentNode.STATE_CHILDREN_PENDING);
        this.targetNode = targetNode;
    }

    public void setPassThroughHandler(XmlHandler passThroughHandler) {
        if (this.passThroughHandler != null) {
            throw new IllegalStateException("A pass-through handler has already been set for this context");
        }
        targetNode.internalSetState(CoreParentNode.STATE_CONSUMED);
        this.passThroughHandler = passThroughHandler;
    }
    
    public boolean isPassThroughEnabled() {
        return passThroughHandler != null;
    }

    private BuilderHandlerDelegate decrementPassThroughDepth() {
        if (passThroughDepth == 0) {
            passThroughHandler = null;
            targetNode = null;
            lastSibling = null;
            return parentContext;
        } else {
            passThroughDepth--;
            return this;
        }
    }
    
    private void appendAttribute(Attribute attr) {
        if (attributes == null) {
            attributes = new Attribute[16];
        } else if (attributes.length == attributeCount) {
            Attribute[] newAttributes = new Attribute[attributes.length*2];
            System.arraycopy(attributes, 0, newAttributes, 0, attributes.length);
            attributes = newAttributes;
        }
        Element element = (Element)targetNode;
        if (element != null) {
            attr.setOwnerElement(element);
            if (attributeCount == 0) {
                element.setFirstAttribute(attr);
            } else {
                attributes[attributeCount-1].setNextAttribute(attr);
            }
        }
        attributes[attributeCount++] = attr;
    }
    
    private void refreshLastSibling() {
        if (lastSibling == null && targetNode.internalGetFirstChildIfMaterialized() != null
                || lastSibling != null && (lastSibling.coreGetParent() != targetNode || lastSibling.internalGetNextSiblingIfMaterialized() != null)) {
            // We get here if the children of the node being built have been modified
            // without building the node, e.g. if the previous node created by the builder has already been
            // detached or moved elsewhere (potentially as a child of the same parent, but
            // in a different position). This is possible because this is a type 2 builder.
            // If this happens, we need to get again to the last materialized child of the
            // node being built:
            lastSibling = null;
            LLChildNode child = targetNode.internalGetFirstChildIfMaterialized();
            while (child != null) {
                lastSibling = child;
                child = child.internalGetNextSiblingIfMaterialized();
            }
        }
    }
    
    private void appendSibling(LLChildNode node) {
        if (lastSibling == null) {
            targetNode.internalSetFirstChild(node);
        } else {
            lastSibling.internalSetNextSibling(node);
        }
        targetNode.internalNotifyChildrenModified(1);
        node.internalSetParent(targetNode);
        lastSibling = node;
    }
    
    private void flushPendingText() {
        if (pendingText != null) {
            // We only defer creation of the text node if the character data is not ignorable.
            // Therefore we can set ignorable=false here.
            appendSibling(new CharacterData(builder.document, pendingText, false));
            pendingText = null;
        }
    }
    
    private void appendNode(LLChildNode node) {
        refreshLastSibling();
        flushPendingText();
        appendSibling(node);
    }
    
    private void nodeCompleted() throws StreamException {
        if (pendingText != null) {
            refreshLastSibling();
            if (lastSibling == null) {
                targetNode.internalSetValue(pendingText);
                targetNode.internalNotifyChildrenModified(1);
                targetNode.internalSetState(CoreParentNode.STATE_VALUE_SET);
                pendingText = null;
            } else {
                flushPendingText();
                targetNode.internalSetState(CoreParentNode.STATE_EXPANDED);
            }
        } else {
            targetNode.internalSetState(CoreParentNode.STATE_EXPANDED);
        }
        targetNode = null;
        lastSibling = null;
    }
    
    void startEntity(boolean fragment, String inputEncoding) {
        // TODO: how to handle pass-through here??
        builder.document.coreSetInputEncoding(inputEncoding);
    }

    void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        // TODO: how to handle pass-through here??
        // TODO: this may actually set the information on the wrong node;
        //       e.g. if coreSetContent is used on a CoreElement, the owner document should not be updated
        builder.document.processXmlDeclaration(version, encoding, standalone);
    }

    void startDocumentTypeDeclaration(String rootName, String publicId, String systemId) throws StreamException {
        if (passThroughHandler == null) {
            appendNode(new DocumentTypeDeclaration(builder.document, rootName, publicId, systemId));
        } else {
            passThroughHandler.startDocumentTypeDeclaration(rootName, publicId, systemId);
        }
    }
    
    BuilderHandlerDelegate endDocumentTypeDeclaration() throws StreamException {
        if (passThroughHandler == null) {
            // TODO: CoreDocumentTypeDeclaration is not a parent node yet
            return this;
        } else {
            passThroughHandler.endDocumentTypeDeclaration();
            return this;
        }
    }

    BuilderHandlerDelegate startElement(String tagName) throws StreamException {
        if (passThroughHandler == null) {
            NSUnawareElement node = new NSUnawareElement(builder.document, tagName, false);
            appendNode(node);
            return newContext(node);
        } else {
            passThroughDepth++;
            passThroughHandler.startElement(tagName);
            return this;
        }
    }
    
    BuilderHandlerDelegate startElement(String namespaceURI, String localName, String prefix) throws StreamException {
        if (passThroughHandler == null) {
            ModelExtensionMapper modelExtensionMapper = builder.modelExtensionMapper;
            // TODO: we could be much smarter here; even though we don't know the namespace URI yet, in many cases, we know that the element must be a plain element (see e.g. SOAP body content)
            if (modelExtensionMapper != null && namespaceURI == null) {
                Context context = newContext(null);
                context.unresolvedElementPrefix = prefix;
                context.unresolvedElementLocalName = localName;
                return context;
            } else {
                Class<?> extensionInterface = modelExtensionMapper == null ? null : modelExtensionMapper.startElement(namespaceURI, localName);
                NSAwareElement node = nsAwareElementFactory.create(extensionInterface, builder.document, namespaceURI, localName, prefix, false);
                appendNode(node);
                return newContext(node);
            }
        } else {
            passThroughDepth++;
            passThroughHandler.startElement(namespaceURI, localName, prefix);
            return this;
        }
    }
    
    BuilderHandlerDelegate endElement() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            if (builder.modelExtensionMapper != null) {
                builder.modelExtensionMapper.endElement();
            }
            return parentContext;
        } else {
            passThroughHandler.endElement();
            return decrementPassThroughDepth();
        }
    }

    BuilderHandlerDelegate startAttribute(String name, String type) throws StreamException {
        if (passThroughHandler == null) {
            NSUnawareAttribute attr = new NSUnawareAttribute(builder.document, name, type, false);
            appendAttribute(attr);
            return newContext(attr);
        } else {
            passThroughDepth++;
            passThroughHandler.startAttribute(name, type);
            return this;
        }
    }

    BuilderHandlerDelegate startAttribute(String namespaceURI, String localName, String prefix, String type) throws StreamException {
        if (passThroughHandler == null) {
            NSAwareAttribute attr = new NSAwareAttribute(builder.document, namespaceURI, localName, prefix, type, false);
            appendAttribute(attr);
            return newContext(attr);
        } else {
            passThroughDepth++;
            passThroughHandler.startAttribute(namespaceURI, localName, prefix, type);
            return this;
        }
    }

    BuilderHandlerDelegate startNamespaceDeclaration(String prefix) throws StreamException {
        if (passThroughHandler == null) {
            NamespaceDeclaration attr = new NamespaceDeclaration(builder.document, prefix, false);
            appendAttribute(attr);
            return newContext(attr);
        } else {
            passThroughDepth++;
            passThroughHandler.startNamespaceDeclaration(prefix);
            return this;
        }
    }

    BuilderHandlerDelegate endAttribute() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            return parentContext;
        } else {
            passThroughHandler.endAttribute();
            return decrementPassThroughDepth();
        }
    }

    void resolveElementNamespace(String namespaceURI) throws StreamException {
        if (passThroughHandler == null) {
            if (targetNode == null) {
                ModelExtensionMapper modelExtensionMapper = builder.modelExtensionMapper;
                Class<?> extensionInterface = modelExtensionMapper == null ? null : modelExtensionMapper.startElement(namespaceURI, unresolvedElementLocalName);
                NSAwareElement element = nsAwareElementFactory.create(extensionInterface, builder.document, namespaceURI, unresolvedElementLocalName, unresolvedElementPrefix, false);
                targetNode = element;
                ((Context)parentContext).appendNode(element);
                unresolvedElementPrefix = null;
                unresolvedElementLocalName = null;
                for (int i=0; i<attributeCount; i++) {
                    Attribute attr = attributes[i];
                    attr.setOwnerElement(element);
                    if (i == 0) {
                        element.setFirstAttribute(attr);
                    } else {
                        attributes[i-1].setNextAttribute(attr);
                    }
                }
            } else {
                try {
                    ((NSAwareElement)targetNode).resolveNamespace(namespaceURI);
                } catch (ElementNameMismatchException ex) {
                    throw new CoreModelStreamException(ex);
                }
            }
        } else {
            passThroughHandler.resolveElementNamespace(namespaceURI);
        }
    }

    void resolveAttributeNamespace(int index, String namespaceURI) throws StreamException {
        if (passThroughHandler == null) {
            ((NSAwareAttribute)attributes[index]).resolveNamespace(namespaceURI);
        } else {
            passThroughHandler.resolveAttributeNamespace(index, namespaceURI);
        }
    }

    void attributesCompleted() throws StreamException {
        if (passThroughHandler == null) {
            attributeCount = 0;
            targetNode.internalSetState(CoreParentNode.STATE_CHILDREN_PENDING);
        } else {
            passThroughHandler.attributesCompleted();
        }
    }

    void processCharacterData(String data, boolean ignorable) throws StreamException {
        if (passThroughHandler == null) {
            // If the character data is ignorable whitespace, then we know that there will
            // be (very likely) at least one child element in addition to the text node
            if (!builder.expand && lastSibling == null && pendingText == null && !ignorable) {
                pendingText = data;
            } else {
                appendNode(new CharacterData(builder.document, data, ignorable));
            }
        } else {
            passThroughHandler.processCharacterData(data, ignorable);
        }
    }
    
    BuilderHandlerDelegate startProcessingInstruction(String target) throws StreamException {
        if (passThroughHandler == null) {
            ProcessingInstruction node = new ProcessingInstruction(builder.document, target, false);
            appendNode(node);
            return newContext(node);
        } else {
            passThroughDepth++;
            passThroughHandler.startProcessingInstruction(target);
            return this;
        }
    }
    
    BuilderHandlerDelegate endProcessingInstruction() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            return parentContext;
        } else {
            passThroughHandler.endProcessingInstruction();
            if (passThroughDepth == 0) {
                return parentContext;
            } else {
                passThroughDepth--;
                return this;
            }
        }
    }

    BuilderHandlerDelegate startComment() throws StreamException {
        if (passThroughHandler == null) {
            Comment node = new Comment(builder.document, false);
            appendNode(node);
            return newContext(node);
        } else {
            passThroughDepth++;
            passThroughHandler.startComment();
            return this;
        }
    }
    
    BuilderHandlerDelegate endComment() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            return parentContext;
        } else {
            passThroughHandler.endComment();
            return decrementPassThroughDepth();
        }
    }
    
    BuilderHandlerDelegate startCDATASection() throws StreamException {
        if (passThroughHandler == null) {
            CDATASection node = new CDATASection(builder.document, false);
            appendNode(node);
            return newContext(node);
        } else {
            passThroughDepth++;
            passThroughHandler.startCDATASection();
            return this;
        }
    }
    
    BuilderHandlerDelegate endCDATASection() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            return parentContext;
        } else {
            passThroughHandler.endCDATASection();
            return decrementPassThroughDepth();
        }
    }

    void processEntityReference(String name) throws StreamException {
        if (passThroughHandler == null) {
            appendNode(new EntityReference(builder.document, name));
        } else {
            passThroughHandler.processEntityReference(name);
        }
    }
    
    BuilderHandlerDelegate completed() throws StreamException {
        if (passThroughHandler == null) {
            nodeCompleted();
            return parentContext;
        } else {
            passThroughHandler.completed();
            // TODO: normally the pass-through depth should always be 0 here
            return decrementPassThroughDepth();
        }
    }
    
    void recycle() {
        targetNode = null;
        passThroughHandler = null;
        lastSibling = null;
    }
}