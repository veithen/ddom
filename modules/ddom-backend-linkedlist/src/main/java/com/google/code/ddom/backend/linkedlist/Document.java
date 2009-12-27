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
import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentType;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.core.builder.Builder;
import com.google.code.ddom.stream.spi.FragmentSource;
import com.google.code.ddom.stream.spi.Producer;

@Implementation
public class Document extends BuilderWrapperImpl implements CoreDocument {
    // TODO: since we are now using a weaver, it should no longer be necessary to have a reference to the node factory
    private final NodeFactory nodeFactory;
    private final FragmentSource source;
    private Builder builder;
    private boolean complete;
    private CoreChildNode firstChild;
    private int children;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private String standalone;
    private String documentURI;

    public Document(NodeFactory nodeFactory, FragmentSource source) {
        this.nodeFactory = nodeFactory;
        this.source = source;
        complete = source == null;
    }

    public final void next() throws DeferredParsingException {
        if (builder == null) {
            builder = new Builder(source.getProducer(), nodeFactory, this, this);
        }
        builder.next();
    }
    
    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public final CoreDocument getDocument() {
        return this;
    }

    public final boolean isComplete() {
        return complete;
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
        complete = true;
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
    protected void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException {
        // TODO: character data is also not allowed in DOM, but is allowed in Axiom; need to handle this somewhere!
        if (newChild instanceof CoreDocumentType) {
            if (!(replacedChild instanceof CoreDocumentType || coreGetDocumentType() == null)) {
                throw new ChildTypeNotAllowedException("The document already has a document type node");
            }
        } else if (newChild instanceof CoreElement) {
            if (!(replacedChild instanceof CoreElement || coreGetDocumentElement() == null)) {
                throw new ChildTypeNotAllowedException("The document already has a document element");
            }
        }
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

    public String coreGetXmlVersion() {
        return xmlVersion;
    }

    public void coreSetXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    public final String coreGetXmlEncoding() {
        return xmlEncoding;
    }

    public final void coreSetXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    public String coreGetStandalone() {
        return standalone;
    }

    public void coreSetStandalone(String standalone) {
        this.standalone = standalone;
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
        // TODO: we know that the document type node must appear before the document element; use this to avoid expansion of the complete document
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentType)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentType)child;
    }
}
