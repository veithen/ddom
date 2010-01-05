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

import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.backend.BuilderTarget;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.stream.spi.CharacterData;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.StreamException;
import com.google.code.ddom.stream.util.CallbackConsumer;

// TODO: also allow for deferred building of attributes
public class Builder extends CallbackConsumer {
    private final Producer producer;
    private final CoreDocument document;
    private StreamException streamException;
    private BuilderTarget parent;
    private CoreChildNode lastSibling;
    private CoreAttribute lastAttribute;
    private boolean nodeAppended;

    public Builder(Producer producer, CoreDocument document, BuilderTarget target) {
        this.producer = producer;
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

    public void setDocumentInfo(String xmlVersion, String xmlEncoding, String inputEncoding, boolean standalone) {
        document.coreSetXmlVersion(xmlVersion);
        document.coreSetXmlEncoding(xmlEncoding);
        document.coreSetInputEncoding(inputEncoding);
        document.coreSetStandalone(standalone);
    }

    public final void processDocumentType(String rootName, String publicId, String systemId) {
        appendNode(new DocumentType(document, rootName, publicId, systemId));
    }
    
    public final void processElement(String tagName) {
        appendNode(new NSUnawareElement(document, tagName, false));
    }
    
    public final void processElement(String namespaceURI, String localName, String prefix) {
        appendNode(new NSAwareElement(document, namespaceURI, localName, prefix, false));
    }
    
    public final void processAttribute(String name, String value, String type) {
        appendAttribute(new NSUnawareAttribute(document, name, value, type));
    }

    public final void processAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        appendAttribute(new NSAwareAttribute(document, namespaceURI, localName, prefix, value, type));
    }

    public final void processNSDecl(String prefix, String namespaceURI) {
        appendAttribute(new NamespaceDeclaration(document, prefix, namespaceURI));
    }

    public void attributesCompleted() {
        nodeAppended = true;
    }

    public final void processProcessingInstruction(String target, CharacterData data) {
        try {
            appendNode(new ProcessingInstruction(document, target, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processText(CharacterData data) {
        try {
            appendNode(new Text(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processComment(CharacterData data) {
        try {
            appendNode(new Comment(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processCDATASection(CharacterData data) {
        try {
            appendNode(new CDATASection(document, data.getString()));
        } catch (StreamException ex) {
            streamException = ex;
            throw new DeferredParsingException(streamException.getMessage(), streamException.getCause());
        }
    }
    
    public final void processEntityReference(String name) {
        appendNode(new EntityReference(document, name));
    }
    
    private void appendNode(CoreChildNode node) {
        if (lastSibling == null) {
            parent.internalSetFirstChild(node);
        } else {
            lastSibling.internalSetNextSibling(node);
        }
        parent.notifyChildrenModified(1);
        node.internalSetParent(parent);
        if (node instanceof CoreElement) {
            // TODO: this assumes that elements are always created as incomplete
            parent = (CoreElement)node;
            lastSibling = null;
        } else {
            lastSibling = node;
            nodeAppended = true;
        }
        lastAttribute = null;
    }
    
    private void appendAttribute(CoreAttribute attr) {
        CoreElement element = (CoreElement)parent;
        if (lastAttribute == null) {
            element.coreAppendAttribute(attr);
        } else {
            lastAttribute.coreInsertAttributeAfter(attr);
        }
        lastAttribute = attr;
    }
    
    public final void nodeCompleted() {
        parent.internalSetComplete();
        if (parent instanceof CoreChildNode) {
            lastSibling = (CoreChildNode)parent;
            // TODO: get rid of cast here
            parent = (BuilderTarget)lastSibling.coreGetParent(); 
        } else {
            parent = null;
        }
    }

    public final void dispose() {
        producer.dispose();
    }
}