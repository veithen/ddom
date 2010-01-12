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

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.IdentityMapper;
import com.google.code.ddom.frontend.axiom.intf.AxiomAttribute;
import com.google.code.ddom.frontend.axiom.intf.AxiomElement;
import com.google.code.ddom.frontend.axiom.support.AxiomAttributeMatcher;

public aspect ElementSupport {
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
        coreSetAttribute(AxiomAttributeMatcher.INSTANCE, axiomAttr.coreGetNamespaceURI(), axiomAttr.coreGetLocalName(), axiomAttr);
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
}
