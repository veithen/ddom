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

import java.util.Iterator;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

import com.google.code.ddom.DeferredParsingException;
import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreChildNode;
import com.google.code.ddom.spi.model.CoreDocument;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.spi.model.NodeFactory;
import com.google.code.ddom.spi.stream.Producer;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;

public class DocumentImpl extends ParentNodeImpl implements CoreDocument {
    private final NodeFactory nodeFactory;
    private Builder builder;
    private DOMImplementationImpl domImplementation;
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

    public final DOMImplementation getImplementation() {
        if (domImplementation == null) {
            domImplementation = new DOMImplementationImpl(nodeFactory);
        }
        return domImplementation;
    }

    public final DocumentImpl getDocument() {
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

    public final String getInputEncoding() {
        return inputEncoding;
    }

    public final void setInputEncoding(String inputEncoding) {
        this.inputEncoding = inputEncoding;
    }

    public final String getXmlEncoding() {
        return xmlEncoding;
    }

    public final void setXmlEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    // TODO: need test for this
    public final String getDocumentURI() {
        return documentURI;
    }

    public final void setDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public final Element getDocumentElement() {
        Node child = getFirstChild();
        while (child != null && !(child instanceof Element)) {
            child = child.getNextSibling();
        }
        return (Element)child;
    }

    public final DocumentType getDoctype() {
        Node child = getFirstChild();
        while (child != null && !(child instanceof DocumentType)) {
            child = child.getNextSibling();
        }
        return (DocumentType)child;
    }

    public final Node importNode(Node node, boolean deep) throws DOMException {
        Node importedNode;
        boolean importChildren;
        switch (node.getNodeType()) {
            case ELEMENT_NODE:
                Element element = (Element)node;
                // TODO: detect DOM 1 elements (as with attributes)
                importedNode = nodeFactory.createElement(this, element.getNamespaceURI(), element.getLocalName(), element.getPrefix(), true);
                importChildren = deep;
                break;
            case ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                String localName = attr.getLocalName();
                if (localName == null) {
                    importedNode = nodeFactory.createAttribute(this, attr.getName(), null, null);
                } else {
                    importedNode = nodeFactory.createAttribute(this, attr.getNamespaceURI(), localName, attr.getPrefix(), null, null);
                }
                importChildren = true;
                break;
            case COMMENT_NODE:
                importedNode = nodeFactory.createComment(this, node.getNodeValue());
                importChildren = false;
                break;
            case TEXT_NODE:
                importedNode = nodeFactory.createText(this, node.getNodeValue());
                importChildren = false;
                break;
            case CDATA_SECTION_NODE:
                importedNode = nodeFactory.createCDATASection(this, node.getNodeValue());
                importChildren = false;
                break;
            case PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction)node;
                importedNode = nodeFactory.createProcessingInstruction(this, pi.getTarget(), pi.getData());
                importChildren = false;
                break;
            case DOCUMENT_FRAGMENT_NODE:
                importedNode = nodeFactory.createDocumentFragment(this);
                importChildren = deep;
                break;
            case ENTITY_REFERENCE_NODE:
                importedNode = nodeFactory.createEntityReference(this, node.getNodeName());
                importChildren = false;
                break;
            default:
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
        if (importChildren) {
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                importedNode.appendChild(importNode(child, true));
            }
        }
        return importedNode;
    }

    public final Element getElementById(String elementId) {
        for (Iterator<CoreElement> it = new DescendantsIterator<CoreElement>(CoreElement.class, this); it.hasNext(); ) {
            CoreElement element = it.next();
            for (CoreAttribute attr = element.internalGetFirstAttribute(); attr != null; attr = attr.internalGetNextAttribute()) {
                if (attr.isId() && elementId.equals(attr.getValue())) {
                    return (DOMElement)element;
                }
            }
        }
        return null;
    }

    public final Node adoptNode(Node source) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final DOMConfiguration getDomConfig() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean getXmlStandalone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getXmlVersion() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void normalizeDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void setXmlVersion(String xmlVersion) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
