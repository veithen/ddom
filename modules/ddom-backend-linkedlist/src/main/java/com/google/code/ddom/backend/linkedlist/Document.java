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

import java.util.LinkedList;
import java.util.List;

import com.google.code.ddom.backend.ChildTypeNotAllowedException;
import com.google.code.ddom.backend.CoreChildNode;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreDocumentType;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreParentNode;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.Implementation;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.SymbolHashTable;
import com.google.code.ddom.stream.spi.Symbols;

@Implementation
public class Document extends ParentNode implements CoreDocument {
    // TODO: since we are now using a weaver, it should no longer be necessary to have a reference to the node factory
    private final NodeFactory nodeFactory;
    private final Symbols symbols;
    private List<Builder> builders = new LinkedList<Builder>();
    private int children;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private boolean standalone;
    private String documentURI;

    public Document(NodeFactory nodeFactory) {
        super(true);
        this.nodeFactory = nodeFactory;
        symbols = new SymbolHashTable();
    }

    final void createBuilder(Producer producer, ParentNode target) {
        builders.add(new Builder(producer, this, target));
    }
    
    final Builder getBuilderFor(CoreParentNode target) {
        for (Builder builder : builders) {
            if (builder.isBuilderFor(target)) {
                return builder;
            }
        }
        throw new IllegalArgumentException("No builder found for target");
    }
    
    public final NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public final Symbols getSymbols() {
        return symbols;
    }

    public final Document internalGetDocument() {
        return this;
    }

    public final void dispose() {
        for (Builder builder : builders) {
            builder.dispose();
        }
    }

    // TODO
//    public final void internalSetComplete() {
//        builder.dispose();
//        builder = null;
//        complete = true;
//    }
    
    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    final void validateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException, DeferredParsingException {
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

    public final int coreGetChildCount() throws DeferredParsingException {
        coreBuild();
        return children;
    }

    private void ensureDocumentInfoReceived() throws DeferredParsingException {
        coreGetFirstChild();
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
