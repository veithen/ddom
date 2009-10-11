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

import org.w3c.dom.DOMImplementation;

import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreDocumentType;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.spi.stream.Producer;

public class DocumentImpl extends ParentNodeImpl implements CoreDocument {
    private final NodeFactory nodeFactory;
    private Builder builder;
    private CoreChildNode firstChild;
    private int children;
    private String inputEncoding;
    private String xmlEncoding;
    private String documentURI;

    public DocumentImpl(NodeFactory nodeFactory, Producer producer) {
        this.nodeFactory = nodeFactory;
        if (producer == null) {
            builder = null;
        } else {
            builder = new Builder(producer, nodeFactory, this, this);
        }
    }

    public final void next() throws DeferredParsingException {
        builder.next();
    }
    
    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public final CoreDocument getDocument() {
        return this;
    }

    public final boolean isComplete() {
        return builder == null;
    }
    
    public final void build() {
        BuilderTargetHelper.build(this);
    }
    
    public final void dispose() {
        if (builder != null) {
            builder.dispose();
        }
    }

    public final void internalSetComplete() {
        builder.dispose();
        builder = null;
    }
    
    public final void internalSetFirstChild(CoreChildNode child) {
        firstChild = child;
    }

    public final CoreChildNode coreGetFirstChild() {
        if (firstChild == null && !isComplete()) {
            next();
        }
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(CoreChildNode newChild) {
        // TODO
    }

    public final int coreGetChildCount() {
        build();
        return children;
    }

    public final String coreGetInputEncoding() {
        return inputEncoding;
    }

    public final void coreSetInputEncoding(String inputEncoding) {
        this.inputEncoding = inputEncoding;
    }

    public final String coreGetXmlEncoding() {
        return xmlEncoding;
    }

    public final void coreSetXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    // TODO: need test for this
    public final String coreGetDocumentURI() {
        return documentURI;
    }

    public final void coreSetDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public final CoreElement coreGetDocumentElement() {
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreElement)) {
            child = child.coreGetNextSibling();
        }
        return (CoreElement)child;
    }

    public final CoreDocumentType coreGetDocumentType() {
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentType)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentType)child;
    }
}
