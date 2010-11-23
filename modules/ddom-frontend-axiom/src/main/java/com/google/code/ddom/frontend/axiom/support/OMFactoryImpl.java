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
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;

import com.google.code.ddom.core.AttributeMatcher;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.NodeFactory;
import com.google.code.ddom.core.util.QNameUtil;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomComment;
import com.google.code.ddom.frontend.axiom.intf.AxiomContainer;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomProcessingInstruction;
import com.google.code.ddom.frontend.axiom.intf.AxiomText;

public class OMFactoryImpl implements OMFactory {
    private final NodeFactory nodeFactory;
    
    public OMFactoryImpl(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public final OMAttribute createOMAttribute(String localName, OMNamespace ns, String value) {
        return (AxiomAttribute)nodeFactory.createAttribute(null, NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns), value, "CDATA");
    }

    public final OMComment createOMComment(OMContainer parent, String content) {
        try {
            return (AxiomComment)((AxiomContainer)parent).coreAppendComment(content);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final OMDocType createOMDocType(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMDocument createOMDocument() {
        return (AxiomDocument)nodeFactory.createDocument();
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
        return element;
    }

    public final OMElement createOMElement(QName qname, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMElement createOMElement(String localName, OMNamespace ns) {
        return (OMElement)nodeFactory.createElement(null, NSUtil.getNamespaceURI(ns), localName, NSUtil.getNamespaceURI(ns));
    }

    public final OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMSourcedElement createOMElement(OMDataSource source, QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMSourcedElement createOMElement(OMDataSource source, String localName, OMNamespace ns) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMElement createOMElement(String localName, String namespaceURI, String namespacePrefix) {
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
            return (AxiomProcessingInstruction)((AxiomContainer)parent).coreAppendProcessingInstruction(piTarget, piData);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final OMText createOMText(String s) {
        return (AxiomText)nodeFactory.createText(null, s);
    }

    public final OMText createOMText(OMContainer parent, String text) {
        try {
            return (AxiomText)((AxiomContainer)parent).coreAppendText(text);
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

    public final OMText createOMText(OMContainer parent, String text, int type) {
        // TODO
        throw new UnsupportedOperationException();
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

    public OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }
}
