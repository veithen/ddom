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

import org.apache.axiom.om.OMNamespace;

import com.google.code.ddom.frontend.axiom.intf.AxiomNamedNode;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;
import com.googlecode.ddom.frontend.Mixin;

/**
 * 
 * 
 * Note that {@link AxiomNamedNode#getLocalName()} is defined by
 * {@link com.google.code.ddom.frontend.axiom.mixin.dom.NamedNodeSupport}.
 * 
 * @author Andreas Veithen
 */
@Mixin(CoreNSAwareNamedNode.class)
public abstract class NamedNodeSupport implements AxiomNamedNode {
    public final void setLocalName(String localName) {
        coreSetLocalName(localName);
    }
    
    public final OMNamespace getNamespace() {
        String namespaceURI = coreGetNamespaceURI();
        // TODO: handle null prefix!
        return namespaceURI == null ? null : getOMFactory().createOMNamespace(namespaceURI, coreGetPrefix());
    }

    public final void setNamespace(OMNamespace namespace) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    public final QName getQName() {
        return coreGetQName();
    }
}
