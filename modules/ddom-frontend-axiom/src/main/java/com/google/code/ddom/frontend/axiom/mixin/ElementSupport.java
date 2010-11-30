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

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.util.OMSerializerUtil;

import com.google.code.ddom.core.Axis;
import com.google.code.ddom.core.CoreChildNode;
import com.google.code.ddom.core.CoreModelException;
import com.google.code.ddom.core.CoreNSAwareElement;
import com.google.code.ddom.core.DeferredParsingException;
import com.google.code.ddom.core.IdentityMapper;
import com.google.code.ddom.frontend.Mixin;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNamespaceDeclaration;
import com.google.code.ddom.frontend.axiom.intf.AxiomNode;
import com.google.code.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.NamespaceDeclarationMapper;
import com.google.code.ddom.frontend.axiom.support.Policies;

@Mixin(CoreNSAwareElement.class)
public abstract class ElementSupport implements AxiomElement {
    private int lineNumber;
    
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
    
    public OMAttribute getAttribute(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String getAttributeValue(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMAttribute addAttribute(OMAttribute attr) {
        AxiomAttribute axiomAttr = (AxiomAttribute)attr;
        try {
            coreSetAttribute(AxiomAttributeMatcher.INSTANCE, axiomAttr.coreGetNamespaceURI(), axiomAttr.coreGetLocalName(), axiomAttr, Policies.ATTRIBUTE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        return attr; // TODO
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

    public void setText(String text) {
        try {
            coreSetValue(text);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void setText(QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String getText() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName getTextAsQName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
    
    public OMNamespace declareNamespace(String uri, String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace declareDefaultNamespace(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace getDefaultNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace declareNamespace(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace findNamespace(String uri, String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace findNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator getAllDeclaredNamespaces() {
        return coreGetAttributesByType(AxiomNamespaceDeclaration.class, NamespaceDeclarationMapper.INSTANCE);
    }
    
    public QName resolveQName(String qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final int getType() {
        return OMNode.ELEMENT_NODE;
    }
    
    public XMLStreamReader getXMLStreamReader() {
        return getXMLStreamReader(true);
    }
    
    public XMLStreamReader getXMLStreamReaderWithoutCaching() {
        return getXMLStreamReader(false);
    }
    
    public XMLStreamReader getXMLStreamReader(boolean cache) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String toStringWithConsume() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
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
