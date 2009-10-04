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
package com.google.code.ddom.dom.impl;

import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.dom.builder.PushConsumer;
import com.google.code.ddom.spi.model.BuilderTarget;
import com.google.code.ddom.spi.model.ChildNode;
import com.google.code.ddom.spi.model.DOMAttribute;
import com.google.code.ddom.spi.model.DOMDocument;
import com.google.code.ddom.spi.model.DOMElement;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.spi.stream.AttributeData;
import com.google.code.ddom.spi.stream.AttributeMode;
import com.google.code.ddom.spi.stream.CharacterData;
import com.google.code.ddom.spi.stream.Producer;
import com.google.code.ddom.spi.stream.StreamException;

// TODO: also allow for deferred building of attributes
public class Builder extends PushConsumer {
    private final Producer producer;
    private final NodeFactory nodeFactory;
    private final DOMDocument document;
    private StreamException streamException;
    private BuilderTarget parent;
    private ChildNode lastSibling;
    private DOMAttribute lastAttribute;
    private boolean nodeAppended;

    public Builder(Producer producer, NodeFactory nodeFactory, DOMDocument document, BuilderTarget target) {
        super(AttributeMode.EVENT);
        this.producer = producer;
        this.nodeFactory = nodeFactory;
        this.document = document;
        parent = target;
    }

    public final void next() throws DeferredParsingException {
        if (streamException == null) {
            try {
                nodeAppended = false; 
                do {
                    producer.proceed(this);
                } while (parent != null && !nodeAppended);
            } catch (StreamException ex) {
                streamException = ex;
            }
        }
        if (streamException != null) {
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }

    public final void processDocumentType(String rootName, String publicId, String systemId) {
        appendNode(nodeFactory.createDocumentType(document, rootName, publicId, systemId));
    }
    
    public final void processElement(String tagName, AttributeData attributes) {
        appendNode(nodeFactory.createElement(document, tagName, false));
    }
    
    public final void processElement(String namespaceURI, String localName, String prefix, AttributeData attributes) {
        appendNode(nodeFactory.createElement(document, namespaceURI, localName, prefix, false));
    }
    
    public final void processAttribute(String name, String value, String type) {
        appendAttribute(nodeFactory.createAttribute(document, name, value, type));
    }

    public final void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        appendAttribute(nodeFactory.createAttribute(document, namespaceURI, localName, prefix, value, type));
    }

    public final void processNSDecl(String prefix, String namespaceURI) {
        appendAttribute(nodeFactory.createNSDecl(document, prefix, namespaceURI));
    }

    public void attributesCompleted() {
        nodeAppended = true;
    }

    public final void processProcessingInstruction(String target, CharacterData data) {
        try {
            appendNode(nodeFactory.createProcessingInstruction(document, target, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processText(CharacterData data) {
        try {
            appendNode(nodeFactory.createText(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processComment(CharacterData data) {
        try {
            appendNode(nodeFactory.createComment(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processCDATASection(CharacterData data) {
        try {
            appendNode(nodeFactory.createCDATASection(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processEntityReference(String name) {
        appendNode(nodeFactory.createEntityReference(document, name));
    }
    
    private void appendNode(ChildNode node) {
        if (lastSibling == null) {
            parent.internalSetFirstChild(node);
        } else {
            lastSibling.internalSetNextSibling(node);
        }
        parent.notifyChildrenModified(1);
        node.internalSetParent(parent);
        if (node instanceof DOMElement) {
            // TODO: this assumes that elements are always created as incomplete
            parent = (DOMElement)node;
            lastSibling = null;
        } else {
            lastSibling = node;
            nodeAppended = true;
        }
        lastAttribute = null;
    }
    
    private void appendAttribute(DOMAttribute attr) {
        DOMElement element = (DOMElement)parent;
        if (lastAttribute == null) {
            element.internalSetFirstAttribute(attr);
        } else {
            lastAttribute.internalSetNextAttribute(attr);
        }
        attr.internalSetOwnerElement(element);
        lastAttribute = attr;
    }
    
    public final void nodeCompleted() {
        if (parent instanceof ChildNode) {
            lastSibling = (ChildNode)parent;
        }
        parent.internalSetComplete();
        // TODO: get rid of cast here
        parent = (BuilderTarget)parent.getParentNode();
    }

    public final void dispose() {
        producer.dispose();
    }
}
