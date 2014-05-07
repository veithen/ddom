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
package com.googlecode.ddom.frontend.axiom.mixin;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMNamespace;

import com.googlecode.ddom.core.CoreModelException;
import com.googlecode.ddom.core.CoreNSAwareNamedNode;
import com.googlecode.ddom.frontend.Mixin;
import com.googlecode.ddom.frontend.axiom.intf.AxiomNamedNode;
import com.googlecode.ddom.frontend.axiom.support.AxiomExceptionTranslator;

/**
 * 
 * 
 * Note that {@link AxiomNamedNode#getLocalName()} is defined by
 * {@link com.googlecode.ddom.frontend.axiom.mixin.dom.compat.NamedNodeSupport}.
 * 
 * @author Andreas Veithen
 */
@Mixin(CoreNSAwareNamedNode.class)
public abstract class NamedNodeSupport implements AxiomNamedNode {
    public final void setLocalName(String localName) {
        coreSetLocalName(localName);
    }
    
    public final OMNamespace getNamespace() {
        try {
            String namespaceURI = coreGetNamespaceURI();
            return namespaceURI.length() == 0 ? null : getOMFactory().createOMNamespace(namespaceURI, coreGetPrefix());
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final QName getQName() {
        try {
            return coreGetQName();
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public boolean hasName(QName name) {
        try {
            return coreGetLocalName().equals(name.getLocalPart()) && coreGetNamespaceURI().equals(name.getNamespaceURI());
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }

    public final void setNamespace(OMNamespace namespace, boolean declare) {
        try {
            String namespaceURI;
            String prefix;
            if (namespace == null) {
                namespaceURI = "";
                prefix = "";
            } else {
                namespaceURI = namespace.getNamespaceURI();
                prefix = namespace.getPrefix();
            }
            coreSetPrefix(prepareSetNamespace(prefix, namespaceURI, declare));
            coreSetNamespaceURI(namespaceURI);
        } catch (CoreModelException ex) {
            throw AxiomExceptionTranslator.translate(ex);
        }
    }
}
