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

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.google.code.ddom.dom.DeferredParsingException;
import com.google.code.ddom.dom.builder.Source;
import com.google.code.ddom.dom.model.BuilderTarget;
import com.google.code.ddom.dom.model.ChildNode;
import com.google.code.ddom.dom.model.DOMAttribute;
import com.google.code.ddom.dom.model.DOMElement;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;

public class DocumentImpl extends ParentNodeImpl implements Document, BuilderTarget {
    private final NodeFactory nodeFactory = new DOMNodeFactory();
    private final Builder builder;
    private DOMImplementationImpl domImplementation;
    private ChildNode firstChild;
    private boolean complete; // TODO: maybe we can replace this by builder == null?
    private int children;
    private String inputEncoding;
    private String xmlEncoding;
    private String documentURI;

    public DocumentImpl(Source source) {
        if (source == null) {
            builder = null;
            complete = true;
        } else {
            builder = new Builder(source.getParser(), nodeFactory, this, this);
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

    public final Element createElement(String tagName) throws DOMException {
        NSUtil.validateName(tagName);
        return nodeFactory.createElement(this, tagName, true);
    }
    
    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        NSUtil.validateNamespace(namespaceURI, prefix);
        return nodeFactory.createElement(this, namespaceURI, localName, prefix, true);
    }
    
    public final Attr createAttribute(String name) throws DOMException {
        NSUtil.validateName(name);
        return nodeFactory.createAttribute(this, name, null, null);
    }

    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            prefix = qualifiedName.substring(0, i);
            localName = qualifiedName.substring(i+1);
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return nodeFactory.createNSDecl(this, NSUtil.getDeclaredPrefix(localName, prefix), null);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            return nodeFactory.createAttribute(this, namespaceURI, localName, prefix, null, null);
        }
    }

    public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        NSUtil.validateName(target);
        return nodeFactory.createProcessingInstruction(this, target, data);
    }
    
    public final DocumentFragment createDocumentFragment() {
        return nodeFactory.createDocumentFragment(this);
    }

    public final Text createTextNode(String data) {
        return nodeFactory.createText(this, data);
    }

    public final Comment createComment(String data) {
        return nodeFactory.createComment(this, data);
    }

    public final CDATASection createCDATASection(String data) throws DOMException {
        return nodeFactory.createCDATASection(this, data);
    }

    public final EntityReference createEntityReference(String name) throws DOMException {
        return nodeFactory.createEntityReference(this, name);
    }

    public final DocumentImpl getDocument() {
        return this;
    }

    public final Document getOwnerDocument() {
        return null;
    }

    public final boolean isComplete() {
        return complete;
    }
    
    public final void build() {
        BuilderTargetHelper.build(this);
    }
    
    public final void internalSetComplete() {
        complete = true;
    }
    
    public final void internalSetFirstChild(ChildNode child) {
        firstChild = child;
    }

    public final ChildNode getFirstChild() {
        if (firstChild == null && !complete) {
            next();
        }
        return firstChild;
    }

    public final void notifyChildrenModified(int delta) {
        children += delta;
    }

    @Override
    protected void validateChildType(ChildNode newChild) {
        // TODO
    }

    public final int getLength() {
        build();
        return children;
    }

    public final short getNodeType() {
        return DOCUMENT_NODE;
    }

    public final String getNodeName() {
        return "#document";
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
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

    public final int getStructureVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    public final Node getParentNode() {
        return null;
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
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

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final String getNamespaceURI() {
        return null;
    }

    public final String getPrefix() {
        return null;
    }

    public final void setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String getLocalName() {
        return null;
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
        for (Iterator<DOMElement> it = new DescendantsIterator<DOMElement>(DOMElement.class, this); it.hasNext(); ) {
            DOMElement element = it.next();
            for (DOMAttribute attr = element.internalGetFirstAttribute(); attr != null; attr = attr.internalGetNextAttribute()) {
                if (attr.isId() && elementId.equals(attr.getValue())) {
                    return element;
                }
            }
        }
        return null;
    }

    @Override
    protected final Node shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
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

    public final Node cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String lookupNamespaceURI(String prefix) {
        return null;
    }

    public final String lookupPrefix(String namespaceURI) {
        return null;
    }
}
