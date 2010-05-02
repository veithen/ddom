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
package com.google.code.ddom.frontend.dom.aspects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreElement;
import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.CoreNSAwareNamedNode;
import com.google.code.ddom.backend.CoreTypedAttribute;
import com.google.code.ddom.frontend.dom.intf.AbortNormalizationException;
import com.google.code.ddom.frontend.dom.intf.DOMCoreNode;
import com.google.code.ddom.frontend.dom.intf.DOMElement;
import com.google.code.ddom.frontend.dom.intf.NormalizationConfig;
import com.google.code.ddom.frontend.dom.support.DOMConfigurationImpl;
import com.google.code.ddom.frontend.dom.support.DOMExceptionUtil;
import com.google.code.ddom.frontend.dom.support.DOMImplementationImpl;
import com.google.code.ddom.frontend.dom.support.NSUtil;
import com.google.code.ddom.frontend.dom.support.NodeUtil;
import com.google.code.ddom.frontend.dom.support.UserData;
import com.google.code.ddom.stream.spi.Symbols;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;

import com.google.code.ddom.frontend.dom.intf.*;

public aspect DocumentSupport {
    private DOMImplementationImpl DOMDocument.domImplementation;
    private final DOMConfigurationImpl DOMDocument.domConfig = new DOMConfigurationImpl();
    private Map<DOMCoreNode,Map<String,UserData>> DOMDocument.userDataMap;
    
    public final DOMImplementation DOMDocument.getImplementation() {
        if (domImplementation == null) {
            domImplementation = new DOMImplementationImpl(getDocumentFactory());
        }
        return domImplementation;
    }

    public final String DOMDocument.getXmlVersion() {
        try {
            String xmlVersion = coreGetXmlVersion();
            return xmlVersion == null ? "1.0" : xmlVersion;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    // TODO: check right behavior when parameter is null
    public final void DOMDocument.setXmlVersion(String xmlVersion) {
        if (xmlVersion.equals("1.0") || xmlVersion.equals("1.1")) {
            coreSetXmlVersion(xmlVersion);
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
    }

    public final String DOMDocument.getInputEncoding() {
        try {
            return coreGetInputEncoding();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final String DOMDocument.getXmlEncoding() {
        try {
            return coreGetXmlEncoding();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final boolean DOMDocument.getXmlStandalone() {
        try {
            return coreGetStandalone();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMDocument.setXmlStandalone(boolean xmlStandalone) throws DOMException {
        coreSetStandalone(xmlStandalone);
    }

    // TODO: need test for this
    public final String DOMDocument.getDocumentURI() {
        try {
            return coreGetDocumentURI();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void DOMDocument.setDocumentURI(String documentURI) {
        try {
            coreSetDocumentURI(documentURI);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final boolean DOMDocument.hasAttributes() {
        return false;
    }

    public final NamedNodeMap DOMDocument.getAttributes() {
        return null;
    }

    public final Element DOMDocument.getDocumentElement() {
        try {
            return (Element)coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final DocumentType DOMDocument.getDoctype() {
        try {
            return NodeUtil.toDOM(coreGetDocumentTypeDeclaration());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Node DOMDocument.importNode(Node node, boolean deep) throws DOMException {
        // TODO: do we really need to use getNodeFactory().createXXX, or can we just use createXXX?
        Node importedNode;
        boolean importChildren;
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element element = (Element)node;
                // TODO: detect DOM 1 elements (as with attributes)
                importedNode = (Node)coreCreateElement(element.getNamespaceURI(), element.getLocalName(), element.getPrefix());
                importChildren = deep;
                break;
            case Node.ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                String localName = attr.getLocalName();
                if (localName == null) {
                    importedNode = (Node)coreCreateAttribute(attr.getName(), null, null);
                } else {
                    importedNode = (Node)coreCreateAttribute(attr.getNamespaceURI(), localName, attr.getPrefix(), null, null);
                }
                importChildren = true;
                break;
            case Node.COMMENT_NODE:
                importedNode = (Node)coreCreateComment(node.getNodeValue());
                importChildren = false;
                break;
            case Node.TEXT_NODE:
                importedNode = (Node)coreCreateText(node.getNodeValue());
                importChildren = false;
                break;
            case Node.CDATA_SECTION_NODE:
                importedNode = (Node)coreCreateCDATASection(node.getNodeValue());
                importChildren = false;
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction)node;
                importedNode = (Node)coreCreateProcessingInstruction(pi.getTarget(), pi.getData());
                importChildren = false;
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                importedNode = (Node)coreCreateDocumentFragment();
                importChildren = deep;
                break;
            case Node.ENTITY_REFERENCE_NODE:
                importedNode = (Node)coreCreateEntityReference(node.getNodeName());
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

    public final Element DOMDocument.getElementById(String elementId) {
        try {
            for (Iterator<DOMElement> it = new DescendantsIterator<DOMElement>(DOMElement.class, this); it.hasNext(); ) {
                DOMElement element = it.next();
                for (CoreAttribute attr = element.coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
                    if (((Attr)attr).isId() && elementId.equals(attr.coreGetValue())) {
                        return element;
                    }
                }
            }
            return null;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Node DOMDocument.adoptNode(Node source) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final DOMConfiguration DOMDocument.getDomConfig() {
        return domConfig;
    }

    public final boolean DOMDocument.getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    // TODO: we don't cover renaming a typed attribute to a namespace declaration and vice-versa
    public final Node DOMDocument.renameNode(Node node, String namespaceURI, String qualifiedName) throws DOMException {
        if (node instanceof CoreNSAwareNamedNode) {
            CoreNSAwareNamedNode namedNode = (CoreNSAwareNamedNode)node;
            
            if (namedNode.coreGetDocument() != this) {
                throw DOMExceptionUtil.newDOMException(DOMException.WRONG_DOCUMENT_ERR);
            }
            
            // TODO: this is suggested by the documentrenamenode04 test case, but not specified in the DOM3 specs; check what is the required behavior also for the Document#createXXX methods
            if (namespaceURI != null && namespaceURI.length() == 0) {
                namespaceURI = null;
            }
            
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
            if (node instanceof CoreTypedAttribute) {
                NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            } else {
                NSUtil.validateNamespace(namespaceURI, prefix);
            }
            namedNode.coreSetNamespaceURI(namespaceURI);
            namedNode.coreSetLocalName(localName);
            namedNode.coreSetPrefix(prefix);
            return node;
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
    }

    public final void DOMDocument.setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node DOMDocument.cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Node DOMDocument.shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Element DOMDocument.createElement(String tagName) throws DOMException {
        NSUtil.validateName(tagName);
        return (Element)coreCreateElement(tagName);
    }
    
    public final Element DOMDocument.createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
        NSUtil.validateNamespace(namespaceURI, prefix);
        return (Element)coreCreateElement(namespaceURI, localName, prefix);
    }
    
    public final Attr DOMDocument.createAttribute(String name) throws DOMException {
        NSUtil.validateName(name);
        return (Attr)coreCreateAttribute(name, null, null);
    }

    public final Attr DOMDocument.createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = null;
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return (Attr)coreCreateNamespaceDeclaration(NSUtil.getDeclaredPrefix(localName, prefix), null);
        } else {
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            return (Attr)coreCreateAttribute(namespaceURI, localName, prefix, null, null);
        }
    }

    public final ProcessingInstruction DOMDocument.createProcessingInstruction(String target, String data) throws DOMException {
        NSUtil.validateName(target);
        return (ProcessingInstruction)coreCreateProcessingInstruction(target, data);
    }
    
    public final DocumentFragment DOMDocument.createDocumentFragment() {
        return (DocumentFragment)coreCreateDocumentFragment();
    }

    public final Text DOMDocument.createTextNode(String data) {
        return (Text)coreCreateText(data);
    }

    public final Comment DOMDocument.createComment(String data) {
        return (Comment)coreCreateComment(data);
    }

    public final CDATASection DOMDocument.createCDATASection(String data) throws DOMException {
        return (CDATASection)coreCreateCDATASection(data);
    }

    public final EntityReference DOMDocument.createEntityReference(String name) throws DOMException {
        return (EntityReference)coreCreateEntityReference(name);
    }

    public final int DOMDocument.getStructureVersion() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public final Document DOMDocument.getOwnerDocument() {
        return null;
    }
    
    public final Node DOMDocument.getParentNode() {
        return null;
    }

    public final Map<String,UserData> DOMDocument.getUserDataMap(DOMCoreNode node, boolean create) {
        if (userDataMap == null) {
            if (!create) {
                return null;
            }
            userDataMap = new HashMap<DOMCoreNode,Map<String,UserData>>();
        }
        Map<String,UserData> mapForNode = userDataMap.get(node);
        if (mapForNode == null) {
            if (!create) {
                return null;
            }
            mapForNode = new HashMap<String,UserData>();
            userDataMap.put(node, mapForNode);
        }
        return mapForNode;
    }

    public final String DOMDocument.getTextContent() {
        return null;
    }

    public final void DOMDocument.setTextContent(@SuppressWarnings("unused") String textContent) {
        // Setting textContent on a Document has no effect.
    }

    public final Node DOMDocument.getNextSibling() {
        return null;
    }

    public final Node DOMDocument.getPreviousSibling() {
        return null;
    }

    public final short DOMDocument.getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    public final String DOMDocument.getNodeValue() throws DOMException {
        return null;
    }

    public final void DOMDocument.setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String DOMDocument.getNodeName() {
        return "#document";
    }
    
    public final CoreElement DOMDocument.getNamespaceContext() {
        try {
            return coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final String DOMDocument.getNamespaceURI() {
        return null;
    }

    public final String DOMDocument.getPrefix() {
        return null;
    }

    public final void DOMDocument.setPrefix(String prefix) throws DOMException {
        // Ignored
    }

    public final String DOMDocument.getLocalName() {
        return null;
    }
    
    public final void DOMDocument.normalizeDocument() {
        try {
            normalize((NormalizationConfig)getDomConfig());
        } catch (AbortNormalizationException ex) {
            // Do nothing, just abort.
        }
    }
    
    public final void DOMDocument.normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
}
