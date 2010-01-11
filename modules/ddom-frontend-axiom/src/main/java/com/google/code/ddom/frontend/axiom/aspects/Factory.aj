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
package com.google.code.ddom.frontend.axiom.aspects;

import javax.xml.namespace.QName;

import org.apache.axiom.ext.stax.datahandler.DataHandlerProvider;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMComment;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMDocType;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLParserWrapper;

import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.NSUtil;
import com.google.code.ddom.frontend.axiom.support.OMNamespaceImpl;

public aspect Factory {
    public OMFactory AxiomNode.getOMFactory() {
        return (OMFactory)coreGetDocument();
    }
    
    public OMFactory AxiomAttribute.getOMFactory() {
        return (OMFactory)coreGetDocument();
    }

    public OMDocument AxiomDocument.createOMDocument() {
        // TODO: check if we should support this operation
        throw new UnsupportedOperationException();
    }

    public OMDocument AxiomDocument.createOMDocument(OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMElement AxiomDocument.createOMElement(String localName, OMNamespace ns) {
        return (OMElement)getNodeFactory().createElement(this, NSUtil.getNamespaceURI(ns), localName, NSUtil.getNamespaceURI(ns));
    }

    public OMElement AxiomDocument.createOMElement(String localName, OMNamespace ns, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement AxiomDocument.createOMElement(String localName, OMNamespace ns, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMSourcedElement AxiomDocument.createOMElement(OMDataSource source, String localName, OMNamespace ns) {
        throw new UnsupportedOperationException();
    }

    public OMSourcedElement AxiomDocument.createOMElement(OMDataSource source, QName qname) {
        throw new UnsupportedOperationException();
    }

    public OMElement AxiomDocument.createOMElement(String localName, String namespaceURI, String namespacePrefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement AxiomDocument.createOMElement(QName qname, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement AxiomDocument.createOMElement(QName qname) {
        NodeFactory nodeFactory = getNodeFactory();
        String namespaceURI = NSUtil.getNamespaceURI(qname);
        String prefix = NSUtil.getPrefix(qname);
        if (prefix == null && namespaceURI != null) {
            prefix = generatePrefix();
        }
        AxiomElement element = (AxiomElement)nodeFactory.createElement(this, namespaceURI, qname.getLocalPart(), prefix);
        if (prefix != null) {
            element.coreAppendAttribute(nodeFactory.createNamespaceDeclaration(this, prefix, namespaceURI));
        }
        return element;
    }

    public OMNamespace AxiomDocument.createOMNamespace(String uri, String prefix) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        return new OMNamespaceImpl(uri, prefix);
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, String text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, OMText source) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMText AxiomDocument.createOMText(OMContainer parent, QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, String text, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, char[] charArary, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, QName text, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(String s) {
        return (OMText)getNodeFactory().createText(this, s);
    }

    public OMText AxiomDocument.createOMText(String s, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(Object dataHandler, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(OMContainer parent, String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(String contentID, DataHandlerProvider dataHandlerProvider, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText AxiomDocument.createOMText(String contentID, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMAttribute AxiomDocument.createOMAttribute(String localName, OMNamespace ns, String value) {
        return (OMAttribute)getNodeFactory().createAttribute(this, NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns), value, "CDATA");
    }

    public OMDocType AxiomDocument.createOMDocType(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMProcessingInstruction AxiomDocument.createOMProcessingInstruction(OMContainer parent, String piTarget, String piData) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMComment AxiomDocument.createOMComment(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
