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

import com.google.code.ddom.spi.model.CoreAttribute;
import com.google.code.ddom.spi.model.CoreElement;
import com.google.code.ddom.utils.dom.iterator.DescendantsIterator;

public aspect DocumentSupport {
    private DOMImplementationImpl DocumentImpl.domImplementation;
    
    public final DOMImplementation DocumentImpl.getImplementation() {
        if (domImplementation == null) {
            domImplementation = new DOMImplementationImpl(getNodeFactory());
        }
        return domImplementation;
    }

    public final String DocumentImpl.getInputEncoding() {
        return coreGetInputEncoding();
    }

    public final String DocumentImpl.getXmlEncoding() {
        return coreGetXmlEncoding();
    }

    // TODO: need test for this
    public final String DocumentImpl.getDocumentURI() {
        return coreGetDocumentURI();
    }

    public final void DocumentImpl.setDocumentURI(String documentURI) {
        coreSetDocumentURI(documentURI);
    }

    public final Element DocumentImpl.getDocumentElement() {
        return (Element)coreGetDocumentElement();
    }
    
    public final DocumentType DocumentImpl.getDoctype() {
        return (DocumentType)coreGetDocumentType();
    }
    
    public final Node DocumentImpl.importNode(Node node, boolean deep) throws DOMException {
        // TODO: do we really need to use getNodeFactory().createXXX, or can we just use createXXX?
        Node importedNode;
        boolean importChildren;
        switch (node.getNodeType()) {
            case ELEMENT_NODE:
                Element element = (Element)node;
                // TODO: detect DOM 1 elements (as with attributes)
                importedNode = getNodeFactory().createElement(this, element.getNamespaceURI(), element.getLocalName(), element.getPrefix(), true);
                importChildren = deep;
                break;
            case ATTRIBUTE_NODE:
                Attr attr = (Attr)node;
                String localName = attr.getLocalName();
                if (localName == null) {
                    importedNode = getNodeFactory().createAttribute(this, attr.getName(), null, null);
                } else {
                    importedNode = getNodeFactory().createAttribute(this, attr.getNamespaceURI(), localName, attr.getPrefix(), null, null);
                }
                importChildren = true;
                break;
            case COMMENT_NODE:
                importedNode = (Node)getNodeFactory().createComment(this, node.getNodeValue());
                importChildren = false;
                break;
            case TEXT_NODE:
                importedNode = (Node)getNodeFactory().createText(this, node.getNodeValue());
                importChildren = false;
                break;
            case CDATA_SECTION_NODE:
                importedNode = (Node)getNodeFactory().createCDATASection(this, node.getNodeValue());
                importChildren = false;
                break;
            case PROCESSING_INSTRUCTION_NODE:
                ProcessingInstruction pi = (ProcessingInstruction)node;
                importedNode = (Node)getNodeFactory().createProcessingInstruction(this, pi.getTarget(), pi.getData());
                importChildren = false;
                break;
            case DOCUMENT_FRAGMENT_NODE:
                importedNode = (Node)getNodeFactory().createDocumentFragment(this);
                importChildren = deep;
                break;
            case ENTITY_REFERENCE_NODE:
                importedNode = (Node)getNodeFactory().createEntityReference(this, node.getNodeName());
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

    public final Element DocumentImpl.getElementById(String elementId) {
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

    public final Node DocumentImpl.adoptNode(Node source) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final DOMConfiguration DocumentImpl.getDomConfig() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean DocumentImpl.getStrictErrorChecking() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final boolean DocumentImpl.getXmlStandalone() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final String DocumentImpl.getXmlVersion() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final Node DocumentImpl.renameNode(Node n, String namespaceURI, String qualifiedName) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void DocumentImpl.setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void DocumentImpl.setXmlStandalone(boolean xmlStandalone) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final void DocumentImpl.setXmlVersion(String xmlVersion) throws DOMException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
