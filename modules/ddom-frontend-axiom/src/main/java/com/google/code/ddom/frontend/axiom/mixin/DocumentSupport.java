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
package com.google.code.ddom.frontend.axiom.mixin;

import javax.xml.namespace.QName;

import org.apache.axiom.ext.stax.datahandler.DataHandlerProvider;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMComment;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMDocType;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLParserWrapper;

import com.google.code.ddom.core.AttributeMatcher;
import com.google.code.ddom.core.CoreDocument;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomDocument;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.NSUtil;
import com.google.code.ddom.frontend.axiom.support.OMNamespaceImpl;

@Mixin(CoreDocument.class)
public abstract class DocumentSupport implements AxiomDocument {
    private int nextGeneratedPrefix = 1;
    
    public String getXMLVersion() {
        try {
            return coreGetXmlVersion();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void setXMLVersion(String version) {
        coreSetXmlVersion(version);
    }
    
    public String getCharsetEncoding() {
        try {
            // TODO: need to check that this is the right property!
            return coreGetXmlEncoding();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void setCharsetEncoding(String charsetEncoding) {
        // TODO: need to check that this is the right property!
        coreSetXmlEncoding(charsetEncoding);
    }
    
    public String isStandalone() {
        try {
            return coreGetStandalone() ? "yes" : "no";
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setStandalone(String isStandalone) {
        coreSetStandalone("yes".equals(isStandalone));
    }
    
    public OMElement getOMDocumentElement() {
        try {
            return (OMElement)coreGetDocumentElement();
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public void setOMDocumentElement(@SuppressWarnings("unused") OMElement rootElement) {
        throw new UnsupportedOperationException("This operation is unsupported because it is ill-defined in the Axiom API");
    }

    public OMDocument createOMDocument() {
        // TODO: check if we should support this operation
        throw new UnsupportedOperationException();
    }

    public OMDocument createOMDocument(OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMElement createOMElement(String localName, OMNamespace ns) {
        return (OMElement)coreCreateElement(NSUtil.getNamespaceURI(ns), localName, NSUtil.getNamespaceURI(ns));
    }

    public OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMSourcedElement createOMElement(OMDataSource source, String localName, OMNamespace ns) {
        throw new UnsupportedOperationException();
    }

    public OMSourcedElement createOMElement(OMDataSource source, QName qname) {
        throw new UnsupportedOperationException();
    }

    public OMElement createOMElement(String localName, String namespaceURI, String namespacePrefix) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement createOMElement(QName qname, OMContainer parent) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMElement createOMElement(QName qname) {
        String namespaceURI = NSUtil.getNamespaceURI(qname);
        String prefix = NSUtil.getPrefix(qname);
        if (prefix == null && namespaceURI != null) {
            prefix = generatePrefix();
        }
        AxiomElement element = (AxiomElement)coreCreateElement(namespaceURI, qname.getLocalPart(), prefix);
        if (prefix != null) {
            element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        }
        return element;
    }

    public OMNamespace createOMNamespace(String uri, String prefix) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        return new OMNamespaceImpl(uri, prefix);
    }

    public OMText createOMText(OMContainer parent, String text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(OMContainer parent, OMText source) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMText createOMText(OMContainer parent, QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(OMContainer parent, String text, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(OMContainer parent, char[] charArary, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(OMContainer parent, QName text, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(String s) {
        return (OMText)coreCreateText(s);
    }

    public OMText createOMText(String s, int type) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(Object dataHandler, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(OMContainer parent, String s, String mimeType, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(String contentID, DataHandlerProvider dataHandlerProvider, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMText createOMText(String contentID, OMContainer parent, OMXMLParserWrapper builder) {
        throw new UnsupportedOperationException();
    }

    public OMAttribute createOMAttribute(String localName, OMNamespace ns, String value) {
        return (OMAttribute)coreCreateAttribute(NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns), value, "CDATA");
    }

    public OMDocType createOMDocType(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMProcessingInstruction createOMProcessingInstruction(OMContainer parent, String piTarget, String piData) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public OMComment createOMComment(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String generatePrefix() {
        return "ns" + nextGeneratedPrefix++;
    }
}
