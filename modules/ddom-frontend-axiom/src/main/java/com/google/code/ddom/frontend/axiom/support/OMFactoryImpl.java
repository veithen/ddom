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
package com.google.code.ddom.frontend.axiom.support;

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
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;

import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomCDATASection;
import com.google.code.ddom.frontend.axiom.intf.AxiomComment;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.google.code.ddom.frontend.axiom.intf.AxiomProcessingInstruction;
import com.google.code.ddom.frontend.axiom.intf.AxiomText;
import com.google.code.ddom.frontend.axiom.intf.AxiomTextNode;
import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.util.QNameUtil;

public class OMFactoryImpl implements OMFactory {
    protected final AxiomNodeFactory nodeFactory;
    
    public OMFactoryImpl(AxiomNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public final OMMetaFactory getMetaFactory() {
        return nodeFactory;
    }

    public final OMAttribute createOMAttribute(String localName, OMNamespace ns, String value) {
        AxiomAttribute attr = (AxiomAttribute)nodeFactory.createAttribute(null, NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns), value, "CDATA");
        attr.setOMFactory(this);
        return attr;
    }

    public final OMComment createOMComment(OMContainer parent, String content) {
        try {
            AxiomComment comment = (AxiomComment)((AxiomContainer)parent).coreAppendComment(content);
            comment.setOMFactory(this);
            return comment;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final OMDocType createOMDocType(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMDocument createOMDocument() {
        AxiomDocument document = (AxiomDocument)nodeFactory.createDocument();
        // TODO
//        document.setOMFactory(this);
        return document;
    }

    public final OMElement createOMElement(QName qname) {
        String namespaceURI = QNameUtil.getNamespaceURI(qname);
        String prefix = QNameUtil.getPrefix(qname);
        if (prefix == null && namespaceURI != null) {
            prefix = OMSerializerUtil.getNextNSPrefix();
        }
        AxiomElement element = (AxiomElement)nodeFactory.createElement(null, namespaceURI, qname.getLocalPart(), prefix);
        if (prefix != null) {
            element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        }
        element.setOMFactory(this);
        return element;
    }

    public final OMElement createOMElement(QName qname, OMContainer parent) {
        if (parent == null) {
            return createOMElement(qname);
        } else {
            try {
                // TODO
                String namespaceURI = QNameUtil.getNamespaceURI(qname);
                String prefix = QNameUtil.getPrefix(qname);
                AxiomElement element = (AxiomElement)((AxiomContainer)parent).coreAppendElement(namespaceURI, qname.getLocalPart(), prefix);
                element.ensureNamespaceIsDeclared(prefix, namespaceURI);
                element.setOMFactory(this);
                return element;
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
    }

    public final OMElement createOMElement(String localName, OMNamespace ns) {
        String namespaceURI = NSUtil.getNamespaceURI(ns);
        String prefix = NSUtil.getPrefix(ns);
        AxiomElement element = (AxiomElement)nodeFactory.createElement(null, namespaceURI, localName, prefix);
        element.setOMFactory(this);
        // TODO: we don't have a test case covering ns == null
        if (ns != null) {
            element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        }
        return element;
    }

    public final OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent) {
        if (parent == null) {
            return createOMElement(localName, ns);
        } else {
            // TODO: namespace declaration?
            try {
                AxiomElement element = (AxiomElement)((AxiomContainer)parent).coreAppendElement(NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns));
                element.setOMFactory(this);
                return element;
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
        }
    }

    public final OMElement createOMElement(String localName, String namespaceURI, String prefix) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Namespace URI must not be null");
        }
        if (namespaceURI.length() == 0) {
            namespaceURI = null;
        }
        AxiomElement element = (AxiomElement)nodeFactory.createElement(null, namespaceURI, localName, prefix);
        // TODO: not always necessary
        element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        element.setOMFactory(this);
        return element;
    }

    public final OMSourcedElement createOMElement(OMDataSource source, QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMSourcedElement createOMElement(OMDataSource source, String localName, OMNamespace ns) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMNamespace createOMNamespace(String uri, String prefix) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        return new OMNamespaceImpl(uri, prefix);
    }

    public final OMProcessingInstruction createOMProcessingInstruction(OMContainer parent, String piTarget, String piData) {
        try {
            AxiomProcessingInstruction pi = (AxiomProcessingInstruction)((AxiomContainer)parent).coreAppendProcessingInstruction(piTarget, piData);
            pi.setOMFactory(this);
            return pi;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final OMText createOMText(String data) {
        AxiomText text = (AxiomText)nodeFactory.createText(null, data);
        text.setOMFactory(this);
        return text;
    }

    public final OMText createOMText(OMContainer parent, String data) {
        try {
            AxiomText text = (AxiomText)((AxiomContainer)parent).coreAppendText(data);
            text.setOMFactory(this);
            return text;
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final OMText createOMText(OMContainer parent, OMText source) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(OMContainer parent, QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(String s, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(Object dataHandler, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(OMContainer parent, String data, int type) {
        AxiomTextNode node;
        try {
            switch (type) {
                case OMNode.TEXT_NODE:
                    node = (AxiomTextNode)((AxiomContainer)parent).coreAppendText(data);
                    break;
                case OMNode.CDATA_SECTION_NODE:
                    AxiomCDATASection cdataSection = (AxiomCDATASection)((AxiomContainer)parent).coreAppendCDATASection();
                    cdataSection.coreSetValue(data);
                    node = cdataSection;
                    break;
                default:
                    // TODO: support the other types
                    throw new UnsupportedOperationException();
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        node.setOMFactory(this);
        return node;
    }

    public final OMText createOMText(OMContainer parent, char[] charArary, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(OMContainer parent, QName text, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(String contentID, DataHandlerProvider dataHandlerProvider, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(String contentID, OMContainer parent, OMXMLParserWrapper builder) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(OMContainer parent, String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    //
    // Methods used by StAXBuilder and StAXOMBuilder.
    //
    
    public final OMDocument createOMDocument(OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public final OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }
}
