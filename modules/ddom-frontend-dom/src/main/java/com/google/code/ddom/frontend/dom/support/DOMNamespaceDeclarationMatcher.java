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

import com.google.code.ddom.backend.AttributeMatcher;
import com.google.code.ddom.backend.CoreAttribute;
import com.google.code.ddom.backend.CoreDocument;
import com.google.code.ddom.backend.CoreNamespaceDeclaration;
import com.google.code.ddom.backend.NodeFactory;

/**
 * {@link AttributeMatcher} implementation that matches {@link CoreNamespaceDeclaration} attributes
 * based on the declared prefix. Parameters are defined as follows:
 * <dl>
 * <dt><code>namespaceURI</code>
 * <dd>Not used.
 * <dt><code>name</code>
 * <dd>The prefix declared by the namespace declaration, or <code>null</code> for the default namespace declaration.
 * <dt><code>value</code>
 * <dd>The namespace URI of the namespace declaration.
 * <dt><code>prefix</code>
 * <dd>Not used.
 * </dl>
 * 
 * @author Andreas Veithen
 */
public class DOMNamespaceDeclarationMatcher implements AttributeMatcher {
    public static final DOMNamespaceDeclarationMatcher INSTANCE = new DOMNamespaceDeclarationMatcher();
    
    private DOMNamespaceDeclarationMatcher() {}
    
    public boolean matches(CoreAttribute attr, String namespaceURI, String name) {
        if (attr instanceof CoreNamespaceDeclaration) {
            String prefix = ((CoreNamespaceDeclaration)attr).getDeclaredPrefix();
            return name == null && prefix == null || name != null && name.equals(prefix);
        } else {
            return false;
        }
    }

    public CoreAttribute createAttribute(NodeFactory factory, CoreDocument document, String namespaceURI, String name, String prefix, String value) {
        return factory.createNSDecl(document, name, value);
    }

    public void update(CoreAttribute attr, String prefix, String value) {
        attr.coreSetValue(value);
    }
}
