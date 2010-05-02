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
package com.google.code.ddom.frontend.axiom.aspects;

import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMXMLParserWrapper;

import com.google.code.ddom.backend.CoreModelException;
import com.google.code.ddom.backend.IdentityMapper;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.intf.AxiomNamespaceDeclaration;
import com.google.code.ddom.frontend.axiom.support.AxiomAttributeMatcher;
import com.google.code.ddom.frontend.axiom.support.AxiomExceptionUtil;
import com.google.code.ddom.frontend.axiom.support.NamespaceDeclarationMapper;
import com.google.code.ddom.frontend.axiom.support.Policies;

public aspect ElementSupport {
    private int AxiomElement.lineNumber;
    
    public void AxiomElement.setNamespaceWithNoFindInCurrentScope(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator AxiomElement.getChildElements() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    private static final IdentityMapper<AxiomAttribute> attributeIdentityMapper = new IdentityMapper<AxiomAttribute>();
    
    public Iterator AxiomElement.getAllAttributes() {
        return coreGetAttributesByType(AxiomAttribute.class, attributeIdentityMapper);
    }
    
    public OMAttribute AxiomElement.getAttribute(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String AxiomElement.getAttributeValue(QName qname) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMAttribute AxiomElement.addAttribute(OMAttribute attr) {
        AxiomAttribute axiomAttr = (AxiomAttribute)attr;
        try {
            coreSetAttribute(AxiomAttributeMatcher.INSTANCE, axiomAttr.coreGetNamespaceURI(), axiomAttr.coreGetLocalName(), axiomAttr, Policies.ATTRIBUTE_MIGRATION_POLICY);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
        return attr; // TODO
    }
    
    public OMAttribute AxiomElement.addAttribute(String attributeName, String value, OMNamespace ns) {
        return addAttribute(getOMFactory().createOMAttribute(attributeName, ns, value));
    }
    
    public void AxiomElement.removeAttribute(OMAttribute attr) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMElement AxiomElement.getFirstElement() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMElement AxiomElement.cloneOMElement() {
        // TODO
        throw new UnsupportedOperationException();
    }

    public void AxiomElement.setBuilder(OMXMLParserWrapper wrapper) {
        throw new UnsupportedOperationException();
    }
    
    public OMXMLParserWrapper AxiomElement.getBuilder() {
        return null;
    }
    
    public void AxiomElement.setFirstChild(@SuppressWarnings("unused") OMNode node) {
        throw new UnsupportedOperationException();
    }

    public void AxiomElement.setText(String text) {
        try {
            coreSetValue(text);
        } catch (CoreModelException ex) {
            throw AxiomExceptionUtil.translate(ex);
        }
    }

    public void AxiomElement.setText(QName text) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public String AxiomElement.getText() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName AxiomElement.getTextAsQName() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public void AxiomElement.setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int AxiomElement.getLineNumber() {
        return lineNumber;
    }
    
    public OMNamespace AxiomElement.declareNamespace(String uri, String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomElement.declareDefaultNamespace(String uri) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomElement.getDefaultNamespace() {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomElement.declareNamespace(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomElement.findNamespace(String uri, String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public OMNamespace AxiomElement.findNamespaceURI(String prefix) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public Iterator AxiomElement.getAllDeclaredNamespaces() {
        return coreGetAttributesByType(AxiomNamespaceDeclaration.class, NamespaceDeclarationMapper.INSTANCE);
    }
    
    public QName AxiomElement.resolveQName(String qname) {
        // TODO
        throw new UnsupportedOperationException();
    }

    public final int AxiomElement.getType() {
        return OMNode.ELEMENT_NODE;
    }
    
    public XMLStreamReader AxiomElement.getXMLStreamReader() {
        return getXMLStreamReader(true);
    }
    
    public XMLStreamReader AxiomElement.getXMLStreamReaderWithoutCaching() {
        return getXMLStreamReader(false);
    }
    
    public XMLStreamReader AxiomElement.getXMLStreamReader(boolean cache) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public String AxiomElement.toStringWithConsume() throws XMLStreamException {
        // TODO
        throw new UnsupportedOperationException();
    }
}
