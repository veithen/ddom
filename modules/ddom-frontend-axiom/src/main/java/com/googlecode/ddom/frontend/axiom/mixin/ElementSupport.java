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
package com.googlecode.ddom.frontend.axiom.mixin;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreChildNode;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.IdentityMapper;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomAttribute;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNamespaceDeclaration;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNode;
import com.googlecode.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.googlecode.ddom.frontend.axiom.support.NSUtil;
import com.googlecode.ddom.frontend.axiom.support.NamespaceDeclarationMapper;
import com.googlecode.ddom.frontend.axiom.support.OMNamespaceImpl;
import com.googlecode.ddom.frontend.axiom.support.Policies;
import com.googlecode.ddom.stream.StreamException;

@Mixin(CoreNSAwareElement.class)
public abstract class ElementSupport implements AxiomElement {
    private int lineNumber;
    
    public final void ensureNamespaceIsDeclared(String prefix, String namespaceURI) throws CoreModelException {
        String existingNamespaceURI = coreLookupNamespaceURI(prefix, true);
        if (!namespaceURI.equals(existingNamespaceURI)) {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        }
    }

    public void setNamespaceWithNoFindInCurrentScope(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Iterator getChildElements() {
        return coreGetChildrenByType(Axis.CHILDREN, AxiomElement.class);
    }
    
    private static final IdentityMapper<AxiomAttribute> attributeIdentityMapper = new IdentityMapper<AxiomAttribute>();
    
    public Iterator getAllAttributes() {
        return coreGetAttributesByType(AxiomAttribute.class, attributeIdentityMapper);
    }
    
    public final OMAttribute getAttribute(QName qname) {
        try {
            return (AxiomAttribute)coreGetAttribute(AxiomAttributeMatcher.INSTANCE, qname.getNamespaceURI(), qname.getLocalPart());
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public final String getAttributeValue(QName qname) {
        OMAttribute attr = getAttribute(qname);
        return attr == null ? null : attr.getAttributeValue();
    }
    
    public final OMAttribute addAttribute(OMAttribute attr) {
        AxiomAttribute axiomAttr = (AxiomAttribute)attr;
        try {
            // TODO: what if one creates an attribute with a namespace URI but no prefix???
            String prefix = axiomAttr.coreGetPrefix();
            if (prefix.length() > 0) {
                ensureNamespaceIsDeclared(prefix, axiomAttr.coreGetNamespaceURI());
            }
            return (AxiomAttribute)coreSetAttribute(AxiomAttributeMatcher.INSTANCE, axiomAttr, Policies.ATTRIBUTE_MIGRATION_POLICY, ReturnValue.ADDED_ATTRIBUTE);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMAttribute addAttribute(String attributeName, String value, OMNamespace ns) {
        return addAttribute(getOMFactory().createOMAttribute(attributeName, ns, value));
    }
    
    public void removeAttribute(OMAttribute attr) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMElement getFirstElement() {
        try {
            return coreGetFirstChildByType(AxiomElement.class);
        } catch (DeferredParsingException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMElement cloneOMElement() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void setBuilder(OMXMLParserWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
    
    public OMXMLParserWrapper getBuilder() {
        return null;
    }
    
    public void setFirstChild(@SuppressWarnings("unused") OMNode node) {
        throw new UnsupportedOperationException();
    }

    public final String getText() {
        try {
            return coreGetTextContent(Policies.GET_TEXT_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public final void setText(String text) {
        try {
            coreSetValue(text);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setText(QName qname) {
        try {
            ensureNamespaceIsDeclared(qname.getPrefix(), qname.getNamespaceURI());
            // TODO
            coreSetValue(qname.getPrefix() + ":" + qname.getLocalPart());
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    // This method is overridden by the SOAPFaultCode implementation for SOAP 1.2.
    public QName getTextAsQName() {
        try {
            // TODO: need unit tests to determine expected behavior if the node has children with unexpected types
            return resolveQName(coreGetTextContent(TextCollectorPolicy.DEFAULT));
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public final QName resolveQName(String qname) {
        try {
            String prefix;
            String localName;
            int colonIndex = qname.indexOf(':');
            if (colonIndex == -1) {
                prefix = "";
                localName = qname;
            } else {
                prefix = qname.substring(0, colonIndex);
                localName = qname.substring(colonIndex+1);
            }
            String namespaceURI = coreLookupNamespaceURI(prefix, true);
            if (namespaceURI == null) {
                return null;
            } else if (namespaceURI.length() == 0) {
                return new QName(localName);
            } else if (prefix.length() == 0) {
                return new QName(namespaceURI, localName);
            } else {
                return new QName(namespaceURI, localName, prefix);
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    
    public OMNamespace declareNamespace(OMNamespace ns) {
        try {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, NSUtil.getPrefix(ns), null, NSUtil.getNamespaceURI(ns));
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        // TODO
        return null;
    }

    public final OMNamespace declareNamespace(String uri, String prefix) {
        // TODO: need to handle empty strings correctly
        try {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        return new OMNamespaceImpl(uri, prefix);
    }
    
    public final OMNamespace declareDefaultNamespace(String uri) {
        // TODO: what if uri is null or empty string?
        try {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, null, null, uri);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        // TODO
        return null;
    }
    
    public OMNamespace getDefaultNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace findNamespace(String queryUri, String queryPrefix) {
        try {
            if (queryUri == null) {
                String uri = coreLookupNamespaceURI(queryPrefix, true);
                return uri == null ? null : new OMNamespaceImpl(uri, queryPrefix);
            } else {
                String prefix = coreLookupPrefix(queryUri, true);
                return prefix == null ? null : new OMNamespaceImpl(queryUri, prefix);
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }
    
    public OMNamespace findNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator getAllDeclaredNamespaces() {
        return coreGetAttributesByType(AxiomNamespaceDeclaration.class, NamespaceDeclarationMapper.INSTANCE);
    }
    
    public final int getType() {
        return OMNode.ELEMENT_NODE;
    }
    
    public final String toStringWithConsume() throws XMLStreamException {
        try {
            return toString(false);
        } catch (StreamException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final String toString() {
        try {
            return toString(true);
        } catch (StreamException ex) {
            throw new OMException(ex);
        }
    }

    public final void internalSerialize(XMLStreamWriter writer, boolean cache) throws XMLStreamException {
        if (cache) {
            // TODO: we should use our own (optimized) code here
            OMSerializerUtil.serializeStartpart(this, writer);
            try {
                // TODO: check first if the element is expanded; otherwise just serialize the content directly
                CoreChildNode child = coreGetFirstChild();
                while (child != null) {
                    ((AxiomNode)child).internalSerialize(writer, cache);
                    child = child.coreGetNextSibling();
                }
            } catch (CoreModelException ex) {
                throw AxiomExceptionUtil.translate(ex);
            }
            OMSerializerUtil.serializeEndpart(writer);
        } else {
            // TODO
            throw new UnsupportedOperationException();
        }
    }
}
