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

import com.google.code.ddom.backend.ExtensionFactoryLocator;
import com.google.code.ddom.core.ChildTypeNotAllowedException;
import com.google.code.ddom.core.CoreCDATASection;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreComment;
import com.google.code.ddom.core.CoreDocumentFragment;
import com.google.code.ddom.core.CoreDocumentTypeDeclaration;
import com.google.code.ddom.core.CoreElement;
import com.google.code.ddom.core.CoreEntityReference;
import com.google.code.ddom.core.CoreNSAwareAttribute;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.CoreNSUnawareAttribute;
import com.google.code.ddom.core.CoreNSUnawareElement;
import com.google.code.ddom.core.CoreNamespaceDeclaration;
import com.google.code.ddom.core.CoreProcessingInstruction;
import com.google.code.ddom.core.CoreText;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.DocumentFactory;
import com.google.code.ddom.core.ext.ModelExtension;
import com.google.code.ddom.stream.spi.Producer;
import com.google.code.ddom.stream.spi.SymbolHashTable;
import com.google.code.ddom.stream.spi.Symbols;

public class Document extends ParentNode implements LLDocument {
    private static final NSAwareElementFactory nsAwareElementFactory = ExtensionFactoryLocator.locate(NSAwareElementFactory.class);
    
    // TODO: since we are now using a weaver, it should no longer be necessary to have a reference to the node factory
    private final DocumentFactory documentFactory;
    private final Symbols symbols;
    private List<Builder> builders = new LinkedList<Builder>();
    private int children;
    private String inputEncoding;
    private String xmlVersion;
    private String xmlEncoding;
    private boolean standalone;
    private String documentURI;

    public Document(DocumentFactory documentFactory) {
        super(true);
        this.documentFactory = documentFactory;
        symbols = new SymbolHashTable();
    }

    public final void internalCreateBuilder(Producer producer, ModelExtension modelExtension, LLParentNode target) {
        builders.add(new Builder(producer, modelExtension, this, target));
    }
    
    public final Builder internalGetBuilderFor(LLParentNode target) {
        for (Builder builder : builders) {
            if (builder.isBuilderFor(target)) {
                return builder;
            }
        }
        throw new IllegalArgumentException("No builder found for target");
    }
    
    /**
     * Reassign the builder linked to one node to another node. This is necessary if the content of
     * a node is moved to another node without building the source node.
     * 
     * @param from
     * @param to
     */
    public final void internalMigrateBuilder(LLParentNode from, LLParentNode to) {
        for (Builder builder : builders) {
            if (builder.migrateBuilder(from, to)) {
                from.internalSetComplete(true);
                to.internalSetComplete(false);
                return;
            }
        }
        throw new IllegalArgumentException("No builder found for target");
    }
    
    public final DocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    public final Symbols getSymbols() {
        return symbols;
    }

    public final LLDocument internalGetDocument() {
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

    public final void internalValidateChildType(CoreChildNode newChild, CoreChildNode replacedChild) throws ChildTypeNotAllowedException, DeferredParsingException {
        // TODO: character data is also not allowed in DOM, but is allowed in Axiom; need to handle this somewhere!
        if (newChild instanceof CoreDocumentTypeDeclaration) {
            if (!(replacedChild instanceof CoreDocumentTypeDeclaration || coreGetDocumentTypeDeclaration() == null)) {
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

    public final CoreDocumentTypeDeclaration coreGetDocumentTypeDeclaration() throws DeferredParsingException {
        // TODO: we know that the document type node must appear before the document element; use this to avoid expansion of the complete document
        CoreChildNode child = coreGetFirstChild();
        while (child != null && !(child instanceof CoreDocumentTypeDeclaration)) {
            child = child.coreGetNextSibling();
        }
        return (CoreDocumentTypeDeclaration)child;
    }

    public final CoreDocumentTypeDeclaration coreCreateDocumentTypeDeclaration(String rootName, String publicId, String systemId) {
        return new DocumentTypeDeclaration(this, rootName, publicId, systemId);
    }

    public final CoreNSUnawareElement coreCreateElement(String tagName) {
        return new NSUnawareElement(this, tagName, true);
    }
    
    public final CoreNSAwareElement coreCreateElement(String namespaceURI, String localName, String prefix) {
        return new NSAwareElement(this, namespaceURI, localName, prefix, true);
    }
    
    public final CoreNSAwareElement coreCreateElement(Class<?> extensionInterface, String namespaceURI, String localName, String prefix) {
        return nsAwareElementFactory.create(extensionInterface, this, namespaceURI, localName, prefix, true);
    }
    
    public final CoreNSUnawareAttribute coreCreateAttribute(String name, String value, String type) {
        return new NSUnawareAttribute(this, name, value, type);
    }
    
    public final CoreNSAwareAttribute coreCreateAttribute(String namespaceURI, String localName, String prefix, String value, String type) {
        return new NSAwareAttribute(this, namespaceURI, localName, prefix, value, type);
    }
    
    public final CoreNamespaceDeclaration coreCreateNamespaceDeclaration(String prefix, String namespaceURI) {
        return new NamespaceDeclaration(this, prefix, namespaceURI);
    }
    
    public final CoreProcessingInstruction coreCreateProcessingInstruction(String target, String data) {
        return new ProcessingInstruction(this, target, data);
    }
    
    public final CoreDocumentFragment coreCreateDocumentFragment() {
        return new DocumentFragment(this);
    }

    public final CoreText coreCreateText(String data) {
        return new Text(this, data);
    }

    public final CoreComment coreCreateComment(String data) {
        return new Comment(this, data);
    }

    public final CoreCDATASection coreCreateCDATASection(String data) {
        return new CDATASection(this, data);
    }

    public final CoreEntityReference coreCreateEntityReference(String name) {
        return new EntityReference(this, name);
    }
}
