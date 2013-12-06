/*
 * Copyright 2009-2013 Andreas Veithen
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

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.ddom.backend.linkedlist.intf.InputContext;
import com.googlecode.ddom.backend.linkedlist.intf.LLDocument;
import com.googlecode.ddom.backend.linkedlist.intf.LLElement;
import com.googlecode.ddom.backend.linkedlist.intf.LLParentNode;
import com.googlecode.ddom.core.ChildNotAllowedException;
import com.googlecode.ddom.core.ClonePolicy;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreDocumentTypeDeclaration;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.ext.ModelExtension;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.XmlHandler;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.symbols.SymbolHashTable;
import com.googlecode.ddom.symbols.Symbols;

public class Document extends ParentNode implements LLDocument {
    private static final Log log = LogFactory.getLog(Document.class);
    
    private final ModelExtension modelExtension;
    private Symbols symbols;
    private final ArrayList<Builder> builders = new ArrayList<Builder>();
    private int children;
    private String inputEncoding;
    private boolean xmlVersionSet;
    private String xmlVersion;
    private boolean xmlEncodingSet;
    private String xmlEncoding;
    private boolean standaloneSet;
    private Boolean standalone;
    private String documentURI;

    public Document(ModelExtension modelExtension) {
        super(Flags.STATE_EXPANDED);
        this.modelExtension = modelExtension;
    }

    public final int coreGetNodeType() {
        return DOCUMENT_NODE;
    }

    // The return type is intentionally specified as ArrayList to indicate that it
    // is faster to iterate over the list using direct access than using an Iterator
    public final ArrayList<Builder> getBuilders() {
        return builders;
    }

    public final InputContext internalCreateInputContext(XmlInput input, LLParentNode target, boolean unwrap) throws DeferredParsingException {
        if (log.isDebugEnabled()) {
            log.debug("Creating builder for " + input);
        }
        Builder builder = new Builder(input, modelExtension, this, target, unwrap);
        target.internalSetState(unwrap ? Flags.STATE_ATTRIBUTES_PENDING : Flags.STATE_CHILDREN_PENDING);
        new Stream(input, builder);
        builders.add(builder);
        return builder.getRootInputContext();
    }
    
    public final InputContext internalGetInputContext(LLParentNode target) {
        for (int i=0, l=builders.size(); i<l; i++) {
            InputContext context = builders.get(i).getInputContext(target);
            if (context != null) {
                return context;
            }
        }
        throw new IllegalArgumentException("No input context found for target");
    }
    
    public final Symbols getSymbols() {
        // TODO: need to allow getting the symbol table from the parser
        if (symbols == null) {
            symbols = new SymbolHashTable();
        }
        return symbols;
    }

    public final LLDocument internalGetOwnerDocument(boolean create) {
        return this;
    }

    public final void dispose() {
        for (int i=0, l=builders.size(); i<l; i++) {
            builders.get(i).dispose();
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
        try {
            if (newChild instanceof CoreDocumentTypeDeclaration) {
                if (!(replacedChild instanceof CoreDocumentTypeDeclaration || coreGetDocumentTypeDeclaration() == null)) {
                    throw new ChildNotAllowedException("The document already has a document type node");
                }
            } else if (newChild instanceof CoreElement) {
                if (!(replacedChild instanceof CoreElement || coreGetDocumentElement() == null)) {
                    throw new ChildNotAllowedException("The document already has a document element");
                }
            }
        } catch (DeferredParsingException ex) {
            throw ex;
        } catch (DeferredBuildingException ex) {
            // If we get here, the exception must be a NodeConsumedException. Don't do anything right now;
            // the caller will get another NodeConsumedException when it tries to add the new node or
            // there will be an exception when the document is serialized.
        }
    }

    public final int coreGetChildCount() throws DeferredParsingException {
        coreBuild();
        return children;
    }

    private void ensureDocumentInfoReceived() throws DeferredParsingException {
        try {
            coreGetFirstChild();
        } catch (DeferredParsingException ex) {
            throw ex;
        } catch (DeferredBuildingException ex) {
            throw new RuntimeException("Unexpected exception", ex);
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

    // TODO: maybe this should be linked to coreClear()??
    @Override
    protected void contentReset() {
        xmlVersionSet = false;
        xmlVersion = null;
        xmlEncodingSet = false;
        xmlEncoding = null;
        standaloneSet = false;
        standalone = null;
    }
    
    void processXmlDeclaration(String version, String encoding, Boolean standalone) {
        if (!xmlVersionSet) {
            xmlVersionSet = true;
            this.xmlVersion = version;
        }
        if (!xmlEncodingSet) {
            xmlEncodingSet = true;
            this.xmlEncoding = encoding;
        }
        if (!standaloneSet) {
            standaloneSet = true;
            this.standalone = standalone;
        }
    }
    
    public final String coreGetXmlVersion() throws DeferredParsingException {
        if (!xmlVersionSet && internalGetState() == Flags.STATE_CONTENT_SET) {
            ensureDocumentInfoReceived();
        }
        return xmlVersion;
    }

    public final void coreSetXmlVersion(String xmlVersion) {
        xmlVersionSet = true;
        this.xmlVersion = xmlVersion;
    }

    public final String coreGetXmlEncoding() throws DeferredParsingException {
        if (!xmlEncodingSet && internalGetState() == Flags.STATE_CONTENT_SET) {
            ensureDocumentInfoReceived();
        }
        return xmlEncoding;
    }

    public final void coreSetXmlEncoding(String xmlEncoding) {
        xmlEncodingSet = true;
        this.xmlEncoding = xmlEncoding;
    }

    public final Boolean coreGetStandalone() throws DeferredParsingException {
        if (!standaloneSet && internalGetState() == Flags.STATE_CONTENT_SET) {
            ensureDocumentInfoReceived();
        }
        return standalone;
    }

    public final void coreSetStandalone(Boolean standalone) {
        standaloneSet = true;
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

    public final CoreElement coreGetDocumentElement() throws DeferredBuildingException {
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreElement)) {
            child = child.coreGetNextSibling();
        }
        return (CoreElement)child;
    }

    public final CoreDocumentTypeDeclaration coreGetDocumentTypeDeclaration() throws DeferredBuildingException {
        // TODO: we know that the document type node must appear before the document element; use this to avoid expansion of the complete document
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentTypeDeclaration)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentTypeDeclaration)child;
    }

    public final void internalGenerateStartEvent(XmlHandler handler) throws StreamException {
        try {
            handler.startEntity(false, coreGetInputEncoding());
            handler.processXmlDeclaration(coreGetXmlVersion(), coreGetXmlEncoding(), coreGetStandalone());
        } catch (DeferredParsingException ex) {
            throw ex.getStreamException();
        }
    }

    public final void internalGenerateEndEvent(XmlHandler handler) {
        // TODO
    }

    @Override
    final LLParentNode shallowClone(ClonePolicy policy) {
        return new Document(modelExtension);
    }
}
