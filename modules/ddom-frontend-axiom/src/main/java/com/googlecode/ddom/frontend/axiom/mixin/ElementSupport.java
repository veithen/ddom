/*
 * Copyright 2009-2013 Andreas Veithen
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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;
import org.apache.axiom.util.namespace.MapBasedNamespaceContext;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.Axis;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreElement;
import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareElement;
import com.googlecode.ddom.core.CoreNamespaceDeclaration;
import com.googlecode.ddom.core.CoreParentNode;
import com.googlecode.ddom.core.IdentityMapper;
import com.googlecode.ddom.core.Selector;
import com.googlecode.ddom.core.TextCollectorPolicy;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomAttribute;
import com.googlecode.ddom.frontend.axiom.intf.AxiomElement;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNamespaceDeclaration;
import com.googlecode.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;
import com.googlecode.ddom.frontend.axiom.support.NamespaceDeclarationMapper;
import com.googlecode.ddom.frontend.axiom.support.NamespaceIterator;
import com.googlecode.ddom.frontend.axiom.support.OMNamespaceImpl;
import com.googlecode.ddom.frontend.axiom.support.Policies;
import com.googlecode.ddom.frontend.axiom.support.PrefixIterator;
import com.googlecode.ddom.frontend.axiom.support.PushTextExtractor;
import com.googlecode.ddom.frontend.axiom.support.TextFromElementReader;
import com.googlecode.ddom.stream.SimpleXmlSource;
import com.googlecode.ddom.stream.Stream;
import com.googlecode.ddom.stream.StreamException;

@Mixin(CoreNSAwareElement.class)
public abstract class ElementSupport implements AxiomElement, NamespaceContext {
    private int lineNumber;
    
    public final void ensureNamespaceIsDeclared(String prefix, String namespaceURI) throws CoreModelException {
        String existingNamespaceURI = coreLookupNamespaceURI(prefix, true);
        if (!namespaceURI.equals(existingNamespaceURI)) {
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, namespaceURI);
        }
    }

    public final void setNamespace(OMNamespace namespace) {
        try {
            String namespaceURI;
            String prefix;
            if (namespace == null) {
                namespaceURI = "";
                prefix = "";
            } else {
                namespaceURI = namespace.getNamespaceURI();
                prefix = namespace.getPrefix();
                if (prefix == null) {
                    prefix = OMSerializerUtil.getNextNSPrefix();
                }
                if (prefix.length() > 0 && namespaceURI.length() == 0) {
                    throw new IllegalArgumentException("Cannot bind a prefix to the empty namespace name");
                }
            }
            coreSetNamespaceURI(namespaceURI);
            coreSetPrefix(prefix);
            ensureNamespaceIsDeclared(prefix, namespaceURI);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public void setNamespaceWithNoFindInCurrentScope(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final Iterator<OMElement> getChildElements() {
        return coreGetNodes(Axis.CHILDREN, Selector.NS_AWARE_ELEMENT, OMElement.class, AxiomExceptionTranslator.INSTANCE);
    }
    
    private static final IdentityMapper<AxiomAttribute> attributeIdentityMapper = new IdentityMapper<AxiomAttribute>();
    
    public Iterator getAllAttributes() {
        return coreGetAttributesByType(AxiomAttribute.class, attributeIdentityMapper);
    }
    
    public final OMAttribute getAttribute(QName qname) {
        try {
            return (AxiomAttribute)coreGetAttribute(AxiomAttributeMatcher.INSTANCE, qname.getNamespaceURI(), qname.getLocalPart());
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
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
            return (AxiomAttribute)coreSetAttribute(AxiomAttributeMatcher.INSTANCE, axiomAttr, Policies.ATTRIBUTE_MIGRATION_POLICY, true, null, ReturnValue.ADDED_ATTRIBUTE);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public OMAttribute addAttribute(String attributeName, String value, OMNamespace ns) {
        return addAttribute(getOMFactory().createOMAttribute(attributeName, ns, value));
    }
    
    public final void removeAttribute(OMAttribute attr) {
        if (attr.getOwner() == this) {
            ((AxiomAttribute)attr).coreRemove(null);
        } else {
            throw new OMException("Attribute not owned by this element");
        }
    }
    
    public OMElement getFirstElement() {
        try {
            return coreGetFirstChildByType(AxiomElement.class);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final OMElement cloneOMElement() {
        // TODO: naive implementation; use the cloning mechanism of the core model
        // TODO: no unit test coverage
        try {
            AxiomElement clone = (AxiomElement)coreGetNodeFactory().createElement(null, coreGetNamespaceURI(), coreGetLocalName(), coreGetPrefix());
            clone.coreSetSource(new SimpleXmlSource(coreGetInput(true)));
            clone.coreBuild();
            return clone;
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public OMXMLParserWrapper getBuilder() {
        return null;
    }
    
    public final String getText() {
        try {
            return coreGetTextContent(Policies.GET_TEXT_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final void setText(String text) {
        try {
            coreSetValue(text);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public void setText(QName qname) {
        try {
            if (qname == null) {
                coreClear();
            } else {
                String namespaceURI = qname.getNamespaceURI();
                String prefix = qname.getPrefix();
                if (prefix.length() == 0 && namespaceURI.length() > 0) {
                    prefix = OMSerializerUtil.getNextNSPrefix();
                }
                ensureNamespaceIsDeclared(prefix, namespaceURI);
                coreSetValue(prefix.length() > 0 ? prefix + ":" + qname.getLocalPart() : qname.getLocalPart());
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final Reader getTextAsStream(boolean cache) {
        return new TextFromElementReader(coreGetInput(cache));
    }

    public final void writeTextTo(Writer out, boolean cache) throws IOException {
        try {
            new Stream(coreGetInput(cache), new PushTextExtractor(out)).flush();
        } catch (StreamException ex) {
            // TODO: need to extract original IOException here?
            throw new OMException(ex);
        }
    }

    // This method is overridden by the SOAPFaultCode implementation for SOAP 1.2.
    public QName getTextAsQName() {
        try {
            // TODO: need unit tests to determine expected behavior if the node has children with unexpected types
            String trimmedText = coreGetTextContent(TextCollectorPolicy.DEFAULT).trim();
            return trimmedText.length() == 0 ? null : resolveQName(trimmedText);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
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
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    
    private void addNamespaceDeclaration(String uri, String prefix) {
        try {
            String elementPrefix = coreGetPrefix();
            String elementNamespaceURI = coreGetNamespaceURI();
            if (elementPrefix.equals(prefix) && !elementNamespaceURI.equals(uri)) {
                throw new OMException("Attempt to add a namespace declaration that conflicts with " +
                        "the namespace information of the element");
            }
            coreSetAttribute(AttributeMatcher.NAMESPACE_DECLARATION, null, prefix, null, uri);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final OMNamespace declareNamespace(OMNamespace ns) {
        String prefix = ns.getPrefix();
        if (prefix == null) {
            return declareNamespace(ns.getNamespaceURI(), null);
        } else {
            String uri = ns.getNamespaceURI();
            if (prefix.length() > 0 && uri.length() == 0) {
                throw new IllegalArgumentException("Cannot bind a prefix to the empty namespace name");
            }
            addNamespaceDeclaration(ns.getNamespaceURI(), prefix);
            return ns;
        }
    }

    public final OMNamespace declareNamespace(String uri, String prefix) {
        if (prefix == null || prefix.length() == 0) {
            prefix = OMSerializerUtil.getNextNSPrefix();
        }
        if (prefix.length() > 0 && uri.length() == 0) {
            throw new IllegalArgumentException("Cannot bind a prefix to the empty namespace name");
        }
        addNamespaceDeclaration(uri, prefix);
        return new OMNamespaceImpl(uri, prefix);
    }
    
    public final OMNamespace declareDefaultNamespace(String uri) {
        // TODO: what if uri is null or empty string?
        addNamespaceDeclaration(uri, "");
        // TODO
        return null;
    }
    
    public final void undeclarePrefix(String prefix) {
        addNamespaceDeclaration("", prefix);
    }

    public final OMNamespace getDefaultNamespace() {
        try {
            String namespaceURI = coreLookupNamespaceURI("", true);
            return namespaceURI == null || namespaceURI.length() == 0 ? null : new OMNamespaceImpl(namespaceURI, "");
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
    
    public final OMNamespace findNamespace(String queryUri, String queryPrefix) {
        try {
            if (queryUri == null) {
                String uri = coreLookupNamespaceURI(queryPrefix, true);
                return uri == null ? null : new OMNamespaceImpl(uri, queryPrefix);
            } else {
                String prefix = coreLookupPrefix(queryUri, true);
                return prefix == null ? null : new OMNamespaceImpl(queryUri, prefix);
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
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
        return toString(false);
    }

    public final String toString() {
        try {
            return toString(true);
        } catch (XMLStreamException ex) {
            throw new OMException(ex);
        }
    }

    public final Iterator<OMNamespace> getNamespacesInScope() {
        return new NamespaceIterator(this);
    }

    public Map<String,String> getNamespaceContextMap() {
        Map<String,String> namespaces = new HashMap<String,String>();
        try {
            CoreElement element = this;
            while (true) {
                CoreAttribute attr = element.coreGetFirstAttribute();
                while (attr != null) {
                    if (attr instanceof CoreNamespaceDeclaration) {
                        CoreNamespaceDeclaration nsDeclaration = (CoreNamespaceDeclaration)attr;
                        String prefix = nsDeclaration.coreGetDeclaredPrefix();
                        if (!namespaces.containsKey(prefix)) {
                            namespaces.put(prefix, nsDeclaration.coreGetDeclaredNamespaceURI());
                        }
                    }
                    attr = attr.coreGetNextAttribute();
                }
                CoreParentNode parent = element.coreGetParent();
                if (parent instanceof CoreElement) {
                    element = (CoreElement)parent;
                } else {
                    break;
                }
            }
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
        return namespaces;
    }

    public final NamespaceContext getNamespaceContext(boolean detached) {
        if (detached) {
            return new MapBasedNamespaceContext(getNamespaceContextMap());
        } else {
            return this;
        }
    }

    public final String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("prefix can't be null");
        } else if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        } else if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        } else {
            try {
                String namespaceURI = coreLookupNamespaceURI(prefix, true);
                return namespaceURI == null ? XMLConstants.NULL_NS_URI : namespaceURI;
            } catch (CoreModelException ex) {
                throw AxiomExceptionTranslator.translate(ex);
            }
        }
    }

    public final String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI can't be null");
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return XMLConstants.XML_NS_PREFIX;
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return XMLConstants.XMLNS_ATTRIBUTE;
        } else {
            try {
                return coreLookupPrefix(namespaceURI, true);
            } catch (CoreModelException ex) {
                throw AxiomExceptionTranslator.translate(ex);
            }
        }
    }

    public final Iterator<String> getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException("namespaceURI can't be null");
        } else if (namespaceURI.equals(XMLConstants.XML_NS_URI)) {
            return Collections.singleton(XMLConstants.XML_NS_PREFIX).iterator();
        } else if (namespaceURI.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI)) {
            return Collections.singleton(XMLConstants.XMLNS_ATTRIBUTE).iterator();
        } else {
            return new PrefixIterator(this, namespaceURI);
        }
    }
}
