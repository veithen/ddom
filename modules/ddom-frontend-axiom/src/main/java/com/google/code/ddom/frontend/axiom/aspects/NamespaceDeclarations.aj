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

import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.frontend.axiom.intf.AxiomElement;

public aspect NamespaceDeclarations {
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
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public QName AxiomElement.resolveQName(String qname) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
