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
package com.googlecode.ddom.frontend.saaj.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
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

import com.googlecode.ddom.frontend.saaj.intf.SAAJDocument;
import com.googlecode.ddom.stream.Options;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;
import com.googlecode.ddom.stream.StreamFactory;
import com.googlecode.ddom.stream.XmlInput;
import com.googlecode.ddom.stream.XmlOutput;

public abstract class AbstractSOAPPartImpl extends SOAPPart {
    private static final StreamFactory streamFactory = StreamFactory.getInstance(AbstractSOAPPartImpl.class.getClassLoader());
    
    // TODO: should be private
    protected SAAJDocument document;
    private Source source;
    
    public AbstractSOAPPartImpl() {
    }
    
    public AbstractSOAPPartImpl(SAAJDocument document) {
        this.document = document;
    }
    
    public AbstractSOAPPartImpl(Source source) {
        this.source = source;
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
    public final Source getContent() throws SOAPException {
        if (source instanceof StreamSource) {
            // We need to copy the content of the StreamSource so that the SOAP part can still
            // be accessed later.
            // TODO: repeated calls to getContent() will created multiple copies; this is unnecessary
            byte[] content;
            try {
                // TODO: this will not work for StreamSources containing a Reader
                // TODO: need to close the input stream?
                // TODO: depending on the type of input stream, there may be smarter ways to clone the stream
                content = IOUtils.toByteArray(((StreamSource)source).getInputStream());
            } catch (IOException ex) {
                throw new SOAPException(ex);
            }
            source = new StreamSource(new ByteArrayInputStream(content));
            return new StreamSource(new ByteArrayInputStream(content));
        } else {
            // TODO: can we guarantee that document != null ?
            return new DOMSource(document);
        }
    }
    
    public final void writeTo(OutputStream out) throws IOException {
        // TODO: there is some code duplication here
        if (source instanceof StreamSource) {
            // We need to copy the content of the StreamSource so that the SOAP part can still
            // be accessed later.
            // TODO: repeated calls to getContent() will created multiple copies; this is unnecessary
                // TODO: this will not work for StreamSources containing a Reader
                // TODO: need to close the input stream?
                // TODO: depending on the type of input stream, there may be smarter ways to clone the stream
            byte[] content = IOUtils.toByteArray(((StreamSource)source).getInputStream());
            source = new StreamSource(new ByteArrayInputStream(content));
            out.write(content);
        } else {
            try {
                XmlInput input = document.coreGetInput(true);
                // TODO: set encoding?
                XmlOutput output = streamFactory.getOutput(out, new Options());
                new Stream(input, output).flush();
            } catch (StreamException ex) {
                // TODO: maybe we can extract an existing IOException??
                IOException ex2 = new IOException();
                ex2.initCause(ex);
                throw ex2;
            }
        }
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

    public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        initDocument();
        return document.createProcessingInstruction(target, data);
    }

    public final Text createTextNode(String data) {
        initDocument();
        return document.createTextNode(data);
    }

    public final DocumentType getDoctype() {
        initDocument();
        return document.getDoctype();
    }

    public final Element getDocumentElement() {
        initDocument();
        return document.getDocumentElement();
    }

    public final String getDocumentURI() {
        initDocument();
        return document.getDocumentURI();
    }

    public final DOMConfiguration getDomConfig() {
        initDocument();
        return document.getDomConfig();
    }

    public final Element getElementById(String elementId) {
        initDocument();
        return document.getElementById(elementId);
    }

    public final NodeList getElementsByTagName(String tagname) {
        initDocument();
        return document.getElementsByTagName(tagname);
    }

    public final NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        initDocument();
        return document.getElementsByTagNameNS(namespaceURI, localName);
    }

    public final DOMImplementation getImplementation() {
        initDocument();
        return document.getImplementation();
    }

    public final String getInputEncoding() {
        initDocument();
        return document.getInputEncoding();
    }

    public final boolean getStrictErrorChecking() {
        initDocument();
        return document.getStrictErrorChecking();
    }

    public final String getXmlEncoding() {
        initDocument();
        return document.getXmlEncoding();
    }

    public final boolean getXmlStandalone() {
        initDocument();
        return document.getXmlStandalone();
    }

    public final String getXmlVersion() {
        initDocument();
        return document.getXmlVersion();
    }

    public final Node importNode(Node importedNode, boolean deep) throws DOMException {
        initDocument();
        return document.importNode(importedNode, deep);
    }

    public final void normalizeDocument() {
        initDocument();
        document.normalizeDocument();
    }

    public final Node renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        initDocument();
        return document.renameNode(n, namespaceURI, qualifiedName);
    }

    public final void setDocumentURI(String documentURI) {
        initDocument();
        document.setDocumentURI(documentURI);
    }

    public final void setStrictErrorChecking(boolean strictErrorChecking) {
        initDocument();
        document.setStrictErrorChecking(strictErrorChecking);
    }

    public final void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        initDocument();
        document.setXmlStandalone(xmlStandalone);
    }

    public final void setXmlVersion(String xmlVersion) throws DOMException {
        initDocument();
        document.setXmlVersion(xmlVersion);
    }

    public final Node appendChild(Node newChild) throws DOMException {
        initDocument();
        return document.appendChild(newChild);
    }

    public final Node cloneNode(boolean deep) {
        initDocument();
        return document.cloneNode(deep);
    }

    public final short compareDocumentPosition(Node other) throws DOMException {
        initDocument();
        return document.compareDocumentPosition(other);
    }

    public final NamedNodeMap getAttributes() {
        initDocument();
        return document.getAttributes();
    }

    public final String getBaseURI() {
        initDocument();
        return document.getBaseURI();
    }

    public final NodeList getChildNodes() {
        initDocument();
        return document.getChildNodes();
    }

    public final Object getFeature(String feature, String version) {
        initDocument();
        return document.getFeature(feature, version);
    }

    public final Node getFirstChild() {
        initDocument();
        return document.getFirstChild();
    }

    public final Node getLastChild() {
        initDocument();
        return document.getLastChild();
    }

    public final String getLocalName() {
        initDocument();
        return document.getLocalName();
    }

    public final String getNamespaceURI() {
        initDocument();
        return document.getNamespaceURI();
    }

    public final Node getNextSibling() {
        initDocument();
        return document.getNextSibling();
    }

    public final String getNodeName() {
        initDocument();
        return document.getNodeName();
    }

    public final short getNodeType() {
        initDocument();
        return document.getNodeType();
    }

    public final String getNodeValue() throws DOMException {
        initDocument();
        return document.getNodeValue();
    }

    public final Document getOwnerDocument() {
        initDocument();
        return document.getOwnerDocument();
    }

    public final Node getParentNode() {
        initDocument();
        return document.getParentNode();
    }

    public final String getPrefix() {
        initDocument();
        return document.getPrefix();
    }

    public final Node getPreviousSibling() {
        initDocument();
        return document.getPreviousSibling();
    }

    public final String getTextContent() throws DOMException {
        initDocument();
        return document.getTextContent();
    }

    public final Object getUserData(String key) {
        initDocument();
        return document.getUserData(key);
    }

    public final boolean hasAttributes() {
        initDocument();
        return document.hasAttributes();
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
