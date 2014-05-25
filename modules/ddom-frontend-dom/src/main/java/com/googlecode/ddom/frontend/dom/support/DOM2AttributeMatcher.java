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
package com.googlecode.ddom.frontend.dom.support;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.CoreNSUnawareAttribute;
import com.googlecode.ddom.core.DeferredBuildingException;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.NodeFactory;

/**
 * {@link AttributeMatcher} implementation that matches attributes based on their namespace URI and
 * local name. Parameters are defined as follows:
 * <dl>
 * <dt><code>namespaceURI</code>
 * <dd>The namespace URI of the attribute.
 * <dt><code>name</code>
 * <dd>The local name of the attribute.
 * <dt><code>value</code>
 * <dd>The attribute value.
 * <dt><code>prefix</code>
 * <dd>The prefix to be used when creating a new attribute or updating an existing one.
 * </dl>
 * If the namespace URI is the emtpy string, then this class will also match namespace unaware
 * attributes. Note that the class doesn't match namespace declarations (for which
 * {@link AttributeMatcher#NAMESPACE_DECLARATION} can be used).
 * 
 * @author Andreas Veithen
 */
public final class DOM2AttributeMatcher implements AttributeMatcher {
    public static final DOM2AttributeMatcher INSTANCE = new DOM2AttributeMatcher();
    
    private DOM2AttributeMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) throws DeferredBuildingException {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute nsAwareAttr = (CoreNSAwareAttribute)attr;
            // Optimization: first compare the local names because they are in general
            // shorter and have higher "uniqueness"
            return name.equals(nsAwareAttr.coreGetLocalName())
                    && namespaceURI.equals(nsAwareAttr.coreGetNamespaceURI());
        } else if (namespaceURI.length() == 0 && attr instanceof CoreNSUnawareAttribute) {
            return name.equals(((CoreNSUnawareAttribute)attr).coreGetName());
        } else {
            return false;
        }
    }

    public String getNamespaceURI(CoreAttribute attr) throws DeferredBuildingException {
        return ((CoreNSAwareAttribute)attr).coreGetNamespaceURI();
    }

    public String getName(CoreAttribute attr) throws DeferredBuildingException {
        return ((CoreNSAwareAttribute)attr).coreGetLocalName();
    }

    public CoreAttribute createAttribute(NodeFactory nodeFactory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return nodeFactory.createAttribute(document, namespaceURI, name, prefix, value, null);
    }

    public void update(CoreAttribute attr, String prefix, String value) throws DeferredParsingException {
        attr.coreSetValue(value);
        if (attr instanceof CoreNSAwareAttribute) {
            ((CoreNSAwareAttribute)attr).coreSetPrefix(prefix);
        }
    }
}
