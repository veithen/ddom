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
package com.google.code.ddom.frontend.axiom.mixin;

import java.io.StringWriter;
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
import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.core.AttributeMatcher;
import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.IdentityMapper;
import com.google.code.ddom.core.util.QNameUtil;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNamespaceDeclaration;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.NSUtil;
import com.google.code.ddom.frontend.axiom.support.NamespaceDeclarationMapper;
import com.google.code.ddom.frontend.axiom.support.OMNamespaceImpl;
import com.google.code.ddom.frontend.axiom.support.Policies;
import com.google.code.ddom.stream.spi.StreamException;

@Mixin(CoreNSAwareElement.class)
public abstract class ElementSupport implements AxiomElement {
    private int lineNumber;
    
    public final void ensureNamespaceIsDeclared(String prefix, String namespaceURI) throws CoreModelException {
        String existingNamespaceURI = coreLookupNamespaceURI(prefix, true);
        if (!ObjectUtils.equals(existingNamespaceURI, namespaceURI)) {
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
        return (AxiomAttribute)coreGetAttribute(AxiomAttributeMatcher.INSTANCE, QNameUtil.getNamespaceURI(qname), qname.getLocalPart());
    }
    
    public final String getAttributeValue(QName qname) {
        OMAttribute attr = getAttribute(qname);
        return attr == null ? null : attr.getAttributeValue();
    }
    
    public final OMAttribute addAttribute(OMAttribute attr) {
        AxiomAttribute axiomAttr = (AxiomAttribute)attr;
        try {
            String namespaceURI = axiomAttr.coreGetNamespaceURI();
            ensureNamespaceIsDeclared(axiomAttr.coreGetPrefix(), namespaceURI);
            return (AxiomAttribute)coreSetAttribute(AxiomAttributeMatcher.INSTANCE, namespaceURI, axiomAttr.coreGetLocalName(), axiomAttr, Policies.ATTRIBUTE_MIGRATION_POLICY, ReturnValue.ADDED_ATTRIBUTE);
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
            // TODO: there may be a mismatch between the way coreGetTextContent collects the text and what is expected for Axiom's getText
            return coreGetTextContent();
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
            ensureNamespaceIsDeclared(QNameUtil.getPrefix(qname), QNameUtil.getNamespaceURI(qname));
            // TODO
            coreSetValue(qname.getPrefix() + ":" + qname.getLocalPart());
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public final QName getTextAsQName() {
        try {
            return resolveQName(coreGetTextContent());
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
                prefix = null;
                localName = qname;
            } else {
                prefix = qname.substring(0, colonIndex);
                localName = qname.substring(colonIndex+1);
            }
            // TODO: we don't cover the case where prefix is not null, but also not bound
            String namespaceURI = coreLookupNamespaceURI(prefix, true);
            if (namespaceURI == null) {
                return new QName(localName);
            } else if (prefix == null) {
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
        coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, NSUtil.getPrefix(ns), null, NSUtil.getNamespaceURI(ns));
        // TODO
        return null;
    }

    public final OMNamespace declareNamespace(String uri, String prefix) {
        // TODO: need to handle empty strings correctly
        coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        // TODO
        return null;
    }
    
    public final OMNamespace declareDefaultNamespace(String uri) {
        // TODO: what if uri is null or empty string?
        coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, null, null, uri);
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
    
    private String toString(boolean preserve) throws StreamException {
        StringWriter sw = new StringWriter();
        internalSerialize(sw, preserve);
        return sw.toString();
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
