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

import java.util.LinkedList;
import java.util.List;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class Document extends ParentNode implements LLDocument {
    private final ModelExtension modelExtension;
    private final Symbols symbols;
    private List<Builder> builders = new LinkedList<Builder>();
    private int children;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private boolean standalone;
    private String documentURI;

    public Document(ModelExtension modelExtension) {
        super(Flags.STATE_EXPANDED);
        this.modelExtension = modelExtension;
        symbols = new SymbolHashTable();
    }

    public final int coreGetNodeType() {
        return DOCUMENT_NODE;
    }

    public final void internalCreateBuilder(XmlInput input, LLParentNode target) {
        Builder builder = new Builder(input, modelExtension, this, target);
        new Stream(input, builder);
        builders.add(builder);
    }
    
    public final InputContext internalGetInputContext(LLParentNode target) {
        for (Builder builder : builders) {
            InputContext context = builder.getInputContext(target);
            if (context != null) {
                return context;
            }
        }
        throw new IllegalArgumentException("No input context found for target");
    }
    
    public final Symbols getSymbols() {
        return symbols;
    }

    public final LLDocument internalGetOwnerDocument() {
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
    
    public final void internalNotifyChildrenModified(int delta) {
        children += delta;
    }

    public final void internalNotifyChildrenCleared() {
        children = 0;
    }

    public final void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildNotAllowedException, DeferredParsingException {
        // TODO: character data is also not allowed in DOM, but is allowed in Axiom; need to handle this somewhere!
        if (newChild instanceof CoreDocumentTypeDeclaration) {
            if (!(replacedChild instanceof CoreDocumentTypeDeclaration || coreGetDocumentTypeDeclaration() == null)) {
                throw new ChildNotAllowedException("The document already has a document type node");
            }
        } else if (newChild instanceof CoreElement) {
            if (!(replacedChild instanceof CoreElement || coreGetDocumentElement() == null)) {
                throw new ChildNotAllowedException("The document already has a document element");
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

    public final CoreDocumentTypeDeclaration coreGetDocumentTypeDeclaration() throws DeferredParsingException {
        // TODO: we know that the document type node must appear before the document element; use this to avoid expansion of the complete document
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentTypeDeclaration)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentTypeDeclaration)child;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) {
        // TODO
    }

    public final void internalGenerateEndEvent(XmlHandler handler) {
        // TODO
    }
}
