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
package com.googlecode.ddom.saaj;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;

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

import com.google.code.ddom.DocumentHelper;
import com.google.code.ddom.DocumentHelperFactory;
import com.google.code.ddom.frontend.saaj.intf.SAAJDocument;

public abstract class SOAPPartImpl extends SOAPPart {
    private static final DocumentHelper documentHelper = DocumentHelperFactory.INSTANCE.newInstance(MessageFactoryImpl.class.getClassLoader());
    
    private final SAAJDocument document;
    
    public SOAPPartImpl() {
        document = (SAAJDocument)documentHelper.newDocument("saaj");
    }
    
    protected abstract SOAPEnvelope createEnvelope(SAAJDocument document);
    
    @Override
    public SOAPEnvelope getEnvelope() throws SOAPException {
        SOAPEnvelope envelope = (SOAPEnvelope)document.getDocumentElement();
        if (envelope == null) {
            envelope = createEnvelope(document);
            document.appendChild(envelope);
            envelope.addHeader();
            envelope.addBody();
        }
        return envelope;
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
    public void setContent(Source arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMimeHeader(String arg0, String arg1) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node adoptNode(Node source) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Attr createAttribute(String name) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return document.createAttributeNS(namespaceURI, qualifiedName);
    }

    public CDATASection createCDATASection(String data) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Comment createComment(String data) {
        return document.createComment(data);
    }

    public final DocumentFragment createDocumentFragment() {
        return document.createDocumentFragment();
    }

    public Element createElement(String tagName) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return document.createElementNS(namespaceURI, qualifiedName);
    }

    public EntityReference createEntityReference(String name)
            throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            String data) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Text createTextNode(String data) {
        return document.createTextNode(data);
    }

    public DocumentType getDoctype() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Element getDocumentElement() {
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
        return document.getFirstChild();
    }

    public Node getLastChild() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getLocalName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getNamespaceURI() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node getNextSibling() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getNodeName() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public short getNodeType() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getNodeValue() throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Document getOwnerDocument() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node getParentNode() {
        // TODO
        throw new UnsupportedOperationException();
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

    public boolean hasChildNodes() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isEqualNode(Node arg) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isSameNode(Node other) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public boolean isSupported(String feature, String version) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String lookupNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String lookupPrefix(String namespaceURI) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void normalize() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node removeChild(Node oldChild) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setPrefix(String prefix) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setTextContent(String textContent) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void detachNode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public SOAPElement getParentElement() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getValue() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void recycleNode() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setParentElement(SOAPElement arg0) throws SOAPException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setValue(String arg0) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
