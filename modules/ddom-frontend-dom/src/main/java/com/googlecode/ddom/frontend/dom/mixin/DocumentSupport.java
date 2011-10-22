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
package com.googlecode.ddom.frontend.dom.mixin;

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

import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;
import com.googlecode.ddom.core.CoreTypedAttribute;
import com.googlecode.ddom.core.NodeFactory;
import com.googlecode.ddom.core.Selector;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.dom.intf.AbortNormalizationException;
import com.googlecode.ddom.frontend.dom.intf.DOMAttribute;
import com.googlecode.ddom.frontend.dom.intf.DOMCoreChildNode;
import com.googlecode.ddom.frontend.dom.intf.DOMCoreNode;
import com.googlecode.ddom.frontend.dom.intf.DOMDocument;
import com.googlecode.ddom.frontend.dom.intf.DOMDocumentFragment;
import com.googlecode.ddom.frontend.dom.intf.DOMElement;
import com.googlecode.ddom.frontend.dom.intf.DOMParentNode;
import com.googlecode.ddom.frontend.dom.intf.NormalizationConfig;
import com.googlecode.ddom.frontend.dom.support.DOMConfigurationImpl;
import com.googlecode.ddom.frontend.dom.support.DOMExceptionUtil;
import com.googlecode.ddom.frontend.dom.support.DOMImplementationImpl;
import com.googlecode.ddom.frontend.dom.support.NSUtil;
import com.googlecode.ddom.frontend.dom.support.NodeUtil;
import com.googlecode.ddom.frontend.dom.support.Policies;
import com.googlecode.ddom.frontend.dom.support.UserData;
import com.googlecode.ddom.symbols.Symbols;

@Mixin(CoreDocument.class)
public abstract class DocumentSupport implements DOMDocument {
    private DOMImplementationImpl domImplementation;
    private DOMConfigurationImpl domConfig;
    private Map<DOMCoreNode,Map<String,UserData>> userDataMap;
    
    public final DOMImplementation getImplementation() {
        if (domImplementation == null) {
            domImplementation = new DOMImplementationImpl(coreGetNodeFactory());
        }
        return domImplementation;
    }

    public final String getXmlVersion() {
        try {
            String xmlVersion = coreGetXmlVersion();
            return xmlVersion == null ? "1.0" : xmlVersion;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    // TODO: check right behavior when parameter is null
    public final void setXmlVersion(String xmlVersion) {
        if (xmlVersion.equals("1.0") || xmlVersion.equals("1.1")) {
            coreSetXmlVersion(xmlVersion);
        } else {
            throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
        }
    }

    public final String getInputEncoding() {
        try {
            return coreGetInputEncoding();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final String getXmlEncoding() {
        try {
            return coreGetXmlEncoding();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final boolean getXmlStandalone() {
        try {
            Boolean standalone = coreGetStandalone();
            return standalone != null && standalone.booleanValue();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        coreSetStandalone(xmlStandalone);
    }

    // TODO: need test for this
    public final String getDocumentURI() {
        try {
            return coreGetDocumentURI();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final void setDocumentURI(String documentURI) {
        try {
            coreSetDocumentURI(documentURI);
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final boolean hasAttributes() {
        return false;
    }

    public final NamedNodeMap getAttributes() {
        return null;
    }

    public final Element getDocumentElement() {
        try {
            return (Element)coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final DocumentType getDoctype() {
        try {
            return NodeUtil.toDOM(coreGetDocumentTypeDeclaration());
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    public final Node importNode(Node node, boolean deep) throws DOMException {
        NodeFactory nodeFactory = coreGetNodeFactory();
        try {
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE: {
                    Element element = (Element)node;
                    // TODO: detect DOM 1 elements (as with attributes)
                    String namespaceURI = element.getNamespaceURI();
                    String prefix = element.getPrefix();
                    DOMElement importedElement = (DOMElement)nodeFactory.createElement(this, namespaceURI == null ? "" : namespaceURI, element.getLocalName(), prefix == null ? "" : prefix);
                    NamedNodeMap attributes = element.getAttributes();
                    for (int length = attributes.getLength(), i=0; i<length; i++) {
                        importedElement.coreAppendAttribute((DOMAttribute)importNode(attributes.item(i), true), Policies.ATTRIBUTE_MIGRATION_POLICY);
                    }
                    if (deep) {
                        importChildren(element, importedElement);
                    }
                    return importedElement;
                }
                case Node.ATTRIBUTE_NODE: {
                    Attr attr = (Attr)node;
                    String localName = attr.getLocalName();
                    DOMAttribute importedAttr;
                    if (localName == null) {
                        importedAttr = (DOMAttribute)nodeFactory.createAttribute(this, attr.getName(), null, null);
                    } else {
                        String namespaceURI = attr.getNamespaceURI();
                        String prefix = attr.getPrefix();
                        if (prefix == null) {
                            prefix = "";
                        }
                        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
                            importedAttr = (DOMAttribute)nodeFactory.createNamespaceDeclaration(this, NSUtil.getDeclaredPrefix(localName, prefix), null);
                        } else {
                            importedAttr = (DOMAttribute)nodeFactory.createAttribute(this, namespaceURI == null ? "" : namespaceURI, localName, prefix, null, null);
                        }
                    }
                    // Children of attributes are always imported, regardless of the value of the "deep" parameter
                    importChildren(attr, importedAttr);
                    return importedAttr;
                }
                case Node.COMMENT_NODE:
                    return (Node)nodeFactory.createComment(this, node.getNodeValue());
                case Node.TEXT_NODE:
                    return (Node)nodeFactory.createCharacterData(this, node.getNodeValue());
                case Node.CDATA_SECTION_NODE:
                    return (Node)coreGetNodeFactory().createCDATASection(this, node.getNodeValue());
                case Node.PROCESSING_INSTRUCTION_NODE:
                    ProcessingInstruction pi = (ProcessingInstruction)node;
                    return (Node)nodeFactory.createProcessingInstruction(this, pi.getTarget(), pi.getData());
                case Node.DOCUMENT_FRAGMENT_NODE:
                    DOMDocumentFragment importedNode = (DOMDocumentFragment)nodeFactory.createDocumentFragment(this);
                    if (deep) {
                        importChildren(node, importedNode);
                    }
                    return importedNode;
                case Node.ENTITY_REFERENCE_NODE:
                    return (Node)nodeFactory.createEntityReference(this, node.getNodeName());
                default:
                    throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
            }
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }
    
    private void importChildren(Node node, DOMParentNode target) throws CoreModelException {
        DOMCoreChildNode previousImportedChild = null;
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            DOMCoreChildNode importedChild = (DOMCoreChildNode)importNode(child, true);
            if (previousImportedChild == null) {
                target.coreAppendChild(importedChild, Policies.NODE_MIGRATION_POLICY);
            } else {
                // Inserting a new child after the last child is more efficient than
                // appending it to the parent (at least in the default linkedlist back-end).
                previousImportedChild.coreInsertSiblingAfter(importedChild, Policies.NODE_MIGRATION_POLICY);
            }
            previousImportedChild = importedChild;
        }
    }

    public final Element getElementById(String elementId) {
        try {
            for (Iterator<DOMElement> it = coreGetNodes(Axis.DESCENDANTS, Selector.ELEMENT, DOMElement.class); it.hasNext(); ) {
                DOMElement element = it.next();
                for (CoreAttribute attr = element.coreGetFirstAttribute(); attr != null; attr = attr.coreGetNextAttribute()) {
                    if (((Attr)attr).isId() && elementId.equals(attr.coreGetTextContent(TextCollectorPolicy.DEFAULT))) {
                        return element;
                    }
                }
            }
            return null;
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
    }

    public final Node adoptNode(Node source) throws DOMException {
        switch (source.getNodeType()) {
            case Node.DOCUMENT_NODE:
                throw DOMExceptionUtil.newDOMException(DOMException.NOT_SUPPORTED_ERR);
            case Node.DOCUMENT_FRAGMENT_NODE:
                ((DOMDocumentFragment)source).coreSetOwnerDocument(this);
                return source;
            case Node.ATTRIBUTE_NODE:
                ((DOMAttribute)source).coreRemove(this);
                return source;
            default:
                try {
                    ((DOMCoreChildNode)source).coreDetach(this);
                    return source;
                } catch (CoreModelException ex) {
                    throw DOMExceptionUtil.translate(ex);
                }
        }
    }

    public final DOMConfiguration getDomConfig() {
        if (domConfig == null) {
            domConfig = new DOMConfigurationImpl();
        }
        return domConfig;
    }

    public final boolean getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    // TODO: we don't cover renaming a typed attribute to a namespace declaration and vice-versa
    public final Node renameNode(Node node, String namespaceURI, String qualifiedName) throws DOMException {
        if (node instanceof CoreNSAwareNamedNode) {
            CoreNSAwareNamedNode namedNode = (CoreNSAwareNamedNode)node;
            
            if (namedNode.coreGetOwnerDocument(true) != this) {
                throw DOMExceptionUtil.newDOMException(DOMException.WRONG_DOCUMENT_ERR);
            }
            
            int i = NSUtil.validateQualifiedName(qualifiedName);
            String prefix;
            String localName;
            if (i == -1) {
                prefix = "";
                localName = qualifiedName;
            } else {
                prefix = qualifiedName.substring(0, i);
                localName = qualifiedName.substring(i+1);
            }
            namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
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

    public final void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node cloneNode(boolean deep) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Node shallowClone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Element createElement(String tagName) throws DOMException {
        NSUtil.validateName(tagName);
        return (Element)coreGetNodeFactory().createElement(this, tagName);
    }
    
    public final Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
        NSUtil.validateNamespace(namespaceURI, prefix);
        return (Element)coreGetNodeFactory().createElement(this, namespaceURI, localName, prefix);
    }
    
    public final Attr createAttribute(String name) throws DOMException {
        NSUtil.validateName(name);
        return (Attr)coreGetNodeFactory().createAttribute(this, name, null, null);
    }

    public final Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        int i = NSUtil.validateQualifiedName(qualifiedName);
        String prefix;
        String localName;
        if (i == -1) {
            prefix = "";
            localName = qualifiedName;
        } else {
            // Use symbol table to avoid creation of new String objects
            Symbols symbols = getSymbols();
            prefix = symbols.getSymbol(qualifiedName, 0, i);
            localName = symbols.getSymbol(qualifiedName, i+1, qualifiedName.length());
        }
        if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI)) {
            return (Attr)coreGetNodeFactory().createNamespaceDeclaration(this, NSUtil.getDeclaredPrefix(localName, prefix), null);
        } else {
            namespaceURI = NSUtil.normalizeNamespaceURI(namespaceURI);
            NSUtil.validateAttributeName(namespaceURI, localName, prefix);
            return (Attr)coreGetNodeFactory().createAttribute(this, namespaceURI, localName, prefix, null, null);
        }
    }

    public final ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        NSUtil.validateName(target);
        return (ProcessingInstruction)coreGetNodeFactory().createProcessingInstruction(this, target, data);
    }
    
    public final DocumentFragment createDocumentFragment() {
        return (DocumentFragment)coreGetNodeFactory().createDocumentFragment(this);
    }

    public final Text createTextNode(String data) {
        return (Text)coreGetNodeFactory().createCharacterData(this, data);
    }

    public final Comment createComment(String data) {
        return (Comment)coreGetNodeFactory().createComment(this, data);
    }

    public final CDATASection createCDATASection(String data) throws DOMException {
        return (CDATASection)coreGetNodeFactory().createCDATASection(this, data);
    }

    public final EntityReference createEntityReference(String name) throws DOMException {
        return (EntityReference)coreGetNodeFactory().createEntityReference(this, name);
    }

    public final int getStructureVersion() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public final Document getOwnerDocument() {
        return null;
    }
    
    public final Node getParentNode() {
        return null;
    }

    public final Map<String,UserData> getUserDataMap(DOMCoreNode node, boolean create) {
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

    public final String getTextContent() {
        return null;
    }

    public final void setTextContent(@SuppressWarnings("unused") String textContent) {
        // Setting textContent on a Document has no effect.
    }

    public final Node getNextSibling() {
        return null;
    }

    public final Node getPreviousSibling() {
        return null;
    }

    public final short getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    public final String getNodeValue() throws DOMException {
        return null;
    }

    public final void setNodeValue(String nodeValue) throws DOMException {
        // Setting the node value has no effect
    }

    public final String getNodeName() {
        return "#document";
    }
    
    public final CoreElement getNamespaceContext() {
        try {
            return coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw DOMExceptionUtil.translate(ex);
        }
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
    
    public final void normalizeDocument() {
        try {
            normalize((NormalizationConfig)getDomConfig());
        } catch (AbortNormalizationException ex) {
            // Do nothing, just abort.
        }
    }
    
    public final void normalize(NormalizationConfig config) throws AbortNormalizationException {
        normalizeChildren(config);
    }
}
