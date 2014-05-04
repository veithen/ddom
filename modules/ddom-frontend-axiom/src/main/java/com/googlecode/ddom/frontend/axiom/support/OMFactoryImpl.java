/*
 * Copyright 2009-2011,2013-2014 Andreas Veithen
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
package com.googlecode.ddom.frontend.axiom.support;

import javax.xml.namespace.QName;

import org.apache.axiom.ext.stax.datahandler.DataHandlerProvider;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMComment;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMDocType;
import org.apache.axiom.om.OMDocument;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMEntityReference;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMProcessingInstruction;
import org.apache.axiom.om.OMSourcedElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.frontend.axiom.intf.AxiomAttribute;
import com.googlecode.ddom.frontend.axiom.intf.AxiomCDATASection;
import com.googlecode.ddom.frontend.axiom.intf.AxiomCharacterData;
import com.googlecode.ddom.frontend.axiom.intf.AxiomComment;
import com.googlecode.ddom.frontend.axiom.intf.AxiomContainer;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocument;
import com.googlecode.ddom.frontend.axiom.intf.AxiomDocumentTypeDeclaration;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomEntityReference;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNodeFactory;
import com.googlecode.ddom.frontend.axiom.intf.AxiomProcessingInstruction;
import com.googlecode.ddom.frontend.axiom.intf.AxiomSourcedElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomText;

public class OMFactoryImpl implements OMFactory {
    protected final AxiomNodeFactory nodeFactory;
    
    public OMFactoryImpl(AxiomNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public final OMMetaFactory getMetaFactory() {
        return nodeFactory;
    }

    public final OMAttribute createOMAttribute(String localName, OMNamespace ns, String value) {
        String namespaceURI;
        String prefix;
        if (ns == null) {
            namespaceURI = "";
            prefix = "";
        } else {
            namespaceURI = ns.getNamespaceURI();
            prefix = ns.getPrefix();
            if (namespaceURI.length() == 0) {
                if (prefix == null) {
                    prefix = "";
                } else if (prefix.length() > 0) {
                    throw new IllegalArgumentException("Cannot create a prefixed attribute with an empty namespace name");
                }
            } else {
                if (prefix == null) {
                    prefix = OMSerializerUtil.getNextNSPrefix();
                } else if (prefix.length() == 0) {
                    throw new IllegalArgumentException("Cannot create an unprefixed attribute with a namespace");
                }
            }
        }
        AxiomAttribute attr = (AxiomAttribute)nodeFactory.createAttribute(null, namespaceURI, localName, prefix, value, "CDATA");
        attr.setOMFactory(this);
        return attr;
    }

    public final OMDocType createOMDocType(OMContainer parent, String content) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMDocType createOMDocType(OMContainer parent, String rootName, String publicId, String systemId, String internalSubset) {
        if (parent == null) {
            return (AxiomDocumentTypeDeclaration)nodeFactory.createDocumentTypeDeclaration(null, rootName, publicId, systemId);
        } else {
            try {
                return (AxiomDocumentTypeDeclaration)((AxiomDocument)parent).coreAppendDocumentTypeDeclaration(rootName, publicId, systemId);
            } catch (CoreModelException ex) {
                throw AxiomExceptionTranslator.translate(ex);
            }
        }
    }

    public final OMDocument createOMDocument() {
        AxiomDocument document = (AxiomDocument)nodeFactory.createDocument();
        // TODO
//        document.setOMFactory(this);
        return document;
    }

    public final OMElement createOMElement(QName qname) {
        String prefix = qname.getPrefix();
        return createOMElement(qname.getLocalPart(), qname.getNamespaceURI(), prefix.length() == 0 ? null : prefix);
    }

    public final OMElement createOMElement(String localName, OMNamespace ns) {
        return createOMElement(localName, NSUtil.getNamespaceURI(ns), NSUtil.getPrefix(ns));
    }

    public final OMElement createOMElement(String localName, String namespaceURI, String prefix) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("Namespace URI must not be null");
        }
        if (prefix == null) {
            prefix = namespaceURI.length() == 0 ? "" : OMSerializerUtil.getNextNSPrefix();
        }
        if (prefix.length() > 0 && namespaceURI.length() == 0) {
            throw new IllegalArgumentException("Cannot create a prefixed element with an empty namespace name");
        }
        AxiomElement element = (AxiomElement)nodeFactory.createElement(null, namespaceURI, localName, prefix);
        if (namespaceURI.length() != 0) {
            try {
                element.coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
            } catch (CoreModelException ex) {
                throw AxiomExceptionTranslator.translate(ex);
            }
        }
        element.setOMFactory(this);
        return element;
    }

    private OMElement createOMElement(String localName, String namespaceURI, String prefix, AxiomContainer parent) {
        try {
            if (prefix != null && prefix.length() > 0 && namespaceURI.length() == 0) {
                throw new IllegalArgumentException("Cannot create a prefixed element with an empty namespace name");
            }
            AxiomElement element = (AxiomElement)parent.coreAppendElement(namespaceURI, localName, null);
            element.coreSetPrefix(element.ensureNamespaceIsDeclared(prefix, namespaceURI));
            element.setOMFactory(this);
            return element;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final OMElement createOMElement(QName qname, OMContainer parent) {
        if (parent == null) {
            return createOMElement(qname);
        } else {
            String prefix = qname.getPrefix();
            // Empty prefix means generate a prefix
            if (prefix.length() == 0) {
                prefix = null;
            }
            return createOMElement(qname.getLocalPart(), qname.getNamespaceURI(), prefix, (AxiomContainer)parent);
        }
    }

    public final OMElement createOMElement(String localName, OMNamespace ns, OMContainer parent) {
        if (parent == null) {
            return createOMElement(localName, ns);
        } else if (ns == null) {
            return createOMElement(localName, "", "", (AxiomContainer)parent);
        } else {
            return createOMElement(localName, ns.getNamespaceURI(), ns.getPrefix(), (AxiomContainer)parent);
        }
    }

    private OMSourcedElement createOMElement(OMDataSource dataSource, String namespaceURI, String localName, String prefix) {
        if (dataSource == null) {
            throw new IllegalArgumentException("OMDataSource can't be null");
        }
        AxiomSourcedElement element = nodeFactory.createElement(null, AxiomSourcedElement.class, namespaceURI, localName, prefix);
        element.setOMFactory(this);
        element.setDataSource(dataSource);
        return element;
    }
    
    public OMSourcedElement createOMElement(OMDataSource source) {
        return createOMElement(source, null, null, null);
    }

    public final OMSourcedElement createOMElement(OMDataSource source, QName qname) {
        return createOMElement(source, qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
    }

    public final OMSourcedElement createOMElement(OMDataSource source, String localName, OMNamespace ns) {
        return createOMElement(source, NSUtil.getNamespaceURI(ns), localName, NSUtil.getPrefix(ns));
    }

    public final OMNamespace createOMNamespace(String uri, String prefix) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        return new OMNamespaceImpl(uri, prefix);
    }

    public final OMComment createOMComment(OMContainer parent, String content) {
        try {
            AxiomComment comment;
            if (parent == null) {
                comment = (AxiomComment)nodeFactory.createComment(null, content);
            } else {
                comment = (AxiomComment)((AxiomContainer)parent).coreAppendComment(content);
            }
            comment.setOMFactory(this);
            return comment;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final OMProcessingInstruction createOMProcessingInstruction(OMContainer parent, String piTarget, String piData) {
        try {
            AxiomProcessingInstruction pi;
            if (parent == null) {
                pi = (AxiomProcessingInstruction)nodeFactory.createProcessingInstruction(null, piTarget, piData);
            } else {
                pi = (AxiomProcessingInstruction)((AxiomContainer)parent).coreAppendProcessingInstruction(piTarget, piData);
            }
            pi.setOMFactory(this);
            return pi;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final OMText createOMText(String data) {
        AxiomText text = (AxiomText)nodeFactory.createCharacterData(null, data);
        text.setOMFactory(this);
        return text;
    }

    public final OMText createOMText(OMContainer parent, String data) {
        try {
            AxiomCharacterData text = (AxiomCharacterData)((AxiomContainer)parent).coreAppendCharacterData(data);
            text.setOMFactory(this);
            return text;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final OMText createOMText(OMContainer parent, OMText source) {
        // TODO: implementation is incorrect for MTOM
        // TODO: there are no test cases for this method in Axiom (technical debt caused by AXIOM-343)
        return createOMText(parent, source.getText());
    }

    public final OMText createOMText(OMContainer parent, QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(Object dataHandler, boolean optimize) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final OMText createOMText(String data, int type) {
        AxiomText node;
        switch (type) {
            case OMNode.TEXT_NODE:
                node = (AxiomText)nodeFactory.createCharacterData(null, data);
                break;
            case OMNode.CDATA_SECTION_NODE:
                node = (AxiomCDATASection)nodeFactory.createCDATASection(null, data);
                break;
            default:
                // TODO: support the other types
                throw new UnsupportedOperationException();
        }
        node.setOMFactory(this);
        return node;
    }

    public final OMText createOMText(OMContainer parent, String data, int type) {
        AxiomText node;
        try {
            switch (type) {
                case OMNode.TEXT_NODE:
                    node = (AxiomText)((AxiomContainer)parent).coreAppendCharacterData(data);
                    break;
                case OMNode.CDATA_SECTION_NODE:
                    node = (AxiomCDATASection)((AxiomContainer)parent).coreAppendCDATASection(data);
                    break;
                default:
                    // TODO: support the other types
                    throw new UnsupportedOperationException();
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
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

    public final OMEntityReference createOMEntityReference(OMContainer parent, String name) {
        if (parent == null) {
            return (AxiomEntityReference)nodeFactory.createEntityReference(null, name);
        } else {
            try {
                return (AxiomEntityReference)((AxiomContainer)parent).coreAppendEntityReference(name);
            } catch (CoreModelException ex) {
                throw AxiomExceptionTranslator.translate(ex);
            }
        }
    }
}
