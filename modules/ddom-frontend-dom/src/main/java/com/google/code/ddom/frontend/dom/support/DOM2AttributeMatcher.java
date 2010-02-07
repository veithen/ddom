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
package com.google.code.ddom.frontend.dom.support;

import org.apache.commons.lang.ObjectUtils;

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreNSAwareAttribute;
import com.google.code.ddom.backend.CoreNSUnawareAttribute;
import com.google.code.ddom.backend.DeferredParsingException;
import com.google.code.ddom.backend.NodeFactory;
import com.google.code.ddom.frontend.dom.intf.DOMAttribute;

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
 * <dd>Not used.
 * </dl>
 * If the namespace URI is <code>null</code>, then this class will also match namespace unaware
 * attributes.
 * 
 * @author Andreas Veithen
 */
public final class DOM2AttributeMatcher implements AttributeMatcher {
    public static final DOM2AttributeMatcher INSTANCE = new DOM2AttributeMatcher();
    
    private DOM2AttributeMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute nsAwareAttr = (CoreNSAwareAttribute)attr;
            return ObjectUtils.equals(namespaceURI, nsAwareAttr.coreGetNamespaceURI())
                    && name.equals(nsAwareAttr.coreGetLocalName());
        } else if (namespaceURI == null && attr instanceof CoreNSUnawareAttribute) {
            return name.equals(((CoreNSUnawareAttribute)attr).coreGetName());
        } else {
            return false;
        }
    }

    public CoreAttribute createAttribute(NodeFactory factory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return factory.createAttribute(document, namespaceURI, name, prefix, value, null);
    }

    public void update(CoreAttribute attr, String prefix, String value) {
        try {
            attr.coreSetValue(value);
        } catch (DeferredParsingException ex) {
            // TODO
            throw new RuntimeException(ex);
        }
        ((DOMAttribute)attr).setPrefix(prefix);
    }
}
