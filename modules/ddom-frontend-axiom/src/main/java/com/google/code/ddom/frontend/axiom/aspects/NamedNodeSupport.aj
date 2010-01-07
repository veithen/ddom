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

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.frontend.axiom.intf.AxiomNamedNode;

/**
 * 
 * 
 * Note that {@link AxiomNamedNode#getLocalName()} is defined by {@link DOMCompatibleMethods}.
 * 
 * @author Andreas Veithen
 */
public aspect NamedNodeSupport {
    public final void AxiomNamedNode.setLocalName(String localName) {
        coreSetLocalName(localName);
    }
    
    public final OMNamespace AxiomNamedNode.getNamespace() {
        String namespaceURI = coreGetNamespaceURI();
        // TODO: handle null prefix!
        return namespaceURI == null ? null : getOMFactory().createOMNamespace(namespaceURI, coreGetPrefix());
    }

    public final void AxiomNamedNode.setNamespace(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final QName AxiomNamedNode.getQName() {
        return coreGetQName();
    }
}
