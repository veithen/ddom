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
package com.google.code.ddom.backend.testsuite;

import org.apache.commons.lang.ObjectUtils;

import com.googlecode.ddom.core.AttributeMatcher;
import com.googlecode.ddom.core.CoreAttribute;
import com.googlecode.ddom.core.CoreDocument;
import com.googlecode.ddom.core.CoreNSAwareAttribute;
import com.googlecode.ddom.core.DeferredParsingException;
import com.googlecode.ddom.core.NodeFactory;

public final class NSAwareAttributeMatcher implements AttributeMatcher {
    public static final NSAwareAttributeMatcher INSTANCE = new NSAwareAttributeMatcher();
    
    private NSAwareAttributeMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
        if (attr instanceof CoreNSAwareAttribute) {
            CoreNSAwareAttribute nsAwareAttr = (CoreNSAwareAttribute)attr;
            return ObjectUtils.equals(namespaceURI, nsAwareAttr.coreGetNamespaceURI())
                    && name.equals(nsAwareAttr.coreGetLocalName());
        } else {
            return false;
        }
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
    }
}
