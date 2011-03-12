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
package com.googlecode.ddom.frontend.axiom.support;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.NodeFactory;

// TODO: once the code in here has been stabilized, check if it can be merged with DOM2AttributeMatcher
public final class AxiomAttributeMatcher implements AttributeMatcher {
    public static final AxiomAttributeMatcher INSTANCE = new AxiomAttributeMatcher();
    
    private AxiomAttributeMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute nsAwareAttr = (CoreNSAwareAttribute)attr;
            return namespaceURI.equals(nsAwareAttr.coreGetNamespaceURI())
                    && name.equals(nsAwareAttr.coreGetLocalName());
        } else if (namespaceURI == null && attr instanceof CoreNSUnawareAttribute) {
            // TODO: this needs testing; for the moment, we don't implement OMAttribute on NS unaware attributes
            return name.equals(((CoreNSUnawareAttribute)attr).coreGetName());
        } else {
            return false;
        }
    }

    public String getNamespaceURI(CoreAttribute attr) {
        return ((CoreNSAwareAttribute)attr).coreGetNamespaceURI();
    }

    public String getName(CoreAttribute attr) {
        return ((CoreNSAwareAttribute)attr).coreGetLocalName();
    }

    public CoreAttribute createAttribute(NodeFactory nodeFactory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return nodeFactory.createAttribute(document, namespaceURI, name, prefix, value, null);
    }

    public void update(CoreAttribute attr, String prefix, String value) {
        try {
            attr.coreSetValue(value);
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
        // TODO: do we have something similar in Axiom???
//        ((DOMAttribute)attr).setPrefix(prefix);
    }
}
