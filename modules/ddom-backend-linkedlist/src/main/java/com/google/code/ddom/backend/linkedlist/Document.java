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

import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentType;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.stream.spi.FragmentSource;
import com.google.code.ddom.stream.spi.SymbolHashTable;
import com.google.code.ddom.stream.spi.Symbols;

@Implementation
public class Document extends BuilderWrapperImpl implements CoreDocument {
    // TODO: since we are now using a weaver, it should no longer be necessary to have a reference to the node factory
    private final NodeFactory nodeFactory;
    private final Symbols symbols;
    private Builder builder;
    private boolean complete;
    private CoreChildNode firstChild;
    private int children;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private boolean standalone;
    private String documentURI;

    public Document(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
        complete = true;
        symbols = new SymbolHashTable();
    }

    public final void coreSetContent(FragmentSource source) {
        // TODO: need to clear any existing content!
        complete = false;
        builder = new Builder(source.getProducer(), this, this);
        // TODO: need to decide how to handle symbol tables in a smart way here
//        symbols = producer.getSymbols();
    }

    public final void next() throws DeferredParsingException {
        builder.next();
    }
    
    public final NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public final Symbols getSymbols() {
        return symbols;
    }

    public final CoreDocument getDocument() {
        return this;
    }

    public final boolean coreIsComplete() {
        return complete;
    }

    public final void build() {
        try {
            BuilderTargetHelper.build(this);
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
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

    public final CoreChildNode coreGetFirstChild() throws DeferredParsingException {
        if (firstChild == null && !coreIsComplete()) {
            next();
        }
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException, DeferredParsingException {
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

    private void ensureDocumentInfoReceived() throws DeferredParsingException {
        if (!coreIsComplete() && firstChild == null) {
            next();
        }
    }
    
    public final String coreGetInputEncoding() throws DeferredParsingException {
        ensureDocumentInfoReceived();
        return inputEncoding;
    }

    public final void coreSetInputEncoding(String inputEncoding) {
//        ensureDocumentInfoReceived();
        this.inputEncoding = inputEncoding;
    }

    public final String coreGetXmlVersion() throws DeferredParsingException {
        ensureDocumentInfoReceived();
        return xmlVersion;
    }

    public final void coreSetXmlVersion(String xmlVersion) {
//        ensureDocumentInfoReceived();
        this.xmlVersion = xmlVersion;
    }

    public final String coreGetXmlEncoding() throws DeferredParsingException {
        ensureDocumentInfoReceived();
        return xmlEncoding;
    }

    public final void coreSetXmlEncoding(String xmlEncoding) {
//        ensureDocumentInfoReceived();
        this.xmlEncoding = xmlEncoding;
    }

    public final boolean coreGetStandalone() throws DeferredParsingException {
        ensureDocumentInfoReceived();
        return standalone;
    }

    public final void coreSetStandalone(boolean standalone) {
//        ensureDocumentInfoReceived();
        this.standalone = standalone;
    }

    // TODO: need test for this
    public final String coreGetDocumentURI() throws DeferredParsingException {
        ensureDocumentInfoReceived();
        return documentURI;
    }

    public final void coreSetDocumentURI(String documentURI) throws DeferredParsingException {
        ensureDocumentInfoReceived();
        this.documentURI = documentURI;
    }

    public final CoreElement coreGetDocumentElement() throws DeferredParsingException {
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreElement)) {
            child = child.coreGetNextSibling();
        }
        return (CoreElement)child;
    }

    public final CoreDocumentType coreGetDocumentType() throws DeferredParsingException {
        // TODO: we know that the document type node must appear before the document element; use this to avoid expansion of the complete document
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentType)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentType)child;
    }
}
