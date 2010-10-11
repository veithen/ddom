/*
 * Copyright 2009-2010 Andreas Veithen
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
package com.google.code.ddom.frontend.saaj.impl;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

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
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;

public abstract class AbstractSOAPPartImpl extends SOAPPart {
    // TODO: should be private
    protected SAAJDocument document;
    private Source source;
    
    public AbstractSOAPPartImpl() {
    }
    
    public AbstractSOAPPartImpl(SAAJDocument document) {
        this.document = document;
    }
    
    protected abstract SAAJDocument createInitialDocument();
    protected abstract SAAJDocument createDocumentFromSource(Source source);
    
    // Should be private
    protected void initDocument() {
        if (document == null) {
            if (source != null) {
                document = createDocumentFromSource(source);
                source = null;
            } else {
                document = createInitialDocument();
            }
        }
    }
    

    @Override
    public void addMimeHeader(String arg0, String arg1) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getAllMimeHeaders() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Source getContent() throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getMatchingMimeHeaders(String[] arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getMimeHeader(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator getNonMatchingMimeHeaders(String[] arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeAllMimeHeaders() {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMimeHeader(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setContent(Source source) throws SOAPException {
        document = null;
        // TODO: This is used by CXF; actually it is surprising that this works with the reference implementation
        if (source instanceof DOMSource && ((DOMSource)source).getNode() == null) {
            this.source = null;
        } else {
            this.source = source;
        }
    }

    @Override
    public void setMimeHeader(String arg0, String arg1) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node adoptNode(Node source) throws DOMException {
        initDocument();
        return document.adoptNode(source);
    }

    public final Attr createAttribute(String name) throws DOMException {
        initDocument();
        return document.createAttribute(name);
    }

    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        initDocument();
        return document.createAttributeNS(namespaceURI, qualifiedName);
    }

    public final CDATASection createCDATASection(String data) throws DOMException {
        initDocument();
        return document.createCDATASection(data);
    }

    public final Comment createComment(String data) {
        initDocument();
        return document.createComment(data);
    }

    public final DocumentFragment createDocumentFragment() {
        initDocument();
        return document.createDocumentFragment();
    }

    public final Element createElement(String tagName) throws DOMException {
        initDocument();
        return document.createElement(tagName);
    }

    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        initDocument();
        return document.createElementNS(namespaceURI, qualifiedName);
    }

    public final EntityReference createEntityReference(String name) throws DOMException {
        initDocument();
        return document.createEntityReference(name);
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            String data) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text createTextNode(String data) {
        initDocument();
        return document.createTextNode(data);
    }

    public DocumentType getDoctype() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Element getDocumentElement() {
        initDocument();
        return document.getDocumentElement();
    }

    public String getDocumentURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public DOMConfiguration getDomConfig() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Element getElementById(String elementId) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public NodeList getElementsByTagName(String tagname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public DOMImplementation getImplementation() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getInputEncoding() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getXmlEncoding() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean getXmlStandalone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getXmlVersion() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node importNode(Node importedNode, boolean deep) throws DOMException {
        initDocument();
        return document.importNode(importedNode, deep);
    }

    public void normalizeDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node renameNode(Node n, String namespaceURI, String qualifiedName)
            throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setDocumentURI(String documentURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setXmlVersion(String xmlVersion) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node appendChild(Node newChild) throws DOMException {
        initDocument();
        return document.appendChild(newChild);
    }

    public Node cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public NamedNodeMap getAttributes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getBaseURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public NodeList getChildNodes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Object getFeature(String feature, String version) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node getFirstChild() {
        initDocument();
        return document.getFirstChild();
    }

    public Node getLastChild() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String getLocalName() {
        initDocument();
        return document.getLocalName();
    }

    public final String getNamespaceURI() {
        initDocument();
        return document.getNamespaceURI();
    }

    public Node getNextSibling() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getNodeName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final short getNodeType() {
        initDocument();
        return document.getNodeType();
    }

    public String getNodeValue() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Document getOwnerDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node getParentNode() {
        initDocument();
        return document.getParentNode();
    }

    public String getPrefix() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node getPreviousSibling() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getTextContent() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Object getUserData(String key) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean hasAttributes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean hasChildNodes() {
        initDocument();
        return document.hasChildNodes();
    }

    public final Node insertBefore(Node newChild, Node refChild) throws DOMException {
        initDocument();
        return insertBefore(newChild, refChild);
    }

    public final boolean isDefaultNamespace(String namespaceURI) {
        initDocument();
        return isDefaultNamespace(namespaceURI);
    }

    public final boolean isEqualNode(Node arg) {
        initDocument();
        return isEqualNode(arg);
    }

    public final boolean isSameNode(Node other) {
        initDocument();
        return document.isSameNode(other);
    }

    public final boolean isSupported(String feature, String version) {
        initDocument();
        return document.isSupported(feature, version);
    }

    public final String lookupNamespaceURI(String prefix) {
        initDocument();
        return document.lookupNamespaceURI(prefix);
    }

    public final String lookupPrefix(String namespaceURI) {
        initDocument();
        return document.lookupPrefix(namespaceURI);
    }

    public final void normalize() {
        initDocument();
        document.normalize();
    }

    public final Node removeChild(Node oldChild) throws DOMException {
        initDocument();
        return removeChild(oldChild);
    }

    public final Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        initDocument();
        return document.replaceChild(newChild, oldChild);
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        initDocument();
        document.setNodeValue(nodeValue);
    }

    public final void setPrefix(String prefix) throws DOMException {
        initDocument();
        document.setPrefix(prefix);
    }

    public final void setTextContent(String textContent) throws DOMException {
        initDocument();
        document.setTextContent(textContent);
    }

    public final Object setUserData(String key, Object data, UserDataHandler handler) {
        initDocument();
        return document.setUserData(key, data, handler);
    }

    public final void detachNode() {
        // A document is always detached
    }

    public final void recycleNode() {
        // TODO: in DDOM recycleNode should probably dispose the document; needs to be analyzed more carefully
    }

    public final SOAPElement getParentElement() {
        return null;
    }

    public final void setParentElement(SOAPElement parent) throws SOAPException {
        throw new SOAPException("The parent element of a soap part is not defined");
    }

    public final String getValue() {
        // In DOM, the document node can't have text nodes as children. Therefore, the
        // return value is always null
        return null;
    }

    public final void setValue(String value) {
        throw new IllegalStateException();
    }
}
